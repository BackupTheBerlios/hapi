/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Interface;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_Interface
{
	// liste des interfaces des processus
	//structure : <id_processus (String), <id_interface (String), interface (E_Interface)>
	static private HashMap interfacesProcessus = new HashMap();

	/**
	 * ajoute un identifiant d'interface � la liste
	 * @param id_processus : identifiant du processus auquel l'interface appartient
	 * @param id : identifiant de l'interface
	 * @param activite : E_Interface
	 */
	synchronized static public void ajouterInterface(String id_processus, String id, E_Interface einterface)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (interfacesProcessus.get(id_processus) == null)
		{
			// ... on la cr��
			interfacesProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'�l�ment
		 ((HashMap) interfacesProcessus.get(id_processus)).put(id, einterface);
	}

	/**
	 * r�cup�re l'interface d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Interface getInterface(String id_processus, String id)
	{
		return (E_Interface) ((HashMap) interfacesProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste des interfaces li�es � un procesus
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getInterfacesDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) interfacesProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * @return Returns the interfaces.
	 */
	synchronized static public HashMap getInterfacesProcessus()
	{
		return interfacesProcessus;
	}

	/**
	 * renvoie le nombre d'interfaces d'un processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) interfacesProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) interfacesProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les interfaces d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerInterfaces(String id_processus)
	{
		interfacesProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste � la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entr�e n'existe pour ce processus ...
		if (interfacesProcessus.get(id_processus) == null)
		{
			// ... on la cr�e
			interfacesProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'�l�ment
		 ((HashMap) interfacesProcessus.get(id_processus)).putAll(laListe);
	}
}
