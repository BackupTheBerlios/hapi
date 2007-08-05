/*
 * Created on 20 janv. 2005
 *
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


/**
 * @author Natalia
 *
 */
public class E_ElementPresentation implements InterfaceMetier
{
	// nom de l'élément
	private String nom = new String();
	// identifiant de l'élément
	private String identifiant = new String();
	// chemin d'icône
	private String cheminIcone = new String();
	// chemin de contenu
	private String cheminContenu = new String();
	// description de l'élément
	private String description = new String();
	private String cheminPage = new String();

	private ArrayList guideId = null;

	private ImageIcon icone;

	public E_ElementPresentation(ImageIcon lIcone)
	{
		guideId = new ArrayList();
		icone = lIcone;
	}

	public void ajouterIdGuide(String id)
	{
		guideId.add(id);
	}
	
	public void replaceIdGuide(String ancien, String id)
	{
		guideId.remove(ancien);
		guideId.add(id);
	}	
	
	public ArrayList getIdGuide()
	{
		return guideId;
	}
	public String getCheminPage()
	{
		return cheminPage;
	}
	public void setCheminPage(String cheminPage)
	{
		this.cheminPage = cheminPage;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getCheminContenu()
	{
		return cheminContenu;
	}
	public void setCheminContenu(String cheminContenu)
	{
		this.cheminContenu = cheminContenu;
	}
	public String getCheminIcone()
	{
		return cheminIcone;
	}
	public void setCheminIcone(String cheminIcone)
	{
		this.cheminIcone = cheminIcone;
	}
	public String getIdentifiant()
	{
		return identifiant;
	}
	public void setIdentifiant(String identifiant)
	{
		this.identifiant = identifiant;
	}
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public ArrayList getGuideId()
	{
		return guideId;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
}
