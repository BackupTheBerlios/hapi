package hapi.application;

import hapi.donnees.E_Modification;
import hapi.exception.IdentifiantInconnuException;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 */
public class C_GestionModifications
{
	// Demandes correspondant à tous les processus
	static private HashMap modifications = new HashMap();

	/**
	 * Ajoute une paire identifiant, entité modification dans la liste
	 * 
	 * @param id : identifiant du processus
	 * @param s : E_Modification
	 */
	static public void ajouterModification(String id_processus, E_Modification modification)
	{
		// si aucune entrée pour le processus n'est trouvée ...
		if (modifications.get(id_processus) == null)
		{
			// ... création de cette entrée
			modifications.put(id_processus, new ArrayList());
		}

		// Ajout de la modification
		 ((ArrayList) (modifications.get(id_processus))).add(modification);
	}

	static public void supprimerModificationsProcessus(String id_proc)
	{
		modifications.remove(id_proc);
	}

	static public void supprimerModification(int id, String id_proc) throws IdentifiantInconnuException
	{
		ArrayList modifications_tmp = (ArrayList) modifications.get(id_proc);
		if (modifications_tmp != null)
		{
			for (int i = 0; i < modifications_tmp.size(); i++)
			{
				if (((E_Modification) modifications_tmp.get(i)).getIdModification() == id)
				{
					modifications_tmp.remove(i);
				}
			}
		}
		else
		{
			// cas où l'identifiant ne correspond à rien
			throw new IdentifiantInconnuException();
		}
	}

	/**
	 * Récupère la liste de modifications correspondant au processus
	 *   
	 * @param id
	 * @return
	 */
	static public ArrayList getModificationsProcessus(String id_processus)
	{
		return (ArrayList) modifications.get(id_processus);
	}

	/**
	 * Recupère la modification d'identifiant id et associé à un 
	 * processus d'identifiant id_processus
	 * 
	 * @param id_processus
	 * @param id
	 * @return
	 */
	static public E_Modification getModificationProcessus(String id_processus, int id) throws IdentifiantInconnuException
	{
		ArrayList modifications_tmp = (ArrayList) modifications.get(id_processus);
		if (modifications_tmp != null)
		{
			for (int i = 0; i < modifications_tmp.size(); i++)
			{
				if (((E_Modification) modifications_tmp.get(i)).getIdModification() == id)
				{
					return (E_Modification) modifications_tmp.get(i);
				}
			}
		}
		// cas où l'identifiant ne correspond à rien
		throw new IdentifiantInconnuException();
	}

	public static void enregistrerModificationsEnBase(String id_processus) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Demandes à sauvegarder
			ArrayList les_modifications = (ArrayList) modifications.get(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_modifications.size(); i++)
			{
				E_Modification modification = (E_Modification) les_modifications.get(i);

				if (modification.getIdModification() == -1)
				{
					lesParametres.clear();
					lesParametres.add(modification.getIdProcessus());
					lesParametres.add(modification.getDateDemande());
					lesParametres.add(modification.getNomComposant());
					lesParametres.add(modification.getDescription());
					lesParametres.add(modification.getVersionModification());
					lesParametres.add(modification.getType());
					lesParametres.add(new Integer(modification.getSeverite()));

					idStatement = cBase.creerPreparedStatement("setHistoriqueModifications");
					cBase.executerRequeteStockee(idStatement, "setHistoriqueModifications", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxHistoriqueModifications");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxHistoriqueModifications");
					leResultat.next();
					modification.setIdModification(leResultat.getInt(1));
					cBase.fermerStatement(idStatement);
				}
			}
			cBase.commit();
		}
		catch (Exception nr)
		{
			cBase.rollback();
			throw nr;
		}
		finally
		{
			try
			{
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		}
	}

	public static void supprimerModificationEnBase(int id) throws SQLException
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();

			// Paramétrage de la requête avec l'id de la demande
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(new Integer(id));

			// Suppression de la demande en base
			idStatement = cBase.creerPreparedStatement("delHistoriqueModifications");
			cBase.executerRequeteStockee(idStatement, "delHistoriqueModifications", lesParametres);
			cBase.fermerConnexion();
		}
		catch (NoRowInsertedException e)
		{}
		catch (SQLException e)
		{
			throw new SQLException();
		}
		finally
		{
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
	}

	/**
	 * Renvoie la liste des modifications
	 */
	public static HashMap getModifications()
	{
		return modifications;
	}
}
