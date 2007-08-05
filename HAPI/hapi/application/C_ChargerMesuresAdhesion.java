/*
 * Créé le 29 sept. 2005
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_MesureAcces;
import hapi.donnees.E_MesureFormation;
import hapi.donnees.E_MesureUtilisation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 */
public class C_ChargerMesuresAdhesion implements Runnable
{

	private C_AccesBaseDonnees cBase = null;

	public C_ChargerMesuresAdhesion(C_AccesBaseDonnees laBase)
	{
		cBase = laBase;
	}

	public void run()
	{
		int idStatement = 0;

		try
		{
			ArrayList lesParametres = new ArrayList();
			ResultSet leResultat = null;

			for (int i = 0; i < C_Processus.size(); i++)
			{
				String idProcessus = C_Processus.get(i).getIdentifiant();

				lesParametres.clear();
				lesParametres.add(idProcessus);
				idStatement = cBase.creerPreparedStatement("getMesureAcces");
				leResultat = cBase.executerRequeteStockee(idStatement, "getMesureAcces", lesParametres);
				while (leResultat.next())
				{
					E_MesureAcces uneMesure = new E_MesureAcces(leResultat.getInt(1), leResultat.getDate(2), leResultat.getInt(3));
					
					C_Processus.ajouterMesureAcces(idProcessus,uneMesure);
				}
				cBase.fermerStatement(idStatement);
				
				lesParametres.clear();
				lesParametres.add(idProcessus);
				idStatement = cBase.creerPreparedStatement("getMesureFormation");
				leResultat = cBase.executerRequeteStockee(idStatement, "getMesureFormation", lesParametres);
				while (leResultat.next())
				{
					E_MesureFormation uneMesure = new E_MesureFormation(leResultat.getInt(1), leResultat.getDate(2), leResultat.getInt(3), leResultat.getInt(4), leResultat.getDouble(5));
					
					C_Processus.ajouterMesureFormation(idProcessus,uneMesure);
				}
				cBase.fermerStatement(idStatement);
				
				lesParametres.clear();
				lesParametres.add(idProcessus);
				idStatement = cBase.creerPreparedStatement("getMesureUtilisation");
				leResultat = cBase.executerRequeteStockee(idStatement, "getMesureUtilisation", lesParametres);
				while (leResultat.next())
				{
					E_MesureUtilisation uneMesure = new E_MesureUtilisation(leResultat.getInt(1), leResultat.getString(2), leResultat.getInt(3), leResultat.getDouble(4), leResultat.getInt(5), leResultat.getDouble(6));
					
					C_Processus.ajouterMesureUtilisation(idProcessus,uneMesure);
				}
				cBase.fermerStatement(idStatement);
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
