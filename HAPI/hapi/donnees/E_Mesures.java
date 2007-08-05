package hapi.donnees;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Vincent
 */
public class E_Mesures
{
	// contient le numero d'itération
	private int numIT;
	// contient la date de début d'itération
	private Date dateDebutIT;
	// contient la date de fin d'itération
	private Date dateFinIT;
	// <id_role(String), liste de E_Membre (ArrayList)> 
	private HashMap listeRoles = null;
	// <id_activite(String), liste de E_Tache (ArrayList)>
	private HashMap listeActivites = null;
	// <id_produit(String), liste de E_Artefact(ArrayList)>
	private HashMap listeProduits = null;

	public E_Mesures(int nIT, Date dateDebut, Date dateFin, HashMap listeAct, HashMap listeProd, HashMap listeRol /*, double chargeTaches, double chargeTot*/
	)
	{
		numIT = nIT;
		dateDebutIT = dateDebut;
		dateFinIT = dateFin;
		listeActivites = listeAct;
		listeProduits = listeProd;
		listeRoles = listeRol;
	}

	public int getNumIT()
	{
		return numIT;
	}

	public Date getDateDebutIT()
	{
		return dateDebutIT;
	}

	public Date getDateFinIT()
	{
		return dateFinIT;
	}

	public HashMap getListeActivites()
	{
		return listeActivites;
	}
	public HashMap getListeProduits()
	{
		return listeProduits;
	}
	public HashMap getListeRoles()
	{
		return listeRoles;
	}

	public void setListeActivites(HashMap listeActivites)
	{
		this.listeActivites = listeActivites;
	}
	public void setListeProduits(HashMap listeProduits)
	{
		this.listeProduits = listeProduits;
	}
	public void setListeRoles(HashMap listeRoles)
	{
		this.listeRoles = listeRoles;
	}
}
