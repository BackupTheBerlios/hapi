package hapi.application;

import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.donnees.metier.E_ExecutionProcessus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Vincent
 */
public class C_ChargerExecutionProcessus implements Runnable
{
	private C_AccesBaseDonnees cBase = null;
	private Thread leChargementCycle = null;

	public C_ChargerExecutionProcessus(C_AccesBaseDonnees laBase, Thread chargementCycle)
	{
		cBase = laBase;
		leChargementCycle = chargementCycle;
	}

	public void run()
	{
		//C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			//cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			ResultSet leResultat = null;
			for (int i = 0; i < C_Processus.size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(C_Processus.get(i).getIdentifiant());
				idStatement = cBase.creerPreparedStatement("getExecutionsProcessus");
				leResultat = cBase.executerRequeteStockee(idStatement, "getExecutionsProcessus", lesParametres);
				while (leResultat.next())
				{
					E_ExecutionProcessus uneExecProc = new E_ExecutionProcessus(leResultat.getString(4), C_Hapi.PROJET_ICONE, -1);
					uneExecProc.setNotreId(leResultat.getInt(1));
					uneExecProc.setId(leResultat.getInt(2));
					uneExecProc.setNom(leResultat.getString(5));
					uneExecProc.setDescription(leResultat.getString(6));
					uneExecProc.setDateDebut(leResultat.getDate(7));
					uneExecProc.setDateFin(leResultat.getDate(8));
					uneExecProc.setCommentaire(leResultat.getString(9));

					ArrayList lesParametres2 = new ArrayList();
					lesParametres2.add(new Integer(leResultat.getInt(1)));
					int idStatement2 = cBase.creerPreparedStatement("getCycleDeVieParProjet");
					ResultSet leResultat2 = cBase.executerRequeteStockee(idStatement2, "getCycleDeVieParProjet", lesParametres2);
					if (leResultat2.next())
						uneExecProc.setIdCycleDeVie(leResultat2.getInt(1));
					cBase.fermerStatement(idStatement2);

					C_ExecutionProcessus.ajouterExecutionProcessus(C_Processus.get(i).getIdentifiant(), uneExecProc.getIdentifiant(), uneExecProc);

				}
			}
		}
		catch (SQLException sqle)
		{}
		finally
		{
			cBase.fermerStatement(idStatement);
			//Lancement du thrad de chargement des cycles
			leChargementCycle.start();

		}
	}

}
