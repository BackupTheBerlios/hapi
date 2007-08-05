/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_PaquetagePresentation;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_PaquetagePresentation
{
	// liste des paquetages de présentation des processus
	// structure <id_processus (String), <id_paquetagePresentation (String), paquetagePresentation (E_PaquetagePresentation)>
	static private HashMap paquetagesPresentationProcessus = new HashMap();

	/**
	 * ajoute un identifiant de paquetage de présentation à la liste
	 * @param id_processus : identifiant du processus auquel appartient la liste des paquetages
	 * @param id : identifiant du paquetage de présentation
	 * @param activite : E_PaquetagePresentation
	 */
	synchronized static public void ajouterPaquetagePresentation(String id_processus, String id, E_PaquetagePresentation paquetagePresentation)
	{
		// si aucune entrée pour ce processus n'existe ...
		if (paquetagesPresentationProcessus.get(id_processus) == null)
		{
			// ... on la crée
			paquetagesPresentationProcessus.put(id_processus, new HashMap());
		}
		// on ajoute le nouvel élement
		 ((HashMap) paquetagesPresentationProcessus.get(id_processus)).put(id, paquetagePresentation);
	}

	/**
	 * récupère le paquetage de présentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_PaquetagePresentation getPaquetagePresentation(String id_processus, String id)
	{
		try
		{
			return (E_PaquetagePresentation) ((HashMap) paquetagesPresentationProcessus.get(id_processus)).get(id);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * renvoie la liste de paquetages de présentation du processus donné 
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getPaquetagesPresentationDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) paquetagesPresentationProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * @return Returns the paquetagesPresentation.
	 */
	synchronized static public HashMap getPaquetagesPresentationProcessus()
	{
		return paquetagesPresentationProcessus;
	}

	/**
	 * renvoie le nombre de paquetage de présentation d'un processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) paquetagesPresentationProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) paquetagesPresentationProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les paquetages de présentation d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerPaquetagesPresentation(String id_processus)
	{
		paquetagesPresentationProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (paquetagesPresentationProcessus.get(id_processus) == null)
		{
			// ... on la crée
			paquetagesPresentationProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) paquetagesPresentationProcessus.get(id_processus)).putAll(laListe);
	}
}
