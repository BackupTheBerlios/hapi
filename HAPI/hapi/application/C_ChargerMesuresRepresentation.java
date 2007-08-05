/*
 * Créé le 20 sept. 2005
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_MesureRepresentation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 */
public class C_ChargerMesuresRepresentation implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerMesuresRepresentation(C_AccesBaseDonnees laBase)
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
				idStatement = cBase.creerPreparedStatement("getMesuresRepresentation");
				leResultat = cBase.executerRequeteStockee(idStatement, "getMesuresRepresentation", lesParametres);
				while (leResultat.next())
				{
					E_MesureRepresentation uneMesure = new E_MesureRepresentation(idProcessus, leResultat.getString(2), leResultat.getInt(3), leResultat.getInt(4), leResultat.getInt(5), leResultat.getString(6), leResultat.getInt(7), leResultat.getString(8), leResultat.getInt(9), leResultat.getString(10), leResultat.getDouble(11), leResultat.getString(12), leResultat.getInt(13), leResultat.getString(14), leResultat.getInt(15), leResultat.getString(16), leResultat.getInt(17), leResultat.getInt(18), leResultat
							.getInt(19), leResultat.getInt(20), leResultat.getDouble(21), leResultat.getString(22), leResultat.getInt(23), leResultat.getString(24), leResultat.getDouble(25), leResultat.getInt(26), leResultat.getString(27), leResultat.getDouble(28), leResultat.getInt(29), leResultat.getString(30), leResultat.getDouble(31), leResultat.getDouble(32), leResultat.getString(33));
					
					C_Processus.ajouterMesureRepresentation(idProcessus,uneMesure);
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
