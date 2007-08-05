/*
 * Auteur Cédric
 *
 */
package hapi.donnees;

import hapi.exception.FichierConfInexistantException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Cette classe contient pour une session les informations de configuration
 */
public class E_Configuration
{
	private static final String CHEMIN_CONFIGURATION = "." + File.separator + "configuration" + File.separator;
	private static final String FICHIER_CONFIGURATION = CHEMIN_CONFIGURATION + "configLocal" + ".cfg";
	private static final String VERSION_CONFIGURATION = "1.2";
	private static boolean fichierCharge = false;
	private static String serveurBaseDeDonnees = null;
	private static String nomBase = null;
	private static String loginBaseDeDonnees = null;
	private static String pwdBaseDeDonnees = null;
	private static String langue = new String("fr");
	private static String lookAndFeel = new String("Metal");

	static {
		serveurBaseDeDonnees = new String();
		nomBase = new String();
		loginBaseDeDonnees = new String();
		pwdBaseDeDonnees = new String();
		fichierCharge = false;
	}

	public static String getLoginBaseDeDonnees()
	{
		return loginBaseDeDonnees;
	}

	public static String getPwdBaseDeDonnees()
	{
		return pwdBaseDeDonnees;
	}

	public static String getServeurBaseDeDonnees()
	{
		return serveurBaseDeDonnees;
	}

	public static void setLoginBaseDeDonnees(String login)
	{
		loginBaseDeDonnees = login;
	}

	public static void setPwdBaseDeDonnees(String pwd)
	{
		pwdBaseDeDonnees = pwd;
	}

	public static void setServeurBaseDeDonnees(String serveur)
	{
		serveurBaseDeDonnees = serveur;
	}

	/**
	 * Chargement du chemin de la base trouvé dans le fichier 
	 */
	public static void chargerCheminBase() throws IOException, FichierConfInexistantException
	{
		//Vérification si le fichier existe
		File file = new File(FICHIER_CONFIGURATION);
		if (!file.exists())
			throw new FichierConfInexistantException();

		//Lecture du fichier local contenant le chemin vers la base
		RandomAccessFile fichier = new RandomAccessFile(file, "r");
		//Vérification que la première ligne correspond bien au numaro de version
		if (!fichier.readUTF().equals(VERSION_CONFIGURATION))
			throw new FichierConfInexistantException();
		//Lecture des champs
		serveurBaseDeDonnees = fichier.readUTF();
		nomBase = fichier.readUTF();
		loginBaseDeDonnees = fichier.readUTF();
		pwdBaseDeDonnees = fichier.readUTF();
		langue = fichier.readUTF();
		lookAndFeel = fichier.readUTF();

		// Fermeture du fichier
		fichier.close();
		//Modification de l'indicateur
		fichierCharge = true;
	}

	public static boolean isFichierCharge()
	{
		return fichierCharge;
	}

	public static void creerFichierLocal() throws IOException
	{
		if (!fichierCharge)
		{
			//Création du répertoire
			File file = new File(CHEMIN_CONFIGURATION);
			file.mkdirs();
			//Création du fichier
			file = new File(FICHIER_CONFIGURATION);
			file.createNewFile();
		}
	}

	/**
	 * Application des paramètres dans le fichier
	 */
	public static void mettreAJourFichier() throws FichierConfInexistantException, IOException
	{
		//Vérification si le fichier existe
		File file = new File(FICHIER_CONFIGURATION);
		if (!file.exists())
			throw new FichierConfInexistantException();

		//Lecture du fichier local contenant le chemin vers la base
		RandomAccessFile fichier = new RandomAccessFile(file, "rw");
		//Insertion du numéro de version
		fichier.writeUTF(VERSION_CONFIGURATION);
		//Ecriture des champs
		fichier.writeUTF(serveurBaseDeDonnees);
		fichier.writeUTF(nomBase);
		fichier.writeUTF(loginBaseDeDonnees);
		fichier.writeUTF(pwdBaseDeDonnees);
		fichier.writeUTF(langue);
		fichier.writeUTF(lookAndFeel);
		//Fermeture du fichier
		fichier.close();
		//Modification de l'indicateur
		fichierCharge = true;
	}

	public static String getLangue()
	{
		return langue;
	}

	public static void setLangue(String laLangue)
	{
		langue = laLangue.substring(0, 2);
	}

	public static String getLookAndFeel()
	{
		return lookAndFeel;
	}

	public static void setLookAndFeel(String string)
	{
		lookAndFeel = string;
	}

	public static String getNomBase()
	{
		return nomBase;
	}

	public static void setNomBase(String string)
	{
		nomBase = string;
	}

}
