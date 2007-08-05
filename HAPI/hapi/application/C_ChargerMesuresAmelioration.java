/*
 * Créé le 1 oct. 2005
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_MesureAmelioration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 */
public class C_ChargerMesuresAmelioration implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerMesuresAmelioration(C_AccesBaseDonnees laBase)
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
				idStatement = cBase.creerPreparedStatement("getMesureAmelioration");
				leResultat = cBase.executerRequeteStockee(idStatement, "getMesureAmelioration", lesParametres);
				while (leResultat.next())
				{
					E_MesureAmelioration uneMesure = new E_MesureAmelioration(idProcessus, leResultat.getString(2), leResultat.getDate(3), leResultat.getInt(4), leResultat.getInt(5), leResultat.getInt(6), leResultat.getInt(7), leResultat.getInt(8));
					
					C_Processus.ajouterMesureAmelioration(idProcessus,uneMesure);
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
