package hapi.application.metier.temporaire;

import hapi.donnees.metier.E_PaquetagePresentation;

import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_PaquetagePresentationTemporaire
{
	//liste des définitions de travail des processus 
	static private HashMap paquetagesPresentation = new HashMap();

	/**
	 * ajoute un identifiant de paquetage de présentation à la liste
	 * @param id : identifiant du paquetage de présentation
	 * @param activite : E_PaquetagePresentation
	 */
	synchronized static public void ajouterPaquetagePresentation(String id, E_PaquetagePresentation paquetagePresentation)
	{
		paquetagesPresentation.put(id, paquetagePresentation);
	}

	/**
	 * récupère le paquetage de présentation d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_PaquetagePresentation getPaquetagePresentation(String id)
	{
		return (E_PaquetagePresentation) paquetagesPresentation.get(id);
	}
	/**
	 * @return Returns the paquetagesPresentation.
	 */
	synchronized static public HashMap getPaquetagesPresentation()
	{
		return paquetagesPresentation;
	}

	synchronized static public int size()
	{
		return paquetagesPresentation.size();
	}

	synchronized static public String getClef(int index)
	{
		return (String) paquetagesPresentation.keySet().toArray()[index];
	}

	synchronized static public void effacerListe()
	{
		paquetagesPresentation.clear();
	}
}
