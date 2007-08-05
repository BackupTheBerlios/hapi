/*
 * Created on 1 févr. 2005
 *
 */
package hapi.application;
import hapi.donnees.E_Seuil;
import hapi.donnees.E_Utilisateur;
import hapi.exception.IdentifiantInconnuException;
import hapi.exception.SeuilNonDefiniException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Natalia
 *
 */
public class C_GestionSeuils
{
	// seuils correspondant à tous les processus de l'utilisateur
	static private HashMap seuils = new HashMap();

	/**
	 * ajoute une paire identifiant, entité seuil dans la liste
	 * @param id : identifiant du processus
	 * @param s : E_Seuil
	 */
	static public void ajouterSeuil(String id_processus, E_Seuil s)
	{
		// si aucune entrée pour le processus n'est trouvée ...
		if (seuils.get(id_processus) == null)
		{
			// ... création de cette entrée
			seuils.put(id_processus, new ArrayList());
		}
		// ajout du seuil
		 ((ArrayList) (seuils.get(id_processus))).add(s);
	}

	/**
	 * récupère la liste de seuil correspondant au processus
	 * ordre des seuils :  
	 * @param id
	 * @return
	 */
	static public ArrayList getSeuilsProcessus(String id_processus)
	{
		return (ArrayList) seuils.get(id_processus);
	}

	/**
	 * recupère le seuil d'identifiant id et associé à un 
	 * processus d'identifiant id_processus
	 * @param id_processus
	 * @param id
	 * @return
	 */
	static public E_Seuil getSeuilProcessus(String id_processus, int id) throws IdentifiantInconnuException
	{
		ArrayList seuils_tmp = (ArrayList) seuils.get(id_processus);
		if (seuils_tmp != null)
		{
			for (int i = 0; i < seuils_tmp.size(); i++)
			{
				if (((E_Seuil) seuils_tmp.get(i)).getId() == id)
				{
					return (E_Seuil) seuils_tmp.get(i);
				}
			}
		}
		// cas où l'identifiant ne correspond à rien
		throw new IdentifiantInconnuException();
	}

	static public E_Seuil getSeuilIndicateur(String id_processus, int id_ind)
	{
		ArrayList seuils_tmp = (ArrayList) seuils.get(id_processus);
		if (seuils_tmp != null)
		{
			for (int i = 0; i < seuils_tmp.size(); i++)
			{
				if (((E_Seuil) seuils_tmp.get(i)).getId_indicateur() == id_ind)
				{
					return (E_Seuil) seuils_tmp.get(i);
				}
			}
		}
		return null;
	}

	static public void modifierSeuil(int id_ind, String id_proc, Double min, Double max)
	{
		E_Seuil s;
		if (!existe(id_proc, id_ind))
		{
			s = new E_Seuil();
			s.setId_indicateur(id_ind);
			s.setId_processus(id_proc);
			s.setMax(max);
			s.setMin(min);
			ajouterSeuil(id_proc, s);

		}
		else
		{
			// modification du seuil
			s = getSeuilIndicateur(id_proc, id_ind);
			s.setMax(max);
			s.setMin(min);
		}
	}

	public static void sauvegarderSeuil(String id_processus) throws SQLException
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			// seuils à sauvegarder
			ArrayList les_seuils = (ArrayList) seuils.get(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_seuils.size(); i++)
			{
				E_Seuil s = (E_Seuil) les_seuils.get(i);
				if (s.getId() == 0)
				{
					lesParametres.clear();
					lesParametres.add(s.getId_processus());
					lesParametres.add(new Integer(s.getId_indicateur()));
					lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
					lesParametres.add(s.getMin());
					lesParametres.add(s.getMax());

					idStatement = cBase.creerPreparedStatement("setSeuil");
					cBase.executerRequeteStockee(idStatement, "setSeuil", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				else
				{
					lesParametres.clear();
					lesParametres.add(s.getMin());
					lesParametres.add(s.getMax());
					lesParametres.add(new Integer(s.getId()));
					idStatement = cBase.creerPreparedStatement("updateSeuil");
					cBase.executerRequeteStockee(idStatement, "updateSeuil", lesParametres);
					cBase.fermerStatement(idStatement);
				}
			}
		}
		finally
		{
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}

		}
	}

	/**
	 * verifie l'existance d'un seuil
	 * @param id_proc
	 * @param id_ind
	 * @return
	 */
	public static boolean existe(String id_proc, int id_ind)
	{
		ArrayList tmp = (ArrayList) seuils.get(id_proc);
		if (tmp == null)
		{
			return false;
		}
		else
		{
			for (int i = 0; i < tmp.size(); i++)
			{
				if (((E_Seuil) tmp.get(i)).getId_indicateur() == id_ind && ((E_Seuil) tmp.get(i)).getId_processus().equals(id_proc))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * renvoie la valeur minimum définie pour l'indicateur pour ce processus
	 * @param id_processus
	 * @param id_indicateur
	 * @return
	 * @throws IdentifiantInconnuException
	* @throws SeuilNonDefini
	 */

	public static Double getMin(String id_processus, int id_indicateur) throws IdentifiantInconnuException, SeuilNonDefiniException
	{
		try
		{
			Double min = ((E_Seuil) getSeuilIndicateur(id_processus, id_indicateur)).getMin();
			if (min.doubleValue() != -1.0)
				return min;
			else
				throw new SeuilNonDefiniException();
		}
		catch (NullPointerException e)
		{
			throw new SeuilNonDefiniException();
		}
	}

	/**
	 * renvoie la valeur maximum définie pour l'indicateur pour ce processus
	 * @param id_processus
	 * @param id_indicateur
	 * @return
	 * @throws IdentifiantInconnuException
	* @throws SeuilNonDefini
	 */
	public static Double getMax(String id_processus, int id_indicateur) throws IdentifiantInconnuException, SeuilNonDefiniException
	{
		try
		{
			Double max = ((E_Seuil) getSeuilIndicateur(id_processus, id_indicateur)).getMax();
			if (max.doubleValue() != -1.0)
				return max;
			else
				throw new SeuilNonDefiniException();
		}
		catch (NullPointerException e)
		{
			throw new SeuilNonDefiniException();
		}
	}

	/**
	 * renvoie la liste des seuils
	 * @return
	 */
	public static HashMap getSeuils()
	{
		return seuils;
	}
}
