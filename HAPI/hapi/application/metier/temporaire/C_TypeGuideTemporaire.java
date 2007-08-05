package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_TypeGuide;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_TypeGuideTemporaire
{
	// liste des guides des processus
	static private HashMap typesGuide = new HashMap();

	/**
	 * ajoute un identifiant de type de guide à la liste
	 * @param id : identifiant du type de guide
	 * @param activite : E_TypeGuide
	 */
	synchronized static public void ajouterTypeGuide(String id, E_TypeGuide typeGuide)
	{
		typesGuide.put(id, typeGuide);
	}

	/**
	 * récupère le type de guide d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_TypeGuide getTypeGuide(String id)
	{
		return (E_TypeGuide) typesGuide.get(id);
	}
	/**
	 * @return Returns the typesGuide.
	 */
	synchronized static public HashMap getTypesGuide()
	{
		return typesGuide;
	}

	synchronized static public int size()
	{
		return typesGuide.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) typesGuide.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		typesGuide.clear();
	}
}
