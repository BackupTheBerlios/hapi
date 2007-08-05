/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Role;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_Role
{
	// liste des roles des processus 
	// structure <id_processus (String), <id_role (String), role (E_Role)>
	static private HashMap rolesProcessus = new HashMap();

	/**
	 * ajoute un r�le � la liste
	 * @param id_processus : identifiant du processus auquel appartient le r�le
	 * @param id : identifiant du r�le
	 * @param role : E_role
	 */
	synchronized static public void ajouterRole(String id_processus, String id, E_Role role)
	{
		// si aucune entr�e pour le processus n'existe ...
		if (rolesProcessus.get(id_processus) == null)
		{
			// ... la cr�er
			rolesProcessus.put(id_processus, new HashMap());
		}
		// dans tous les cas, inserer le nouveau r�le 
		 ((HashMap) rolesProcessus.get(id_processus)).put(id, role);
	}

	/**
	 * r�cup�re le r�le d'identifiant id du processus id_processus
	 * @param id
	 * @return
	 */
	synchronized static public E_Role getRole(String id_processus, String id)
	{
		return (E_Role) ((HashMap) rolesProcessus.get(id_processus)).get(id);
	}

	synchronized static public HashMap getRolesProcessus()
	{
		return rolesProcessus;
	}

	/**
	 * retourne la liste des role associ�e aux processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getRolesDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) rolesProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * retourne le nombre de roles li�s � un processus donn�
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) rolesProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) rolesProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les roles d'u processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerRoles(String id_processus)
	{
		rolesProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (rolesProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			rolesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) rolesProcessus.get(id_processus)).putAll(laListe);
	}
}
