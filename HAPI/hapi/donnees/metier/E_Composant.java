/*
 * Created on 7 janv. 2005
 *
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


/**
 * @author boursier
 *
 */
public class E_Composant implements InterfaceMetier
{
	// identifiant du composant
	private String id = new String();
	// nom du composant
	private String nom = new String();
	// nom de l'auteur du composant
	private String nomAuteur = new String();
	// e-mail de l'auteur du composant
	private String emailAuteur = new String();
	// description du composant
	private String description = new String();
	// version du composant
	private String version = new String();
	// date de placement du composant dans le référentiel
	private String datePlacement = new String();
	// interfaces
	private String interfaceRequise = new String();
	private String interfaceFournie = new String();
	// identifiant de l'élément de présentation
	private String elementPresentationId = new String();
	// liste des identifiants des rôles du composant
	private ArrayList idRoles = new ArrayList();
	// liste des identifiants des produits du composant
	private ArrayList idProduits = new ArrayList();
	// liste des identifiants des définitions de travail qu'il contient
	private ArrayList idDefinitionTravail = new ArrayList();

	private ImageIcon icone;

	public E_Composant(ImageIcon lIcone)
	{
		icone = lIcone;
	}

	/**
	 * ajoute un identifiant de rôle à la liste
	 * @param id
	 */
	public void ajouterIdRole(String id)
	{
		idRoles.add(id);
	}
	
	public void replaceIdRole(String ancien, String id)
	{
		idRoles.remove(ancien);
		idRoles.add(id);
	}	

	/**
	 * ajoute un identifiant de produit à la liste
	 * @param id
	 */
	public void ajouterIdProduit(String id)
	{
		idProduits.add(id);
	}
	
	public void replaceIdProduit(String ancien,String id)
	{
		idProduits.remove(ancien);
		idProduits.add(id);
	}

	/**
	 * ajoute un identifiant de définition de travail à la liste
	 * @param id
	 */
	public void ajouterIdDefinitionTravail(String id)
	{
		idDefinitionTravail.add(id);
	}
	
	public void replaceIdDefinitionTravail(String ancien, String id)
	{
		idDefinitionTravail.remove(ancien);
		idDefinitionTravail.add(id);
	}	

	// getters et setters

	public String getDescription()
	{
		return description;
	}

	/**
	 * @return
	 */
	public String getEmailAuteur()
	{
		return emailAuteur;
	}

	/**
	 * @return
	 */
	public String getIdentifiant()
	{
		return id;
	}

	/**
	 * @return
	 */
	public String getNom()
	{
		return nom;
	}

	/**
	 * @return
	 */
	public String getNomAuteur()
	{
		return nomAuteur;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		description = string;
	}

	/**
	 * @param string
	 */
	public void setEmailAuteur(String string)
	{
		emailAuteur = string;
	}

	/**
	 * @param i
	 */
	public void setId(String i)
	{
		id = i;
	}

	/**
	 * @param string
	 */
	public void setNom(String string)
	{
		nom = string;
	}

	/**
	 * @param string
	 */
	public void setNomAuteur(String string)
	{
		nomAuteur = string;
	}

	/**
	 * @return
	 */
	public ArrayList getIdDefinitionTravail()
	{
		return idDefinitionTravail;
	}

	/**
	 * @return
	 */
	public ArrayList getIdProduits()
	{
		return idProduits;
	}

	/**
	 * @return
	 */
	public ArrayList getIdRoles()
	{
		return idRoles;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getInterfaceFournie()
	{
		return interfaceFournie;
	}
	public String getElementPresentationId()
	{
		return elementPresentationId;
	}
	public void setElementPresentationId(String elementPresentationId)
	{
		this.elementPresentationId = elementPresentationId;
	}
	public void setInterfaceFournie(String interfaceFournie)
	{
		this.interfaceFournie = interfaceFournie;
	}
	public String getInterfaceRequise()
	{
		return interfaceRequise;
	}
	public void setInterfaceRequise(String interfaceRequise)
	{
		this.interfaceRequise = interfaceRequise;
	}
	public String getDatePlacement()
	{
		return datePlacement;
	}
	public void setDatePlacement(String datePlacement)
	{
		this.datePlacement = datePlacement;
	}
	public ImageIcon getIcone()
	{
		return icone;
	}
}