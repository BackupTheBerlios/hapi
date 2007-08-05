package Tests;

import hapi.application.C_Local;
import hapi.exception.ChangerRepertoireException;
import hapi.exception.ConnexionException;
import hapi.exception.CreerRepertoireException;
import hapi.exception.EnvoyerFichierException;
import hapi.exception.ListerFichiersException;
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
public class C_LocalTest extends TestCase
{
	C_Local local;

	public C_LocalTest(String arg0)
	{
		super(arg0);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		local = new C_Local();
	}

	/*
	 * Test for void seConnecter(String)
	 */
	public void testSeConnecterString()
	{
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion anonyme en local ----");
			local.seConnecter("C:\\");
			Vector listFic = local.listerFichiers();
			System.out.println("Liste des fichiers du dossier : ");
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

		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Connexion à à un dossier inexistant ----");
			local.seConnecter("serveur.bidon");
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
			System.out.println("---- Connexion à avec login et pwd ----");
			local.seConnecter("C:\\", "sdfgsdfg", "sdfgsdfg");

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
	}

	/*
	 * Test for Vector listerFichiers()
	 */
	public void testListerFichiers()
	{
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Lister les fichiers de C: ----");
			local.seConnecter("C:\\");
			Vector listFic = new Vector(local.listerFichiers());
			System.out.println("Liste des fichiers du dossier : ");
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
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Lister les fichiers .txt sur C: ----");
			local.seConnecter("C:\\");
			Vector listFic = new Vector(local.listerFichiers("txt"));
			System.out.println("Liste des fichiers .txt du dossier : ");
			for (int i = 0; i < listFic.size(); i++)
				System.out.println(listFic.get(i));
			System.out.println("Fin du listing des .txt");
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
		File f = new File("C:\\Program Files\\testJUnitClassFTPLocal.txt");
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
			System.out.println("Connexion à un dossier");
			local.seConnecter("C:\\");
			local.envoyerFichier("C:\\Program Files\\testJUnitClassFTPLocal.txt");
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
			local.seConnecter("C:\\", "domino", "Nothomb");
			InputStream in = local.recupererFichier("testJUnitClassFTPLocal.txt");
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
	}

	public void testSupprimerFichier()
	{
		// Test de suppression du fichier
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("----Suppression du fichier ----");
			local.seConnecter("C:\\");
			local.supprimerFichier("testJUnitClassFTPLocal.txt");
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
		catch (SupprimerFichierException e)
		{
			System.err.println("---- ECHEC ---- Erreur de suppression");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}

	public void testCreerRepertoire()
	{
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Creer un répertoire ----");
			local.seConnecter("C:\\");
			local.creerRepertoire("JUnitCreationRepFTP");
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
	}

	public void testChangerRepertoire()
	{
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Changer de répertoire ---- ");
			local.seConnecter("C:\\");
			local.changerRepertoire("JUnitCreationRepFTP");
			System.out.println("Repertoire courant : " + local.getRepertoireCourant());
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
		catch (RepertoireCourantException e)
		{
			System.out.println("---- ECHEC ---- Probleme de récupération du répertoire courant");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}
	
	public void testSupprimerRepertoire()
	{
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("---- Suppressions de répertoire ---- ");
			local.seConnecter("C:\\");
			local.supprimerRepertoire("JUnitCreationRepFTP");
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
		catch (SupprimerRepertoireException e)
		{
			System.out.println("---- ECHEC ---- Erreur lors de la suppression du répertoire");
			assertTrue(false);
			System.out.println("----------------------------------------------");
		}
	}
}