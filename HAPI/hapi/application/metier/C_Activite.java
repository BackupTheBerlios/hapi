/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Activite;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */
public class C_Activite
{
	// liste des activités des processus
	// structure <id_processus (String), <id_activite(String), activite (E_Activite)>    
	static private HashMap activitesProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'activité à la liste
	 * @param id_processus : identifiant du processus auquel appartient l'activité
	 * @param id : identifiant de l'activité
	 * @param activite : E_Activité
	 */
	synchronized static public void ajouterActivite(String id_processus, String id, E_Activite activite)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (activitesProcessus.get(id_processus) == null)
		{
			// ... on la crée
			activitesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) activitesProcessus.get(id_processus)).put(id, activite);
	}

	/**
	 * récupère l'activité d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Activite getActivite(String id_processus, String id)
	{
		return (E_Activite) ((HashMap) activitesProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie une hashmap <idactivite, E_Activite> par definition de travail du processus passé en paramètre
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getActivitesParDefinitionDeTravail(String id_processus)
	{
		HashMap retour = new HashMap();
		for (Iterator it = ((HashMap) activitesProcessus.get(id_processus)).keySet().iterator(); it.hasNext();)
		{
			String idAct = it.next().toString();
			String idDef = getActivite(id_processus, idAct).getAgregatDefinitionTravail();
			if (!retour.containsKey(idDef))
			{
				retour.put(idDef, new HashMap());
			}
			((HashMap) retour.get(idDef)).put(idAct, getActivite(id_processus, idAct));
		}
		return retour;
	}

	/**
	 * renvoie la liste des activités du processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getActivitesDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) activitesProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * @return Returns the activites.
	 */
	synchronized static public HashMap getActivitesProcessus()
	{
		return activitesProcessus;
	}

	/**
	 * renvoie le nombre d'activités d'un processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) activitesProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) activitesProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les activites d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerActivites(String id_processus)
	{
		activitesProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (activitesProcessus.get(id_processus) == null)
		{
			// ... on la crée
			activitesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) activitesProcessus.get(id_processus)).putAll(laListe);
	}
}
