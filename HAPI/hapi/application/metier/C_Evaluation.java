/*
 * Created on 13 févr. 2005
 */
package hapi.application.metier;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_Indicateur;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.modeles.ModeleTable;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author Stéphane
 */

public class C_Evaluation
{
	//	liste des évaluations
	// structure <id_Processus (String), <id_execProcessus(int) , <numIteration (int), evaluation(E_Evaluation)>>>    
	static private HashMap evaluations = new HashMap();

	/**
	 * ajoute une évaluation quantitative à la liste
	 * @param id_executionProcessus : identifiant du processus auquel appartient l'exécution
	 * @param numIteration : numéro de l'itération
	 * @param evalQuantitative : E_EvaluationQuantitative
	 */
	synchronized static public void ajouterEvaluationQuantitative(String id_processus, int id_executionProcessus, int numIteration, E_Evaluation evalQuantitative)
	{
		// si aucune entrée n'existe pour le processus	    
		if (evaluations.get(id_processus) == null)
		{
			// on la crée
			evaluations.put(id_processus, new HashMap());
		}
		// si aucune entrée n'existe pour l'exécution de processus
		if (((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus)) == null)
		{
			// on la crée
			 ((HashMap) evaluations.get(id_processus)).put(new Integer(id_executionProcessus), new HashMap());
		}
		// on ajoute l'élement
		 ((HashMap) ((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus))).put(new Integer(numIteration), evalQuantitative);
	}

	/**
	 * récupère l'évaluation quantitative correspondant à l'exécution de processus et au numéro d'itération passés en paramètres
	 */
	synchronized static public E_Evaluation getEvaluation(String id_processus, int id_executionProcessus, int numIT)
	{
		try
		{
			return (E_Evaluation) ((HashMap) ((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus))).get(new Integer(numIT));
		}
		catch (Exception e)
		{
			return null;
		}

	}

	synchronized static public E_Evaluation getEvaluation(int id_evaluation)
	{
		String id_proc = null;
		int id_exec = 0, num_it = 0;

		for (Iterator it1 = evaluations.keySet().iterator(); it1.hasNext();)
		{
			id_proc = it1.next().toString();
			for (Iterator it2 = ((HashMap) evaluations.get(id_proc)).keySet().iterator(); it2.hasNext();)
			{
				id_exec = ((Integer) it2.next()).intValue();
				for (Iterator it3 = ((HashMap) ((HashMap) evaluations.get(id_proc)).get(new Integer(id_exec))).keySet().iterator(); it3.hasNext();)
				{
					num_it = ((Integer) it3.next()).intValue();
					if (((E_Evaluation) ((HashMap) ((HashMap) evaluations.get(id_proc)).get(new Integer(id_exec))).get(new Integer(num_it))).getId() == id_evaluation)
					{
						return (E_Evaluation) ((HashMap) ((HashMap) evaluations.get(id_proc)).get(new Integer(id_exec))).get(new Integer(num_it));
					}
				}
			}
		}
		return null;
	}

	/** 
	 * récupère les RUA, RCT, RUP, RUR et ordre des activités d'une evaluation quantitative 
	 */
	synchronized static public ArrayList getInformationsEvaluationQuantitative(String id_processus, int id_executionProcessus, int numIT)
	{
		ArrayList liste = new ArrayList();

		E_Evaluation eval = getEvaluation(id_processus, id_executionProcessus, numIT);
		liste.add(new Integer(C_Indicateur.getIdIndicateur("RUA")));
		liste.add(new Float(eval.getRUA()));
		liste.add(new Integer(C_Indicateur.getIdIndicateur("RCT")));
		liste.add(new Float(eval.getRCT()));
		liste.add(new Integer(C_Indicateur.getIdIndicateur("RUR")));
		liste.add(new Float(eval.getRUR()));
		liste.add(new Integer(C_Indicateur.getIdIndicateur("RUP")));
		liste.add(new Float(eval.getRUP()));
		liste.add(eval.getOrdreTaches());
		liste.add(eval.getDateDebut());
		liste.add(eval.getDateFin());
		return liste;
	}

	/**
	 * renvoie la liste des évaluations quantitatives pour une exécution de processus donnée
	 * !!! ATTENTION cette méthode renvoie une hashmap vide quand ce qui est recherché n'est 
	 * pas trouvé !!! (Ne pas faire de test sur un renvoi "null" donc
	 * 
	 */
	synchronized static public HashMap getEvaluationPourUneExecProc(String id_processus, int id_executionProcessus)
	{
		try
		{
			HashMap retour = (HashMap) ((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus));
			if (retour == null)
				return new HashMap();
			else
				return retour;
		}
		catch (Exception e)
		{
			return new HashMap();
		}
	}

	synchronized static public ModeleTable getModeleTableChargeParComposant(String idProc, HashMap chargeParComposant)
	{
		ModeleTable model = new ModeleTable();
		model.addColumn(Bundle.getText("OO_EvaluationQuantitative_ChargeParComposant_1"));
		model.addColumn(Bundle.getText("OO_EvaluationQuantitative_ChargeParComposant_2"));

		DecimalFormat df = new DecimalFormat("##0.0");

		for (Iterator it = chargeParComposant.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			String charge = df.format(((Float) chargeParComposant.get(idComp)).floatValue() * 100);

			Vector uneLigne = new Vector();
			uneLigne.add(C_Composant.getComposant(idProc, idComp).getNom());
			uneLigne.add(charge);
			model.addRow(uneLigne);
		}
		return model;
	}

	synchronized static public HashMap getEvaluations()
	{
		return evaluations;
	}

	/**
	 * Renvoie la liste de répartition des charges pas composant
	 * @param idProcessus
	 * @param idExec
	 * @param idEval
	 * @return
	 */
	synchronized static public HashMap getRepartitionDesChargesParComposant(String idProcessus, int idExec, int idEval)
	{
		return getEvaluation(idProcessus, idExec, idEval).getRepartitionDeschargesParComposant();
	}

	/**
	 * renvoie la nombre d'évaluations quantitatives pour une exécution de processus
	 * @return
	 */
	synchronized static public int size(String id_processus, int id_executionProcessus)
	{
		try
		{
			return ((HashMap) ((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus))).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public int getClef(String id_processus, int id_executionProcessus, int index)
	{
		TreeSet lesClefs = new TreeSet(((HashMap) ((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus))).keySet());
		int i = -1;
		Iterator it = lesClefs.iterator();
		while (++i < index)
		{
			it.next();
		}

		return ((Integer) it.next()).intValue();
	}

	/**
	 * supprime les évaluations d'une exécution de processus donnée
	 * @param id_processus
	 */
	synchronized static public void supprimerEvaluations(String id_processus, int id_executionProcessus)
	{
		try
		{
			((HashMap) evaluations.get(id_processus)).remove(new Integer(id_executionProcessus));
		}
		catch (Exception e)
		{}
	}

	/**
	 * supprime une évaluation d'une exécution de processus donnée
	 * @param id_processus
	 */
	synchronized static public void supprimerUneEvaluation(String id_processus, int id_executionProcessus, int num_iteration)
	{
		((HashMap) (((HashMap) evaluations.get(id_processus)).get(new Integer(id_executionProcessus)))).remove(new Integer(num_iteration));
	}

	/**
	 * supprime une évaluation en base
	 * @param id_processus
	 */
	synchronized static public void supprimerUneEvaluationEnBase(C_AccesBaseDonnees cBase, String id_processus, int id_executionProcessus, int num_iteration) throws SQLException
	{
		int idStatement = 0;

		//id_executionProcessus = C_ExecutionProcessus.getExecutionProcessus(id_processus, new Integer(id_executionProcessus).toString()).getId();

		ArrayList lesParametres = new ArrayList();

		lesParametres.add(new Integer(C_Evaluation.getEvaluation(id_processus, id_executionProcessus, num_iteration).getId()));
		idStatement = cBase.creerPreparedStatement("delOrdreActivites");
		try
		{
			cBase.executerRequeteStockee(idStatement, "delOrdreActivites", lesParametres);
		}
		catch (NoRowInsertedException e)
		{}
		cBase.fermerStatement(idStatement);

		idStatement = cBase.creerPreparedStatement("delRolesEvaluations");
		try
		{
			cBase.executerRequeteStockee(idStatement, "delRolesEvaluations", lesParametres);
		}
		catch (NoRowInsertedException e)
		{}
		cBase.fermerStatement(idStatement);

		idStatement = cBase.creerPreparedStatement("delChargesDesComposants");
		try
		{
			cBase.executerRequeteStockee(idStatement, "delChargesDesComposants", lesParametres);
		}
		catch (NoRowInsertedException e)
		{}
		cBase.fermerStatement(idStatement);

		lesParametres.clear();
		lesParametres.add(id_processus);
		lesParametres.add(new Integer(id_executionProcessus));
		lesParametres.add(new Integer(num_iteration));

		idStatement = cBase.creerPreparedStatement("delEvaluationUnique");
		try
		{
			cBase.executerRequeteStockee(idStatement, "delEvaluationUnique", lesParametres);
		}
		catch (NoRowInsertedException e)
		{}
		cBase.fermerStatement(idStatement);

	}
	/**
	 * Méthode permattant la sauvegarde d'une évaluation quantitative en base
	 */
	synchronized static public void insererEvaluationEnBase(E_Evaluation lEvaluation) throws Exception
	{
		int idStatement = 0;

		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(lEvaluation.getIdProcessus());
		lesParametres.add(new Integer(lEvaluation.getIdExecutionProcessus()));
		lesParametres.add(new Integer(lEvaluation.getNumIteration()));
		lesParametres.add(new Float(lEvaluation.getRUA()));
		lesParametres.add(new Float(lEvaluation.getRCT()));
		lesParametres.add(new Float(lEvaluation.getRUR()));
		lesParametres.add(new Float(lEvaluation.getRUP()));
		lesParametres.add(new java.sql.Date(lEvaluation.getDateDebut().getTime()));
		lesParametres.add(new java.sql.Date(lEvaluation.getDateFin().getTime()));
		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);
			idStatement = cBase.creerPreparedStatement("setEvaluation");
			cBase.executerRequeteStockee(idStatement, "setEvaluation", lesParametres);
			cBase.fermerStatement(idStatement);
			//Recherche de l'id d'insertion
			idStatement = cBase.creerPreparedStatement("getMaxEvaluations");
			ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxEvaluations");
			leResultat.next();
			lEvaluation.setId(leResultat.getInt(1));
			cBase.fermerStatement(idStatement);
			//Insertion de l'ordre des taches
			for (int i = 0; i < lEvaluation.getOrdreTaches().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(new Integer(i + 1));
				lesParametres.add(((ArrayList) lEvaluation.getOrdreTaches().get(new Integer(i + 1))).get(0));
				lesParametres.add(((ArrayList) lEvaluation.getOrdreTaches().get(new Integer(i + 1))).get(1));
				idStatement = cBase.creerPreparedStatement("setOrdreActivites");
				cBase.executerRequeteStockee(idStatement, "setOrdreActivites", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des activités out
			for (int i = 0; i < lEvaluation.getActivitesHorsProcessus().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getActivitesHorsProcessus().get(i));
				lesParametres.add("1"); // 1 = OUT
				lesParametres.add("1"); // 1 = activités
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des activités non réalisées
			for (int i = 0; i < lEvaluation.getActivitesNonUtilisees().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getActivitesNonUtilisees().get(i));
				lesParametres.add("0"); // 0 = non utilisés
				lesParametres.add("1"); // 1 = activités
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des produits out
			for (int i = 0; i < lEvaluation.getProduitsHorsProcessus().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getProduitsHorsProcessus().get(i));
				lesParametres.add("1"); // 1 = OUT
				lesParametres.add("2"); // 2 = produits
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des produits non réalisées
			for (int i = 0; i < lEvaluation.getProduitsNonUtilises().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getProduitsNonUtilises().get(i));
				lesParametres.add("0"); // 0 = non utilisés
				lesParametres.add("2"); // 2 = produits
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des roles out
			for (int i = 0; i < lEvaluation.getRolesHorsProcessus().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getRolesHorsProcessus().get(i));
				lesParametres.add("1"); // 1 = OUT
				lesParametres.add("3"); // 3 = roles
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			//Insertion des roles non réalisées
			for (int i = 0; i < lEvaluation.getRolesNonUtilises().size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(lEvaluation.getRolesNonUtilises().get(i));
				lesParametres.add("0"); // 0 = non utilisés
				lesParametres.add("3"); // 3 = roles
				idStatement = cBase.creerPreparedStatement("setRolesEvaluations");
				cBase.executerRequeteStockee(idStatement, "setRolesEvaluations", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			// insertion des charges pas composant
			for (Iterator it = lEvaluation.getRepartitionDeschargesParComposant().keySet().iterator(); it.hasNext();)
			{
				String idComp = it.next().toString();
				lesParametres.clear();
				lesParametres.add(new Integer(lEvaluation.getId()));
				lesParametres.add(idComp);
				lesParametres.add((Float) lEvaluation.getRepartitionDeschargesParComposant().get(idComp));
				idStatement = cBase.creerPreparedStatement("setChargesParComposant");
				cBase.executerRequeteStockee(idStatement, "setChargesParComposant", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			cBase.commit();
		}
		catch (Exception e)
		{
			cBase.rollback();
			throw e;
		}
		finally
		{
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
	}

	/**
	 * Méthode permettant la sauvegarde de l'évaluation qualitative
	 */
	synchronized static public void saveEvaluationQualitativeEnBase(E_Evaluation lEvaluation) throws SQLException
	{
		int idStatement = 0;

		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(lEvaluation.getEvalQualitative());
		lesParametres.add(new Integer(lEvaluation.getId()));

		try
		{
			cBase.ouvrirConnexion();
			idStatement = cBase.creerPreparedStatement("setEvaluationQualitative");
			cBase.executerRequeteStockee(idStatement, "setEvaluationQualitative", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		finally
		{
			cBase.fermerConnexion();
		}
	}

	/**
	 * Méthode permettant la suppression d'une entité en base et dans la liste
	 */
	/*
	synchronized static public void delereEvaluationEnBase(String idProcessus, int idProjet, int idEvaluation, int numIt) throws SQLException
	{
		int idStatement = 0;
	
		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(new Integer(idEvaluation));
	
		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);
			idStatement = cBase.creerPreparedStatement("delOrdreActivites");
			cBase.executerRequeteStockee(idStatement, "delOrdreActivites", lesParametres);
			cBase.fermerStatement(idStatement);
	
			idStatement = cBase.creerPreparedStatement("delEvaluation");
			cBase.executerRequeteStockee(idStatement, "delEvaluation", lesParametres);
			cBase.fermerStatement(idStatement);
			
			idStatement = cBase.creerPreparedStatement("delRolesEvaluations");
			cBase.executerRequeteStockee(idStatement, "delRolesEvaluations", lesParametres);
			cBase.fermerStatement(idStatement);
			
			idStatement = cBase.creerPreparedStatement("delChargesDesComposants");
			cBase.executerRequeteStockee(idStatement, "delChargesDesComposants", lesParametres);
			cBase.fermerStatement(idStatement);
	
			cBase.commit();
	
			//Si la suppression en base est bonne, on efface de la liste
			 ((HashMap) ((HashMap) evaluations.get(idProcessus)).get(new Integer(idProjet))).remove(new Integer(numIt));
		}
		catch (Exception e)
		{
			cBase.rollback();
		}
		finally
		{
			cBase.fermerConnexion();
		}
	}
	*/
}
