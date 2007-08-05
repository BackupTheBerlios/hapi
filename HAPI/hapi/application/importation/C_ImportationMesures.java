package hapi.application.importation;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_FTP;
import hapi.application.C_Local;
import hapi.application.C_Mesures;
import hapi.application.C_TransfertFichier;
import hapi.application.C_Utilisateur;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.metier.temporaire.C_ElementPresentationTemporaire;
import hapi.application.metier.temporaire.C_EtatTemporaire;
import hapi.application.metier.temporaire.C_GuideTemporaire;
import hapi.application.metier.temporaire.C_InterfaceTemporaire;
import hapi.application.metier.temporaire.C_PaquetagePresentationTemporaire;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.metier.temporaire.C_ProduitTemporaire;
import hapi.application.metier.temporaire.C_RoleTemporaire;
import hapi.application.metier.temporaire.C_TypeGuideTemporaire;
import hapi.application.metier.temporaire.C_TypeProduitTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.exception.AucuneNouvelleITterminee;
import hapi.exception.ConnexionException;
import hapi.exception.LoginFTPException;
import hapi.exception.ProcessusOuVersionException;
import hapi.exception.RecupererFichierException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

/**
 * @author Vincent
 */
public class C_ImportationMesures extends C_TransfertFichier
{
	/**
	 * Chargement du fichier de mesures
	 */
	// l'exécution de processus importée
	E_ExecutionProcessus eExecProcessus = null;
	// l'évaluation quantitative
	E_Evaluation eEvalQuantitative = null;
	//id du processus utilisé par le projet importé
	String idProcessusImporte = null;

	public C_ImportationMesures()
	{
		super();
		leModeleChemin.initialise(C_Utilisateur.getServeursMES());
	}

	public void chargerFichier(String fichier, String chemin) throws FileNotFoundException, SQLException, ConnexionException, RecupererFichierException, SAXException, ProcessusOuVersionException, AucuneNouvelleITterminee
	{
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_debut_transfert"));
		C_Local local = new C_Local();
		local.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = local.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier_recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fin_transfert"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		//parsage du processus dans le fichier XML
		ProcessusHandler pHandler = new ProcessusHandler();
		new C_Parser(fic, pHandler);
		//parsage des mesures dans le fichier XML
		fic = local.recupererFichier(fichier);
		MesuresHandler mHandler = new MesuresHandler();
		new C_Parser(fic, mHandler);
		// Si le fichier de mesures ne contient aucune itération commencée et terminée,
		// on ne va pas plus loin...
		if (C_Mesures.getNbMesures() == 0)
			throw new AucuneNouvelleITterminee();
		//recuperation des entites parsees
		this.eExecProcessus = mHandler.getExecutionProcessusImportee();
		this.idProcessusImporte = C_ProcessusTemporaire.get().getIdentifiant();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		//si le processus n'est pas connu
		if (C_Processus.getProcessus(this.idProcessusImporte) == null)
			throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurProcessusInconnu"));
		else
		{
			//Si l'exécution de processus existe
			if (C_ExecutionProcessus.getExecutionProcessusAPartirDeLeurId(this.idProcessusImporte, String.valueOf(this.eExecProcessus.getId()), this.eExecProcessus.getDateDebut(), this.eExecProcessus.getDateFin()) != null)
			{
				//on vérifie si la version de l'execution de processus importée correspond à la version de celle existante
				if (!C_ExecutionProcessus.getExecutionProcessusAPartirDeLeurId(this.idProcessusImporte, String.valueOf(this.eExecProcessus.getId()), this.eExecProcessus.getDateDebut(), this.eExecProcessus.getDateFin()).getVersionProcessus().equals(eExecProcessus.getVersionProcessus()))
					throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurVersionProcessus"));
			}
			else //si l'exécution de processus n'existe pas
				{
				//si la version du processus associé à cette exécution est la plus récente
				if (eExecProcessus.getVersionProcessus().equals(C_Processus.getProcessus(this.idProcessusImporte).getDateExport()))
				{
					leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_execution_processus_enregistre_BD"));
					C_ExecutionProcessus.enregistrerExecutionProcessusDansBD(idProcessusImporte, eExecProcessus);
					//on modifie les entités après la modification dans la BD (au cas où pb BD)
					C_ExecutionProcessus.ajouterExecutionProcessus(this.idProcessusImporte, this.eExecProcessus.getIdentifiant(), this.eExecProcessus);
				}
				else
					throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurDerniereVersionProcessus") + " " + C_Processus.getProcessus(this.idProcessusImporte).getNomSansVersion());
			}
		}
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
	}

	public void chargerFichier(String fichier, String chemin, String login, String passwd) throws FileNotFoundException, SQLException, ConnexionException, RecupererFichierException, LoginFTPException, SAXException, ProcessusOuVersionException, AucuneNouvelleITterminee
	{
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();

		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_serveur"));
		C_FTP ftp = new C_FTP();
		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = ftp.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		//parsage du processus dans le fichier XML
		ProcessusHandler pHandler = new ProcessusHandler();
		new C_Parser(fic, pHandler);
		//parsage des mesures dans le fichier XML
		fic = ftp.recupererFichier(fichier);
		MesuresHandler mHandler = new MesuresHandler();
		new C_Parser(fic,  mHandler);
		// Si le fichier de mesures ne contient aucune itération commencée et terminée,
		// on ne va pas plus loin...
		if (C_Mesures.getNbMesures() == 0)
			throw new AucuneNouvelleITterminee();
		//recuperation des entites parsees
		this.eExecProcessus = mHandler.getExecutionProcessusImportee();
		this.idProcessusImporte = C_ProcessusTemporaire.get().getIdentifiant();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		//si le processus n'est pas connu
		if (C_Processus.getProcessus(this.idProcessusImporte) == null)
			throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurProcessusInconnu"));
		else
		{
			//Si l'exécution de processus existe
			if (C_ExecutionProcessus.getExecutionProcessusAPartirDeLeurId(this.idProcessusImporte, String.valueOf(this.eExecProcessus.getId()), this.eExecProcessus.getDateDebut(), this.eExecProcessus.getDateFin()) != null)
			{
				//on vérifie si la version de l'execution de processus importée correspond à la version de celle existante
				if (!C_ExecutionProcessus.getExecutionProcessusAPartirDeLeurId(this.idProcessusImporte, String.valueOf(this.eExecProcessus.getId()), this.eExecProcessus.getDateDebut(), this.eExecProcessus.getDateFin()).getVersionProcessus().equals(eExecProcessus.getVersionProcessus()))
					throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurVersionProcessus"));
			}
			else //si l'exécution de processus n'existe pas
				{
				//si la version du processus associé à cette exécution est la plus récente
				if (eExecProcessus.getVersionProcessus().equals(C_Processus.getProcessus(this.idProcessusImporte).getDateExport()))
				{
					leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_execution_processus_enregistre_BD"));
					C_ExecutionProcessus.enregistrerExecutionProcessusDansBD(idProcessusImporte, eExecProcessus);
					//on modifie les entités après la modification dans la BD (au cas où pb BD)
					C_ExecutionProcessus.ajouterExecutionProcessus(this.idProcessusImporte, this.eExecProcessus.getIdentifiant(), this.eExecProcessus);
				}
				else
					throw new ProcessusOuVersionException(Bundle.getText("BD_ImporterMesures_ErreurDerniereVersionProcessus") + " " + C_Processus.getProcessus(this.idProcessusImporte));
			}
		}
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
	}

	public void addChemin(String chemin)
	{
		C_Utilisateur.addServeurMES(chemin, "", "");
		leModeleChemin.addElement(chemin);
	}

	/**
	 * Récupération du login du serveur workflow à partir de son adresse
	 */
	public String getLogin(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurMES(adresse).getLogin();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Récupération du mot de passe du serveur workflow à partir de son adresse
	 */
	public String getPWD(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurMES(adresse).getMotDePasse();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Sauvegarde du serveur sélectionné
	 */
	public void sauvegarderSelectionChemin(String lAdresse)
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		int idStatement = 0;
		int idStatement2 = 0;

		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add("MES");
			lesParametres.add(lAdresse);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
			idStatement = cBase.creerPreparedStatement("setServeurSelection");
			idStatement2 = cBase.creerPreparedStatement("setServeurDeselection");
			cBase.executerRequeteStockee(idStatement, "setServeurSelection", lesParametres);
			cBase.executerRequeteStockee(idStatement2, "setServeurDeselection", lesParametres);
			
			C_Utilisateur.setSelectedMES(lAdresse);
		}
		catch (Exception e)
		{
			//Rien d'interresant à faire ici
		}
		finally
		{
			try
			{
				cBase.fermerStatement(idStatement);
				cBase.fermerStatement(idStatement2);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{
				// Rien d'interresant à faire ici
			}
		}

	}

	private void effacerEntitesTemporaires()
	{
		//Effacement de chaque entites temporaires
		C_ProcessusTemporaire.setProcessus(null);
		C_ActiviteTemporaire.effacerListe();
		C_ComposantTemporaire.effacerListe();
		C_DefinitionTemporaire.effacerListe();
		C_ElementPresentationTemporaire.effacerListe();
		C_EtatTemporaire.effacerListe();
		C_GuideTemporaire.effacerListe();
		C_InterfaceTemporaire.effacerListe();
		C_PaquetagePresentationTemporaire.effacerListe();
		C_ProduitTemporaire.effacerListe();
		C_RoleTemporaire.effacerListe();
		C_TypeGuideTemporaire.effacerListe();
		C_TypeProduitTemporaire.effacerListe();
	}
}
