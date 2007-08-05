package hapi.application;

import hapi.donnees.E_Demande;
import hapi.exception.IdentifiantInconnuException;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Robin EYSSERIC
 */
public class C_GestionDemandes
{
	// Demandes correspondant à tous les processus
	static private HashMap demandes = new HashMap();

	/**
	 * Ajoute une paire identifiant, entité demande dans la liste
	 * 
	 * @param id : identifiant du processus
	 * @param s : E_Demande
	 */
	static public void ajouterDemande(String id_processus, E_Demande demande)
	{
		// si aucune entrée pour le processus n'est trouvée ...
		if (demandes.get(id_processus) == null)
		{
			// ... création de cette entrée
			demandes.put(id_processus, new ArrayList());
		}

		// Ajout de la demande
		 ((ArrayList) (demandes.get(id_processus))).add(demande);
	}

	static public void supprimerDemandesProcessus(String id_proc)
	{
		demandes.remove(id_proc);
	}

	static public void supprimerDemande(int id, String id_proc) throws IdentifiantInconnuException
	{
		ArrayList demandes_tmp = (ArrayList) demandes.get(id_proc);
		if (demandes_tmp != null)
		{
			for (int i = 0; i < demandes_tmp.size(); i++)
			{
				if (((E_Demande) demandes_tmp.get(i)).getIdDemande() == id)
				{
					demandes_tmp.remove(i);
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
	 * Récupère la liste de demandes de modification correspondant au processus
	 *   
	 * @param id
	 * @return
	 */
	static public ArrayList getDemandesProcessus(String id_processus)
	{
		return (ArrayList) demandes.get(id_processus);
	}

	/**
	 * Recupère la demande de modification d'identifiant id et associé à un 
	 * processus d'identifiant id_processus
	 * 
	 * @param id_processus
	 * @param id
	 * @return
	 */
	static public E_Demande getDemandeProcessus(String id_processus, int id) throws IdentifiantInconnuException
	{
		ArrayList demandes_tmp = (ArrayList) demandes.get(id_processus);
		if (demandes_tmp != null)
		{
			for (int i = 0; i < demandes_tmp.size(); i++)
			{
				if (((E_Demande) demandes_tmp.get(i)).getIdDemande() == id)
				{
					return (E_Demande) demandes_tmp.get(i);
				}
			}
		}
		// cas où l'identifiant ne correspond à rien
		throw new IdentifiantInconnuException();
	}

	public static void enregistrerDemandesEnBase(String id_processus) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Demandes à sauvegarder
			ArrayList les_demandes = (ArrayList) demandes.get(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_demandes.size(); i++)
			{
				E_Demande demande = (E_Demande) les_demandes.get(i);

				if (demande.getIdDemande() == -1)
				{
					lesParametres.clear();
					lesParametres.add(demande.getIdProcessus());
					lesParametres.add(demande.getDateDemande());
					lesParametres.add(demande.getNomComposant());
					lesParametres.add(demande.getDescription());
					lesParametres.add(demande.getType());
					lesParametres.add(new Integer(demande.getSeverite()));
					

					idStatement = cBase.creerPreparedStatement("setDemandesModifications");
					cBase.executerRequeteStockee(idStatement, "setDemandesModifications", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxDemandesModifications");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxDemandesModifications");
					leResultat.next();
					demande.setIdDemande(leResultat.getInt(1));
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
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		}
	}

	public static void supprimerDemandeEnBase(int id) throws SQLException
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
			idStatement = cBase.creerPreparedStatement("delDemandesModifications");
			cBase.executerRequeteStockee(idStatement, "delDemandesModifications", lesParametres);
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
	 * Renvoie la liste des demandes
	 */
	public static HashMap getDemandes()
	{
		return demandes;
	}
}
