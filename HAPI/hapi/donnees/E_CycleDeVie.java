/*
 * Créé le 4 juin 2005
 */
package hapi.donnees;

import java.util.ArrayList;

/**
 * @author Cédric
 */
public class E_CycleDeVie
{
	private ArrayList lesActivites = null;
	private ArrayList lesRoles = null;
	private ArrayList lesProduits = null;
	
	/**
	 * Contructeur de cycle de vie
	 * @param activites ArrayList d'identifiants
	 * @param roles ArrayList d'identifiants
	 * @param produits ArrayList d'identifiants
	 */
	public E_CycleDeVie(ArrayList activites,ArrayList roles,ArrayList produits)
	{
		lesActivites = activites;
		lesRoles = roles;
		lesProduits = produits;
	}
	
	/**
	 * @return Renvoie lesActivites.
	 */
	public ArrayList getLesActivites()
	{
		return lesActivites;
	}
	/**
	 * @param lesActivites Les activites inclues dans le cycle de vie.
	 */
	public void setLesActivites(ArrayList lesActivites)
	{
		this.lesActivites = lesActivites;
	}
	/**
	 * @return Renvoie lesProduits.
	 */
	public ArrayList getLesProduits()
	{
		return lesProduits;
	}
	/**
	 * @param lesProduits Les produits inclus dans le cycle de vie.
	 */
	public void setLesProduits(ArrayList lesProduits)
	{
		this.lesProduits = lesProduits;
	}
	/**
	 * @return Renvoie lesRoles.
	 */
	public ArrayList getLesRoles()
	{
		return lesRoles;
	}
	/**
	 * @param lesRoles les roles inclus dans le cycle de vie.
	 */
	public void setLesRoles(ArrayList lesRoles)
	{
		this.lesRoles = lesRoles;
	}
}
