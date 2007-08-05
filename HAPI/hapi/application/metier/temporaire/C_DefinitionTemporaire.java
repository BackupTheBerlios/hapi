package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Definition;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_DefinitionTemporaire
{
	//liste des d�finitions de travail des processus 
	static private HashMap definitions = new HashMap();

	/**
	 * ajoute un identifiant de d�finition de travail � la liste
	 * @param id : identifiant de d�finition de travail
	 * @param definition : E_Definition
	 */
	synchronized static public void ajouterDefinition(String id, E_Definition definition)
	{
		definitions.put(id, definition);
	}

	/**
	 * r�cup�re la d�finition de travail d'identifiant id 
	 * @param id
	 * @return
	 */
	synchronized static public E_Definition getDefinition(String id)
	{
		return (E_Definition) definitions.get(id);
	}

	synchronized static public HashMap getDefinitions()
	{
		return definitions;
	}

	synchronized static public int size()
	{
		return definitions.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) definitions.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		definitions.clear();
	}
}
