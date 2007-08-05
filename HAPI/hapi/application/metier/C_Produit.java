/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_Produit;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_Produit
{
	// hashmap de structure : <id_procesus (String), <id_produit (String), produit (E_Produit)>) 
	static private HashMap produitsProcessus = new HashMap();

	/**
	 * ajoute un produit à la liste
	 * @param id_processus : identifiant du processus auquel appartient le produit
	 * @param id_produit : identifiant du produit
	 * @param produit : E_Produit
	 */
	synchronized static public void ajouterProduit(String id_processus, String id_produit, E_Produit produit)
	{
		// si aucune entrée pour le processus n'est trouvée ...
		if (produitsProcessus.get(id_processus) == null)
		{
			// ... création de cette entrée
			produitsProcessus.put(id_processus, new HashMap());
		}
		// ajout du produit
		 ((HashMap) (produitsProcessus.get(id_processus))).put(id_produit, produit);
	}

	/**
	 * récupère le produit d'identifiant id
	 * @param id_processus : processus auquel appartient le produit
	 * @param id : identifiant du produit
	 * @return
	 */
	synchronized static public E_Produit getProduit(String id_processus, String id)
	{
		return (E_Produit) ((HashMap) produitsProcessus.get(id_processus)).get(id);
	}

	/**
	 * revoie la liste (hashmap) des produits d'un processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getProduitsDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) produitsProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	synchronized static public HashMap getProduitsProcessus()
	{
		return produitsProcessus;
	}

	/**
	 * renvoie le nombre de produits d'un processus
	 * @param id_processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) produitsProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) produitsProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les produits d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerProduits(String id_processus)
	{
		produitsProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (produitsProcessus.get(id_processus) == null)
		{
			// ... on la crée
			produitsProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) produitsProcessus.get(id_processus)).putAll(laListe);
	}
}
