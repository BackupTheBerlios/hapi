/*
 * Created on 3 déc. 2004
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

public class E_Role implements InterfaceMetier
{
	// liste d'identifiants d'activités associées à ce rôle
	private ArrayList idParticipationsActivite = null;
	// nom du role
	private String nom = new String();
	// identifiant du role
	private String identifiant = new String();
	// identifiant du composant auquel il appartient
	private String agregatComposant = new String();
	// liste des identifiants des produits dont le rôle est 
	// responsable
	private ArrayList idResponsabilitesProduit = null;
	// element de présentation
	private String elementPresentationId = new String();

	private ImageIcon icone;

	public E_Role(ImageIcon lIcone)
	{
		// initialisation des listes
		idParticipationsActivite = new ArrayList();
		idResponsabilitesProduit = new ArrayList();

		icone = lIcone;
	}

	/**
	 * ajoute un identifiant d'activité à la liste
	 * @param id
	 */
	public void ajouterIdParticipationActivite(String id)
	{
		idParticipationsActivite.add(id);
	}
	
	public void replaceIdParticipationActivite(String ancien, String id)
	{
		idParticipationsActivite.remove(ancien);
		idParticipationsActivite.add(id);
	}	

	/**
	 * ajoute un identifiant de produits à la liste
	 * @param id
	 */
	public void ajouterIdResponsabiliteProduit(String id)
	{
		idResponsabilitesProduit.add(id);
	}
	
	public void replaceIdResponsabiliteProduit(String ancien, String id)
	{
		idResponsabilitesProduit.remove(ancien);
		idResponsabilitesProduit.add(id);
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

	public String getIdentifiant()
	{
		return identifiant;
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
	public ArrayList getIdResponsabilitesProduit()
	{
		return idResponsabilitesProduit;
	}

	public String toString()
	{
		return nom;
	}

	/**
	 * @return
	 */
	public String getAgregatComposant()
	{
		return agregatComposant;
	}

	/**
	 * @param string
	 */
	public void setAgregatComposant(String id)
	{
		agregatComposant = id;
	}

	/**
	 * @return
	 */
	public ArrayList getIdParticipationsActivite()
	{
		return idParticipationsActivite;
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
