/*
 * Created on 21 janv. 2005
 *
 */
package hapi.application;

import hapi.donnees.E_Indicateur;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */
public class C_Indicateur
{
	// liste de tous les indicateurs
	static private ArrayList indicateurs = new ArrayList();

	static public void ajouterIndicateur(E_Indicateur indicateur)
	{
		indicateurs.add(indicateur);
	}

	static private E_Indicateur getIndicateur(int id) //throws IdentifiantInconnuException
	{
		for (int i = 0; i < indicateurs.size(); i++)
		{
			if (((E_Indicateur) indicateurs.get(i)).getId() == id)
			{
				return (E_Indicateur) indicateurs.get(i);
			}
		}
		return null;
		//throw new IdentifiantInconnuException();
	}

	static public String getNom(int id) //throws IdentifiantInconnuException
	{
		return getIndicateur(id).getNom();
	}

	static public String getFormule(int id) //throws IdentifiantInconnuException
	{
		return getIndicateur(id).getFormule();
	}

	public static ArrayList getIndicateurs()
	{
		return indicateurs;
	}

	public static ArrayList getIdIndicateurs()
	{
		ArrayList ids = new ArrayList();
		for (int i = 0; i < indicateurs.size(); i++)
		{
			ids.add(new Integer(((E_Indicateur) indicateurs.get(i)).getId()));
		}
		return ids;
	}

	public static ArrayList getIdNomIndicateurs()
	{
		ArrayList ids = new ArrayList();
		for (int i = 0; i < indicateurs.size(); i++)
		{
			ids.add(new Integer(((E_Indicateur) indicateurs.get(i)).getId()));
			ids.add(((E_Indicateur) indicateurs.get(i)));
		}
		return ids;
	}
	public static int getIdIndicateur(String nom)
	{
		for (Iterator it = indicateurs.iterator(); it.hasNext();)
		{
			E_Indicateur ind = (E_Indicateur) it.next();
			if (ind.getNom().equals(nom))
				return ind.getId();
		}
		return -1;
	}
}
