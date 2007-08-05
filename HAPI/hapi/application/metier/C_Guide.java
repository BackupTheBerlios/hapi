/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Guide;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_Guide
{
	// liste des guides des processus
	// structure <id_processus (String), <id_guide (String), guide (E_Guide)>
	static private HashMap guidesProcessus = new HashMap();

	/**
	 * ajoute un identifiant de guide à la liste
	 * @param id_processus : identifiant du processus auquel le guide appartient
	 * @param id : identifiant du guide
	 * @param activite : E_Guide
	 */
	synchronized static public void ajouterGuide(String id_processus, String id, E_Guide guide)
	{
		// si aucune entrée n'existe pour le processus ...
		if (guidesProcessus.get(id_processus) == null)
		{
			// ... on la crée
			guidesProcessus.put(id_processus, new HashMap());
		}
		((HashMap) guidesProcessus.get(id_processus)).put(id, guide);
	}

	/**
	 * récupère le guide d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Guide getGuide(String id_processus, String id)
	{
		return (E_Guide) ((HashMap) guidesProcessus.get(id_processus)).get(id);
	}

	synchronized static public HashMap getGuidesDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) guidesProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}
	/**
	 * @return Returns the guides.
	 */
	synchronized static public HashMap getGuidesProcessus()
	{
		return guidesProcessus;
	}

	/**
	 * renvoie le nombre de guides d'un processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) guidesProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) guidesProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les rôles d'un processus
	 * @param id_processus
	 */
	synchronized static public void supprimerGuides(String id_processus)
	{
		guidesProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (guidesProcessus.get(id_processus) == null)
		{
			// ... on la crée
			guidesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) guidesProcessus.get(id_processus)).putAll(laListe);
	}
}
