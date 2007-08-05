package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Activite;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_ActiviteTemporaire
{
	//liste des activit�s temporaires
	static private HashMap activites = new HashMap();

	/**
	 * ajoute un identifiant d'activit� � la liste
	 * @param id : identifiant de l'activit�
	 * @param activite : E_Activit�
	 */
	synchronized static public void ajouterActivite(String id, E_Activite activite)
	{
		activites.put(id, activite);
	}

	/**
	 * r�cup�re l'activit� d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Activite getActivite(String id)
	{
		return (E_Activite) activites.get(id);
	}

	/**
	 * @return Returns the activites.
	 */
	synchronized static public HashMap getActivites()
	{
		return activites;
	}

	synchronized static public int size()
	{
		return activites.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) activites.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		activites.clear();
	}
}
