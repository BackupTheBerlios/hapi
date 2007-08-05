package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_Produit;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_ProduitTemporaire
{
	// liste des produits des processus
	static private HashMap produits = new HashMap();

	/**
	 * ajoute un produit à la liste
	 * @param id : identifiant du produit
	 * @param produit : E_Produit
	 */
	synchronized static public void ajouterProduit(String id, E_Produit produit)
	{
		produits.put(id, produit);
	}

	/**
	 * récupère le produit d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_Produit getProduit(String id)
	{
		return (E_Produit) produits.get(id);
	}

	synchronized static public HashMap getProduits()
	{
		return produits;
	}

	synchronized static public int size()
	{
		return produits.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) produits.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		produits.clear();
	}
}
