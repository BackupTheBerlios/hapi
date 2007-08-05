/*
 * Created on 28 janv. 2005
 */
package hapi.application.indicateurs;

import hapi.application.metier.C_ExecutionProcessus;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.donnees.modeles.ModeleTableOrdreDesActivites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Stéphane
 */
public class C_TableauDeBordProcessus
{
	synchronized public static double getMoyenneRUR(String idProcess, String versionProcess)
	{
		double moyenne = 0.0;
		//On récupère les exécutions du processus défini par l'identifiant et la version passés en paramètre
		Collection execProcessVersion = C_ExecutionProcessus.getExecutionsProcessusDuProcessusEnVersion(idProcess, versionProcess).values();
		if (execProcessVersion.size() > 0)
		{
			Iterator itExecProc = execProcessVersion.iterator();
			//pour chaque execution de processus trouvé
			while (itExecProc.hasNext())
				//On récupère les évaluations quantitatives de l'exécution de processus
				moyenne += C_TableauDeBordExecutionProcessus.getMoyenneRUR(idProcess, Integer.parseInt(((E_ExecutionProcessus) itExecProc.next()).getIdentifiant()));
			moyenne = moyenne / execProcessVersion.size();
		}
		return moyenne;
	}

	synchronized public static double getMoyenneRCT(String idProcess, String versionProcess)
	{
		double moyenne = 0.0;
		//On récupère les exécutions du processus défini par l'identifiant et la version passés en paramètre
		Collection execProcessVersion = C_ExecutionProcessus.getExecutionsProcessusDuProcessusEnVersion(idProcess, versionProcess).values();
		if (execProcessVersion.size() > 0)
		{
			Iterator itExecProc = execProcessVersion.iterator();
			//pour chaque execution de processus trouvé
			while (itExecProc.hasNext())
				//On récupère les évaluations quantitatives de l'exécution de processus
				moyenne += C_TableauDeBordExecutionProcessus.getMoyenneRCT(idProcess, Integer.parseInt(((E_ExecutionProcessus) itExecProc.next()).getIdentifiant()));
			moyenne = moyenne / execProcessVersion.size();
		}
		return moyenne;
	}
	synchronized public static double getMoyenneRUA(String idProcess, String versionProcess)
	{
		double moyenne = 0.0;
		//On récupère les exécutions du processus défini par l'identifiant et la version passés en paramètre
		Collection execProcessVersion = C_ExecutionProcessus.getExecutionsProcessusDuProcessusEnVersion(idProcess, versionProcess).values();
		if (execProcessVersion.size() > 0)
		{
			Iterator itExecProc = execProcessVersion.iterator();
			//pour chaque execution de processus trouvé
			while (itExecProc.hasNext())
				//On récupère les évaluations quantitatives de l'exécution de processus
				moyenne += C_TableauDeBordExecutionProcessus.getMoyenneRUA(idProcess, Integer.parseInt(((E_ExecutionProcessus) itExecProc.next()).getIdentifiant()));
			moyenne = moyenne / execProcessVersion.size();
		}
		return moyenne;
	}
	synchronized public static double getMoyenneRUP(String idProcess, String versionProcess)
	{
		double moyenne = 0.0;
		//On récupère les exécutions du processus défini par l'identifiant et la version passés en paramètre
		Collection execProcessVersion = C_ExecutionProcessus.getExecutionsProcessusDuProcessusEnVersion(idProcess, versionProcess).values();
		if (execProcessVersion.size() > 0)
		{
			Iterator itExecProc = execProcessVersion.iterator();
			//pour chaque execution de processus trouvé
			while (itExecProc.hasNext())
				//On récupère les évaluations quantitatives de l'exécution de processus
				moyenne += C_TableauDeBordExecutionProcessus.getMoyenneRUP(idProcess, Integer.parseInt(((E_ExecutionProcessus) itExecProc.next()).getIdentifiant()));
			moyenne = moyenne / execProcessVersion.size();
		}
		return moyenne;
	}

	synchronized public static ModeleTableOrdreDesActivites getModele(ArrayList ordresAct, ArrayList listeNomsAct)
	{
		return new ModeleTableOrdreDesActivites(ordresAct, listeNomsAct);
	}

	/*
	 * Renvoie une arraylist contenant une arraylist des rangs moyens des activités, 1 arraylist des noms des activités et 1 arraylist des identifiants des activités
	 */
	/*synchronized public static ArrayList getMoyenneRangsDesActivites(String idProcess, String versionProcess)
	{
		//<Integer ordreMoyen>
		ArrayList rangsMoyensDesActivites = new ArrayList();
		//<String nomsActivites>
		ArrayList nomsActivites = new ArrayList();
		//<String idsActivites>
		ArrayList idsActivites = new ArrayList();
			  
		//On récupère les exécutions du processus défini par l'identifiant et la version passés en paramètre
		Collection execProcessVersion = C_ExecutionProcessus.getExecutionsProcessusDuProcessusEnVersion(idProcess, versionProcess).values();
		if(execProcessVersion.size() > 0)
		{
			Iterator itExecProc = execProcessVersion.iterator();
			//<String idActivite, HashMap<Double rangMoyen, Integer nbOccurrencesACeRang> >
			HashMap ordresDesActivites = new HashMap();
			//pour chaque execution de processus trouvé
			while(itExecProc.hasNext())
			{
				ArrayList retourExecProcess = C_TableauDeBordExecutionProcessus.getMoyenneRangsDesActivites(idProcess, Integer.parseInt(((E_ExecutionProcessus)itExecProc.next()).getIdentifiant()));  
				ArrayList rangsMoyensDesActivitesExecProc = (ArrayList)retourExecProcess.get(0);
				ArrayList nomsActivitesExecProc = (ArrayList)retourExecProcess.get(1);
				ArrayList idsActivitesExecProc = (ArrayList)retourExecProcess.get(2);
				//Pour chaque rang moyen des activités de l'exécution de processus
				for(int i = 0 ; i < rangsMoyensDesActivitesExecProc.size() ; i++)
				{
					Integer rangMoyenAct = (Integer)rangsMoyensDesActivitesExecProc.get(i);
					String idAct = (String)idsActivitesExecProc.get(i);
					//l'activité n'est pas dans la map => on l'ajoute
					if(!ordresDesActivites.containsKey(idAct))
						ordresDesActivites.put(idAct, new HashMap());
					HashMap occurrencesRangsMoyens = (HashMap)ordresDesActivites.get(idAct);
					//Le rang moyen de cette activité n'est pas dans la map => on l'ajoute
					if(!occurrencesRangsMoyens.containsKey(rangMoyenAct))
						occurrencesRangsMoyens.put(rangMoyenAct, new Integer(1));
					else//le rang moyen de cette activité existe => on incrémente le nb d'occurrences
						occurrencesRangsMoyens.put(rangMoyenAct, new Integer(((Integer)occurrencesRangsMoyens.get(rangMoyenAct)).intValue()+1));
				}
			}
			System.out.println("rangs moyens : "+ordresDesActivites);
			//Calcul de la moyenne pondérée du rang moyen de chaque activité
			//Pour chaque activité
			Iterator itActivite = ordresDesActivites.keySet().iterator();
			while(itActivite.hasNext())
			{
				double moyenne = 0.0;
				int nbOccurrencesTotal = 0;
				String idAct = (String)itActivite.next();
				HashMap rangsOccurrences = (HashMap)ordresDesActivites.get(idAct);
				Iterator itRangsOccur = rangsOccurrences.keySet().iterator();
				//Pour chaque rang moyen de l'activité
				while(itRangsOccur.hasNext())
				{
					Integer rang = (Integer)itRangsOccur.next();
					//On calcule la moyenne pondérée
					moyenne += (rang.doubleValue()*((Integer)rangsOccurrences.get(rang)).doubleValue());
					nbOccurrencesTotal += ((Integer)rangsOccurrences.get(rang)).intValue();
				}
				moyenne = moyenne/nbOccurrencesTotal;
				//on ajoute le rang moyen de l'activité dans la liste à retourner
				rangsMoyensDesActivites.add(0,  new Integer(new Double(moyenne).intValue()));
				//on ajoute l'id de l'activité dans la liste à retourner
				idsActivites.add(0, idAct);
				//on ajoute le nom de l'activité dans la liste à retourner
				E_Activite act = C_Activite.getActivite(idProcess, idAct);
				if(act == null)
					nomsActivites.add(0, Bundle.getText("OO_TableauDeBord_tableau_activite_supprimee"));
				else
					nomsActivites.add(0, act.getNom());
			}
		}
		ArrayList retour = new ArrayList();
		retour.add(rangsMoyensDesActivites);
		retour.add(nomsActivites);
		retour.add(idsActivites);
		return retour;
	}*/
}
