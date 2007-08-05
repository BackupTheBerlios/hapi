/*
 * Créé le 23 sept. 2005
 *
 */
package hapi.application;

import hapi.application.ressources.Bundle;
import hapi.donnees.E_RevueProcessus;
import hapi.donnees.modeles.ModeleTableIdProcessus;
import hapi.exception.ChampsVideException;
import hapi.exception.IdentifiantInconnuException;
import hapi.exception.NoRowInsertedException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Cédric
 *
 */
public class C_RevueProcessus
{
	static private HashMap revues = new HashMap();
	private static ModeleTableIdProcessus modele = null;	

	static public void ajouterRevue(String id_processus, E_RevueProcessus uneRevue)
	{
		// si aucune entrée pour le processus n'est trouvée ...
		if (revues.get(id_processus) == null)
		{
			// ... création de cette entrée
			revues.put(id_processus, new ArrayList());
		}

		// Ajout de la demande
		((ArrayList) (revues.get(id_processus))).add(uneRevue);
	}

	static private void supprimerRevuesP(int id, String id_proc) throws IdentifiantInconnuException
	{
		ArrayList revues_tmp = (ArrayList) revues.get(id_proc);
		if (revues_tmp != null)
		{
			for (int i = 0; i < revues_tmp.size(); i++)
			{
				if (((E_RevueProcessus) revues_tmp.get(i)).getIdRevue() == id)
				{
					revues_tmp.remove(i);
				}
			}
		}
		else
		{
			// cas où l'identifiant ne correspond à rien
			throw new IdentifiantInconnuException();
		}
	}

	static public ArrayList getRevuesProcessus(String id_processus)
	{
		return (ArrayList) revues.get(id_processus);
	}

	static public E_RevueProcessus getRevueProcessus(String id_processus, int id) throws IdentifiantInconnuException
	{
		ArrayList revues_tmp = (ArrayList) revues.get(id_processus);
		if (revues_tmp != null)
		{
			for (int i = 0; i < revues_tmp.size(); i++)
			{
				if (((E_RevueProcessus) revues_tmp.get(i)).getIdRevue() == id)
				{
					return (E_RevueProcessus) revues_tmp.get(i);
				}
			}
		}
		// cas où l'identifiant ne correspond à rien
		throw new IdentifiantInconnuException();
	}
	
	static public E_RevueProcessus getRevueProcessusIndice(String id_processus, int indice)
	{
		ArrayList revues_tmp = (ArrayList) revues.get(id_processus);
		if (revues_tmp != null)
		{
			return (E_RevueProcessus) revues_tmp.get(indice);
		}
		return null;
	}	

	private static void miseAJourRevuesEnBaseP(String id_processus, E_RevueProcessus uneRevue) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Demandes à sauvegarder
			ArrayList lesParametres = new ArrayList();

			lesParametres.clear();
			lesParametres.add(uneRevue.getDateRevue());
			lesParametres.add(uneRevue.getDateProchaineRevue());
			lesParametres.add(uneRevue.getDecision());
			lesParametres.add(uneRevue.getAction());
			lesParametres.add(uneRevue.getIdProcessus());
			lesParametres.add(new Integer(uneRevue.getIdRevue()));

			idStatement = cBase.creerPreparedStatement("setMAJRevuesProcessus");
			cBase.executerRequeteStockee(idStatement, "setMAJRevuesProcessus", lesParametres);
			cBase.fermerStatement(idStatement);

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
	
	private static void enregistrerRevuesEnBaseP(String id_processus) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Demandes à sauvegarder
			ArrayList les_revues = (ArrayList) revues.get(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_revues.size(); i++)
			{
				E_RevueProcessus uneRevue = (E_RevueProcessus) les_revues.get(i);

				if (uneRevue.getIdRevue() == -1)
				{
					lesParametres.clear();
					lesParametres.add(uneRevue.getDateRevue());
					lesParametres.add(uneRevue.getDateProchaineRevue());
					lesParametres.add(uneRevue.getDecision());
					lesParametres.add(uneRevue.getAction());
					lesParametres.add(uneRevue.getIdProcessus());

					idStatement = cBase.creerPreparedStatement("setRevuesProcessus");
					cBase.executerRequeteStockee(idStatement, "setRevuesProcessus", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxRevuesProcessus");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxRevuesProcessus");
					leResultat.next();
					uneRevue.setIdRevue(leResultat.getInt(1));
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

	private static void supprimerRevueEnBaseP(int id) throws SQLException
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();

			// Paramétrage de la requête avec l'id
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(new Integer(id));

			// Suppression
			idStatement = cBase.creerPreparedStatement("delRevuesProcessus");
			cBase.executerRequeteStockee(idStatement, "delRevuesProcessus", lesParametres);
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

	public static ModeleTableIdProcessus getModeleRevuesProcessus(String idProcessus)
	{
		modele = new ModeleTableIdProcessus();

		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_RevueProcessus_champ_date"));
		modele.addColumn(Bundle.getText("OO_RevueProcessus_champ_dateProchaine"));
		modele.addColumn(Bundle.getText("OO_RevueProcessus_champ_decision"));
		modele.addColumn(Bundle.getText("OO_RevueProcessus_champ_action"));

		ArrayList lesRevuesProcessus = getRevuesProcessus(idProcessus);
		
		if (lesRevuesProcessus != null)
		{
			SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
			for (int i = 0; i < lesRevuesProcessus.size(); i++)
			{
				modele.getId().add(new Integer(((E_RevueProcessus) lesRevuesProcessus.get(i)).getIdRevue()));
				Vector uneLigne = new Vector();
				
				uneLigne.add(sfDate.format(((E_RevueProcessus) lesRevuesProcessus.get(i)).getDateRevue()));
				uneLigne.add(((E_RevueProcessus) lesRevuesProcessus.get(i)).getDateProchaineRevue()==null?"":sfDate.format(((E_RevueProcessus) lesRevuesProcessus.get(i)).getDateProchaineRevue()));
				uneLigne.add(((E_RevueProcessus) lesRevuesProcessus.get(i)).getDecision());
				uneLigne.add(((E_RevueProcessus) lesRevuesProcessus.get(i)).getAction());
				modele.addRow(uneLigne);
			}
		}

		return modele;
	}

	public static void ajouterRevue(String idProc, Date dateRevue, Date dateProchaineRevue, String decision, String action) throws Exception
	{
		// Récupération de la date du jour
		Calendar date = Calendar.getInstance();
		Date dateJour = new java.sql.Date(date.getTimeInMillis());
		
		// Teste si une description a été saisie
		if (dateRevue.equals(""))
		{
			throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateNonRemplie"));
		}
		else
		{
			if (dateRevue.compareTo(dateJour) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateAvantAujourdhui"));
				
		}
		if ( dateProchaineRevue != null)
		{
			if (dateRevue.compareTo(dateProchaineRevue) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateProchaineAvant"));
			if (dateJour.compareTo(dateProchaineRevue) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateProchaineAvantAujourdhui"));			
		}			
		if (decision.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DecisionNonRemplie"));
		}	

		E_RevueProcessus uneRevue = new E_RevueProcessus(dateRevue,dateProchaineRevue,decision,action,idProc);

		// Enregistrement en mémoire
		ajouterRevue(idProc, uneRevue);

		// Enregistrement en base
		enregistrerRevuesEnBaseP(idProc);
	}
	
	public static void modifierRevue(int indiceRevue,String idProc, Date dateRevue, Date dateProchaineRevue, String decision, String action) throws Exception
	{
		// Récupération de la date du jour
		Calendar date = Calendar.getInstance();
		Date dateJour = new java.sql.Date(date.getTimeInMillis());
		
		// Teste si une description a été saisie
		if (dateRevue.equals(""))
		{
			throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateNonRemplie"));
		}
		else
		{
			if (dateRevue.compareTo(dateJour) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateAvantAujourdhui"));
				
		}
		if ( dateProchaineRevue != null)
		{
			if (dateRevue.compareTo(dateProchaineRevue) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateProchaineAvant"));
			if (dateJour.compareTo(dateProchaineRevue) > 0)
				throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DateProchaineAvantAujourdhui"));			
		}			
		if (decision.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_RevueProcessus_DecisionNonRemplie"));
		}	

		E_RevueProcessus uneRevue = (E_RevueProcessus) getRevuesProcessus(idProc).get(indiceRevue);  
		uneRevue.setDateRevue(dateRevue);
		uneRevue.setAction(action);
		uneRevue.setDecision(decision);
		uneRevue.setDateProchaineRevue(dateProchaineRevue);

		// Enregistrement en base
		miseAJourRevuesEnBaseP(idProc, uneRevue);
	}	

	public static void supprimerRevue(int ligne, String idProcessus) throws SQLException
	{
		int idRevue = ((Integer) modele.getId().get(ligne)).intValue();

		try
		{
			supprimerRevueEnBaseP(idRevue);
			supprimerRevuesP(idRevue, idProcessus);
		}
		catch (SQLException e)
		{
			throw new SQLException();
		}
		catch (IdentifiantInconnuException e2)
		{
			// Normalement on doit pas pouvoir passer ici
		}

	}
	
	static public int size(String idProcessus)
	{
		if ((ArrayList)revues.get(idProcessus) == null)
			return 0;
		else
			return ((ArrayList)revues.get(idProcessus)).size();
	}
}