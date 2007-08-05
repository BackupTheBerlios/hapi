/*
 * Créé le 26 sept. 2005
 */
package hapi.donnees;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import javax.swing.ImageIcon;


/**
 * @author Cédric
 */
public class E_ParticipantHorsProcessus implements InterfaceMetier
{
	private String nom;

	public E_ParticipantHorsProcessus(String leNom)
	{
		nom = leNom;
	}
	
	public String getNom()
	{
		return nom;
	}

	public String getIdentifiant()
	{
		return "-1";
	}

	public ImageIcon getIcone()
	{
		return new ImageIcon();
	}

}
