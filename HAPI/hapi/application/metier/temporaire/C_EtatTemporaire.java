package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Etat;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_EtatTemporaire
{
	//liste des etats des processus
	static private HashMap etats = new HashMap();

	/**
	 * ajoute un identifiant d'etat à la liste
	 * @param id : identifiant de l'etat
	 * @param activite : E_Etat
	 */
	synchronized static public void ajouterEtat(String id, E_Etat etat)
	{
		etats.put(id, etat);
	}

	/**
	 * récupère l'etat d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Etat getEtat(String id)
	{
		return (E_Etat) etats.get(id);
	}
	/**
	 * @return Returns the etats.
	 */
	synchronized static public HashMap getEtats()
	{
		return etats;
	}

	synchronized static public int size()
	{
		return etats.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) etats.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		etats.clear();
	}
}
