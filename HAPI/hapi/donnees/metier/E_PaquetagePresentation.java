/*
 * Created on 20 janv. 2005
 *
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;

import javax.swing.ImageIcon;


/**
 * @author Natalia
 *
 */
public class E_PaquetagePresentation implements InterfaceMetier
{
	// nom du paquetage
	private String nom = new String();
	// identifiant du paquetage
	private String identifiant = new String();
	// dossier d'icône
	private String dossierIcone = new String();
	// dossier de contenu
	private String dossierContenu = new String();

	private ArrayList agregeElementPresentation = null;
	// element de présentation
	private String elementPresentationId = new String();

	private ImageIcon icone;

	public E_PaquetagePresentation(ImageIcon lIcone)
	{
		agregeElementPresentation = new ArrayList();
		icone = lIcone;
	}

	public void ajouterAgregeElementPresentation(String id)
	{
		agregeElementPresentation.add(id);
	}
	
	public void replaceAgregeElementPresentation(String ancien, String id)
	{
		agregeElementPresentation.remove(ancien);
		agregeElementPresentation.add(id);
	}	
	public ArrayList getAgregeElementPresentation()
	{
		return agregeElementPresentation;
	}
	public String getDossierContenu()
	{
		return dossierContenu;
	}
	public void setDossierContenu(String dossierContenu)
	{
		this.dossierContenu = dossierContenu;
	}
	public String getDossierIcone()
	{
		return dossierIcone;
	}
	public void setDossierIcone(String dossierIcone)
	{
		this.dossierIcone = dossierIcone;
	}
	public String getElementPresentationId()
	{
		return elementPresentationId;
	}
	public void setElementPresentationId(String elementPresentationId)
	{
		this.elementPresentationId = elementPresentationId;
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
