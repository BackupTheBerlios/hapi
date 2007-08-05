/*
 * Fichier TransfertFichier.java
 * Auteur C�dric
 *
 */
package hapi.application;

import hapi.donnees.modeles.ModeleLDserveurs;
import hapi.donnees.modeles.ModeleListe;
import hapi.exception.ConnexionException;
import hapi.exception.ListerFichiersException;
import hapi.exception.LoginFTPException;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * Commentaire sur la classe/la m�thode
 */
public abstract class C_TransfertFichier
{
	//Attributs de la classe 
	protected ModeleListe leModeleFichier = null;
	protected ModeleListe leModeleTelechargement = null;
	protected ModeleLDserveurs leModeleChemin = null;

	/**
	 * Constructeur pr�parant les mod�les
	 */
	public C_TransfertFichier()
	{
		leModeleFichier = new ModeleListe();
		leModeleTelechargement = new ModeleListe();
		leModeleChemin = new ModeleLDserveurs();
	}

	/**
	 * R�cup�ration du mod�le des fichiers
	 */
	public ModeleListe getModeleFichiers()
	{
		return leModeleFichier;
	}

	/**
	 * R�cup�ration du mod�le des fichiers
	 */
	public DefaultComboBoxModel getModeleChemin()
	{
		return leModeleChemin;
	}

	/**
	 * R�cup�ration du mod�le de la b�ite de t�l�chargement
	 */
	public ModeleListe getModeleTelechargement()
	{
		return leModeleTelechargement;
	}

	/**
	 * Effacement de la liste des fichiers rep�r�s
	 */
	public void effacerListeFichiers()
	{
		leModeleFichier.clearDonnees();
	}

	/**
	 * R�cup�ration de la liste des fichiers en local
	 */
	public void getListeFichiersLocal(String extension, String chemin, String login, String passwd) throws ConnexionException, ListerFichiersException
	{
		C_Local local = new C_Local();

		local.seConnecter(chemin, login, passwd);

		// R�cup�ration de la liste des fichiers et affectation au modele
		Vector lesFichiers = local.listerFichiers(extension);
		for (int i = 0; i < lesFichiers.size(); i++)
			leModeleFichier.addDonnees(lesFichiers.get(i));
	}

	/**
	 * R�cup�ration des fichiers sur le serveur distant
	 */
	public void getListeFichiers(String extension, String chemin, String login, String passwd) throws ListerFichiersException, ConnexionException, LoginFTPException
	{
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);

		// R�cup�ration de la liste des fichiers et affectation au modele
		Vector lesFichiers = ftp.listerFichiers(extension);
		for (int i = 0; i < lesFichiers.size(); i++)
			leModeleFichier.addDonnees(lesFichiers.get(i));
	}
	
	/**
	 * Sauvegarde du serveur s�lectionn�
	 */
	public abstract void sauvegarderSelectionChemin(String lAdresse);

	public abstract void addChemin(String chemin);
	/**
	* R�cup�ration du login du serveur � partir de son adresse
	*/
	public abstract String getLogin(String adresse);
	/**
	* R�cup�ration du mot de passe du serveur � partir de son adresse
	*/
	public abstract String getPWD(String adresse);	
}
