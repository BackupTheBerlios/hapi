/*
 * Created on 20 janv. 2005
 *
 */
package hapi.application.metier;

import hapi.donnees.metier.E_TypeProduit;

import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_TypeProduit
{
	// liste des types de produits des processus
	// structure <id_processus (String), <id_typeproduit (String), type_produit (E_TypeProduit)>>
	static private HashMap typesProduitProcessus = new HashMap();

	/**
	 * ajoute un identifiant de type de produit à la liste
	 * @param id_processus : identifiant du processus auquel appartient le type de produit
	 * @param id : identifiant du type de produit
	 * @param activite : E_TypeProduit
	 */
	synchronized static public void ajouterTypeProduit(String id_processus, String id, E_TypeProduit typeProduit)
	{
		// si aucune entrée n'existe pour le processus ...
		if (typesProduitProcessus.get(id_processus) == null)
		{
			//... alors on la créé
			typesProduitProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'élément
		 ((HashMap) typesProduitProcessus.get(id_processus)).put(id, typeProduit);
	}

	/**
	 * récupère le type de produit d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_TypeProduit getTypeProduit(String id_processus, String id)
	{
		return (E_TypeProduit) ((HashMap) typesProduitProcessus.get(id_processus)).get(id);
	}

	/**
	 * renvoie la liste de types de produits du processus donné
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getTypesProduitDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) typesProduitProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}
	/**
	 * @return Returns the typesProduit.
	 */
	synchronized static public HashMap getTypesProduitProcessus()
	{
		return typesProduitProcessus;
	}

	/**
	 * renvoie le nombre de types de produits associés au processus
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) typesProduitProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		return (String) ((HashMap) typesProduitProcessus.get(id_processus)).keySet().toArray()[index];
	}

	/**
	 * supprime les types de produits d'un processus donné
	 * @param id_processus
	 */
	synchronized static public void supprimerTypesProduits(String id_processus)
	{
		typesProduitProcessus.remove(id_processus);
	}

	/**
	 * ajoute une liste à la liste
	 */
	synchronized static public void ajouterHashMap(String id_processus, HashMap laListe)
	{
		// si aucune entrée n'existe pour ce processus ...
		if (typesProduitProcessus.get(id_processus) == null)
		{
			// ... on la crée
			typesProduitProcessus.put(id_processus, new HashMap());
		}
		// ajout de l'élément
		 ((HashMap) typesProduitProcessus.get(id_processus)).putAll(laListe);
	}
}
