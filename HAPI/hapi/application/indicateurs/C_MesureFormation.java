/*
 * Créé le 30 sept. 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureFormation;
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
public class C_MesureFormation
{
	private static ModeleTableIdProcessus modele = null;
	
	public static ModeleTableIdProcessus getModeleMesureFormationProcessus(String idProcessus)
	{
		modele = new ModeleTableIdProcessus();

		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_MesureFormation_champ_date"));
		modele.addColumn(Bundle.getText("OO_MesureFormation_champ_effPro"));
		modele.addColumn(Bundle.getText("OO_MesureFormation_champ_effMes"));
		modele.addColumn(Bundle.getText("OO_MesureFormation_champ_note"));

		ArrayList lesMesureFormationProcessus = getMesureFormationProcessus(idProcessus);
		
		if (lesMesureFormationProcessus != null)
		{
			SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
			for (int i = 0; i < lesMesureFormationProcessus.size(); i++)
			{
				modele.getId().add(new Integer(((E_MesureFormation) lesMesureFormationProcessus.get(i)).getId()));
				Vector uneLigne = new Vector();
				
				uneLigne.add(sfDate.format(((E_MesureFormation) lesMesureFormationProcessus.get(i)).getDateMesure()));
				uneLigne.add(new Integer(((E_MesureFormation) lesMesureFormationProcessus.get(i)).getEffectifProcessus()));
				uneLigne.add(new Integer(((E_MesureFormation) lesMesureFormationProcessus.get(i)).getEffectifMesure()));
				uneLigne.add(new Double(((E_MesureFormation) lesMesureFormationProcessus.get(i)).getNote()));
				modele.addRow(uneLigne);
			}
		}

		return modele;
	}
	
	static public ArrayList getMesureFormationProcessus(String id_processus)
	{
		return (ArrayList) C_Processus.getMesuresFormation(id_processus);
	}	

	static public void supprimerMesureFormation(int ligne, String id_proc) throws SQLException
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
			idStatement = cBase.creerPreparedStatement("delMesureFormation");
			cBase.executerRequeteStockee(idStatement, "delMesureFormation", lesParametres);
			
			ArrayList mesures_tmp = getMesureFormationProcessus(id_proc);
			if (mesures_tmp != null)
			{
				for (int i = 0; i < mesures_tmp.size(); i++)
				{
					if (((E_MesureFormation) mesures_tmp.get(i)).getId() == idMesure)
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
	
	public static void ajouterMesure(String idProc, Date dateMesure, String effectifPro,String effectifMes,Double note) throws Exception
	{
		// Teste si une description a été saisie
		if (dateMesure.equals(""))
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureFormation_DateNonRemplie"));
		}
		if (effectifPro.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureFormation_EffProNonRemplie"));
		}
		if (effectifMes.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureFormation_EffMesNonRemplie"));
		}	

		E_MesureFormation uneMesure = new E_MesureFormation(dateMesure,new Integer(effectifPro).intValue(),new Integer(effectifMes).intValue(),note.doubleValue());

		// Enregistrement en mémoire
		C_Processus.ajouterMesureFormation(idProc, uneMesure);

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
			ArrayList les_mesures = getMesureFormationProcessus(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_mesures.size(); i++)
			{
				E_MesureFormation uneMesure = (E_MesureFormation) les_mesures.get(i);

				if (uneMesure.getId() == -1)
				{
					lesParametres.clear();
					lesParametres.add(uneMesure.getDateMesure());
					lesParametres.add(new Integer(uneMesure.getEffectifProcessus()));
					lesParametres.add(new Integer(uneMesure.getEffectifMesure()));
					lesParametres.add(new Double(uneMesure.getNote()));
					lesParametres.add(id_processus);

					idStatement = cBase.creerPreparedStatement("setMesureFormation");
					cBase.executerRequeteStockee(idStatement, "setMesureFormation", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxMesureFormation");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxMesureFormation");
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
