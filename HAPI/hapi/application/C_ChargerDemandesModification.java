package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_Demande;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Robin EYSSERIC
 */
public class C_ChargerDemandesModification implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerDemandesModification(C_AccesBaseDonnees laBase)
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

				idStatement = cBase.creerPreparedStatement("getDemandesModifications");
				leResultat = cBase.executerRequeteStockee(idStatement, "getDemandesModifications", lesParametres);

				//Pour chaque processus
				while (leResultat.next())
				{
					//Création de la demande
					E_Demande demande = new E_Demande();
					demande.setIdDemande(leResultat.getInt(1));
					demande.setIdProcessus(leResultat.getString(2));
					demande.setDateDemande(leResultat.getDate(3));
					demande.setNomComposant(leResultat.getString(4));
					demande.setDescription(leResultat.getString(5));
					demande.setType(leResultat.getString(6));
					demande.setSeverite(leResultat.getInt(7));

					C_GestionDemandes.ajouterDemande(C_Processus.get(i).getIdentifiant(), demande);
				}
				cBase.fermerStatement(idStatement);
			}
		}
		catch (SQLException sqle)
		{}
		finally
		{
			//try
			//{
			cBase.fermerStatement(idStatement);
			//cBase.fermerConnexion();
			//}
			//catch(SQLException e)
			//{
			//e.printStackTrace();
			//}
		}
	}

}
