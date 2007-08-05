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
	// liste des activit�s des processus
	// structure <id_processus (String), <id_activite(String), activite (E_Activite)>    
	static private HashMap activitesProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'activit� � la liste
	 * @param id_processus : identifiant du processus auquel appartient l'activit�
	 * @param id : identifiant de l'activit�
	 * @param activite : E_Activit�
	 */
	synchronized static public void ajouterActivite(String id_processus, String id, E_Activite activite)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (activitesProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			activitesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) activitesProcessus.get(id_processus)).put(id, activite);
	}

	/**
	 * r�cup�re l'activit� d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Activite getActivite(String id_processus, String id)
	{
		return (E_Activite) ((HashMap) activitesProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie une hashmap <idactivite, E_Activite> par definition de travail du processus pass� en param�tre
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
	 * renvoie la liste des activit�s du processus donn�
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
	 * renvoie le nombre d'activit�s d'un processus
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
	 * supprime les activites d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerActivites(String id_processus)
	{
		activitesProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (activitesProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			activitesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) activitesProcessus.get(id_processus)).putAll(laListe);
	}
}
