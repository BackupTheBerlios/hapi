/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Etat;

import java.util.HashMap;

/**
 * @author Natalia
 */
public class C_Etat
{
	// liste des etats des processus
	// <id_processus (String), <id_etat (String), etat (E_Etat)>
	static private HashMap etatsProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'etat � la liste
	 * @param id_processus : identifiant du processus auquel appartient l'�tat
	 * @param id : identifiant de l'etat
	 * @param activite : E_Etat
	 */
	synchronized static public void ajouterEtat(String id_processus, String id, E_Etat etat)
	{
		// si aucune entr�e n'exsite pour ce processus ...
		if (etatsProcessus.get(id_processus) == null)
		{
			// ... on la cr��
			etatsProcessus.put(id_processus, new HashMap());
		}
		((HashMap) etatsProcessus.get(id_processus)).put(id, etat);
	}

	/**
	 * r�cup�re l'etat d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Etat getEtat(String id_processus, String id)
	{
		return (E_Etat) ((HashMap) etatsProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des �tats du processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getEtatsDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) etatsProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * @return Returns the etats.
	 */
	synchronized static public HashMap getEtatsProcessus()
	{
		return etatsProcessus;
	}

	/**
	 * renvoie le nombre d'�tats un processus donn�
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) etatsProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) etatsProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les �tats d'un processus
	 * @param id_processus
	 */
	synchronized static public void supprimerEtats(String id_processus)
	{
		etatsProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (etatsProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			etatsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) etatsProcessus.get(id_processus)).putAll(laListe);
	}
}