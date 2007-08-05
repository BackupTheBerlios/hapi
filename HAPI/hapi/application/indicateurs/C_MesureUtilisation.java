/*
 * Créé le 30 sept. 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureUtilisation;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.donnees.modeles.ModeleComboWithId;
import hapi.donnees.modeles.ModeleTableIdProcessus;
import hapi.exception.ChampsVideException;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Cédric
 */
public class C_MesureUtilisation
{
	private static ModeleTableIdProcessus modele = null;
	private static ModeleComboWithId modeleProjets = new ModeleComboWithId();
	
	public static ModeleTableIdProcessus getModeleMesureUtilisationProcessus(String idProcessus)
	{
		modele = new ModeleTableIdProcessus();

		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_MesureUtilisation_champ_projet"));
		modele.addColumn(Bundle.getText("OO_MesureUtilisation_champ_nombrePT"));
		modele.addColumn(Bundle.getText("OO_MesureUtilisation_champ_pourPT"));
		modele.addColumn(Bundle.getText("OO_MesureUtilisation_champ_nombreAct"));
		modele.addColumn(Bundle.getText("OO_MesureUtilisation_champ_pourAct"));

		ArrayList lesMesureUtilisationProcessus = getMesureUtilisationProcessus(idProcessus);
		
		if (lesMesureUtilisationProcessus != null)
		{
			for (int i = 0; i < lesMesureUtilisationProcessus.size(); i++)
			{
				modele.getId().add(new Integer(((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getId()));
				Vector uneLigne = new Vector();
				
				uneLigne.add(C_ExecutionProcessus.getExecutionProcessus(idProcessus,((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getIdExec()).getNomSansVersion());
				uneLigne.add(new Integer(((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getNombrePlanType()));
				uneLigne.add(new Double(((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getPourPlanType()));
				uneLigne.add(new Integer(((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getNombreActivites()));
				uneLigne.add(new Double(((E_MesureUtilisation) lesMesureUtilisationProcessus.get(i)).getPourActivite()));
				modele.addRow(uneLigne);
			}
		}

		return modele;
	}
	
	static public ArrayList getMesureUtilisationProcessus(String id_processus)
	{
		return C_Processus.getMesuresUtilisation(id_processus);
	}
	

	static public void supprimerMesureUtilisation(int ligne, String id_proc) throws SQLException
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
			idStatement = cBase.creerPreparedStatement("delMesureUtilisation");
			cBase.executerRequeteStockee(idStatement, "delMesureUtilisation", lesParametres);
			
			ArrayList mesures_tmp = getMesureUtilisationProcessus(id_proc);
			if (mesures_tmp != null)
			{
				for (int i = 0; i < mesures_tmp.size(); i++)
				{
					if (((E_MesureUtilisation) mesures_tmp.get(i)).getId() == idMesure)
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
	
	public static void ajouterMesure(String idProc, int indiceProjet, String nombrePT, double pourPT,String nombreAct,double pourAct) throws Exception
	{
		// Teste si une description a été saisie
		if (nombrePT.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureUtilisation_NombrePTNonRemplie"));
		}
		if (nombreAct.length() == 0)
		{
			throw new ChampsVideException(Bundle.getText("OO_MesureUtilisation_NombreActNonRemplie"));
		}	

		E_MesureUtilisation uneMesure = new E_MesureUtilisation(modeleProjets.getCleAt(indiceProjet),new Integer(nombrePT).intValue(), pourPT, new Integer(nombreAct).intValue(), pourAct);

		// Enregistrement en mémoire
		C_Processus.ajouterMesureUtilisation(idProc, uneMesure);

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
			ArrayList les_mesures = getMesureUtilisationProcessus(id_processus);
			ArrayList lesParametres = new ArrayList();

			for (int i = 0; i < les_mesures.size(); i++)
			{
				E_MesureUtilisation uneMesure = (E_MesureUtilisation) les_mesures.get(i);

				if (uneMesure.getId() == -1)
				{
					lesParametres.clear();
					lesParametres.add(uneMesure.getIdExec());
					lesParametres.add(new Integer(uneMesure.getNombrePlanType()));
					lesParametres.add(new Double(uneMesure.getPourPlanType()));
					lesParametres.add(new Integer(uneMesure.getNombreActivites()));
					lesParametres.add(new Double(uneMesure.getPourActivite()));
					lesParametres.add(id_processus);

					idStatement = cBase.creerPreparedStatement("setMesureUtilisation");
					cBase.executerRequeteStockee(idStatement, "setMesureUtilisation", lesParametres);
					cBase.fermerStatement(idStatement);

					// Recherche de l'id d'insertion
					idStatement = cBase.creerPreparedStatement("getMaxMesureUtilisation");
					ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxMesureUtilisation");
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
	
	public static ModeleComboWithId getModeleProjets(String idProcessus)
	{
		HashMap lesProjets = C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus);
		ArrayList lesIdProjet = new ArrayList(lesProjets.keySet());
		ArrayList lesMesures = C_Processus.getProcessus(idProcessus).getListeMesuresUtilisation();
		
		modeleProjets.removeAllElements();
		
		for (Iterator it=lesMesures.iterator();it.hasNext();)
		{
			E_MesureUtilisation uneMesure = (E_MesureUtilisation) it.next();
			lesIdProjet.remove(uneMesure.getIdExec());
		}
		
		for (Iterator it=lesIdProjet.iterator();it.hasNext();)
		{
			String idProjet = (String) it.next();
			modeleProjets.addElement(idProjet,((E_ExecutionProcessus)lesProjets.get(idProjet)).getNomSansVersion());
		}
		
		return modeleProjets;
	}
	
	public static String getClefProjet(int indiceSelectionne)
	{
		return modeleProjets.getCleAt(indiceSelectionne);
	}

}
