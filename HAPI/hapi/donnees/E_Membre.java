package hapi.donnees;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import javax.swing.ImageIcon;


/**
 * @author Stéphane
 */
public class E_Membre implements InterfaceMetier
{
	private String idMembre;
	private String nomMembre;
	private ImageIcon icone;

	public E_Membre(String id, String nom, ImageIcon lIcone)
	{
		idMembre = id;
		nomMembre = nom;
		icone = lIcone;
	}

	public String getNom()
	{
		return nomMembre;
	}

	public String getIdentifiant()
	{
		return idMembre;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}

}
