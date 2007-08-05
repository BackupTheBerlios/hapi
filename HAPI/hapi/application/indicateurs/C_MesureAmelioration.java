/*
 * Créé le 1 oct. 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Processus;
import hapi.donnees.E_MesureAmelioration;
import hapi.donnees.modeles.ModeleComboWithId;
import hapi.exception.NoRowInsertedException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Cédric
 */
public class C_MesureAmelioration
{
	private static ModeleComboWithId modeleVersion = null;

	public static ModeleComboWithId getModeleVersion(String idProcessus)
	{
		modeleVersion = new ModeleComboWithId();
		for (Iterator it = C_Processus.getProcessus(idProcessus).getListeDesVersion().iterator(); it.hasNext();)
		{
			ArrayList uneVersion = (ArrayList) it.next();
			modeleVersion.addElement((String) uneVersion.get(0),uneVersion.get(1));
		}

		return modeleVersion;
	}
	
	public static void selectionnerModele(String dateExport)
	{
		modeleVersion.setSelectedCle(dateExport);
	}

	public static void ajouterMesure(String idProcessus, String dateExport, Date dateModification, int nombrePropose, int nombrePris, int presentation, int modele, int documentation) throws Exception
	{
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		try
		{
			//Création d'un mesure
			E_MesureAmelioration uneMesure = new E_MesureAmelioration(idProcessus, dateExport, dateModification, nombrePropose, nombrePris, presentation, modele, documentation);

			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			ArrayList lesParametres = new ArrayList();
			//Suppression si elle existe
			lesParametres.clear();
			lesParametres.add(idProcessus);
			lesParametres.add(dateExport);

			try
			{
				idStatement = cBase.creerPreparedStatement("delUneMesureAmelioration");
				cBase.executerRequeteStockee(idStatement, "delUneMesureAmelioration", lesParametres);
				cBase.fermerStatement(idStatement);
			}
			catch (NoRowInsertedException e)
			{}

			//Insertion
			lesParametres.clear();
			lesParametres.add(idProcessus);
			lesParametres.add(dateExport);
			lesParametres.add(dateModification);
			lesParametres.add(new Integer(uneMesure.getNombrePropose()));
			lesParametres.add(new Integer(uneMesure.getNombrePris()));
			lesParametres.add(new Integer(uneMesure.getPresentation()));
			lesParametres.add(new Integer(uneMesure.getModele()));
			lesParametres.add(new Integer(uneMesure.getDocumentation()));

			idStatement = cBase.creerPreparedStatement("setMesureAmelioration");
			cBase.executerRequeteStockee(idStatement, "setMesureAmelioration", lesParametres);
			cBase.fermerStatement(idStatement);

			//insertion en mémoire
			C_Processus.getProcessus(idProcessus).ajouterMesureAmelioration(uneMesure);

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
	
	public static void ajouterMesureSansBD(String idProcessus, String dateExport, Date dateModification, int nombrePropose, int nombrePris, int presentation, int modele, int documentation)
	{
		//Création d'un mesure
		E_MesureAmelioration uneMesure = new E_MesureAmelioration(idProcessus, dateExport, dateModification, nombrePropose, nombrePris, presentation, modele, documentation);
		//insertion en mémoire
		C_Processus.getProcessus(idProcessus).ajouterMesureAmelioration(uneMesure);
	}
	
	public static void supprimerMesureSansBD(String idProcessus, String dateExport)
	{
		//suppression
		C_Processus.getProcessus(idProcessus).supprimerMesureAmelioration(dateExport);
	}	
}
