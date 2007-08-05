/*
 * Créé le 24 sept. 2005
 */
package hapi.application.importation;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_FTP;
import hapi.application.C_Local;
import hapi.application.C_TransfertFichier;
import hapi.application.C_Utilisateur;
import hapi.application.ressources.Bundle;
import hapi.exception.ConnexionException;
import hapi.exception.LoginFTPException;
import hapi.exception.RecupererFichierException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 *
 */
public class C_ImportationCSS extends C_TransfertFichier
{
	public static final String CHEMIN_SAUVEGARDE = "." + File.separator + "temp" + File.separator;
	private String nomFichierTemp = "";	
	
	/**
	 * Constructeur préparant la configuration et les modèles
	 */
	public C_ImportationCSS()
	{
		super();
		leModeleChemin.initialise(C_Utilisateur.getServeursCSS());
	}

	/**
	 * Récupération du login du serveur CSS à partir de son adresse
	 */
	public String getLogin(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurCSS(adresse).getLogin();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Récupération du mot de passe du serveur CSS à partir de son adresse
	 */
	public String getPWD(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurCSS(adresse).getMotDePasse();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Téléchargement du fichier sur le serveur
	 * 
	 */
	public void chargerFichier(String fichier, String chemin, String login, String passwd) throws ConnexionException, LoginFTPException, RecupererFichierException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_serveur"));
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = ftp.recupererFichier(fichier);
		SaveFichier(fichier,fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier recupere"));
	}

	/**
	 * Récupération du fichier en local
	 * 
	 */
	public void chargerFichier(String fichier, String chemin) throws RecupererFichierException, ConnexionException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_debut_transfert"));
		C_Local local = new C_Local();

		local.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = local.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier_recupere"));
		SaveFichier(fichier,fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fin_transfert"));
	}

	/**
	 * Ajout d'un serveur de fichier CSS
	 */
	public void addChemin(String chemin)
	{
		C_Utilisateur.addServeurCSS(chemin, "", "");
		leModeleChemin.addElement(chemin);
	}

	/**
	 * Sauvegarde du serveur sélectionné
	 */
	public void sauvegarderSelectionChemin(String lAdresse)
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add("CSS");
			lesParametres.add(lAdresse);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

			idStatement = cBase.creerPreparedStatement("setServeurSelection");
			cBase.executerRequeteStockee(idStatement, "setServeurSelection", lesParametres);
			cBase.fermerStatement(idStatement);

			idStatement = cBase.creerPreparedStatement("setServeurDeselection");
			cBase.executerRequeteStockee(idStatement, "setServeurDeselection", lesParametres);
			
			C_Utilisateur.setSelectedCSS(lAdresse);
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
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{
				// Rien à faire
			}

		}
	}

	public void SaveFichier(String fichier,InputStream source)
	{
		copier(source, CHEMIN_SAUVEGARDE, fichier);
		nomFichierTemp = fichier;
	}

	private static boolean copier(InputStream source, String cheminDestination, String destination)
	{
		boolean resultat = false;

		// Declaration des flux
		File dest = new File(cheminDestination);

		FileOutputStream destinationFile = null;

		try
		{
			//Création du répertoire
			dest.mkdirs();
			//Création du fichier
			dest = new File(cheminDestination + destination);
			dest.createNewFile();

			// Ouverture des flux
			destinationFile = new FileOutputStream(dest);

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			//source.reset();
			int nbLecture;

			while ((nbLecture = source.read(buffer)) != -1)
			{
				destinationFile.write(buffer, 0, nbLecture);
			}

			// Copie réussie
			resultat = true;
			source.reset();
		}
		catch (java.io.FileNotFoundException f)
		{}
		catch (java.io.IOException e)
		{}
		finally
		{
			// Quoi qu'il arrive, on ferme les flux
			try
			{
				destinationFile.close();
			}
			catch (Exception e)
			{}
		}
		return (resultat);
	}
	
	public String getNomFichierTemp()
	{
		return nomFichierTemp;
	}

}
