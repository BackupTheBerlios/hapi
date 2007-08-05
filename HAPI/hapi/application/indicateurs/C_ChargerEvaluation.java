/*
 * Created on 24 févr. 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_Hapi;
import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_Processus;
import hapi.donnees.metier.E_Evaluation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */
public class C_ChargerEvaluation implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerEvaluation(C_AccesBaseDonnees laBase)
	{
		cBase = laBase;
	}

	public void run()
	{
		int idStatement;
		//C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			//cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			ResultSet leResultat = null;

			// liste des evaluations
			ArrayList listeEval = new ArrayList();

			// Chargement des evaluations quantitatives
			// Pour chaque processus, recherche des composants associés
			for (int j = 0; j < C_Processus.size(); j++)
			{
				String id_processus = C_Processus.get(j).getIdentifiant();

				/**
				 * Récupération des évaluations
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getEvaluationParProcessus");
				leResultat = cBase.executerRequeteStockee(idStatement, "getEvaluationParProcessus", lesParametres);

				//Pour chaque evaluation
				while (leResultat.next())
				{
					//Ajout de l'id dans le processus
					C_Processus.get(j).ajouterIdComposant(leResultat.getString(1));

					//Création du composant
					E_Evaluation eval = new E_Evaluation(C_Hapi.EVALUATION_ICONE);
					eval.setId(leResultat.getInt(1));
					listeEval.add(new Integer(leResultat.getInt(1)));
					eval.setIdProcessus(leResultat.getString(2));
					eval.setIdExecutionProcessus(leResultat.getInt(3));
					eval.setNumIteration(leResultat.getInt(4));
					eval.setRUA(leResultat.getFloat(5));
					eval.setRCT(leResultat.getFloat(6));
					eval.setRUR(leResultat.getFloat(7));
					eval.setRUP(leResultat.getFloat(8));
					eval.setEvalQualitative(leResultat.getString(9));
					eval.setDateDebut(leResultat.getDate(10));
					eval.setDateFin(leResultat.getDate(11));

					C_Evaluation.ajouterEvaluationQuantitative(leResultat.getString(2), leResultat.getInt(3), leResultat.getInt(4), eval);

				}
				cBase.fermerStatement(idStatement);

			}

			// pour chaque évaluation, récupération de la liste d'activités
			for (Iterator it = listeEval.iterator(); it.hasNext();)
			{
				E_Evaluation eval = C_Evaluation.getEvaluation(((Integer) it.next()).intValue());

				lesParametres.clear();
				lesParametres.add(new Integer(eval.getId()));

				idStatement = cBase.creerPreparedStatement("getOrdreActivites");
				leResultat = cBase.executerRequeteStockee(idStatement, "getOrdreActivites", lesParametres);

				//	Pour chaque liste
				while (leResultat.next())
				{
					eval.classerActivite(leResultat.getInt(2), leResultat.getString(3), leResultat.getString(4));
				}
				cBase.fermerStatement(idStatement);

				idStatement = cBase.creerPreparedStatement("getRolesEvaluations");
				leResultat = cBase.executerRequeteStockee(idStatement, "getRolesEvaluations", lesParametres);

				//Pour chaque evaluation
				while (leResultat.next())
				{
					switch (Integer.parseInt(leResultat.getString(4)))
					{
						case 1 : //Activités
							if (leResultat.getString(3).equals("1")) // hors processus
								eval.addActivitesHorsProcessus(leResultat.getString(2));
							else
								eval.addActivitesNonUtilisees(leResultat.getString(2));
							break;
						case 2 : //Produits
							if (leResultat.getString(3).equals("1")) // hors processus
								eval.addProduitsHorsProcessus(leResultat.getString(2));
							else
								eval.addProduitsNonUtilises(leResultat.getString(2));
							break;
						case 3 : // roles
							if (leResultat.getString(3).equals("1")) // hors processus
								eval.addRolesHorsProcessus(leResultat.getString(2));
							else
								eval.addRolesNonUtilises(leResultat.getString(2));
							break;

						default :
							throw new SQLException();
					}
				}
				cBase.fermerStatement(idStatement);

				// recherche des charges pas composant
				idStatement = cBase.creerPreparedStatement("getChargesParComposant");
				leResultat = cBase.executerRequeteStockee(idStatement, "getChargesParComposant", lesParametres);

				HashMap chargesParComposant = new HashMap();
				//	Pour chaque liste
				while (leResultat.next())
				{
					chargesParComposant.put(leResultat.getString(2), new Float(leResultat.getFloat(3)));
				}
				eval.setRepartitionDeschargesParComposant(chargesParComposant);

				cBase.fermerStatement(idStatement);
			}
		}
		catch (SQLException e)
		{}
		finally
		{
			/*try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}*/
		}
	}
}
