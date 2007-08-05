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
import hapi.exception.LoginFTPException;
import hapi.exception.RecupererFichierException;
import hapi.exception.RepertoireCourantException;
import hapi.exception.SupprimerFichierException;
import hapi.exception.SupprimerRepertoireException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

public class C_FTP implements RecupererFichier
{
	/**
	 * Le socket avec lequel on se connecte
	 */
	private Socket connexionSocket = null;

	/**
	 * Flux général de sortie
	 */
	private PrintStream outputStream = null;

	/**
	 * Flux général d'entrée
	 */
	private BufferedReader inputStream = null;

	/**
	 * Taille du buffer pour les transfers
	 */
	private static int BLOCK_SIZE = 4096;

	/**
	 * Socket spécifique au mode passif.
	 */
	private Socket pasvSocket = null;

	/**
	 * Connexion au serveur FTP en anonyme
	 */
	public void seConnecter(String chemin) throws ConnexionException
	{
		try
		{
			seConnecter(chemin, "anonymous", "adresse@bidon");
		}
		catch (LoginFTPException e)
		{
			//Exception à priori impossible
		}
	}

	/**
	 * Connexion et identification au serveur FTP
	 */
	public void seConnecter(String chemin, String login, String motDePasse) throws ConnexionException, LoginFTPException
	{
		try
		{
			/* Phase de connection */
			connexionSocket = new Socket(chemin, 21);
			outputStream = new PrintStream(connexionSocket.getOutputStream());
			inputStream = new BufferedReader(new InputStreamReader(connexionSocket.getInputStream()));

			if (!isReponseCompletePositive(getReponseServeur()))
				throw new ConnexionException();

			/* Phase de login */
			int reponse = executerCommande("user " + login);
			if (!isReponseIntermediairePositive(reponse))
				throw new LoginFTPException();
			reponse = executerCommande("pass " + motDePasse);
			if (!isReponseCompletePositive(reponse))
				throw new LoginFTPException();
		}
		catch (UnknownHostException uhe)
		{
			throw new ConnexionException();
		}
		catch (IOException uhe)
		{
			throw new ConnexionException();
		}
	}

	/**
	 * Fermeture de la connexion
	 */
	protected void finalize()
	{
		try
		{
			connexionSocket.close();
		}
		catch (IOException e)
		{}
	}

	/**
	 * On récupère la liste des fichiers présents dans le dossier courant.
	 */
	public Vector listerFichiers() throws ListerFichiersException
	{
		return listerFichiers("*");
	}

	/**
	 * On récupère la liste des fichiers ayant une extension particuliere.
	 */
	public Vector listerFichiers(String extension) throws ListerFichiersException
	{
		if (extension != "*")
			extension = "*." + extension;
		Vector listeFichiers = new Vector();

		// On fait les demmandes de listage avec les commandes NLST et LIST
		try
		{
			String shortList = cmdListerFichiers("NLST " + extension);
			String longList = cmdListerFichiers("LIST " + extension);
			// On tokenize les lignes récupérées
			StringTokenizer sList = new StringTokenizer(shortList, "\n");
			StringTokenizer lList = new StringTokenizer(longList, "\n");

			String sString;
			String lString;

			// A noter que les deux lists ont le même nombre de ligne.
			while ((sList.hasMoreTokens()) && (lList.hasMoreTokens()))
			{
				sString = sList.nextToken();
				lString = lList.nextToken();

				if (lString.length() > 0)
				{
					if (lString.startsWith("-"))
					{
						listeFichiers.add(sString.trim());
					}
				}
			}
			return listeFichiers;
		}
		catch (IOException e)
		{
			throw new ListerFichiersException();
		}
	}

	/**
	 * Récupère un fichier sur le serveur
	 */
	public InputStream recupererFichier(String nomFichier) throws RecupererFichierException
	{
		try
		{
			if (!configurerExecutionCommande("retr " + nomFichier))
				throw new RecupererFichierException();
			InputStream in = pasvSocket.getInputStream();
			return in;
		}
		catch (IOException ioe)
		{
			throw new RecupererFichierException();
		}
	}

	/**
	 * Commande pour envoyer un fichier sur le serveur.
	 */
	public void envoyerFichier(String nomFichier) throws EnvoyerFichierException
	{
		//On supprime le chemin du fichier pour ne recuperer que le nom
		int i = nomFichier.length() - 1;
		while ((nomFichier.charAt(i) != File.separator.charAt(0)) && (i >= 0))
			i--;
		String nomFicDest = nomFichier.substring(i + 1, nomFichier.length());

		try
		{
			if (!ecrireDonneesFichier("stor " + nomFicDest, nomFichier))
				throw new EnvoyerFichierException();
		}
		catch (IOException ioe)
		{
			throw new EnvoyerFichierException();
		}
	}

	/**
	 * Commande pour créer un répertoire
	 */
	public void creerRepertoire(String nomDossier) throws CreerRepertoireException
	{
		try
		{
			int reponse = executerCommande("mkd " + nomDossier);
			if (!isReponseCompletePositive(reponse))
				throw new CreerRepertoireException();
		}
		catch (IOException ioe)
		{
			throw new CreerRepertoireException();
		}
	}

	/**
	 * Commande pour supprimer un répertoire
	 */
	public void supprimerRepertoire(String nomDossier) throws SupprimerRepertoireException
	{
		try
		{
			int reponse = executerCommande("rmd " + nomDossier);
			if (!isReponseCompletePositive(reponse))
				throw new SupprimerRepertoireException();
		}
		catch (IOException ioe)
		{
			throw new SupprimerRepertoireException();
		}
	}

	/**
	 * Commande pour supprimer un fichier.
	 */
	public void supprimerFichier(String nomFichier) throws SupprimerFichierException
	{
		try
		{
			int reponse = executerCommande("dele " + nomFichier);
			if (!isReponseCompletePositive(reponse))
				throw new SupprimerFichierException();
		}
		catch (IOException ioe)
		{
			throw new SupprimerFichierException();
		}
	}

	/**
	 * Commande pour changer de répertoire
	 */
	public void changerRepertoire(String nomDossier) throws ChangerRepertoireException
	{
		try
		{
			int reponse = executerCommande("cwd " + nomDossier);
			if (!isReponseCompletePositive(reponse))
				throw new ChangerRepertoireException();
		}
		catch (IOException ioe)
		{
			throw new ChangerRepertoireException();
		}

	}

	/**
	 * On récupère le nom du répertoire courant.
	 */
	public String getRepertoireCourant() throws RepertoireCourantException
	{
		String reponse;
		try
		{
			reponse = getExecutionReponse("pwd");
		}
		catch (IOException e)
		{
			throw new RepertoireCourantException();
		}
		StringTokenizer strtok = new StringTokenizer(reponse);

		// On recupere le nom du repertoire
		if (strtok.countTokens() < 2)
			return null;
		strtok.nextToken();
		String nomRep = strtok.nextToken();

		// Les serveurs mettent la plupart du temps des guillemets, on les
		// supprime
		int longueurChaine = nomRep.length();
		if (longueurChaine == 0)
			return null;
		if (nomRep.charAt(0) == '\"')
		{
			nomRep = nomRep.substring(1);
			longueurChaine--;
		}
		if (nomRep.charAt(longueurChaine - 1) == '\"')
			return nomRep.substring(0, longueurChaine - 1);
		return nomRep;
	}

	/**
	 * Récupèrer le code de réponse du server FTP. En effet les serveurs FTP
	 * répondent par des phrases de ce type "xxx message". L'on récupère donc ce
	 * code à 3 chiffres pour identifier la nature de la réponse (erreur, envoi,
	 * etc...).
	 */
	private int getReponseServeur() throws IOException
	{
		return Integer.parseInt(getReponseCompleteServeur().substring(0, 3));
	}

	/**
	 * On retourne la dèrnière ligne de réponse du serveur.
	 */
	private String getReponseCompleteServeur() throws IOException
	{
		String reponse;
		do
		{
			reponse = inputStream.readLine();
		} while (!(Character.isDigit(reponse.charAt(0)) && Character.isDigit(reponse.charAt(1)) && Character.isDigit(reponse.charAt(2)) && reponse.charAt(3) == ' '));

		return reponse;
	}

	/**
	 * Envoi et récupère le résultat d'une commande de listage tel que LIST ou
	 * NLIST.
	 */
	private String cmdListerFichiers(String command) throws IOException
	{
		StringBuffer reponse = new StringBuffer();
		String chaineReponse;

		if (!executerCommande(command, reponse))
			return "";

		chaineReponse = reponse.toString();

		if (reponse.length() > 0)
			return chaineReponse.substring(0, reponse.length() - 1);
		else
			return chaineReponse;
	}

	/**
	 * Execute une commande simple sur le FTP et retourne juste le code réponse
	 * du message de retour.
	 */
	public int executerCommande(String command) throws IOException
	{
		outputStream.println(command);
		return getReponseServeur();
	}

	/**
	 * Execute une commande FTP et retourne la dernière ligne de réponse du
	 * serveur.
	 */
	public String getExecutionReponse(String command) throws IOException
	{
		outputStream.println(command);
		return getReponseCompleteServeur();
	}

	/**
	 * Execute une commande depuis le contenu d'un fichier. Cette fonction
	 * retourne un boulean pour être tenu au courant du bon déroulemet de
	 * l'opération.
	 */
	public boolean ecrireDonneesFichier(String command, String fileName) throws IOException
	{
		// On ouvre le fichier en local
		RandomAccessFile infile = new RandomAccessFile(fileName, "r");

		// Converti le RandomAccessFile en un InputStream
		FileInputStream fileStream = new FileInputStream(infile.getFD());
		boolean success = executerCommande(command, fileStream);

		infile.close();

		return success;
	}

	/**
	 * Exécute une commande sur le serveur FTP depuis le flux d'entrée spécifié
	 * en argument. Retourne true si l'opération c'est effectuée sans problème,
	 * sinon false.
	 */
	public boolean executerCommande(String command, InputStream in) throws IOException
	{
		if (!configurerExecutionCommande(command))
			return false;
		OutputStream out = pasvSocket.getOutputStream();
		transfertDonnees(in, out);
		out.close();
		pasvSocket.close();

		return isReponseCompletePositive(getReponseServeur());
	}

	/**
	 * Exécute une commande sur le serveur FTP et retourne le résultat dans un
	 * buffer spécifié en argument. Retourne true si l'opération s'est effectuée
	 * sans problème, sinon false.
	 */
	public boolean executerCommande(String command, StringBuffer sb) throws IOException
	{
		if (!configurerExecutionCommande(command))
			return false;
		InputStream in = pasvSocket.getInputStream();
		transfertDonnees(in, sb);
		in.close();
		pasvSocket.close();

		return isReponseCompletePositive(getReponseServeur());
	}

	/**
	 * Transfer des données depuis un flux d'entrée vers un flux de sortie.
	 */
	private void transfertDonnees(InputStream in, OutputStream out) throws IOException
	{
		byte b[] = new byte[BLOCK_SIZE];
		int nb;

		// Stocke les données dans un fichier
		while ((nb = in.read(b)) > 0)
			out.write(b, 0, nb);
	}

	/**
	 * Transfer des données depuis un flux d'entrée vers un buffer.
	 */
	private void transfertDonnees(InputStream in, StringBuffer sb) throws IOException
	{
		byte b[] = new byte[BLOCK_SIZE];
		int nb;

		// Stock les donnés dans un buffer
		while ((nb = in.read(b)) > 0)
			sb.append(new String(b, 0, nb));
	}

	/**
	 * On execute la commande donnée en spécifiant au préalable: - le PORT sur
	 * lequel l'on va se connecter - le type de transfer. "TYPE i" pour un
	 * transfer binaire. - le restartpoint si il existe Si l'oprération s'est
	 * effectuée avec succès on retourne true, sinon false.
	 */
	private boolean configurerExecutionCommande(String command) throws IOException
	{
		if (!recupererIpPort())
			return false;

		// Lance le mode binaire pour la récéption des donnés
		outputStream.println("TYPE i");
		if (!isReponseCompletePositive(getReponseServeur()))
			return false;

		// Envoi de la commande
		outputStream.println(command);

		return isReponsePreliminairePositive(getReponseServeur());
	}

	/**
	 * On demande au serveur FTP sur quelle adresse IP et quel numero de PORT on
	 * va établir la connexion.
	 */
	private boolean recupererIpPort() throws IOException
	{
		//On passe en mode passif
		String tmp = getExecutionReponse("PASV");
		String pasv = supprimerCodeReponse(tmp);
		//On récupère l'IP et le PORT pour la connection
		pasv = pasv.substring(pasv.indexOf("(") + 1, pasv.indexOf(")"));
		String[] splitedPasv = pasv.split(",");
		int port1 = Integer.parseInt(splitedPasv[4]);
		int port2 = Integer.parseInt(splitedPasv[5]);
		int port = (port1 * 256) + port2;
		String ip = splitedPasv[0] + "." + splitedPasv[1] + "." + splitedPasv[2] + "." + splitedPasv[3];

		pasvSocket = new Socket(ip, port);

		return isReponseCompletePositive(Integer.parseInt(tmp.substring(0, 3)));
	}

	/**
	 * Retourne true si le code réponse du serveur est compris entre 100 et 199.
	 */
	private boolean isReponsePreliminairePositive(int response)
	{
		return (response >= 100 && response < 200);
	}

	/**
	 * Retourne true si le code réponse du serveur est compris entre 300 et 399.
	 */
	private boolean isReponseIntermediairePositive(int response)
	{
		return (response >= 300 && response < 400);
	}

	/**
	 * Retourne true si le code réponse du serveur est compris entre 200 et 299.
	 */
	private boolean isReponseCompletePositive(int response)
	{
		return (response >= 200 && response < 300);
	}

	/**
	 * Supprime le code réponse au début d'une string.
	 */
	private String supprimerCodeReponse(String response)
	{
		if (response.length() < 5)
			return response;
		return response.substring(4);
	}
}
