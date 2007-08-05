/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Definition;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */

public class C_Definition
{
	// liste des définitions de travail des processus 
	// structure : <id_processus (String), <id_definition (String), definition (E_Definition)>
	static private HashMap definitionsProcessus = new HashMap();

	/**
	 * ajoute un identifiant de définition de travail à la liste
	 * @param id_processus : identifiant du processus auquel appartient la définition de travail
	 * @param id : identifiant de définition de travail
	 * @param definition : E_Definition
	 */
	synchronized static public void ajouterDefinition(String id_processus, String id, E_Definition definition)
	{
		// si aucune entrée n'existe pour le processus ...
		if (definitionsProcessus.get(id_processus) == null)
		{
			// .. on la crée
			definitionsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) definitionsProcessus.get(id_processus)).put(id, definition);
	}

	/**
	 * récupère la définition de travail d'identifiant id 
	 * @param id
	 * @return
	 */
	synchronized static public E_Definition getDefinition(String id_processus, String id)
	{
		return (E_Definition) ((HashMap) definitionsProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des définitions de travail d'un processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getDefinitionsDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) definitionsProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	synchronized static public HashMap getDefinitionsDesComposantsDuProcessus(String id_processus)
	{
		// liste des définitions de travail du processus
		HashMap definitionsDuProcessus = getDefinitionsDuProcessus(id_processus);
		// hashMap de retour
		// structure <idComposant, <idDefinition, E_Definition> >
		HashMap definitionsDesComposants = new HashMap();
		// pour chaque définition de travail ...
		for (Iterator it = definitionsDuProcessus.keySet().iterator(); it.hasNext();)
		{
			String idDef = it.next().toString();
			// ... récupération de l'identifiant du composant auquel elle appartient ...
			String idComp = ((E_Definition) definitionsDuProcessus.get(idDef)).getAgregatComposant();

			// ... si aucune entrée n'existe dans la liste de retour ...
			if (definitionsDesComposants.get(idComp) == null)
			{
				// ... on la crée
				definitionsDesComposants.put(idComp, new HashMap());
			}
			// insertion du nouvel élément
			 ((HashMap) definitionsDesComposants.get(idComp)).put(idDef, definitionsDuProcessus.get(idDef));
		}

		return definitionsDesComposants;
	}

	synchronized static public HashMap getDefinitionsProcessus()
	{
		return definitionsProcessus;
	}

	/**
	 * renvoie le nombre de définitions de travail du processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) definitionsProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) definitionsProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les definitions de travail d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerDefinitions(String id_processus)
	{
		definitionsProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (definitionsProcessus.get(id_processus) == null)
		{
			// ... on la crée
			definitionsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) definitionsProcessus.get(id_processus)).putAll(laListe);
	}
}
