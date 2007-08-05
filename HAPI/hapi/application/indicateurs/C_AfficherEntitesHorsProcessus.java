/*
 * Created on 6 mars 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.donnees.modeles.ModeleTable;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Natalia
 *
 */
public class C_AfficherEntitesHorsProcessus
{

	private String idProcessus = null;
	private int idExecution = 0;
	private int idEvaluation = 0;

	private ModeleTable modele = null;

	public C_AfficherEntitesHorsProcessus(String idProcesuss, int idExec, int idEval, int indicateur)
	{
		this.idProcessus = idProcesuss;
		this.idExecution = idExec;
		this.idEvaluation = idEval;

		modele = new ModeleTable();
		switch (indicateur)
		{
			case 0 : /* RUA */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Activites"));
				break;
			case 2 : /* RUP */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Produits"));
				break;
			case 3 : /* RUR */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Roles"));
				break;
		}

	}

	public ModeleTable getModeleDesActivitesHorsProcessus()
	{
		initialiserLeModele();

		// récupération des activités hors processus de l'évaluation
		ArrayList activitesHorsProcessus = C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getActivitesHorsProcessus();

		for (int i = 0; i < activitesHorsProcessus.size(); i++)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(activitesHorsProcessus.get(i));
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public ModeleTable getModeleDesRolesHorsProcessus()
	{
		initialiserLeModele();

		// récupération des activités hors processus de l'évaluation
		ArrayList rolesHorsProcessus = C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getRolesHorsProcessus();

		for (int i = 0; i < rolesHorsProcessus.size(); i++)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(rolesHorsProcessus.get(i));
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public ModeleTable getModeleDesProduitsHorsProcessus()
	{
		initialiserLeModele();

		// récupération des activités hors processus de l'évaluation
		ArrayList produitsHorsProcessus = C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getProduitsHorsProcessus();

		for (int i = 0; i < produitsHorsProcessus.size(); i++)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(produitsHorsProcessus.get(i));
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public int getNombreActivitesNonRealisees()
	{
		return C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getActivitesNonUtilisees().size();
	}

	public int getNombreProduitsNonUtilises()
	{
		return C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getProduitsNonUtilises().size();
	}

	public int getNombreRolesNonUtilises()
	{
		return C_Evaluation.getEvaluation(idProcessus, idExecution, idEvaluation).getRolesNonUtilises().size();
	}

	private void initialiserLeModele()
	{
		while (modele.getRowCount() > 0)
		{
			modele.removeRow(0);
		}
	}

}
