/*
 * Created on 24 janv. 2005
 */
package hapi.application;

import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.exception.ConnexionException;
import hapi.exception.EnvoyerFichierException;
import hapi.exception.LoginFTPException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Vincent
 */
public class C_ExportationCommentaire extends C_TransfertFichier
{
	private String idProjet = null;
	private String idProcessus = null;

	/**
	 * Constructeur préparant la configuration et les modèles
	 */
	public C_ExportationCommentaire(String lIdProjet, String lIdProcessus)
	{
		super();
		idProjet = lIdProjet;
		idProcessus = lIdProcessus;
		leModeleChemin.initialise(C_Utilisateur.getServeursDOM());
	}

	/**
	 * Ajout d'un serveur de fichier DOM
	 */
	public void addChemin(String chemin)
	{
		C_Utilisateur.addServeurDOM(chemin, "", "");
		leModeleChemin.addElement(chemin);
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
			lesParametres.add("DOM");
			lesParametres.add(lAdresse);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
			idStatement = cBase.creerPreparedStatement("setServeurSelection");
			cBase.executerRequeteStockee(idStatement, "setServeurSelection", lesParametres);
			idStatement2 = cBase.creerPreparedStatement("setServeurDeselection");
			cBase.executerRequeteStockee(idStatement2, "setServeurDeselection", lesParametres);
			
			C_Utilisateur.setSelectedDOM(lAdresse);
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
			catch (SQLException e1)
			{}

		}
	}

	/**
	 * Téléchargement du fichier sur le serveur
	 */
	public void uploaderFichier(String fichier, String chemin, String login, String passwd) throws ConnexionException, LoginFTPException, EnvoyerFichierException, FileNotFoundException, IOException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_serveur"));
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier"));

		creerFichierCommentaire(fichier);

		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_transfert_fichier"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_debut_transfert"));
		ftp.envoyerFichier("temp" + File.separator + fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_fichier_transfere"));
		supprimerFichierCommentaireTemporaire(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_deconnexion"));
	}

	/**
	 * Récupération du fichier en local
	 */
	public void uploaderFichier(String fichier, String chemin) throws ConnexionException, FileNotFoundException, IOException, EnvoyerFichierException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_serveur"));
		C_Local ftp = new C_Local();

		ftp.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier"));

		creerFichierCommentaire(fichier);

		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_transfert_fichier"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_debut_transfert"));
		ftp.envoyerFichier("temp" + File.separator + fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_fichier_transfere"));
		supprimerFichierCommentaireTemporaire(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_deconnexion"));
	}

	/**
	 * Récupération du login du serveur DOM à partir de son adresse
	 */
	public String getLogin(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurDOM(adresse).getLogin();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Récupération du mot de passe du serveur DOM à partir de son adresse
	 */
	public String getPWD(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurDOM(adresse).getMotDePasse();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Création du fichier
	 */
	private void creerFichierCommentaire(String nomFichierExport) throws FileNotFoundException, IOException
	{
		//Création du répertoire temposaire
		File repTemp = new File("temp" + File.separator);
		if (!repTemp.isDirectory())
			repTemp.mkdir();

		File fichExp = new File(nomFichierExport);
		OutputStreamWriter fichierExport = new OutputStreamWriter(new FileOutputStream("temp" + File.separator + fichExp), "UTF-16");

		fichierExport.write("<commentaireProjet>\n");

		fichierExport.write("\t<idProjet>");
		fichierExport.write(String.valueOf(idProjet));
		fichierExport.write("</idProjet>\n");

		fichierExport.write("\t<commentaire>");
		fichierExport.write(C_ExecutionProcessus.getCommentaireProjet(idProcessus, idProjet)==null?"":C_ExecutionProcessus.getCommentaireProjet(idProcessus, idProjet));
		fichierExport.write("</commentaire>\n");

		fichierExport.write("</commentaireProjet>");

		fichierExport.close();
	}

	/**
	 * Suppression su fichier temporaire
	 */
	private void supprimerFichierCommentaireTemporaire(String nomFichierExport)
	{
		File ficTemp = new File("temp" + File.separator + nomFichierExport);
		if (ficTemp.isFile())
			ficTemp.delete();
	}
}
