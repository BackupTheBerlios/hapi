/**
 * @author Stéphane
 */

package hapi.application;

import hapi.application.interfaces.RecupererFichier;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class C_Local implements RecupererFichier
{
	private File rep;

	public C_Local()
	{}

	//Se "connecter"
	public void seConnecter(String chemin) throws ConnexionException
	{
		rep = new File(chemin);
		if (!rep.isDirectory() || !rep.exists())
			throw new ConnexionException();
	}

	public void seConnecter(String chemin, String login, String motDePasse) throws ConnexionException
	{
		seConnecter(chemin);
	}

	//Lister les fichier du dossier courant
	public Vector listerFichiers() throws ListerFichiersException
	{
		return listerFichiers("");
	}

	public Vector listerFichiers(String extension) throws ListerFichiersException
	{
		File[] list = rep.listFiles();
		Vector v = new Vector();
		for (int i = 0; i < list.length; i++)
			if ((list[i].isFile()) && (list[i].getName().endsWith(extension)))
				v.add(list[i].getName());
		return v;
	}

	//Récupérer un fichier
	public InputStream recupererFichier(String nomFichier) throws RecupererFichierException
	{
		FileInputStream in;
		try
		{
			in = new FileInputStream(rep.getAbsolutePath() + File.separator + nomFichier);
			return in;
		}
		catch (FileNotFoundException e)
		{
			throw new RecupererFichierException();
		}
	}

	//Déposer un fichier
	public void envoyerFichier(String nomFichier) throws EnvoyerFichierException
	{
		// Declaration des flux
		FileInputStream fichierSource = null;
		FileOutputStream fichierDestination = null;

		//On supprime le chemin du fichier pour ne recuperer que le nom
		int i = nomFichier.length() - 1;
		while ((nomFichier.charAt(i) != File.separator.charAt(0)) && (i >= 0))
			i--;

		File ficDest;
		try
		{
			ficDest = new File(getRepertoireCourant() + File.separator + nomFichier.substring(i + 1, nomFichier.length()));
			//if (ficDest.exists())
			//	throw new EnvoyerFichierException();
		}
		catch (RepertoireCourantException e)
		{
			throw new EnvoyerFichierException();
		}

		try
		{
			// Ouverture des flux
			fichierSource = new FileInputStream(nomFichier);
			fichierDestination = new FileOutputStream(ficDest.getAbsoluteFile());

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLus;

			while ((nbLus = fichierSource.read(buffer)) != -1)
				fichierDestination.write(buffer, 0, nbLus);
		}
		catch (java.io.FileNotFoundException f)
		{
			throw new EnvoyerFichierException();
		}
		catch (java.io.IOException e)
		{
			throw new EnvoyerFichierException();
		}
		finally
		{
			// Quoi qu'il arrive, on ferme les flux
			try
			{
				fichierSource.close();
				fichierDestination.close();
			}
			catch (Exception e)
			{
				throw new EnvoyerFichierException();
			}
		}

	}

	//Création d'un dossier
	public void creerRepertoire(String nomDossier) throws CreerRepertoireException
	{
		File newRep = new File(rep.getAbsolutePath() + File.separator + nomDossier);
		if (!newRep.exists())
			newRep.mkdir();
		else
			throw new CreerRepertoireException();
	}

	//Commande pour supprimer un répertoire
	public void supprimerRepertoire(String nomDossier) throws SupprimerRepertoireException
	{
		File oldRep = new File(rep.getAbsolutePath() + File.separator + nomDossier);
		if (oldRep.exists() && oldRep.isDirectory())
			oldRep.delete();
		else
			throw new SupprimerRepertoireException();
	}

	//Commande pour supprimer un fichier.
	public void supprimerFichier(String nomFichier) throws SupprimerFichierException
	{
		File oldFic = new File(rep.getAbsolutePath() + File.separator + nomFichier);
		if (oldFic.exists() && oldFic.isFile())
		{
			oldFic.delete();
		}
		else
			throw new SupprimerFichierException();
	}

	//Déplacement dans un dossier (gérer les . et ..)
	public void changerRepertoire(String nomDossier) throws ChangerRepertoireException
	{
		String[] tabString;
		if (File.separator.charAt(0) == '/')
			tabString = nomDossier.split(File.separator);
		else
			tabString = nomDossier.split(File.separator + File.separator);

		File newRep = null;

		for (int i = 0; i < tabString.length; i++)
		{
			if (tabString[i] == "..")
			{
				newRep = new File(rep.getParent());
				if (newRep.exists() && newRep.isDirectory())
					rep = newRep;
				else
					throw new ChangerRepertoireException();
			}
			else if (tabString[i] != ".")
			{
				try
				{
					newRep = new File(rep.getCanonicalPath() + File.separator + tabString[i]);
				}
				catch (IOException e)
				{
					throw new ChangerRepertoireException();
				}
				if (newRep.exists() && newRep.isDirectory())
					rep = newRep;
				else
					throw new ChangerRepertoireException();
			}
		}
	}

	//Retourne la position courante
	public String getRepertoireCourant() throws RepertoireCourantException
	{
		return rep.getAbsolutePath();
	}
}
