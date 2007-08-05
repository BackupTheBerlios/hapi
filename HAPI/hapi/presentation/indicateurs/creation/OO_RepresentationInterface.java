/*
 * Créé le 19 sept. 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_MesureVersion;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
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
public class OO_RepresentationInterface extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 7954536286039689963L;
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
	private JLabel STC_Version = null;
	private JLabel STC_InterfacesE = null;
	private JLabel STC_InterfacesS = null;
	private JLabel STC_Scenario = null;
	private JLabel STC_Explicite = null;
	private JLabel STC_Estime = null;
	private JLabel STC_Evaluation = null;

	private JComboBox LD_Version = null;
	private JTextField ES_InterfaceE = null;
	private JTextField ES_InterfaceS = null;
	private JSpinner ES_Evaluation = null;
	private JSpinner ES_Explicite = null;
	private JSpinner ES_Estime = null;
	private JSpinner ES_Scenario = null;

	private JTextField ES_CommScenario = null;
	private JTextField ES_CommExplicite = null;
	private JTextField ES_CommEstime = null;
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
	
	//Ecouteurs de la fenêtre
	private ActionListener actionVersion = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doVersion();
		}
	};

	
	public OO_RepresentationInterface()
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
		STC_Version = new JLabel();
		STC_InterfacesE = new JLabel();
		STC_InterfacesS = new JLabel();
		STC_Scenario = new JLabel();
		STC_Explicite = new JLabel();
		STC_Estime = new JLabel();
		STC_Evaluation = new JLabel();

		LD_Version = new JComboBox();
		ES_InterfaceE = new JTextField();
		ES_InterfaceS = new JTextField();
		ES_Evaluation = new JSpinner(new SpinnerNumberModel(0,0,5,0.1));
		ES_Explicite = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		ES_Estime = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		ES_Scenario = new JSpinner(new SpinnerNumberModel(0,0,100,1));

		ES_CommScenario = new JTextField();
		ES_CommExplicite = new JTextField();
		ES_CommEstime = new JTextField();
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
		STC_Evaluation.setText(Bundle.getText("BD_MesureVersion_EvaluationIT")+ " : ");
		STC_Explicite.setText(Bundle.getText("BD_MesureVersion_Explicite")+ " : ");
		STC_Estime.setText(Bundle.getText("BD_MesureVersion_Estime")+ " : ");
		STC_Scenario.setText(Bundle.getText("BD_MesureVersion_Scenario")+ " : ");
		STC_Version.setText(Bundle.getText("BD_MesureVersion_Version")+ " : ");
		STC_InterfacesS.setText(Bundle.getText("BD_MesureVersion_InterfacesS")+ " : ");
		STC_InterfacesE.setText(Bundle.getText("BD_MesureVersion_InterfacesE")+ " : ");
		
		//Champs non éditables
		LD_Version.setMaximumSize(new Dimension(50,1));
		ES_InterfaceE.setEditable(false);
		ES_InterfaceS.setEditable(false);
		
		//Version
		LD_Version.addActionListener(actionVersion);
		
		ES_CommEstime.setPreferredSize(new Dimension(1,25));
		ES_Estime.setPreferredSize(new Dimension(1,25));
		ES_Evaluation.setPreferredSize(new Dimension(1,25));
		STC_Evaluation.setPreferredSize(STC_Estime.getPreferredSize().width > STC_Explicite.getPreferredSize().width?STC_Estime.getPreferredSize():STC_Explicite.getPreferredSize());
	}

	public void operationSurPanel()
	{		
		SC_Valeurs.setLayout(layoutValeur);
		SC_Valeurs.add(SC_Saisie, BorderLayout.EAST);
		SC_Valeurs.add(SC_Libelles, BorderLayout.CENTER);
		
		layoutLibelles.setRows(6);
		SC_Libelles.setLayout(layoutLibelles);
		SC_Libelles.add(STC_Version);
		SC_Libelles.add(STC_InterfacesE);
		SC_Libelles.add(STC_InterfacesS);
		SC_Libelles.add(STC_Scenario);
		SC_Libelles.add(STC_Explicite);
		SC_Libelles.add(STC_Estime);
		
		layoutSaisie.setRows(6);
		SC_Saisie.setLayout(layoutSaisie);
		SC_Saisie.add(LD_Version);		
		SC_Saisie.add(ES_InterfaceE);
		SC_Saisie.add(ES_InterfaceS);
		SC_Saisie.add(ES_Scenario);
		SC_Saisie.add(ES_Explicite);
		SC_Saisie.add(ES_Estime);
		
		layoutCommentaire.setRows(6);
		SC_Commentaire.setLayout(layoutCommentaire);
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(new JPanel());
		SC_Commentaire.add(ES_CommScenario);
		SC_Commentaire.add(ES_CommExplicite);
		SC_Commentaire.add(ES_CommEstime);
		
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
		
		retour.add(ES_Scenario.getValue());
		retour.add(ES_Explicite.getValue());
		retour.add(ES_Estime.getValue());
		DecimalFormat df = new DecimalFormat(Bundle.MODEL_DECIMAL_COURT);
		String laValeur = df.format(((Double)(ES_Evaluation.getValue())).doubleValue());
		laValeur = laValeur.replaceAll(",",".");
		retour.add(new Double(laValeur));
		retour.add(ES_CommScenario.getText());
		retour.add(ES_CommExplicite.getText());
		retour.add(ES_CommEstime.getText());
		retour.add(EM_CommEvaluation.getText());
		retour.add(ES_InterfaceE.getText());
		retour.add(ES_InterfaceS.getText());		
		
		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		LD_Version.setModel((ComboBoxModel) lesParametres.get(0));
		//Spinners
		ES_Scenario.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(1));
		ES_Explicite.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(2));
		ES_Estime.setValue((lesParametres.size()==1)?new Integer(0):lesParametres.get(3));
		ES_Evaluation.setValue((lesParametres.size()==1)?new Double(0):lesParametres.get(4));		
		//Commentaires
		ES_CommScenario.setText((lesParametres.size()==1)?"":(String) lesParametres.get(5));
		ES_CommExplicite.setText((lesParametres.size()==1)?"":(String) lesParametres.get(6));
		ES_CommEstime.setText((lesParametres.size()==1)?"":(String) lesParametres.get(7));
		EM_CommEvaluation.setText((lesParametres.size()==1)?"":(String) lesParametres.get(8));
		//Champs non modifiables
		ES_InterfaceE.setText((lesParametres.size()==1)?"":(String) lesParametres.get(9));		
		ES_InterfaceS.setText((lesParametres.size()==1)?"":(String) lesParametres.get(10));		
	}
	
	private void doVersion()
	{
		C_MesureVersion.setIndiceVersion(LD_Version.getSelectedIndex());
	}

}
