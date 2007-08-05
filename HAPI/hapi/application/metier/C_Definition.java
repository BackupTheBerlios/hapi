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
	// liste des d�finitions de travail des processus 
	// structure : <id_processus (String), <id_definition (String), definition (E_Definition)>
	static private HashMap definitionsProcessus = new HashMap();

	/**
	 * ajoute un identifiant de d�finition de travail � la liste
	 * @param id_processus : identifiant du processus auquel appartient la d�finition de travail
	 * @param id : identifiant de d�finition de travail
	 * @param definition : E_Definition
	 */
	synchronized static public void ajouterDefinition(String id_processus, String id, E_Definition definition)
	{
		// si aucune entr�e n'existe pour le processus ...
		if (definitionsProcessus.get(id_processus) == null)
		{
			// .. on la cr�e
			definitionsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) definitionsProcessus.get(id_processus)).put(id, definition);
	}

	/**
	 * r�cup�re la d�finition de travail d'identifiant id 
	 * @param id
	 * @return
	 */
	synchronized static public E_Definition getDefinition(String id_processus, String id)
	{
		return (E_Definition) ((HashMap) definitionsProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des d�finitions de travail d'un processus donn�
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
		// liste des d�finitions de travail du processus
		HashMap definitionsDuProcessus = getDefinitionsDuProcessus(id_processus);
		// hashMap de retour
		// structure <idComposant, <idDefinition, E_Definition> >
		HashMap definitionsDesComposants = new HashMap();
		// pour chaque d�finition de travail ...
		for (Iterator it = definitionsDuProcessus.keySet().iterator(); it.hasNext();)
		{
			String idDef = it.next().toString();
			// ... r�cup�ration de l'identifiant du composant auquel elle appartient ...
			String idComp = ((E_Definition) definitionsDuProcessus.get(idDef)).getAgregatComposant();

			// ... si aucune entr�e n'existe dans la liste de retour ...
			if (definitionsDesComposants.get(idComp) == null)
			{
				// ... on la cr�e
				definitionsDesComposants.put(idComp, new HashMap());
			}
			// insertion du nouvel �l�ment
			 ((HashMap) definitionsDesComposants.get(idComp)).put(idDef, definitionsDuProcessus.get(idDef));
		}

		return definitionsDesComposants;
	}

	synchronized static public HashMap getDefinitionsProcessus()
	{
		return definitionsProcessus;
	}

	/**
	 * renvoie le nombre de d�finitions de travail du processus
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
	 * supprime les definitions de travail d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerDefinitions(String id_processus)
	{
		definitionsProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (definitionsProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			definitionsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) definitionsProcessus.get(id_processus)).putAll(laListe);
	}
}
