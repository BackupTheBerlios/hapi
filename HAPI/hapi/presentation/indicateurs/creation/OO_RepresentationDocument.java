/*
 * Créé le 20 sept. 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.interfaces.FenetreAssistee;
import hapi.application.modele.DecimalModel;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Cédric
 */
public class OO_RepresentationDocument extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 2202989865824531895L;
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
	private JLabel STC_NbProdType = null;
	private JLabel STC_PourPlan = null;
	private JLabel STC_NbGuide = null;
	private JLabel STC_PourGuide = null;
	private JLabel STC_NbDocu = null;
	private JLabel STC_PourDocu = null;
	private JLabel STC_Evaluation = null;

	private JFormattedTextField ES_PourPlan = null;
	private JFormattedTextField ES_PourGuide = null;
	private JFormattedTextField ES_PourDocu = null;
	private JSpinner ES_Evaluation = null;
	private JSpinner ES_NbProdType = null;
	private JSpinner ES_NbGuide = null;
	private JSpinner ES_NbDocu = null;

	private JTextField ES_CommNbProdType = null;
	private JTextField ES_CommGuide = null;
	private JTextField ES_CommDocu = null;
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
	
	//Variable
	private int NombreProduit = 0;
	private int NombreElements = 0;
	
	//Ecouteurs
	private ChangeListener actionChangerType = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChangeType();			
		}
	};
	private ChangeListener actionChangerGuide = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChangeGuide();			
		}
	};
	private ChangeListener actionChangerDocu = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChangeDocu();			
		}
	};	
	
	public OO_RepresentationDocument()
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
		STC_NbProdType = new JLabel();
		STC_PourPlan = new JLabel();
		STC_NbGuide = new JLabel();
		STC_PourGuide = new JLabel();
		STC_NbDocu = new JLabel();
		STC_PourDocu = new JLabel();
		STC_Evaluation = new JLabel();

		ES_PourPlan = new JFormattedTextField(new DecimalModel(Bundle.formatDecimalCourt));
		ES_PourGuide = new JFormattedTextField(new DecimalModel(Bundle.formatDecimalCourt));
		ES_PourDocu = new JFormattedTextField(new DecimalModel(Bundle.formatDecimalCourt));
		ES_Evaluation = new JSpinner(new SpinnerNumberModel(0,0,5,0.1));
		ES_NbProdType = new JSpinner(new SpinnerNumberModel(0,0,9999,1));
		ES_NbGuide = new JSpinner(new SpinnerNumberModel(0,0,9999,1));
		ES_NbDocu = new JSpinner(new SpinnerNumberModel(0,0,9999,1));

		ES_CommNbProdType = new JTextField();
		ES_CommGuide = new JTextField();
		EM_CommEvaluation = new JTextArea();
		ES_CommDocu = new JTextField();

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
		STC_Evaluation.setText(Bundle.getText("BD_MesureVersion_EvaluationDO")+ " : ");
		STC_NbProdType.setText(Bundle.getText("BD_MesureVersion_ProduitsPlan")+ " : ");
		STC_PourPlan.setText(" "+Bundle.getText("BD_MesureVersion_PourPlan"));
		STC_NbGuide.setText(Bundle.getText("BD_MesureVersion_Guide")+ " : ");
		STC_PourGuide.setText(" "+Bundle.getText("BD_MesureVersion_PourGuide"));
		STC_NbDocu.setText(Bundle.getText("BD_MesureVersion_Documents")+ " : ");
		STC_PourDocu.setText(" "+Bundle.getText("BD_MesureVersion_PourDoc"));
		
		//Champs non éditables
		ES_PourPlan.setMaximumSize(new Dimension(50,1));
		ES_PourPlan.setEditable(false);
		//ES_PourPlan.setHorizontalAlignment(SwingConstants.RIGHT);
		ES_PourGuide.setEditable(false);
		//ES_PourGuide.setHorizontalAlignment(SwingConstants.RIGHT);
		ES_PourDocu.setEditable(false);
		//ES_PourDocu.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Spnners
		ES_NbProdType.addChangeListener(actionChangerType);
		ES_NbGuide.addChangeListener(actionChangerGuide);
		ES_NbDocu.addChangeListener(actionChangerDocu);
		
		ES_CommDocu.setPreferredSize(new Dimension(1,25));
		ES_NbDocu.setPreferredSize(new Dimension(1,25));
		ES_Evaluation.setPreferredSize(new Dimension(1,25));
		STC_Evaluation.setPreferredSize(STC_NbProdType.getPreferredSize());		
	}

	public void operationSurPanel()
	{		
		SC_Valeurs.setLayout(layoutValeur);
		SC_Valeurs.add(SC_Saisie, BorderLayout.EAST);
		SC_Valeurs.add(SC_Libelles, BorderLayout.CENTER);
		
		layoutLibelles.setRows(6);
		SC_Libelles.setLayout(layoutLibelles);
		SC_Libelles.add(STC_NbProdType);
		SC_Libelles.add(new JPanel());
		SC_Libelles.add(STC_NbGuide);
		SC_Libelles.add(new JPanel());
		SC_Libelles.add(STC_NbDocu);
		SC_Libelles.add(new JPanel());
		
		layoutSaisie.setRows(6);
		SC_Saisie.setLayout(layoutSaisie);
		SC_Saisie.add(ES_NbProdType);		
		SC_Saisie.add(ES_PourPlan);
		SC_Saisie.add(ES_NbGuide);
		SC_Saisie.add(ES_PourGuide);
		SC_Saisie.add(ES_NbDocu);
		SC_Saisie.add(ES_PourDocu);
		
		layoutCommentaire.setRows(6);
		SC_Commentaire.setLayout(layoutCommentaire);
		SC_Commentaire.add(ES_CommNbProdType);
		SC_Commentaire.add(STC_PourPlan);
		SC_Commentaire.add(ES_CommGuide);
		SC_Commentaire.add(STC_PourGuide);
		SC_Commentaire.add(ES_CommDocu);
		SC_Commentaire.add(STC_PourDocu);
		
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
		
		retour.add(ES_NbProdType.getValue());
		retour.add(ES_PourPlan.getText());
		retour.add(ES_NbGuide.getValue());
		retour.add(ES_PourGuide.getText());
		retour.add(ES_NbDocu.getValue());
		retour.add(ES_PourDocu.getText());
		DecimalFormat df = new DecimalFormat(Bundle.MODEL_DECIMAL_COURT);
		String laValeur = df.format(((Double)(ES_Evaluation.getValue())).doubleValue());
		laValeur = laValeur.replaceAll(",",".");
		retour.add(new Double(laValeur));
		retour.add(ES_CommNbProdType.getText());
		retour.add(ES_CommGuide.getText());
		retour.add(ES_CommDocu.getText());
		retour.add(EM_CommEvaluation.getText());		
		
		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		//Spinners
		ES_NbProdType.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(1));
		//Champs non modifiables
		ES_PourPlan.setText((lesParametres.size()==1)?"":lesParametres.get(2).toString().replace(',','.'));
		//Spinners
		ES_NbGuide.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(3));
		//Champs non modifiables
		ES_PourGuide.setText((lesParametres.size()==1)?"":lesParametres.get(4).toString().replace(',','.'));
		//Spinners
		ES_NbDocu.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(5));
		//Champs non modifiables
		ES_PourDocu.setText((lesParametres.size()==1)?"":lesParametres.get(6).toString().replace(',','.'));		
		//Spinners
		ES_Evaluation.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(7));		
		//Commentaires
		ES_CommNbProdType.setText((lesParametres.size()==1)?"":(String) lesParametres.get(8));
		ES_CommGuide.setText((lesParametres.size()==1)?"":(String) lesParametres.get(9));
		ES_CommDocu.setText((lesParametres.size()==1)?"":(String) lesParametres.get(10));
		EM_CommEvaluation.setText((lesParametres.size()==1)?"":(String) lesParametres.get(11));
		//Paramètres supplémentaires
		NombreProduit = ((lesParametres.size()==1)?0:((Integer) lesParametres.get(12)).intValue());
		NombreElements = ((lesParametres.size()==1)?0:((Integer) lesParametres.get(13)).intValue());
		//Précalcul
		doChangeType();
		doChangeGuide();
		doChangeDocu();
	}
	
	private void doChangeType()
	{
		if (NombreProduit > 0)
		{
			double Val1 = ((Integer) ES_NbProdType.getValue()).intValue();
			double Val2 = NombreProduit;
			double Multi = Val1 / Val2;
			ES_PourPlan.setValue(new Double( Multi*100));
		}
		else
		{
			ES_PourPlan.setText("");
		}
		
	}
	
	private void doChangeGuide()
	{
		if (NombreElements > 0)
		{
			double Val1 = ((Integer) ES_NbGuide.getValue()).intValue();
			double Val2 = NombreElements;
			double Multi = Val1 / Val2;
			ES_PourGuide.setValue(new Double( Multi*100));
		}
		else
		{
			ES_PourGuide.setText("");
		}		
	}
	
	private void doChangeDocu()
	{
		if (NombreElements > 0)
		{
			double Val1 = ((Integer) ES_NbDocu.getValue()).intValue();
			double Val2 = NombreElements;
			double Multi = Val1 / Val2;
			ES_PourDocu.setValue(new Double( Multi*100));
		}
		else
		{
			ES_PourDocu.setText("");
		}			
	}
}
