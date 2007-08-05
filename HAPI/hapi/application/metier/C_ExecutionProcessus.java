/*
 * Fichier C_ExecutionProcessus.java
 * Auteur C�dric
 *
 */
package hapi.application.metier;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_Hapi;
import hapi.application.indicateurs.C_CycleDeVie;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Contr�leur des ex�cutions de processus
 */
public class C_ExecutionProcessus
{
	// liste des ex�cution du processus
	// structure <id_processus (String), <id_execution (String), execution(E_ExecutionPrcessus)>    
	static private HashMap executionsProcessus = new HashMap();

	/**
	 * ajoute une ex�cution � la liste
	 * @param id_processus : identifiant du processus auquel appartient l'ex�cution
	 * @param id : identifiant de l'ex�cution
	 * @param execution : E_Exectuion
	 */
	synchronized static public void ajouterExecutionProcessus(String id_processus, String id, E_ExecutionProcessus execution)
	{
		// si aucune entr�e n'existe pour le processus 
		if (executionsProcessus.get(id_processus) == null)
		{
			// ... alors on la cr�e
			executionsProcessus.put(id_processus, new HashMap());
		}
		// on ajoute l'�lement
		 ((HashMap) executionsProcessus.get(id_processus)).put(id, execution);
	}

	/**
	 * 
	 * @param id_processus
	 * @param leurIdProjet
	 * @return
	 */

	static public E_ExecutionProcessus getExecutionProcessusAPartirDeLeurId(String id_processus, String leurIdProjet, Date dateDebut, Date dateFin)
	{
		// r�cup�ration de la liste des exec de proc du processus pass� en param
		/*Set lesProc = executionsProcessus.keySet();
		Iterator i = lesProc.iterator();
		while(i.hasNext())
			System.out.println((String)i.next());*/
		HashMap tmp = (HashMap) executionsProcessus.get(id_processus);

		// recherche de l'execution de processus d'id (d�fini par les outils de workflow
		// donc diff�rent de l'identifiant cl� de la seconde hashmap)
		if (tmp != null)
		{
			E_ExecutionProcessus exec = null;
			for (Iterator it = tmp.keySet().iterator(); it.hasNext();)
			{
				exec = (E_ExecutionProcessus) tmp.get(it.next());
				if (String.valueOf(exec.getId()).equals(leurIdProjet) && exec.getDateDebut().equals(dateDebut) && exec.getDateFin().equals(dateFin))
					return exec;
			}
		}
		return null;
	}

	/**
	 * r�cup�re l'ex�cution d'identifiant id
	 * @param id
	 * @return
	 */
	synchronized static public E_ExecutionProcessus getExecutionProcessus(String id_processus, String id)
	{
		if ((HashMap) executionsProcessus.get(id_processus) != null)
			return (E_ExecutionProcessus) ((HashMap) executionsProcessus.get(id_processus)).get(id);
		else
			return null;
	}

	/**
	 * renvoie la liste des ex�cution du processus donn�
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getExecutionsProcessusDuProcessus(String id_processus)
	{
		HashMap retour = (HashMap) executionsProcessus.get(id_processus);
		if (retour == null)
			return new HashMap();
		else
			return retour;
	}

	/**
	 * renvoie la liste des ex�cution suivant le processus donn�
	 * dans la version pass�e en param�tre
	 * @param id_processus
	 * @return
	 */
	synchronized static public HashMap getExecutionsProcessusDuProcessusEnVersion(String idProcess, String versionProcess)
	{
		HashMap lesExecDuProcess = (HashMap) getExecutionsProcessusDuProcessus(idProcess).clone();
		Object[] lesCles = lesExecDuProcess.keySet().toArray();

		int i = -1;
		int maxi = lesCles.length;
		while (++i < maxi)
		{
			String idExecProc = (String) lesCles[i];
			E_ExecutionProcessus execProc = (E_ExecutionProcessus) lesExecDuProcess.get(idExecProc);
			if (!execProc.getVersionProcessus().equals(versionProcess))
			{
				lesExecDuProcess.remove(idExecProc);
				maxi--;
			}
		}
		return lesExecDuProcess;
	}

	synchronized static public HashMap getExecutionProcessus()
	{
		return executionsProcessus;
	}

	/**
	 * renvoie la nombre d'ex�cution associ�s au processus donn�
	 * @return
	 */
	synchronized static public int size(String id_processus)
	{
		try
		{
			return ((HashMap) executionsProcessus.get(id_processus)).size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	synchronized static public String getClef(String id_processus, int index)
	{
		TreeSet lesClefs = new TreeSet(((HashMap) executionsProcessus.get(id_processus)).keySet());
		int i = -1;
		Iterator it = lesClefs.iterator();
		while (++i < index)
		{
			it.next();
		}

		return (String) it.next();
	}

	/**
	 * supprime les ex�cution d'un processus donn�
	 * @param id_processus
	 */
	synchronized static public void supprimerExecutionProcessus(String id_processus)
	{
		executionsProcessus.remove(id_processus);
	}

	synchronized static public void supprimerExecutionProcessusEtDependances(String id_execProcessus, String id_processus)
	{
		//Suppression des �valuations li��s
		C_Evaluation.supprimerEvaluations(id_processus, new Integer(id_execProcessus).intValue());
		C_Processus.getProcessus(id_processus).supprimerMesureUtilisation(id_execProcessus);
		//Suppression du cycle de vie
		C_CycleDeVie.supprimerCycleDeVie(((E_ExecutionProcessus)((HashMap) executionsProcessus.get(id_processus)).get(id_execProcessus)).getIdCycleDeVie());
		//Suppression de l'ex�cution concern�e
		 ((HashMap) executionsProcessus.get(id_processus)).remove(id_execProcessus);
	}

	public static void supprimerExecutionProcessusEnBase(String idProcessus, String idExecProcessus, C_AccesBaseDonnees cBase) throws Exception
	{
		int idStatement = 0;
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(idExecProcessus);

		try
		{
			idStatement = cBase.creerPreparedStatement("delExecutionProcessus");
			cBase.executerRequeteStockee(idStatement, "delExecutionProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			HashMap evals = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, new Integer(idExecProcessus).intValue());

			if (evals != null)
			{
				ArrayList lesEvals = new ArrayList(evals.keySet());

				for (int i = 0; i < lesEvals.size(); i++)
				{
					C_Evaluation.supprimerUneEvaluationEnBase(cBase, idProcessus, new Integer(idExecProcessus).intValue(), ((Integer) lesEvals.get(i)).intValue());
				}

			}
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delEvaluationsExecProc");
			cBase.executerRequeteStockee(idStatement, "delEvaluationsExecProc", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		
		try
		{
			lesParametres.clear();
			lesParametres.add(idProcessus);
			lesParametres.add(idExecProcessus);
			idStatement = cBase.creerPreparedStatement("delMesureInformationExecProc");
			cBase.executerRequeteStockee(idStatement, "delMesureInformationExecProc", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}		
		
		//Suppression du cycle de vie associ�
		C_CycleDeVie.supprimerCycleEnBase(((E_ExecutionProcessus)((HashMap) executionsProcessus.get(idProcessus)).get(idExecProcessus)).getIdCycleDeVie(),cBase);
	}

	synchronized public static void modifierCommentaireProjet(String leProcessus, String leProjet, String commentaireProjet) throws SQLException
	{
		//	Cr�ation d'une instance de la base
		C_AccesBaseDonnees accesBD = new C_AccesBaseDonnees();
		accesBD.ouvrirConnexion();
		//Cr�ation de la liste des param�tres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(commentaireProjet);
		lesParametres.add(new Integer(leProjet));
		int idStatement = 0;

		try
		{
			idStatement = accesBD.creerPreparedStatement("setCommentaireProjet");
			accesBD.executerRequeteStockee(idStatement, "setCommentaireProjet", lesParametres);
			setCommentaireProjet(leProcessus, leProjet, commentaireProjet);
		}
		finally
		{
			//Fermeture de la conception
			accesBD.fermerStatement(idStatement);
			accesBD.fermerConnexion();
		}
	}

	synchronized public static String getCommentaireProjet(String leProcessus, String leProjet)
	{
		return ((E_ExecutionProcessus) ((HashMap) executionsProcessus.get(leProcessus)).get(leProjet)).getCommentaire();
	}

	synchronized public static void setCommentaireProjet(String leProcessus, String leProjet, String leCommentaire)
	{
		((E_ExecutionProcessus) ((HashMap) executionsProcessus.get(leProcessus)).get(leProjet)).setCommentaire(leCommentaire);
	}

	public static void creerExecutionProcessus(String idProcessus, String versionProcessus, ArrayList lesParametres, int cycleDeVie) throws SQLException
	{
		E_ExecutionProcessus exec = new E_ExecutionProcessus(versionProcessus, C_Hapi.PROJET_ICONE,cycleDeVie);
		exec.setId(((Integer) lesParametres.get(0)).intValue());
		exec.setNom(lesParametres.get(1).toString());
		exec.setDescription(lesParametres.get(2).toString());
		exec.setCommentaire(lesParametres.get(3).toString());
		exec.setDateDebut((Date) lesParametres.get(4));
		exec.setDateFin((Date) lesParametres.get(5));
		// enregistrement en base de la nouvelle execution de processus
		enregistrerExecutionProcessusDansBD(idProcessus, exec);
		// un identifiant a �t� affect� au nouveau projet
		// ajout de la nouvelle 
		ajouterExecutionProcessus(idProcessus, exec.getIdentifiant(), exec);
	}

	synchronized public static void enregistrerExecutionProcessusDansBD(String idProcessus, E_ExecutionProcessus eExecProcessus) throws SQLException
	{
		int idStatement = 0;

		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Cr�ation de la liste des param�tres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(new Integer(eExecProcessus.getId()));
		lesParametres.add(eExecProcessus.getNomSansVersion());
		lesParametres.add(eExecProcessus.getVersionProcessus());
		if (eExecProcessus.getDescription() != null)
			lesParametres.add(eExecProcessus.getDescription());
		else
			lesParametres.add("");
		lesParametres.add(new java.sql.Date(eExecProcessus.getDateDebut().getTime()));
		lesParametres.add(new java.sql.Date(eExecProcessus.getDateFin().getTime()));
		lesParametres.add(idProcessus);
		try
		{
			cBase.ouvrirConnexion();
			idStatement = cBase.creerPreparedStatement("setExecutionProcessus");
			cBase.executerRequeteStockee(idStatement, "setExecutionProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
			// Recherche de l'id d'insertion
			idStatement = cBase.creerPreparedStatement("getMaxExecutionProcessus");
			ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxExecutionProcessus");
			leResultat.next();
			eExecProcessus.setNotreId(leResultat.getInt(1));
			cBase.fermerStatement(idStatement);
		}
		finally
		{
			cBase.fermerConnexion();
		}
	}

}
