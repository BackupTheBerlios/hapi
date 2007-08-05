package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_Modification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Robin EYSSERIC
 */
public class C_ChargerModifications implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerModifications(C_AccesBaseDonnees laBase)
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

				idStatement = cBase.creerPreparedStatement("getHistoriqueModifications");
				leResultat = cBase.executerRequeteStockee(idStatement, "getHistoriqueModifications", lesParametres);

				//Pour chaque processus
				while (leResultat.next())
				{
					//Création de la modification
					E_Modification modification = new E_Modification();
					modification.setIdModification(leResultat.getInt(1));
					modification.setIdProcessus(leResultat.getString(2));
					modification.setDateDemande(leResultat.getString(3));
					modification.setNomComposant(leResultat.getString(4));
					modification.setDescription(leResultat.getString(5));
					modification.setVersionModification(leResultat.getString(6));
					modification.setType(leResultat.getString(7));
					modification.setSeverite(leResultat.getInt(8));

					C_GestionModifications.ajouterModification(C_Processus.get(i).getIdentifiant(), modification);
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
			//	e.printStackTrace();
			//}
		}
	}

}
