package hapi.presentation.composants.onglets;

import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Robin & Cécile
 */

public class OO_ListeProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -5116108525523008607L;
    //Panels
	private JPanel SC_Composant = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Recapitulatif = null;
	//Composants
	private JLabel STV_NombreProcessus = null;
	private JLabel STV_NombreExecution = null;
	private JLabel STV_NombreEvaluation = null;
	//Layout
	private BorderLayout layoutFond = null;
	private GridLayout layoutRecapitulatif = null;
	private BorderLayout layoutComposant = null;

	public OO_ListeProcessus()
	{
		super();
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appel de l'interface
		operationSurPanel();
		operationSurComposants();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Création des panels
		SC_Composant = new JPanel();
		SC_Droite = new JPanel();
		SC_Gauche = new JPanel();
		SC_Bas = new JPanel();
		SC_Haut = new JPanel();
		SC_Recapitulatif = new JPanel();
		//Création des composants
		STV_NombreProcessus = new JLabel();
		STV_NombreExecution = new JLabel();
		STV_NombreEvaluation = new JLabel();
		//Création des layout
		layoutFond = new BorderLayout();
		layoutRecapitulatif = new GridLayout(4, 0);
		layoutComposant = new BorderLayout();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{}

	public void operationSurPanel()
	{
		//Composants
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Recapitulatif, BorderLayout.NORTH);
		//Evaluation
		SC_Recapitulatif.setLayout(layoutRecapitulatif);
		SC_Recapitulatif.add(STV_NombreProcessus);
		SC_Recapitulatif.add(STV_NombreExecution);
		SC_Recapitulatif.add(STV_NombreEvaluation);
	}

	public void operationSurFenetre()
	{
		//Affectation du layout
		this.setLayout(layoutFond);
		//Ajout des panels de bordure
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Haut, BorderLayout.NORTH);
		//Ajout du panel des composants
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{
		STV_NombreProcessus.setText(Bundle.getText("OO_ListeProcessus_nombre_process") + C_Processus.size());

		int cumulExec = 0;
		int cumulEval = 0;

		//Pour chaque processus, cumul des executions
		for (int i = 0; i < C_Processus.size(); i++)
		{
			String idProcessus = C_Processus.get(i).getIdentifiant();
			cumulExec += C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus).size();

			for (int j = 0; j < C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus).size(); j++)
			{
				String idExecution = C_ExecutionProcessus.getClef(idProcessus, j);
				cumulEval += C_Evaluation.getEvaluationPourUneExecProc(idProcessus, new Integer(idExecution).intValue()).size();
			}
		}

		STV_NombreExecution.setText(Bundle.getText("OO_ListeProcessus_nombre_exec") + cumulExec);
		STV_NombreEvaluation.setText(Bundle.getText("OO_ListeProcessus_nombre_evaluation") + cumulEval);
	}

	public void updateTexte()
	{}
}
