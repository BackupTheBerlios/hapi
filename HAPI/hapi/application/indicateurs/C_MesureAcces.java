/*
 * Créé le 30 sept. 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureAcces;
import hapi.donnees.modeles.ModeleTableIdProcessus;
import hapi.exception.ChampsVideException;
import hapi.exception.NoRowInsertedException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Cédric
 */
public class C_MesureAcces
{
	private static ModeleTableIdProcessus modele = null;
	
	public static ModeleTableIdProcessus getModeleMesureAccesProcessus(String idProcessus)
	{
		modele = new ModeleTableIdProcessus();

		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_MesureAcces_champ_mois"));
		modele.addColumn(Bundle.getText("OO_MesureAcces_champ_nombre"));

		ArrayList lesMesureAccesProcessus = getMesureAccesProcessus(idProcessus);
		
		if (lesMesureAccesProcessus != null)
		{
			SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.PETITMOIS_MODEL);
			for (int i = 0; i < lesMesureAccesProcessus.size(); i++)
			{
				modele.getId().add(new Integer(((E_MesureAcces) lesMesureAccesProcessus.get(i)).getId()));
				Vector uneLigne = new Vector();
				
				uneLigne.add(sfDate.format(((E_MesureAcces) lesMesureAccesProcessus.get(i)).getMois()));
				uneLigne.add(new Integer(((E_MesureAcces) lesMesureAccesProcessus.get(i)).getNombre()));
				modele.addRow(uneLigne);
			}
		}

		return modele;
	}
	
	static public ArrayList getMesureAccesProcessus(String id_processus)
	{
		return (ArrayList) C_Processus.getMesuresAcces(id_processus);
	}	

	static public void supprimerMesureAcces(int ligne, String id_proc) throws SQLException
	{
		int idMesure = ((Integer) modele.getId().get(ligne)).intValue();
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Paramétrage de la requête avec l'id
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(new Integer(idMesure));

			// Suppression
			idStatement = cBase.creerPreparedStatement("delMesureAcces");
			cBase.executerRequeteStockee(idStatement, "delMesureAcces", lesParametres);
			
			ArrayList mesures_tmp = getMesureAccesProcessus(id_proc);
			if (mesures_tmp != null)
			{
				for (int i = 0; i < mesures_tmp.size(); i++)
				{
					if (((E_MesureAcces) mesures_tmp.get(i)).getId() == idMesure)
					{
						mesures_tmp.remove(i);
					}
				}
			}
			else
			{}
			
			cBase.commit();
			cBase.fermerConnexion();
		}
		catch (NoRowInsertedException e)
		{
			cBase.rollback();
		}
		catch (SQLException e)
		{
			cBase.rollback();
			throw new SQLException();
		}
		finally
		{
			cBase.fermerStatement(idStatement);
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		}
	}
	
	public static void ajouterMesure(String idProc, Date moisMesure, String nombreAcces) throws Exception
	{
		// Teste si une description a été saisie
		if (moisMesure.equals(""))
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureAcces_DateNonRemplie"));
		}
		if (nombreAcces.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureAcces_NombreNonRemplie"));
		}	

		E_MesureAcces uneMesure = new E_MesureAcces(moisMesure,new Integer(nombreAcces).intValue());

		// Enregistrement en mémoire
		C_Processus.ajouterMesureAcces(idProc, uneMesure);

		// Enregistrement en base
		enregistrerMesureEnBaseP(idProc);
	}
	
	private static void enregistrerMesureEnBaseP(String id_processus) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			// Demandes à sauvegarder
			ArrayList les_mesures = getMesureAccesProcessus(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_mesures.size(); i++)
			{
				E_MesureAcces uneMesure = (E_MesureAcces) les_mesures.get(i);

				if (uneMesure.getId() == -1)
				{
					lesParametres.clear();
					lesParametres.add(uneMesure.getMois());
					lesParametres.add(new Integer(uneMesure.getNombre()));
					lesParametres.add(id_processus);

					idStatement = cBase.creerPreparedStatement("setMesureAcces");
					cBase.executerRequeteStockee(idStatement, "setMesureAcces", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxMesureAcces");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxMesureAcces");
					leResultat.next();
					uneMesure.setId(leResultat.getInt(1));
					cBase.fermerStatement(idStatement);
				}
			}
			cBase.commit();
		}
		catch (Exception nr)
		{
			cBase.rollback();
			throw nr;
		}
		finally
		{
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		}
	}	
	
}
