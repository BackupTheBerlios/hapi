package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


/**
 * @author boursier
 *
 */
public class E_Activite implements InterfaceMetier
{
	// identifiant de l'activité
	private String identifiant = new String();
	// nom de l'activité
	private String nom = new String();
	// identifiant du rôle associé
	private String participationRole = new String();
	// identifiant de la définition de travail à laquelle elle appartient
	private String agregatDefinitionTravail = new String();
	// élement de présentation
	private String elementPresentationId = new String();

	// liste des identifiants des produits en entrée
	private ArrayList produitsEntree = null;
	// liste des identifiants des produits en sortie
	private ArrayList produitsSortie = null;

	private ImageIcon icone;

	public E_Activite(ImageIcon lIcone)
	{
		// initialisation des listes
		produitsEntree = new ArrayList();
		produitsSortie = new ArrayList();
		icone = lIcone;
	}

	/**
	 * ajoute un identifiant de produit à la liste
	 * @param id
	 */
	public void ajouterProduitEntree(String id)
	{
		produitsEntree.add(id);
	}
	
	public void replaceProduitEntree(String ancien, String id)
	{
		produitsEntree.remove(ancien);
		produitsEntree.add(id);
	}	

	/**
	 * ajoute un identifiant de produit en entrée à la liste 
	 * @param id
	 */
	public void ajouterProduitSortie(String id)
	{
		produitsSortie.add(id);
	}
	
	public void replaceProduitSortie(String ancien,String id)
	{
		produitsSortie.remove(ancien);
		produitsSortie.add(id);
	}	

	// getters et setters
	public void setNom(String n)
	{
		nom = n;
	}

	public void setIdentifiant(String id)
	{
		identifiant = id;
	}

	public String getNom()
	{
		return nom;
	}

	/**
	 * @return
	 */
	public String getIdentifiant()
	{
		return identifiant;
	}

	/**
	 * @return
	 */
	public ArrayList getProduitsEntree()
	{
		return produitsEntree;
	}

	/**
	 * @return
	 */
	public ArrayList getProduitsSortie()
	{
		return produitsSortie;
	}

	public String toString()
	{
		return nom;
	}

	/**
	 * @return
	 */
	public String getAgregatDefinitionTravail()
	{
		return agregatDefinitionTravail;
	}

	/**
	 * @return
	 */
	public String getParticipationRole()
	{
		return participationRole;
	}

	/**
	 * @param i
	 */
	public void setAgregatDefinitionTravail(String i)
	{
		agregatDefinitionTravail = i;
	}

	/**
	 * @param i
	 */
	public void setParticipationRole(String i)
	{
		participationRole = i;
	}

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
