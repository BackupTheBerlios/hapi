package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Composant;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_ComposantTemporaire
{
	//liste des composants temporatires des processus
	static private HashMap composants = new HashMap();

	/**
	 * ajoute un composant à la liste
	 * @param id : identifiant du composant
	 * @param composant : E_Composant
	 */
	synchronized static public void ajouterComposant(String id, E_Composant composant)
	{
		composants.put(id, composant);
	}

	/**
	 * récupère le composant d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Composant getComposant(String id)
	{
		return (E_Composant) composants.get(id);
	}

	synchronized static public HashMap getComposants()
	{
		return composants;
	}

	synchronized static public int size()
	{
		return composants.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) composants.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		composants.clear();
	}
}
