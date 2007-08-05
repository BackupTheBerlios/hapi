/*
 * Created on 20 janv. 2005
 *
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import javax.swing.ImageIcon;


/**
 * @author Natalia
 *
 */
public class E_TypeProduit implements InterfaceMetier
{
	// nom du type de produit
	private String nom = new String();
	// identifiant du type de produit
	private String identifiant = new String();

	private ImageIcon icone;

	public E_TypeProduit(ImageIcon lIcone)
	{
		icone = lIcone;
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

	public ImageIcon getIcone()
	{
		return icone;
	}
}
