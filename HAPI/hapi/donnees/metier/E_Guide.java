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
public class E_Guide implements InterfaceMetier
{
	// nom du guide
	private String nom = new String();
	// identifiant du guide
	private String identifiant = new String();
	// type du guide
	private String typeGuideId = new String();
	// element de présentation
	private String elementPresentationId = new String();

	private ImageIcon icone;

	public E_Guide(ImageIcon lIcone)
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
	public String getElementPresentationId()
	{
		return elementPresentationId;
	}
	public void setElementPresentationId(String elementPresentationId)
	{
		this.elementPresentationId = elementPresentationId;
	}
	public String getTypeGuideId()
	{
		return typeGuideId;
	}
	public void setTypeGuideId(String typeGuideId)
	{
		this.typeGuideId = typeGuideId;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
}
