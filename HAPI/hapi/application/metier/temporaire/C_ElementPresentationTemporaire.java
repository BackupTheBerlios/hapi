package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_ElementPresentation;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_ElementPresentationTemporaire
{
	//liste des �l�ments de pr�sentation des processus
	static private HashMap elementsPresentation = new HashMap();

	/**
	 * ajoute un identifiant d'�l�ments de pr�sentation � la liste
	 * @param id : identifiant de l'�l�ments de pr�sentation
	 * @param activite : E_ElementPresentation
	 */
	synchronized static public void ajouterElementPresentation(String id, E_ElementPresentation elementPresentation)
	{
		elementsPresentation.put(id, elementPresentation);
	}

	/**
	 * r�cup�re l'�l�ments de pr�sentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_ElementPresentation getElementPresentation(String id)
	{
		return (E_ElementPresentation) elementsPresentation.get(id);
	}
	/**
	 * @return Returns the elementsPresentation.
	 */
	synchronized static public HashMap getElementsPresentation()
	{
		return elementsPresentation;
	}

	/**
	 * r�cup�re l'�l�ments de pr�sentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_ElementPresentation getActivite(String id)
	{
		return (E_ElementPresentation) elementsPresentation.get(id);
	}

	synchronized static public int size()
	{
		return elementsPresentation.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) elementsPresentation.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		elementsPresentation.clear();
	}
}
