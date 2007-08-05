/*
 * Auteur Cédric
 */
package hapi.application.ressources;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * La classe permet de gérer le changement de langues
 */
public class Bundle
{

	//Chemin d'acces aux fichiers langue
	private static final String BASENAME = "Langues" + File.separator;
	private static final String FILENAME = "HAPI_trad";
	//Modele défini pour les champs de saisie de décimaux
	public static final String MODEL_DECIMAL = "#############0.0#############";
	public static DecimalFormat formatDecimal = new DecimalFormat(Bundle.MODEL_DECIMAL);
	public static final String MODEL_DECIMAL_COURT = "###0.0";
	public static DecimalFormat formatDecimalCourt = new DecimalFormat(Bundle.MODEL_DECIMAL_COURT);
	//Modele defini pour les champs date
	public static final String DATE_MODEL = "dd/MM/yyyy";
	public static final String PETITMOIS_MODEL = "MM/yyyy";
	public static final String MOIS_MODEL = "MMMMM yyyy";
	public static SimpleDateFormat formatDate = new SimpleDateFormat(Bundle.DATE_MODEL);
	public static SimpleDateFormat formatPetitMois = new SimpleDateFormat(Bundle.PETITMOIS_MODEL);
	//Model defini pour les champs entier
	public static final String NATUREL_FORMAT = "################";
	public static DecimalFormat naturelFormat = new DecimalFormat(Bundle.NATUREL_FORMAT);
	//Affectation du bundle
	private static ResourceBundle bundle = ResourceBundle.getBundle(BASENAME + FILENAME);
	;
	//Liste des langues disponibles
	private static Vector languesDisponibles = new Vector();

	/**
	 * Récupération des langues disponibles 
	 */
	static {
		//Pacours des ressources
		File file = new File(BASENAME);
		//Liste des fichiers de ce répertoire
		File[] fliste = file.listFiles();
		//Pour chaque fichier
		for (int i = 0; i < fliste.length; i++)
		{
			//Si c'est un fichier
			if (fliste[i].isFile())
			{
				//Si le fichier courant correspond bien un fichier.lng, c'est un fichier de langue
				if (fliste[i].getName().substring(fliste[i].getName().length() - 1 - "properties".length()).equals(".".concat("properties")))
				{
					//Récupération de l'identifiant de la langue
					Locale laLocale = new Locale(fliste[i].getName().substring(fliste[i].getName().lastIndexOf("_") + 1, fliste[i].getName().lastIndexOf(".")));
					Locale.setDefault(laLocale);
					//Ajout dans le vecteur
					languesDisponibles.add(mettreMajuscule(laLocale.getDisplayLanguage()));
				}
			}
		}
		//Locale par défaut
		Locale.setDefault(Locale.FRENCH);
	}

	/**
	 * Méthode permettant de mettre la première lettre en majuscule
	 */
	private static String mettreMajuscule(String source)
	{
		//Récupération du premier caractère après l'avoir mis en majuscule
		char premier = source.toUpperCase().charAt(0);
		//Modification du premier caractère
		return premier + source.substring(1);
	}

	/**
	 * Affectation de la locale
	 */
	static public void setLocaleCourante(Locale laLocale)
	{
		try
		{
			//Récupération du bundle
			bundle = ResourceBundle.getBundle(BASENAME + FILENAME, laLocale);
			//Affectation de la locale
			Locale.setDefault(laLocale);
			Bundle.formatDecimal = new DecimalFormat(Bundle.MODEL_DECIMAL);
		}
		catch (MissingResourceException Ex)
		{
			//Message d'erreur en dur si la resource n'est pas trouvée
			JOptionPane.showMessageDialog(null, "Le fichier de langue demandé est introuvable !", "Erreur", JOptionPane.ERROR_MESSAGE);
			Locale.setDefault(Locale.getDefault());
		}

	}

	/**
	 * Récupération de la traduction
	 */
	static public String getText(String key)
	{
		String texte = null;

		try
		{
			//Récupération de la traduction
			texte = bundle.getString(key);
			//S'il y a des guillements
			if (texte.charAt(0) == '"')
				return texte.substring(1, texte.length() - 1);
			else
				return texte;
		}
		catch (MissingResourceException Ex)
		{
			return "##ERREUR##";
		}
	}

	/**
	 * Récupération de la traduction
	 */
	static public char getChar(String key)
	{
		return getText(key).charAt(0);
	}

	/**
	 * Récupération des langues disponibles
	 */
	public static Vector getLanguesDisponibles()
	{
		return languesDisponibles;
	}

	/**
	 * Récupération de la langue courant
	 */
	public static String getLangueCourante()
	{
		return mettreMajuscule(bundle.getLocale().getDisplayLanguage());
	}

}