/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_ElementPresentation;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_ElementPresentation
{
	// liste des éléments de présentation des processus
	// structure <id_processus (String), <id_elementPresentation (String), elementPresentation (E_ElementPresentation)>    
	static private HashMap elementsPresentationProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'éléments de présentation à la liste
	 * @param id_processus : identifiant du processus auquel appartiennent les éléments de présentation
	 * @param id : identifiant de l'éléments de présentation
	 * @param activite : E_ElementPresentation
	 */
	synchronized static public void ajouterElementPresentation(String id_processus, String id, E_ElementPresentation elementPresentation)
	{
		// si aucune entrée pour le processus n'existe ...
		if (elementsPresentationProcessus.get(id_processus) == null)
		{
			// ... on la crée
			elementsPresentationProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'élément
		 ((HashMap) elementsPresentationProcessus.get(id_processus)).put(id, elementPresentation);
	}

	/**
	 * récupère l'éléments de présentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_ElementPresentation getElementPresentation(String id_processus, String id)
	{
		return (E_ElementPresentation) ((HashMap) elementsPresentationProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des élements de présentation du processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getElementsPresentationDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) elementsPresentationProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * @return Returns the elementsPresentation.
	 */
	synchronized static public HashMap getElementsPresentationProcessus()
	{
		return elementsPresentationProcessus;
	}

	/**
	 * renvoie le nombre d'éléments de présentation du processus donné 
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) elementsPresentationProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) elementsPresentationProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les élements de présentation d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerElementsPresentation(String id_processus)
	{
		elementsPresentationProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (elementsPresentationProcessus.get(id_processus) == null)
		{
			// ... on la crée
			elementsPresentationProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) elementsPresentationProcessus.get(id_processus)).putAll(laListe);
	}
}
