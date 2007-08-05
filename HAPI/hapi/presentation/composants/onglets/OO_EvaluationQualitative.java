package hapi.presentation.composants.onglets;

import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Robin & Cécile
 */

public class OO_EvaluationQualitative extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 3835080242240069363L;
    //Panels
	private JPanel SC_Boutons = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Composant = null;
	private JScrollPane SC_Scroll = null;
	//Layout	
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private FlowLayout layoutBoutons = null;
	//Boutons
	private JButton BP_Modifier = null;
	//Composants
	private JTextArea EM_Evaluation = null;
	private JLabel STC_Titre = null;
	//Variables
	private String identifiantProjet = null;
	private String identifiantProcessus = null;
	private String identifiantEvaluation = null;

	//Ecouteur
	private ActionListener actionModificationCommentaire = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doModifier();
		}
	};

	public OO_EvaluationQualitative(String identifiant, ArrayList idProcessus)
	{
		super();
		//Création des éléments
		creationElements();
		//Affecation de l'identifiant
		identifiantEvaluation = identifiant;
		identifiantProcessus = (String) idProcessus.get(0);
		identifiantProjet = (String) idProcessus.get(1);
		//Mise à jour des textes
		updateTexte();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurBoutons();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Création des panels
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Composant = new JPanel();
		SC_Scroll = new JScrollPane();
		//Création des composants
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutBoutons = new FlowLayout();
		//Création des boutons
		BP_Modifier = new JButton();
		//Création des layout
		EM_Evaluation = new JTextArea();
		STC_Titre = new JLabel();
	}

	public void operationSurBoutons()
	{
		BP_Modifier.setMnemonic(Bundle.getChar("Modifier_mne"));
		BP_Modifier.setText(Bundle.getText("Modifier"));
		BP_Modifier.addActionListener(actionModificationCommentaire);
	}

	public void operationSurComposants()
	{
		//Commentaire
		EM_Evaluation.setLineWrap(true);
		//Titre
		STC_Titre.setText(Bundle.getText("OO_Qualitative_titre"));
	}

	public void operationSurPanel()
	{
		//Panel des composants
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(STC_Titre, BorderLayout.NORTH);
		SC_Scroll.setViewportView(EM_Evaluation);
		SC_Composant.add(SC_Scroll, BorderLayout.CENTER);
		//Boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Modifier);

	}

	public void operationSurFenetre()
	{
		//Layout du fond
		this.setLayout(layoutFond);
		//Layout des côtés
		this.add(SC_Boutons, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);
		//Layout des composants
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{
		//Chargement de l'évaluation
		EM_Evaluation.setText(C_Evaluation.getEvaluation(identifiantProcessus, new Integer(identifiantProjet).intValue(), new Integer(identifiantEvaluation).intValue()).getEvalQualitative());
	}

	public void updateTexte()
	{}

	public void doModifier()
	{
		try
		{
			C_Evaluation.getEvaluation(identifiantProcessus, new Integer(identifiantProjet).intValue(), new Integer(identifiantEvaluation).intValue()).setEvalQualitative(EM_Evaluation.getText());
			C_Evaluation.saveEvaluationQualitativeEnBase(C_Evaluation.getEvaluation(identifiantProcessus, new Integer(identifiantProjet).intValue(), new Integer(identifiantEvaluation).intValue()));

		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e)
		{}
	}
}