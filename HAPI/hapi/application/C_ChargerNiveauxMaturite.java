/*
 * Créé le 18 sept. 2005
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_NiveauMaturite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Cédric
 */
public class C_ChargerNiveauxMaturite implements Runnable
{

	private C_AccesBaseDonnees cBase = null;

	public C_ChargerNiveauxMaturite(C_AccesBaseDonnees laBase)
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
				lesParametres.clear();
				lesParametres.add(C_Processus.get(i).getIdentifiant());
				ArrayList lesNiveauxDuProcessus = new ArrayList();
				
				idStatement = cBase.creerPreparedStatement("getNiveauMaturite");
				leResultat = cBase.executerRequeteStockee(idStatement, "getNiveauMaturite", lesParametres);
				while (leResultat.next())
				{
					lesNiveauxDuProcessus.add(leResultat.getInt(2)-1,new E_NiveauMaturite(leResultat.getInt(2),leResultat.getString(3),leResultat.getDate(4),leResultat.getDate(5)));
				}
				cBase.fermerStatement(idStatement);
				
				C_NiveauMaturite.ajouterNiveauMaturite(C_Processus.get(i).getIdentifiant(),lesNiveauxDuProcessus);
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
