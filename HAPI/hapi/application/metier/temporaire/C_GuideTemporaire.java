package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Guide;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_GuideTemporaire
{
	//liste des guides des processus
	static private HashMap guides = new HashMap();

	/**
	 * ajoute un identifiant de guide à la liste
	 * @param id : identifiant du guide
	 * @param activite : E_Guide
	 */
	synchronized static public void ajouterGuide(String id, E_Guide guide)
	{
		guides.put(id, guide);
	}

	/**
	 * récupère le guide d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Guide getGuide(String id)
	{
		return (E_Guide) guides.get(id);
	}
	/**
	 * @return Returns the guides.
	 */
	synchronized static public HashMap getGuides()
	{
		return guides;
	}

	synchronized static public int size()
	{
		return guides.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) guides.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		guides.clear();
	}
}
