/*
 * Auteur Cédric
 *
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


public class E_Definition implements InterfaceMetier
{
	// identifiant de la définition de travail
	private String identifiant = new String();
	// nom de la définition de tavail
	private String nom = new String();
	// identifiant du composant auquel elle appartient
	private String agregatComposant = new String();
	// liste des identifiants des activités qu'elle contient
	private ArrayList idActivites = null;
	// element de présentation
	private String elementPresentationId = new String();

	private ImageIcon icone;

	public E_Definition(ImageIcon lIcone)
	{
		// initialisation des listes
		idActivites = new ArrayList();
		icone = lIcone;
	}

	/**
	 * ajoute l'identifiant d'une activité à la liste
	 * @param id
	 */
	public void ajouterIdActivite(String id)
	{
		idActivites.add(id);
	}
	
	public void replaceIdActivite(String ancien, String id)
	{
		idActivites.remove(ancien);
		idActivites.add(id);
	}	

	/**
	 * getters et setters 
	 *
	 */
	public void setNom(String n)
	{
		nom = n;
	}

	public String getAgregatComposant()
	{
		return agregatComposant;
	}

	public String getIdentifiant()
	{
		return identifiant;
	}

	public String getNom()
	{
		return nom;
	}

	public void setAgregatComposant(String i)
	{
		agregatComposant = i;
	}

	public void setIdentifiant(String i)
	{
		identifiant = i;
	}

	public String toString()
	{
		return nom;
	}

	public ArrayList getIdActivites()
	{
		return idActivites;
	}

	/* v2.0
	public ArrayList getIdEntreeProduits()
	{
		return idEntreeProduits;
	}
	
	public ArrayList getIdSortieProduits()
	{
		return idSortieProduits;
	}
	*/

	public String getElementPresentationId()
	{
		return elementPresentationId;
	}
	public void setElementPresentationId(String elementPresentationId)
	{
		this.elementPresentationId = elementPresentationId;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
}
