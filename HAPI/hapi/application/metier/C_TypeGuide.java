/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_TypeGuide;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_TypeGuide
{
	// liste des guides des processus
	// structure <id_processus (String), <id_typeguide (String), type_guide (E_TypeGuide)>>
	static private HashMap typesGuideProcessus = new HashMap();

	/**
	 * ajoute un identifiant de type de guide à la liste
	 * @param id_processus : identifiant du processus auquel appartient le type de guide
	 * @param id : identifiant du type de guide
	 * @param activite : E_TypeGuide
	 */
	synchronized static public void ajouterTypeGuide(String id_processus, String id, E_TypeGuide typeGuide)
	{
		// si aucune entrée pour le processus n'existe ...
		if (typesGuideProcessus.get(id_processus) == null)
		{
			// ... on la créé 
			typesGuideProcessus.put(id_processus, new HashMap());
		}
		// insertion du nouvel élément
		 ((HashMap) typesGuideProcessus.get(id_processus)).put(id, typeGuide);
	}

	/**
	 * récupère le type de guide d'identifiant id
	 * @param id_processus
	 * @param id
	 * @return
	 */
	synchronized static public E_TypeGuide getTypeGuide(String id_processus, String id)
	{
		return (E_TypeGuide) ((HashMap) typesGuideProcessus.get(id_processus)).get(id);
	}
	/**
	 * @return Returns the typesGuide.
	 */
	synchronized static public HashMap getTypesGuideProcessus()
	{
		return typesGuideProcessus;
	}

	/**
	 * renvoie la liste des types de guide d'un processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getTypesGuideDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) typesGuideProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}
	/**
	 * retourne le nombre de guide d'un processus donné
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) typesGuideProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) typesGuideProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les types de guide d'un processus
	 * @param id_processus
	 */
	synchronized static public void supprimerTypesGuide(String id_processus)
	{
		typesGuideProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (typesGuideProcessus.get(id_processus) == null)
		{
			// ... on la crée
			typesGuideProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) typesGuideProcessus.get(id_processus)).putAll(laListe);
	}
}
