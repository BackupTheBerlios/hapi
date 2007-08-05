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
public class E_Interface implements InterfaceMetier
{
	// identifiant de l'interface
	private String identifiant = new String();
	// identifiants de composants 
	private String interfaceRequiseComposant = new String();
	private String interfaceFournieComposant = new String();

	// liste d'identifiants de produits
	private ArrayList interfaceProduit = null;

	private ImageIcon icone;

	public E_Interface(ImageIcon lIcone)
	{
		interfaceProduit = new ArrayList();
		icone = lIcone;
	}

	public void ajouterInterfaceProduit(String id)
	{
		interfaceProduit.add(id);
	}
	
	public void replaceInterfaceProduit(String ancien, String id)
	{
		interfaceProduit.remove(ancien);
		interfaceProduit.add(id);
	}	
	
	public String getIdentifiant()
	{
		return identifiant;
	}
	public void setIdentifiant(String identifiant)
	{
		this.identifiant = identifiant;
	}
	public String getInterfaceFournieComposant()
	{
		return interfaceFournieComposant;
	}
	public void setInterfaceFournieComposant(String interfaceFournieComposant)
	{
		this.interfaceFournieComposant = interfaceFournieComposant;
	}
	public String getInterfaceRequiseComposant()
	{
		return interfaceRequiseComposant;
	}
	public void setInterfaceRequiseComposant(String interfaceRequiseComposant)
	{
		this.interfaceRequiseComposant = interfaceRequiseComposant;
	}
	public ArrayList getInterfaceProduit()
	{
		return interfaceProduit;
	}

	public String getNom()
	{
		return "";
	}

	public ImageIcon getIcone()
	{
		return icone;
	}

}
