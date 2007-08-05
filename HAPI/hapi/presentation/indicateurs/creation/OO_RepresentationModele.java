/*
 * Créé le 20 sept. 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.interfaces.FenetreAssistee;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * @author Cédric
 */
public class OO_RepresentationModele extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 1660234865295353522L;
    //Panels
	private JPanel SC_Valeurs = null;
	private JPanel SC_Commentaire = null;
	private JPanel SC_Libelles = null;
	private JPanel SC_Saisie = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Composant = null;
	private JPanel SC_CadreGauche = null;
	private JPanel SC_ValeurBas = null;
	private JPanel SC_ChampsBas = null;
	private JPanel SC_CadreDroit = null;
	private JScrollPane SC_Scroll = null;	

	//Composants
	private JLabel STC_Coherence = null;
	private JLabel STC_Completude = null;
	private JLabel STC_NbDef = null;
	private JLabel STC_NbAct = null;
	private JLabel STC_NbProd = null;
	private JLabel STC_NbRole = null;
	private JLabel STC_Evaluation = null;

	private JTextField ES_NbDef = null;
	private JTextField ES_NbAct = null;
	private JTextField ES_NbProd = null;
	private JTextField ES_NbRole = null;
	private JSpinner ES_Evaluation = null;
	private JSpinner ES_Completude = null;
	private JSpinner ES_Coherence = null;

	private JTextField ES_CommCoherence = null;
	private JTextField ES_CommCompletude = null;
	private JTextArea EM_CommEvaluation = null;

	//layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private GridLayout layoutLibelles = null;
	private GridLayout layoutSaisie = null;
	private GridLayout layoutCommentaire = null;
	private BorderLayout layoutValeur = null;
	private BorderLayout layoutCadreGauche = null;
	private BorderLayout layoutValeurBas = null;
	private BorderLayout layoutChampsBas = null;
	private BorderLayout layoutCadreDroit = null;	
	
	public OO_RepresentationModele()
	{
		super();
		//Création des élémnts
		creationElements();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Panels
		SC_Valeurs = new JPanel();
		SC_Commentaire = new JPanel();
		SC_Libelles = new JPanel();
		SC_Saisie = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Composant = new JPanel();
		SC_CadreGauche = new JPanel();
		SC_ValeurBas = new JPanel();
		SC_ChampsBas = new JPanel();
		SC_CadreDroit = new JPanel();
		SC_Scroll = new JScrollPane();		

		//Composants
		STC_Coherence = new JLabel();
		STC_Completude = new JLabel();
		STC_NbDef = new JLabel();
		STC_NbAct = new JLabel();
		STC_NbProd = new JLabel();
		STC_NbRole = new JLabel();
		STC_Evaluation = new JLabel();

		ES_NbDef = new JTextField();
		ES_NbAct = new JTextField();
		ES_NbProd = new JTextField();
		ES_NbRole = new JTextField();
		ES_Evaluation = new JSpinner(new SpinnerNumberModel(0,0,5,0.1));
		ES_Completude = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		ES_Coherence = new JSpinner(new SpinnerNumberModel(0,0,9999,1));

		ES_CommCoherence = new JTextField();
		ES_CommCompletude = new JTextField();
		EM_CommEvaluation = new JTextArea();

		//layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutLibelles = new GridLayout();
		layoutSaisie = new GridLayout();
		layoutCommentaire = new GridLayout();
		layoutValeur = new BorderLayout();
		layoutCadreGauche = new BorderLayout();
		layoutValeurBas = new BorderLayout();
		layoutChampsBas = new BorderLayout();
		layoutCadreDroit = new BorderLayout();		
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		//Libellés les : sont ajoutés car il ne faut pas qu'ils soient dans le bundle à cause de la page html générée
		STC_Evaluation.setText(Bundle.getText("BD_MesureVersion_EvaluationMO")+ " : ");
		STC_Coherence.setText(Bundle.getText("BD_MesureVersion_Coherence")+ " : ");
		STC_Completude.setText(Bundle.getText("BD_MesureVersion_Completude")+ " : ");
		STC_NbAct.setText(Bundle.getText("BD_MesureVersion_Activites")+ " : ");
		STC_NbDef.setText(Bundle.getText("BD_MesureVersion_Definitions")+ " : ");
		STC_NbProd.setText(Bundle.getText("BD_MesureVersion_Produits")+ " : ");
		STC_NbRole.setText(Bundle.getText("BD_MesureVersion_Roles")+ " : ");
		
		//Champs non éditables
		ES_NbAct.setMaximumSize(new Dimension(50,1));
		ES_NbAct.setEditable(false);
		ES_NbDef.setEditable(false);
		ES_NbProd.setEditable(false);
		ES_NbRole.setEditable(false);
		
		ES_CommCoherence.setPreferredSize(new Dimension(1,25));
		ES_NbProd.setPreferredSize(new Dimension(1,25));
		ES_Evaluation.setPreferredSize(new Dimension(1,25));
		STC_Evaluation.setPreferredSize(STC_NbDef.getPreferredSize());		
	}

	public void operationSurPanel()
	{		
		SC_Valeurs.setLayout(layoutValeur);
		SC_Valeurs.add(SC_Saisie, BorderLayout.EAST);
		SC_Valeurs.add(SC_Libelles, BorderLayout.CENTER);
		
		layoutLibelles.setRows(6);
		SC_Libelles.setLayout(layoutLibelles);
		SC_Libelles.add(STC_Coherence);
		SC_Libelles.add(STC_Completude);
		SC_Libelles.add(STC_NbDef);
		SC_Libelles.add(STC_NbAct);
		SC_Libelles.add(STC_NbProd);
		SC_Libelles.add(STC_NbRole);
		
		layoutSaisie.setRows(6);
		SC_Saisie.setLayout(layoutSaisie);
		SC_Saisie.add(ES_Coherence);		
		SC_Saisie.add(ES_Completude);
		SC_Saisie.add(ES_NbDef);
		SC_Saisie.add(ES_NbAct);
		SC_Saisie.add(ES_NbProd);
		SC_Saisie.add(ES_NbRole);
		
		layoutCommentaire.setRows(6);
		SC_Commentaire.setLayout(layoutCommentaire);
		SC_Commentaire.add(ES_CommCoherence);
		SC_Commentaire.add(ES_CommCompletude);
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(new JPanel());
		
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_CadreGauche, BorderLayout.WEST);
		SC_Composant.add(SC_CadreDroit, BorderLayout.CENTER);
		
		SC_CadreGauche.setLayout(layoutCadreGauche);
		SC_CadreGauche.add(SC_Valeurs, BorderLayout.NORTH);
		SC_CadreGauche.add(SC_ValeurBas, BorderLayout.CENTER);
		
		SC_CadreDroit.setLayout(layoutCadreDroit);
		SC_CadreDroit.add(SC_Commentaire, BorderLayout.NORTH);
		SC_CadreDroit.add(SC_Scroll, BorderLayout.CENTER);
		SC_Scroll.setViewportView(EM_CommEvaluation);
		
		SC_ValeurBas.setLayout(layoutValeurBas);
		SC_ValeurBas.add(SC_ChampsBas, BorderLayout.NORTH);
		
		SC_ChampsBas.setLayout(layoutChampsBas);
		SC_ChampsBas.add(STC_Evaluation, BorderLayout.WEST);
		SC_ChampsBas.add(ES_Evaluation, BorderLayout.CENTER);
		
	}

	public void operationSurFenetre()
	{
		this.setLayout(layoutFond);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);		
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{

	}

	public void updateTexte()
	{

	}

	public ArrayList getParametresSaisis()
	{
		ArrayList retour = new ArrayList();
		
		retour.add(ES_Coherence.getValue());
		retour.add(ES_Completude.getValue());
		retour.add(ES_NbDef.getText());
		retour.add(ES_NbAct.getText());
		retour.add(ES_NbProd.getText());
		retour.add(ES_NbRole.getText());
		DecimalFormat df = new DecimalFormat(Bundle.MODEL_DECIMAL_COURT);
		String laValeur = df.format(((Double)(ES_Evaluation.getValue())).doubleValue());
		laValeur = laValeur.replaceAll(",",".");
		retour.add(new Double(laValeur));
		retour.add(ES_CommCoherence.getText());
		retour.add(ES_CommCompletude.getText());
		retour.add(EM_CommEvaluation.getText());		
		
		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		//Spinners
		ES_Coherence.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(1));
		ES_Completude.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(2));
		//Champs non modifiables
		ES_NbDef.setText((lesParametres.size()==1)?"":(String) lesParametres.get(3));		
		ES_NbAct.setText((lesParametres.size()==1)?"":(String) lesParametres.get(4));
		ES_NbProd.setText((lesParametres.size()==1)?"":(String) lesParametres.get(5));
		ES_NbRole.setText((lesParametres.size()==1)?"":(String) lesParametres.get(6));
		//Spinners
		ES_Evaluation.setValue((lesParametres.size()==1)?new Double(0):lesParametres.get(7));		
		//Commentaires
		ES_CommCoherence.setText((lesParametres.size()==1)?"":(String) lesParametres.get(8));
		ES_CommCompletude.setText((lesParametres.size()==1)?"":(String) lesParametres.get(9));
		EM_CommEvaluation.setText((lesParametres.size()==1)?"":(String) lesParametres.get(10));
	}	
}
