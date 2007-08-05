/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Composant;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */
public class C_Composant
{
	// liste des composants des processus
	// structure <id_processus (String), <id_composant (String), composant (E_Composant)>    
	static private HashMap composantsProcessus = new HashMap();

	/**
	 * ajoute un composant � la liste
	 * @param id_processus : identifiant du processus auquel appartient le composant
	 * @param id : identifiant du composant
	 * @param composant : E_Composant
	 */
	synchronized static public void ajouterComposant(String id_processus, String id, E_Composant composant)
	{
		// si aucune entr�e n'existe pour le processus 
		if (composantsProcessus.get(id_processus) == null)
		{
			// ... alors on la cr�e
			composantsProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'�lement
		 ((HashMap) composantsProcessus.get(id_processus)).put(id, composant);
	}

	/**
	 * r�cup�re le composant d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Composant getComposant(String id_processus, String id)
	{
		return (E_Composant) ((HashMap) composantsProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des composants du processus donn�
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getComposantsDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) composantsProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	synchronized static public HashMap getComposantsProcessus()
	{
		return composantsProcessus;
	}

	/**
	* renvoie une hash map class� � l'envers <id_composant (String), <id_processus (String)>> 
	*/
	synchronized static public HashMap getProcessusComposants()
	{
		HashMap procComp = new HashMap();

		// Liste les cl�s
		for (Iterator i = composantsProcessus.keySet().iterator(); i.hasNext();)
		{
			Object id_proc = i.next();
			// r�cup�ration de la hashmap (liste de composants) pour chaque processus
			HashMap comp_temp = (HashMap) composantsProcessus.get(id_proc);

			for (Iterator j = comp_temp.keySet().iterator(); j.hasNext();)
			{
				// r�cup�ration de l'id du composant							
				Object id_comp = j.next();
				E_Composant c = getComposant(id_proc.toString(), id_comp.toString());
				// constitution du nouvel id_comp
				// <nom_composant> <version>
				String laDate = c.getDatePlacement();
				laDate = laDate.substring(6, 8) + "/" + laDate.substring(4, 6) + "/" + laDate.substring(0, 4) + " " + laDate.substring(8, 10) + ":" + laDate.substring(10, 12) + ":" + laDate.substring(12, 14);
				String nouvel_id = c.getNom() + " " + laDate;

				// si aucune entr�e pour ce composant n'existe ...
				if (!procComp.containsKey(nouvel_id))
				{
					// ... on la cr�er
					HashMap temp = new HashMap();
					temp.put(id_proc, null);
					procComp.put(nouvel_id, temp);
				}
				else
				{
					// ... sinon on remplace l'�l�ment 
					HashMap temp = (HashMap) procComp.get(nouvel_id);

					temp.put(id_proc, null);
					procComp.remove(nouvel_id);
					procComp.put(nouvel_id, temp);
				}
			}
		}

		return procComp;
	}

	/**
	 * renvoie la nombre de composants associ�s au processus donn�
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) composantsProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) composantsProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les composants d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerComposants(String id_processus)
	{
		composantsProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (composantsProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			composantsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) composantsProcessus.get(id_processus)).putAll(laListe);
	}
}
