package Tests;

import hapi.application.C_FTP;
import hapi.exception.ChangerRepertoireException;
import hapi.exception.ConnexionException;
import hapi.exception.CreerRepertoireException;
import hapi.exception.EnvoyerFichierException;
import hapi.exception.ListerFichiersException;
import hapi.exception.LoginFTPException;
import hapi.exception.RecupererFichierException;
import hapi.exception.RepertoireCourantException;
import hapi.exception.SupprimerFichierException;
import hapi.exception.SupprimerRepertoireException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * @author Stéphane
 * @author Cédric
 */
public class C_FTPTest extends TestCase
{
	C_FTP ftp;

	public C_FTPTest(String arg0)
	{
		super(arg0);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		ftp = new C_FTP();
	}

	/*
	 * Test for void seConnecter(String)
	 */
	public void testSeConnecterString()
	{
		// Connexion à un serveur FTP anonyme qui existe : ftp.cict.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion anonyme à un serveur ftp ----");
			System.out.println("Connexion au serveur FTP anonyme ftp.cict.fr");
			ftp.seConnecter("ftp.cict.fr");
			Vector listFic = ftp.listerFichiers();
			System.out.println("Liste des fichiers du serveur anonyme : ");
			for (int i = 0; i < listFic.size(); i++)
				System.out.println(listFic.get(i));
			assertTrue(true);
			System.out.println("---- OK -----");
			System.out.println("----------------------------------------------");			
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (ListerFichiersException e1)
		{
			System.err.println("---- ECHEC ---- Probleme de listing de fichiers");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}

		// Connexion à un serveur FTP anonyme qui n'existe pas
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion à un serveur FTP anonyme inexistant ----");
			ftp.seConnecter("serveur.bidon");
			assertTrue(false);
			System.err.println("---- ECHEC ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.out.println("---- OK ----");
			assertTrue(true);
			System.out.println("----------------------------------------------");
		}
	}

	/*
	 * Test for void seConnecter(String, String, String)
	 */
	public void testSeConnecterStringStringString()
	{
		// Connexion à un serveur FTP qui existe : ftp.cict.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion à une serveur avec login et pwd ----");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");

			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}

		// Connexion à un serveur FTP qui n'existe pas
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion à un serveur FTP inexistant ----");
			ftp.seConnecter("serveur.bidon", "toto", "toto");
			assertTrue(false);
			System.err.println("---- ECHEC ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.out.println("---- OK ----");
			assertTrue(true);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}

		// Test avec login incorrect
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Test avec Login incorrect ----");
			ftp.seConnecter("ftp.cict.fr", "bidon", "sfghgf");
			assertTrue(false);
			System.err.println("---- ECHEC ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.out.println("---- OK ----");
			assertTrue(true);
			System.out.println("----------------------------------------------");
		}

		// Test avec mot de passe incorrect
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Test avec Mot de passe incorrect ----");
			ftp.seConnecter("ftp.cict.fr", "domino", "bidon");
			assertTrue(false);
			System.err.println("---- ECHEC ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.out.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.out.println("---- OK ----");
			assertTrue(true);
			System.out.println("----------------------------------------------");
		}
	}

	/*
	 * Test for Vector listerFichiers()
	 */
	public void testListerFichiers()
	{
		// Lister les fichiers du serveur marine.edu.ups-tlse.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Lister les fichiers de ftp.cict.fr ----");
			ftp.seConnecter("ftp.cict.fr");
			Vector listFic = new Vector(ftp.listerFichiers());
			System.out.println("Liste des fichiers du serveur : ");
			for (int i = 0; i < listFic.size(); i++)
				System.out.println(listFic.get(i));
			System.out.println("Fin du listing");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (ListerFichiersException e1)
		{
			System.err.println("---- ECHEC ---- Probleme de listing de fichiers");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	/*
	 * Test for Vector listerFichiers(String)
	 */
	public void testListerFichiersString()
	{
		// Lister les fichiers d'extension .gz du serveur marine.edu.ups-tlse.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Lister les fichiers .gz sur ftp.cict.fr ----");
			ftp.seConnecter("ftp.cict.fr");
			Vector listFic = new Vector(ftp.listerFichiers("gz"));
			System.out.println("Liste des fichiers .gz du serveur : ");
			for (int i = 0; i < listFic.size(); i++)
				System.out.println(listFic.get(i));
			System.out.println("Fin du listing des .gz");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (ListerFichiersException e1)
		{
			System.err.println("---- ECHEC ---- Probleme de listing de fichiers");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	public void testEnvoyerFichier()
	{
		System.out.println("----------------------------------------------");
		System.out.println("---- Envoie de fichier ----");
		//On cree un fichier en local
		File f = new File("C:\\testJUnitClassFTP.txt");
		try
		{
			f.createNewFile();
			FileOutputStream o = new FileOutputStream(f.getAbsoluteFile());
			byte[] buffer = { 'a', 'b', 'c' };
			o.write(buffer, 0, 3);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Test de l'envoie de ce fichier
		try
		{
			System.out.println("Connexion au serveur ftp.cict.fr");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			ftp.envoyerFichier("C:\\testJUnitClassFTP.txt");
			System.out.println("Fichier envoyé");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (EnvoyerFichierException e)
		{
			System.err.println("---- ECHEC ---- Impossible d'envoyer le fichier");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		finally
		{
			f.delete();
		}
	}

	public void testRecupererFichier()
	{
		// Test de l'envoie de ce fichier
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("----Récupération du fichier ----");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			InputStream in = ftp.recupererFichier("testJUnitClassFTP.txt");
			System.out.println(in.read());
			System.out.println(in.read());
			System.out.println(in.read());
			System.out.println("Fichier Récupéré");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (RecupererFichierException e)
		{
			System.err.println("---- ECHEC ---- Impossible de récupérer le fichier");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (IOException e)
		{
			System.err.println("---- ECHEC ---- Probleme IOException");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	public void testSupprimerFichier()
	{
		// Test de suppression du fichier
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("----Suppression du fichier ----");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			ftp.supprimerFichier("testJUnitClassFTP.txt");
			System.out.println("Fichier Supprimer");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (SupprimerFichierException e)
		{
			System.err.println("---- ECHEC ---- Erreur de suppression");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	public void testCreerRepertoire()
	{
		// Creer un répertoire sur le serveur marine.edu.ups-tlse.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Creer un répertoire ----");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			ftp.creerRepertoire("JUnitCreationRepFTP");
			System.out.println("Répertoire crée");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (CreerRepertoireException e)
		{
			System.err.println("---- ECHEC ---- Probleme de creation de repertoire");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.err.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	public void testChangerRepertoire()
	{
		// Changer de répertoire sur le serveur marine.edu.ups-tlse.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Changer de répertoire ---- ");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			ftp.changerRepertoire("JUnitCreationRepFTP");
			System.out.println("Repertoire courant : " + ftp.getRepertoireCourant());
			System.out.println("Changement de répertoire réussi");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (ChangerRepertoireException e)
		{
			System.err.println("---- ECHEC ---- Impossible d'aller dans le repertoire");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.out.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (RepertoireCourantException e)
		{
			System.out.println("---- ECHEC ---- Probleme de récupération du répertoire courant");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}
	
	public void testSupprimerRepertoire()
	{
		// Supprimer le répertoire sur le serveur marine.edu.ups-tlse.fr
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Suppressions de répertoire ---- ");
			ftp.seConnecter("ftp.cict.fr", "domino", "Nothomb");
			ftp.supprimerRepertoire("JUnitCreationRepFTP");
			assertTrue(true);
			System.out.println("---- OK ----");
			System.out.println("----------------------------------------------");
		}
		catch (ConnexionException e)
		{
			System.err.println("---- ECHEC ---- Probleme de connexion");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (LoginFTPException e)
		{
			System.out.println("---- ECHEC ---- Probleme de login");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
		catch (SupprimerRepertoireException e)
		{
			System.out.println("---- ECHEC ---- Erreur lors de la suppression du répertoire");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}
}
