/*
 * Créé le 19 sept. 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureRepresentation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

/**
 * @author Cédric
 */
public class C_MesureVersion
{
	//Mémorisation entre chaque fenêtres
	public final static int NOMBRE_FENETRES = 3;
	private static FenetreAssistee laFenetreCourante = null;
	private static ArrayList lesParametresFen0 = new ArrayList();
	private static ArrayList lesParametresFen1 = new ArrayList();
	private static ArrayList lesParametresFen2 = new ArrayList();
	private static String dateExportEnCours = null;
	private static String idProcessus;

	public static String getTitreFenetre(int Numero)
	{
		switch (Numero)
		{
			case 0:
				return Bundle.getText("BD_MesureVersion_fen0");
			case 1:
				return Bundle.getText("BD_MesureVersion_fen1");
			case 2:
				return Bundle.getText("BD_MesureVersion_fen2");
		}
		return "Erreur grave n° 6 !!!!";
	}

	/**
	 * Création d'une fenêtre
	 */
	public static void initialiseFenetre(int Numero, String id_Processus, FenetreAssistee fenetreCourante)
	{
		idProcessus = id_Processus;
		if (dateExportEnCours == null)
			dateExportEnCours = C_Processus.getProcessus(idProcessus).getDateExport();

		laFenetreCourante = fenetreCourante;

		laFenetreCourante.setParametres(prepareParametres(Numero));
		laFenetreCourante.setNumeroFenetre(Numero);
	}

	/**
	 * Récupération des paramètres des fenêtres
	 */
	private static ArrayList prepareParametres(int Numero)
	{
		ArrayList retour = null;

		DefaultComboBoxModel lesVersions = new DefaultComboBoxModel();

		for (Iterator it = C_Processus.getProcessus(idProcessus).getListeNumeroVersion().iterator(); it.hasNext();)
			lesVersions.addElement(it.next());
		lesVersions.setSelectedItem(C_Processus.getProcessus(idProcessus).getnumeroVersion(dateExportEnCours));

		switch (Numero)
		{
			case 0:
				retour = prepareParam0(idProcessus);
				break;
			case 1:
				retour = prepareParam1(idProcessus);
				break;
			case 2:
				retour = prepareParam2(idProcessus);
				break;
		}

		retour.add(0, lesVersions);

		return retour;
	}

	/**
	 * Préparation des paramètres de la fenêtre 0
	 */
	private static ArrayList prepareParam0(String idProcessus)
	{
		if (lesParametresFen0.size() == 0)
		{
			ArrayList retour = new ArrayList();
	
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getScenario()));
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getExplicite()));
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getEstime()));
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getEvaluationIT()));
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComScenario());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComExplicite());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComEstime());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComEvalIT());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getInterfaceE()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getInterfaceS()).toString());		
	
			return retour;
		}
		else
		{
			if (lesParametresFen0.get(0) instanceof DefaultComboBoxModel)
				lesParametresFen0.remove(0);
			return lesParametresFen0;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 1
	 */
	private static ArrayList prepareParam1(String idProcessus)
	{
		if (lesParametresFen1.size() == 0)
		{
			ArrayList retour = new ArrayList();
	
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getCoherence()));
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getCompletude()));
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbDef()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbAct()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProd()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbRole()).toString());
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getEvaluationMo()));
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComCoherence());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComCompletude());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComEvalMo());
	
			return retour;
		}
		else
		{
			if (lesParametresFen1.get(0) instanceof DefaultComboBoxModel)
				lesParametresFen1.remove(0);
			return lesParametresFen1;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 2
	 */
	private static ArrayList prepareParam2(String idProcessus)
	{
		if (lesParametresFen2.size() == 0)
		{
			ArrayList retour = new ArrayList();
	
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProdType()));
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getPourPlan()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbGuide()));
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getPourGuide()).toString());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbDocu()));
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getPourDocu()).toString());
			retour.add(new Double(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getEvaluationDocu()));
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComNbProdType());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComGuide());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComDocu());
			retour.add(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getComEvalDocu());
			retour.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProd()));
	
			int total = C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbDef() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbAct() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProd() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbRole();
	
			retour.add(new Integer(total));
	
			return retour;
		}
		else
		{
			lesParametresFen2.add(new Integer(C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProd()));
			int total = C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbDef() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbAct() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbProd() + C_Processus.getProcessus(idProcessus).getMesureRepresentation(dateExportEnCours).getNbRole();
			lesParametresFen2.add(new Integer(total));

			if (lesParametresFen2.get(0) instanceof DefaultComboBoxModel)
				lesParametresFen2.remove(0);
			return lesParametresFen2;			
		}
	}

	public static void setIndiceVersion(int lIndice)
	{
		dateExportEnCours = (String) C_Processus.getProcessus(idProcessus).getListeDateExport().get(lIndice);
		clearListesCourantes();
		laFenetreCourante.setParametres(prepareParametres(laFenetreCourante.getNumeroFenetre()));
	}

	public static void memoriseListesCourantes()
	{
		ArrayList lesParams = laFenetreCourante.getParametresSaisis();
		switch (laFenetreCourante.getNumeroFenetre())
		{
			case 0:
				lesParametresFen0.clear();
				lesParametresFen0.addAll(lesParams);
				break;
			case 1:
				lesParametresFen1.clear();
				lesParametresFen1.addAll(lesParams);
				break;
			case 2:
				lesParametresFen2.clear();
				lesParametresFen2.addAll(lesParams);
				break;
		}

	}

	public static void clearListesCourantes()
	{
		lesParametresFen0.clear();
		lesParametresFen1.clear();
		lesParametresFen2.clear();
	}

	public static void saveListe() throws SQLException
	{
		if (lesParametresFen0.size() == 0)
		{
			lesParametresFen0 = prepareParam0(idProcessus);
		}
		if (lesParametresFen1.size() == 0)
		{
			lesParametresFen1 = prepareParam1(idProcessus);
		}
		if (lesParametresFen2.size() == 0)
		{
			lesParametresFen2 = prepareParam2(idProcessus);
		}		
		
		int idStatement = 0;
		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			lesParametres.add(new Integer((String)lesParametresFen0.get(8)));
			lesParametres.add(new Integer((String)lesParametresFen0.get(9)));
			lesParametres.add(lesParametresFen0.get(0));
			lesParametres.add(lesParametresFen0.get(4));
			lesParametres.add(lesParametresFen0.get(1));
			lesParametres.add(lesParametresFen0.get(5));
			lesParametres.add(lesParametresFen0.get(2));
			lesParametres.add(lesParametresFen0.get(6));
			lesParametres.add(lesParametresFen0.get(3));
			lesParametres.add(lesParametresFen0.get(7));
			lesParametres.add(idProcessus);
			lesParametres.add(dateExportEnCours);			
			
			idStatement = cBase.creerPreparedStatement("setMesuresRepresentation1");
			cBase.executerRequeteStockee(idStatement, "setMesuresRepresentation1", lesParametres);
			cBase.fermerStatement(idStatement);
			
			lesParametres.clear();
			lesParametres.add(lesParametresFen1.get(0));
			lesParametres.add(lesParametresFen1.get(7));
			lesParametres.add(lesParametresFen1.get(1));
			lesParametres.add(lesParametresFen1.get(8));
			lesParametres.add(new Integer((String)lesParametresFen1.get(2)));
			lesParametres.add(new Integer((String)lesParametresFen1.get(3)));
			lesParametres.add(new Integer((String)lesParametresFen1.get(4)));
			lesParametres.add(new Integer((String)lesParametresFen1.get(5)));
			lesParametres.add(lesParametresFen1.get(6));
			lesParametres.add(lesParametresFen1.get(9));
			lesParametres.add(idProcessus);
			lesParametres.add(dateExportEnCours);			
			
			idStatement = cBase.creerPreparedStatement("setMesuresRepresentation2");
			cBase.executerRequeteStockee(idStatement, "setMesuresRepresentation2", lesParametres);
			cBase.fermerStatement(idStatement);
			
			lesParametres.clear();
			lesParametres.add(lesParametresFen2.get(0));
			lesParametres.add(lesParametresFen2.get(7));
			if (((String)lesParametresFen2.get(1)).indexOf(',') > 0)
				lesParametres.add(new Double(((String)lesParametresFen2.get(1)).replace(',','.')));
			else
				lesParametres.add(new Double((String)lesParametresFen2.get(1)));
			lesParametres.add(lesParametresFen2.get(2));
			lesParametres.add(lesParametresFen2.get(8));
			if (((String)lesParametresFen2.get(3)).indexOf(',') > 0)
				lesParametres.add(new Double(((String)lesParametresFen2.get(3)).replace(',','.')));
			else
				lesParametres.add(new Double((String)lesParametresFen2.get(3)));
			lesParametres.add(lesParametresFen2.get(4));
			lesParametres.add(lesParametresFen2.get(9));
			if (((String)lesParametresFen2.get(5)).indexOf(',') > 0)
				lesParametres.add(new Double(((String)lesParametresFen2.get(5)).replace(',','.')));
			else
				lesParametres.add(new Double((String)lesParametresFen2.get(5)));
			lesParametres.add(lesParametresFen2.get(6));
			lesParametres.add(lesParametresFen2.get(10));
			lesParametres.add(idProcessus);
			lesParametres.add(dateExportEnCours);
			
			idStatement = cBase.creerPreparedStatement("setMesuresRepresentation3");
			cBase.executerRequeteStockee(idStatement, "setMesuresRepresentation3", lesParametres);
			cBase.fermerStatement(idStatement);

			//Ajout de le mesure
			E_MesureRepresentation uneMesure = new E_MesureRepresentation(idProcessus, dateExportEnCours,
					new Integer((String)lesParametresFen0.get(8)).intValue(),
					new Integer((String)lesParametresFen0.get(9)).intValue(),
					((Integer)lesParametresFen0.get(0)).intValue(),
					(String)lesParametresFen0.get(4),
					((Integer)lesParametresFen0.get(1)).intValue(),
					(String)lesParametresFen0.get(5),
					((Integer)lesParametresFen0.get(2)).intValue(),
					(String)lesParametresFen0.get(6),
					((Double)lesParametresFen0.get(3)).doubleValue(),
					(String)lesParametresFen0.get(7),
					((Integer)lesParametresFen1.get(0)).intValue(),
					(String)lesParametresFen1.get(7),
					((Integer)lesParametresFen1.get(1)).intValue(),
					(String)lesParametresFen1.get(8),
					new Integer((String)lesParametresFen1.get(2)).intValue(),
					new Integer((String)lesParametresFen1.get(3)).intValue(),
					new Integer((String)lesParametresFen1.get(4)).intValue(),
					new Integer((String)lesParametresFen1.get(5)).intValue(),
					((Double)lesParametresFen1.get(6)).doubleValue(),
					(String)lesParametresFen1.get(9),
					((Integer)lesParametresFen2.get(0)).intValue(),
					(String)lesParametresFen2.get(7),
					(((String)lesParametresFen2.get(1)).indexOf(',') > 0)?new Double(((String)lesParametresFen2.get(1)).replace(',','.')).doubleValue():new Double((String)lesParametresFen2.get(1)).doubleValue(),
					((Integer)lesParametresFen2.get(2)).intValue(),
					(String)lesParametresFen2.get(8),
					(((String)lesParametresFen2.get(3)).indexOf(',') > 0)?new Double(((String)lesParametresFen2.get(3)).replace(',','.')).doubleValue():new Double((String)lesParametresFen2.get(3)).doubleValue(),
					((Integer)lesParametresFen2.get(4)).intValue(),
					(String)lesParametresFen2.get(9),
					(((String)lesParametresFen2.get(5)).indexOf(',') > 0)?new Double(((String)lesParametresFen2.get(5)).replace(',','.')).doubleValue():new Double((String)lesParametresFen2.get(5)).doubleValue(),
					((Double)lesParametresFen2.get(6)).doubleValue(),
					(String)lesParametresFen2.get(10));
			C_Processus.getProcessus(idProcessus).ajouterMesureRepresentation(uneMesure);

			cBase.commit();
		}
		catch (SQLException e)
		{
			cBase.rollback();
			throw e;
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

	/**
	 * @param dateExportEnCours dateExportEnCours à définir.
	 */
	public static void setDateExportEnCours(String dateExportEnCours)
	{
		C_MesureVersion.dateExportEnCours = dateExportEnCours;
	}
}