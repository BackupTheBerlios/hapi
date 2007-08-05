package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_TypeProduit;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_TypeProduitTemporaire
{
	// liste des types de produits des processus
	static private HashMap typesProduit = new HashMap();

	/**
	 * ajoute un identifiant de type de produit à la liste
	 * @param id : identifiant du type de produit
	 * @param activite : E_TypeProduit
	 */
	synchronized static public void ajouterTypeProduit(String id, E_TypeProduit typeProduit)
	{
		typesProduit.put(id, typeProduit);
	}

	/**
	 * récupère le type de produit d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_TypeProduit getTypeProduit(String id)
	{
		return (E_TypeProduit) typesProduit.get(id);
	}
	/**
	 * @return Returns the typesProduit.
	 */
	synchronized static public HashMap getTypesProduit()
	{
		return typesProduit;
	}

	synchronized static public int size()
	{
		return typesProduit.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) typesProduit.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		typesProduit.clear();
	}
}
