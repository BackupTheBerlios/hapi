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
	// liste des �l�ments de pr�sentation des processus
	// structure <id_processus (String), <id_elementPresentation (String), elementPresentation (E_ElementPresentation)>    
	static private HashMap elementsPresentationProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'�l�ments de pr�sentation � la liste
	 * @param id_processus : identifiant du processus auquel appartiennent les �l�ments de pr�sentation
	 * @param id : identifiant de l'�l�ments de pr�sentation
	 * @param activite : E_ElementPresentation
	 */
	synchronized static public void ajouterElementPresentation(String id_processus, String id, E_ElementPresentation elementPresentation)
	{
		// si aucune entr�e pour le processus n'existe ...
		if (elementsPresentationProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			elementsPresentationProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'�l�ment
		 ((HashMap) elementsPresentationProcessus.get(id_processus)).put(id, elementPresentation);
	}

	/**
	 * r�cup�re l'�l�ments de pr�sentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_ElementPresentation getElementPresentation(String id_processus, String id)
	{
		return (E_ElementPresentation) ((HashMap) elementsPresentationProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des �lements de pr�sentation du processus donn�
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
	 * renvoie le nombre d'�l�ments de pr�sentation du processus donn� 
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
	 * supprime les �lements de pr�sentation d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerElementsPresentation(String id_processus)
	{
		elementsPresentationProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (elementsPresentationProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			elementsPresentationProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) elementsPresentationProcessus.get(id_processus)).putAll(laListe);
	}
}
