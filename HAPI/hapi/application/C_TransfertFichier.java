/*
 * Fichier TransfertFichier.java
 * Auteur Cédric
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
 * Commentaire sur la classe/la méthode
 */
public abstract class C_TransfertFichier
{
	//Attributs de la classe 
	protected ModeleListe leModeleFichier = null;
	protected ModeleListe leModeleTelechargement = null;
	protected ModeleLDserveurs leModeleChemin = null;

	/**
	 * Constructeur préparant les modèles
	 */
	public C_TransfertFichier()
	{
		leModeleFichier = new ModeleListe();
		leModeleTelechargement = new ModeleListe();
		leModeleChemin = new ModeleLDserveurs();
	}

	/**
	 * Récupération du modèle des fichiers
	 */
	public ModeleListe getModeleFichiers()
	{
		return leModeleFichier;
	}

	/**
	 * Récupération du modèle des fichiers
	 */
	public DefaultComboBoxModel getModeleChemin()
	{
		return leModeleChemin;
	}

	/**
	 * Récupération du modèle de la bôite de téléchargement
	 */
	public ModeleListe getModeleTelechargement()
	{
		return leModeleTelechargement;
	}

	/**
	 * Effacement de la liste des fichiers repérés
	 */
	public void effacerListeFichiers()
	{
		leModeleFichier.clearDonnees();
	}

	/**
	 * Récupération de la liste des fichiers en local
	 */
	public void getListeFichiersLocal(String extension, String chemin, String login, String passwd) throws ConnexionException, ListerFichiersException
	{
		C_Local local = new C_Local();

		local.seConnecter(chemin, login, passwd);

		// Récupération de la liste des fichiers et affectation au modele
		Vector lesFichiers = local.listerFichiers(extension);
		for (int i = 0; i < lesFichiers.size(); i++)
			leModeleFichier.addDonnees(lesFichiers.get(i));
	}

	/**
	 * Récupération des fichiers sur le serveur distant
	 */
	public void getListeFichiers(String extension, String chemin, String login, String passwd) throws ListerFichiersException, ConnexionException, LoginFTPException
	{
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);

		// Récupération de la liste des fichiers et affectation au modele
		Vector lesFichiers = ftp.listerFichiers(extension);
		for (int i = 0; i < lesFichiers.size(); i++)
			leModeleFichier.addDonnees(lesFichiers.get(i));
	}
	
	/**
	 * Sauvegarde du serveur sélectionné
	 */
	public abstract void sauvegarderSelectionChemin(String lAdresse);

	public abstract void addChemin(String chemin);
	/**
	* Récupération du login du serveur à partir de son adresse
	*/
	public abstract String getLogin(String adresse);
	/**
	* Récupération du mot de passe du serveur à partir de son adresse
	*/
	public abstract String getPWD(String adresse);	
}
