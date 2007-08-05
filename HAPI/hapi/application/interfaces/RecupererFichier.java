/**
 * @author Cédric
 */

package hapi.application.interfaces;

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

import java.io.InputStream;
import java.util.Vector;

/**
 * Interface des accesseur FTP et LOCAL
 */
public interface RecupererFichier
{
	//Se connecter à un serveur
	abstract public void seConnecter(String chemin) throws ConnexionException;
	abstract public void seConnecter(String chemin, String login, String motDePasse) throws ConnexionException, LoginFTPException;

	//Lister les fichier du dossier courant
	abstract public Vector listerFichiers() throws ListerFichiersException;
	abstract public Vector listerFichiers(String extension) throws ListerFichiersException;

	//Récupérer un fichier
	abstract public InputStream recupererFichier(String nomFichier) throws RecupererFichierException;

	//Déposer un fichier
	abstract public void envoyerFichier(String nomFichier) throws EnvoyerFichierException;

	//Création d'un dossier
	abstract public void creerRepertoire(String nomDossier) throws CreerRepertoireException;

	//Commande pour supprimer un répertoire
	abstract public void supprimerRepertoire(String nomDossier) throws SupprimerRepertoireException;

	//Commande pour supprimer un fichier.	 
	abstract public void supprimerFichier(String nomFichier) throws SupprimerFichierException;

	//Déplacement dans un dossier (gérer les . et ..)
	abstract public void changerRepertoire(String nomDossier) throws ChangerRepertoireException;

	//Retourne la position actuelle
	abstract public String getRepertoireCourant() throws RepertoireCourantException;
}
