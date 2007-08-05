package hapi.donnees;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import javax.swing.ImageIcon;


/**
 * @author Stéphane
 */
public class E_Artefact implements InterfaceMetier
{
	private String idArtefact;
	private String nomArtefact;
	private ImageIcon icone;

	public E_Artefact(String id, String nom, ImageIcon lIcone)
	{
		idArtefact = id;
		nomArtefact = nom;
		icone = lIcone;
	}

	public String getNom()
	{
		return nomArtefact;
	}

	public String getIdentifiant()
	{
		return idArtefact;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}

}
