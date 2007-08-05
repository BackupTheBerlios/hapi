package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Role;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_RoleTemporaire
{
	// liste des roles des processus 
	static private HashMap roles = new HashMap();

	/**
	 * ajoute un rôle à la liste
	 * @param id : identifiant du rôle
	 * @param role : E_role
	 */
	synchronized static public void ajouterRole(String id, E_Role role)
	{
		roles.put(id, role);
	}

	/**
	 * récupère le rôle d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Role getRole(String id)
	{
		return (E_Role) roles.get(id);
	}

	synchronized static public HashMap getRoles()
	{
		return roles;
	}

	synchronized static public int size()
	{
		return roles.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) roles.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		roles.clear();
	}
}
