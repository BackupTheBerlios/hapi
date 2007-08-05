/*
 * Créé le 24 juin 2005
 */
package hapi.application;

import hapi.application.indicateurs.C_CycleDeVie;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.donnees.E_CycleDeVie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Cédric
 */
public class C_ChargerCycleDeVie implements Runnable
{

	private C_AccesBaseDonnees cBase = null;

	public C_ChargerCycleDeVie(C_AccesBaseDonnees laBase)
	{
		cBase = laBase;
	}

	public void run()
	{
		int idStatement = 0;

		try
		{
			ArrayList lesParametres = new ArrayList();
			HashMap lesExecutions = new HashMap();
			ResultSet leResultat = null;
			for (int i = 0; i < C_Processus.size(); i++)
			{
				//Récupération des exécutions du processus
				//Récupération de tous les projets chargés
				lesExecutions = C_ExecutionProcessus.getExecutionsProcessusDuProcessus(C_Processus.get(i).getIdentifiant());

				ArrayList lesIdentifiantsCycle = new ArrayList();

				//Pour chaque exécutions
				for (Iterator it = lesExecutions.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(it.next());

					idStatement = cBase.creerPreparedStatement("getCycleDeVieParProjet");
					leResultat = cBase.executerRequeteStockee(idStatement, "getCycleDeVieParProjet", lesParametres);
					if (leResultat.next())
					{
						lesIdentifiantsCycle.add(new Integer(leResultat.getInt(1)));
					}
					cBase.fermerStatement(idStatement);
				}

				//Pour chaque cycle
				for (Iterator it = lesIdentifiantsCycle.iterator(); it.hasNext();)
				{
					Integer unIdentifiantCycle = (Integer) it.next();
					ArrayList lesProduits = new ArrayList();
					ArrayList lesActivites = new ArrayList();
					ArrayList lesRoles = new ArrayList();

					lesParametres.clear();
					lesParametres.add(unIdentifiantCycle);

					idStatement = cBase.creerPreparedStatement("getCycleProduit");
					leResultat = cBase.executerRequeteStockee(idStatement, "getCycleProduit", lesParametres);
					while (leResultat.next())
					{
						lesProduits.add(leResultat.getString(2));
					}
					cBase.fermerStatement(idStatement);

					idStatement = cBase.creerPreparedStatement("getCycleRole");
					leResultat = cBase.executerRequeteStockee(idStatement, "getCycleRole", lesParametres);
					while (leResultat.next())
					{
						lesRoles.add(leResultat.getString(2));
					}
					cBase.fermerStatement(idStatement);

					idStatement = cBase.creerPreparedStatement("getCycleActivite");
					leResultat = cBase.executerRequeteStockee(idStatement, "getCycleActivite", lesParametres);
					while (leResultat.next())
					{
						lesActivites.add(leResultat.getString(2));
					}
					cBase.fermerStatement(idStatement);

					E_CycleDeVie unCycle = new E_CycleDeVie(lesActivites, lesRoles, lesProduits);
					C_CycleDeVie.ajouterUnCycleDeVie(unIdentifiantCycle.intValue(), unCycle);
				}
			}
		}
		catch (SQLException sqle)
		{}
		finally
		{
			cBase.fermerStatement(idStatement);
		}
	}
}
