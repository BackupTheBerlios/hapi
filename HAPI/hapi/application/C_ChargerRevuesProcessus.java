/*
 * Créé le 23 sept. 2005
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_RevueProcessus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 *
 */
public class C_ChargerRevuesProcessus implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerRevuesProcessus(C_AccesBaseDonnees laBase)
	{
		cBase = laBase;
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

				idStatement = cBase.creerPreparedStatement("getRevuesProcessus");
				leResultat = cBase.executerRequeteStockee(idStatement, "getRevuesProcessus", lesParametres);

				//Pour chaque processus
				while (leResultat.next())
				{
					//Création de la demande
					E_RevueProcessus UneRevue = new E_RevueProcessus(leResultat.getInt(1),leResultat.getDate(2),leResultat.getDate(3),leResultat.getString(4),leResultat.getString(5),C_Processus.get(i).getIdentifiant());

					C_RevueProcessus.ajouterRevue(C_Processus.get(i).getIdentifiant(), UneRevue);
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
