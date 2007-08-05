package hapi.application.indicateurs;

import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.modeles.ModeleTableOrdreDesActivites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Vincent
 */
public class C_TableauDeBordExecutionProcessus
{

	public static ModeleTableOrdreDesActivites getModeleTableOrdreDesActivites(ArrayList ordresAct, ArrayList listeNomsAct)
	{
		return new ModeleTableOrdreDesActivites(ordresAct, listeNomsAct);
	}

	public static double getMoyenneRUR(String idProcessus, int idExecutionProcess)
	{
		double moyenne = 0.0;
		//on teste le nb d'�valuations quantitatives pour �viter la division par 0
		if (C_Evaluation.size(idProcessus, idExecutionProcess) > 0)
		{
			Collection lesEvalExecProc = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExecutionProcess).values();
			Iterator it = lesEvalExecProc.iterator();
			while (it.hasNext())
				moyenne += ((E_Evaluation) it.next()).getRUR();
			moyenne = moyenne / lesEvalExecProc.size();
		}
		return moyenne;
	}

	public static double getMoyenneRCT(String idProcessus, int idExecutionProcess)
	{
		double moyenne = 0.0;
		//on teste le nb d'�valuations quantitatives pour �viter la division par 0
		if (C_Evaluation.size(idProcessus, idExecutionProcess) > 0)
		{
			Collection lesEvalExecProc = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExecutionProcess).values();
			Iterator it = lesEvalExecProc.iterator();
			while (it.hasNext())
				moyenne += ((E_Evaluation) it.next()).getRCT();
			moyenne = moyenne / lesEvalExecProc.size();
		}
		return moyenne;
	}

	public static double getMoyenneRUA(String idProcessus, int idExecutionProcess)
	{
		double moyenne = 0.0;
		//on teste le nb d'�valuations quantitatives pour �viter la division par 0
		if (C_Evaluation.size(idProcessus, idExecutionProcess) > 0)
		{
			Collection lesEvalExecProc = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExecutionProcess).values();
			Iterator it = lesEvalExecProc.iterator();
			while (it.hasNext())
				moyenne += ((E_Evaluation) it.next()).getRUA();
			moyenne = moyenne / lesEvalExecProc.size();
		}
		return moyenne;
	}

	public static double getMoyenneRUP(String idProcessus, int idExecutionProcess)
	{
		double moyenne = 0.0;
		//on teste le nb d'�valuations quantitatives pour �viter la division par 0
		if (C_Evaluation.size(idProcessus, idExecutionProcess) > 0)
		{
			Collection lesEvalExecProc = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExecutionProcess).values();
			Iterator it = lesEvalExecProc.iterator();
			while (it.hasNext())
				moyenne += ((E_Evaluation) it.next()).getRUP();
			moyenne = moyenne / lesEvalExecProc.size();
		}
		return moyenne;
	}

	/*
	 * Renvoie une arraylist contenant une arraylist des rangs moyens des activit�s, 1 arraylist des noms des activit�s et 1 arraylist des identifiants des activit�s
	 */
	/*public static ArrayList getMoyenneRangsDesActivites(String idProcessus,int idExecutionProcess)
	{
		//<Integer ordreMoyen>
		ArrayList rangsMoyensDesActivites = new ArrayList();
		//<String nomsActivites>
		ArrayList nomsActivites = new ArrayList();
		//<String idsActivites>
		ArrayList idsActivites = new ArrayList();
		
		//on teste le nb d'�valuations quantitatives pour �viter la division par 0
		if (C_Evaluation.size(idProcessus, idExecutionProcess) > 0)
		{
			//<String idActivite, HashMap<Integer rang, Integer nbOccurrencesACeRang> >
			HashMap ordresDesActivites = new HashMap();
			Collection lesEvalExecProc = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExecutionProcess).values();
			Iterator it = lesEvalExecProc.iterator();
			HashMap ordreActEval;
			while (it.hasNext())
			{
				ordreActEval = ((E_Evaluation) it.next()).getOrdreActivite();
				Iterator itOrdreActEval = ordreActEval.keySet().iterator();
				//Pour chaque activit� de l'�valuation quantitative
				while(itOrdreActEval.hasNext())
				{
					Integer rangAct = (Integer)itOrdreActEval.next();
					String idAct = (String)ordreActEval.get(rangAct);
					//l'activit� n'est pas dans la map => on l'ajoute
					if(!ordresDesActivites.containsKey(idAct))
						ordresDesActivites.put(idAct, new HashMap());
					HashMap occurrencesRangs = (HashMap)ordresDesActivites.get(idAct);
					//Le rang de cette activit� n'est pas dans la map => on l'ajoute
					if(!occurrencesRangs.containsKey(rangAct))
						occurrencesRangs.put(rangAct, new Integer(1));
					else//le rang de cette activit� existe => on incr�mente le nb d'occurrences
						occurrencesRangs.put(rangAct, new Integer(((Integer)occurrencesRangs.get(rangAct)).intValue()+1));
				}
			}
			//Calcul de la moyenne pond�r�e du rang de chaque activit�
			//Pour chaque activit�
			Iterator itActivite = ordresDesActivites.keySet().iterator();
			while(itActivite.hasNext())
			{
				double moyenne = 0.0;
				int nbOccurrencesTotal = 0;
				String idAct = (String)itActivite.next();
				HashMap rangsOccurrences = (HashMap)ordresDesActivites.get(idAct);
				Iterator itRangsOccur = rangsOccurrences.keySet().iterator();
				//Pour chaque rang de l'activit�
				while(itRangsOccur.hasNext())
				{
					Integer rang = (Integer)itRangsOccur.next();
					//On calcule la moyenne pond�r�e
					moyenne += (rang.doubleValue()*((Integer)rangsOccurrences.get(rang)).doubleValue());
					nbOccurrencesTotal += ((Integer)rangsOccurrences.get(rang)).intValue();
				}
				moyenne = moyenne/nbOccurrencesTotal;
	
				//on ajoute l'id de l'activit� dans la liste � retourner
				idsActivites.add(0, idAct);
				//on ajoute l'activit� et son rang moyen dans la liste � retourner
				Integer laMoyenne = new Integer(new Double(moyenne).intValue());
				rangsMoyensDesActivites.add(0,laMoyenne);
				//on ajoute le nom de l'activit� dans la liste � retourner
				E_Activite act = C_Activite.getActivite(idProcessus, idAct);
				if(act == null)
					nomsActivites.add(0,Bundle.getText("OO_TableauDeBord_tableau_activite_supprimee"));
				else
					nomsActivites.add(0,act.getNom());
			}
		}		
		
		ArrayList retour = new ArrayList();
		retour.add(rangsMoyensDesActivites);
		retour.add(nomsActivites);
		retour.add(idsActivites);
		return retour;
	}*/

	public static Date getDateDebut(String idProcessus, String idExec)
	{
		return C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExec).getDateDebut();
	}

	public static Date getDateFin(String idProcessus, String idExec)
	{
		return C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExec).getDateFin();
	}

	public static String getDescription(String idProcessus, String idExec)
	{
		return C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExec).getDescription();
	}
}
