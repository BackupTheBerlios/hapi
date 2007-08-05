/*
 * Auteur Cédric
 *
 */
package hapi.application.modele;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe permettant de filrer les fichiers
 */
public class FiltreFichier extends FileFilter
{
	private String fileExtension = null;
	private String fileDescription = null;

	/**
	 * Constructeur
	 */
	public FiltreFichier(String aFileExtension, String aFileDescription)
	{
		super();
		fileExtension = aFileExtension;
		fileDescription = aFileDescription;
	}

	/**
	 * Fonction d'acceptation de fichier
	 */
	public boolean accept(File f)
	{
		//Si l'extension demandée est *, affichage de tous les fichiers
		if (fileExtension.equals("*"))
			return true;
		//Si c'est un repertoire on l'affiche aussi
		if (f.isDirectory())
			return true;
		else
			return (f.getName().substring(f.getName().lastIndexOf(".") + 1).equals(fileExtension)) ? true : false;
	}

	/**
	 * Retourne la description de l'extension
	 */
	public String getDescription()
	{
		return fileDescription;
	}
}