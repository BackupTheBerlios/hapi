package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Interface;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_InterfaceTemporaire
{
	// liste des interfaces des processus
	static private HashMap interfaces = new HashMap();

	/**
	 * ajoute un identifiant d'interface à la liste
	 * @param id : identifiant de l'interface
	 * @param activite : E_Interface
	 */
	synchronized static public void ajouterInterface(String id, E_Interface einterface)
	{
		interfaces.put(id, einterface);
	}

	/**
	 * récupère l'interface d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Interface getInterface(String id)
	{
		return (E_Interface) interfaces.get(id);
	}
	/**
	 * @return Returns the interfaces.
	 */
	synchronized static public HashMap getInterfaces()
	{
		return interfaces;
	}

	synchronized static public int size()
	{
		return interfaces.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) interfaces.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		interfaces.clear();
	}
}
