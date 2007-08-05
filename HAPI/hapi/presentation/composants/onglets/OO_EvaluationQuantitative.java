/*
 * Created on 23 févr. 2005
 *
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_GestionSeuils;
import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.exception.IdentifiantInconnuException;
import hapi.exception.SeuilNonDefiniException;
import hapi.exception.StopActionException;
import hapi.presentation.indicateurs.BM_DetailCalculDesIndicateurs;
import hapi.presentation.indicateurs.BM_OrdreDesActivites;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 * @author Natalia
 *
 */
public class OO_EvaluationQuantitative extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 1883667698613993054L;
    //  Panels
	private JPanel SC_OrdreActivites = null;
	private JPanel SC_Role = null;
	private JPanel SC_Produits = null;
	private JPanel SC_Ratios = null;
	private JPanel SC_RUA = null;
	private JPanel SC_RCT = null;
	private JPanel SC_OA = null;
	private JPanel SC_Composant = null;
	private JPanel SC_PanelComposant = null;
	private JPanel SC_Periode = null;

	//Espacement
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	//Composants
	private JLabel STC_RUA = null;
	private JLabel STC_RCT = null;
	private JLabel STC_RUR = null;
	private JLabel STC_RUP = null;
	private JLabel STV_RUA = null;
	private JLabel STV_RCT = null;
	private JLabel STV_RUR = null;
	private JLabel STV_RUP = null;
	private JLabel STV_SeuilNonDefinis = null;

	private JButton BP_OrdreActivites = null;
	private JButton BP_DetailCalculs = null;

	private JLabel STV_Debut = null;
	private JLabel STV_Fin = null;

	private JTable LS_ChargeComposant = null;
	private JScrollPane SC_Scroll = null;
	//Layouts
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutFond = null;
	private BorderLayout layoutPanelComposant = null;
	//private BorderLayout layoutOA = null; 
	private BorderLayout layoutBPOrdreActivites = null;
	private BorderLayout layoutRole = null;
	private BorderLayout layoutProduit = null;
	private GridLayout layoutRatios = null;
	private BorderLayout layoutRUA = null;
	private BorderLayout layoutRCT = null;
	private BorderLayout layoutOA = null;
	private GridLayout layoutPeriode = null;
	//Titres
	private TitledBorder SC_TitreRatios = null;
	private TitledBorder SC_TitreChargeComposant = null;
	private TitledBorder SC_TitrePeriode = null;

	private static final String FORMAT_DATE = "dd/MM/yyyy";
	private static String FORMAT_INDICATEURS = "##0.0";

	private int idEval = 0;
	private int idExec = 0;
	private String idProc = null;
	private HashMap OrdreDesTaches = null;

	private JFrame laFenetre = null;

	private ActionListener actionAfficherOA = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			doAfficherOA();
		}
	};

	private ActionListener actionAfficherDetails = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			doAfficherDetails();
		}
	};

	public OO_EvaluationQuantitative(JFrame parent, int id_eval, ArrayList identifiants)
	{
		super();
		//Affectation des identifiants
		idEval = id_eval;
		idExec = new Integer(identifiants.get(1).toString()).intValue();
		idProc = identifiants.get(0).toString();
		laFenetre = parent;
		//Création des élémnts
		creationElements();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
		operationSurBoutons();
		updateTexte();
	}

	public void creationElements()
	{
		//      Panels
		SC_OrdreActivites = new JPanel();
		SC_Role = new JPanel();
		SC_Produits = new JPanel();

		SC_Ratios = new JPanel();
		SC_RUA = new JPanel();
		SC_RCT = new JPanel();
		SC_OA = new JPanel();
		SC_Composant = new JPanel();
		SC_PanelComposant = new JPanel();
		SC_Periode = new JPanel();

		//Espacement
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		//Composants
		STC_RUA = new JLabel();
		STC_RCT = new JLabel();
		STC_RUR = new JLabel();
		STC_RUP = new JLabel();
		STV_RUA = new JLabel();
		STV_RCT = new JLabel();
		STV_RUR = new JLabel();
		STV_RUP = new JLabel();
		STV_SeuilNonDefinis = new JLabel();

		LS_ChargeComposant = new JTable();

		BP_OrdreActivites = new JButton(Bundle.getText("OO_EvaluationQuantitative_OrdreDesActivites"));
		BP_DetailCalculs = new JButton(Bundle.getText("OO_TableauDeBord_BP_Detail"));

		STV_Debut = new JLabel();
		STV_Fin = new JLabel();

		SC_Scroll = new JScrollPane();
		//Layout
		layoutComposant = new BorderLayout();
		layoutFond = new BorderLayout();
		layoutPanelComposant = new BorderLayout();
		layoutBPOrdreActivites = new BorderLayout();
		layoutRole = new BorderLayout();
		layoutProduit = new BorderLayout();
		layoutRUA = new BorderLayout();
		layoutRCT = new BorderLayout();
		layoutOA = new BorderLayout();
		layoutRatios = new GridLayout(4, 0);
		layoutPeriode = new GridLayout(0, 2);

		//titre
		SC_TitreRatios = new TitledBorder(Bundle.getText("OO_TableauDeBord_ratios"));
		SC_TitreChargeComposant = new TitledBorder(Bundle.getText("OO_TableauDeBord_charge_composant"));
		SC_TitrePeriode = new TitledBorder(Bundle.getText("OO_TableauDeBord_periode"));
	}

	public void operationSurBoutons()
	{
		BP_OrdreActivites.addActionListener(actionAfficherOA);
		BP_OrdreActivites.setMnemonic('C');
		BP_DetailCalculs.addActionListener(actionAfficherDetails);
	}

	public void operationSurComposants()
	{
		STV_SeuilNonDefinis.setText("");

		SC_Scroll.setBorder(SC_TitreChargeComposant);

		// affichage des labels statiques
		STC_RUA.setText(Bundle.getText("OO_TableauDeBord_RUA"));
		STC_RUR.setText(Bundle.getText("OO_TableauDeBord_RUR"));
		STC_RCT.setText(Bundle.getText("OO_TableauDeBord_RCT"));
		STC_RUP.setText(Bundle.getText("OO_TableauDeBord_RUP"));
	}

	public void operationSurPanel()
	{
		// panel du haut
		SC_Periode.setLayout(layoutPeriode);
		SC_Periode.add(STV_Debut);
		SC_Periode.add(STV_Fin);
		SC_Periode.setBorder(SC_TitrePeriode);

		// panel des composants 
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Periode, BorderLayout.NORTH);
		SC_Composant.add(SC_PanelComposant, BorderLayout.CENTER);
		SC_Composant.add(BP_OrdreActivites, BorderLayout.SOUTH);

		SC_PanelComposant.setLayout(layoutPanelComposant);
		SC_PanelComposant.add(SC_OA, BorderLayout.CENTER);
		SC_PanelComposant.add(SC_Ratios, BorderLayout.NORTH);

		SC_OrdreActivites.setLayout(layoutBPOrdreActivites);
		SC_OrdreActivites.add(BP_DetailCalculs, BorderLayout.WEST);
		SC_OrdreActivites.add(BP_OrdreActivites, BorderLayout.EAST);

		SC_OA.setLayout(layoutOA);
		SC_OA.add(SC_OrdreActivites, BorderLayout.SOUTH);
		SC_OA.add(SC_Scroll, BorderLayout.CENTER);

		SC_Ratios.setLayout(layoutRatios);
		SC_Ratios.add(SC_RUA);
		SC_Ratios.add(SC_RCT);
		SC_Ratios.add(SC_Role);
		SC_Ratios.add(SC_Produits);
		SC_Ratios.setBorder(SC_TitreRatios);

		SC_RUA.setLayout(layoutRUA);
		SC_RUA.add(STC_RUA, BorderLayout.WEST);
		SC_RUA.add(STV_RUA, BorderLayout.CENTER);

		SC_RCT.setLayout(layoutRCT);
		SC_RCT.add(STC_RCT, BorderLayout.WEST);
		SC_RCT.add(STV_RCT, BorderLayout.CENTER);

		SC_Scroll.setViewportView(LS_ChargeComposant);
		LS_ChargeComposant.setPreferredScrollableViewportSize(new Dimension(40, 100));

		SC_Role.setLayout(layoutRole);
		SC_Role.add(STC_RUR, BorderLayout.WEST);
		SC_Role.add(STV_RUR, BorderLayout.CENTER);

		SC_Produits.setLayout(layoutProduit);
		SC_Produits.add(STC_RUP, BorderLayout.WEST);
		SC_Produits.add(STV_RUP, BorderLayout.CENTER);

		SC_Haut.add(STV_SeuilNonDefinis);
	}

	public void operationSurFenetre()
	{
		//      Affectation du layout
		this.setLayout(layoutFond);
		//Ajout du panel des composants
		this.add(SC_Composant, BorderLayout.CENTER);
		//Ajout des bordures
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);

	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{
		ArrayList infos = C_Evaluation.getInformationsEvaluationQuantitative(this.idProc, this.idExec, this.idEval);
		// affichage des labels dynamiques en fonction du processus courant
		int cpt_seuilNonDefini = 0;
		int idIndicateur = 0;
		DecimalFormat df = new DecimalFormat(FORMAT_INDICATEURS);
		try
		{
			String rua = df.format(((Float) infos.get(1)).doubleValue());
			STV_RUA.setText(" " + rua + " %");
			idIndicateur = ((Integer) infos.get(0)).intValue();
			// si la valeur du RUA est hors seuil, on le signifie
			//System.out.println("++++++ Id proc = "+ idProc + " Id indicateur = "+idIndicateur); 
			try
			{
				if (((Float) infos.get(1)).floatValue() < C_GestionSeuils.getMin(idProc, idIndicateur).doubleValue() || ((Float) infos.get(1)).floatValue() > C_GestionSeuils.getMax(idProc, idIndicateur).doubleValue())
				{
					STV_RUA.setFont(new java.awt.Font(STV_RUA.getFont().getFontName(), java.awt.Font.BOLD, STV_RUA.getFont().getSize()));
					STV_RUA.setForeground(Color.red);
				}
			}
			catch (SeuilNonDefiniException s)
			{
				cpt_seuilNonDefini++;
			}

			String rct = df.format(((Float) infos.get(3)).doubleValue());
			STV_RCT.setText(" " + rct + " %");
			idIndicateur = ((Integer) infos.get(2)).intValue();
			try
			{
				if (((Float) infos.get(3)).floatValue() < C_GestionSeuils.getMin(idProc, idIndicateur).doubleValue() || ((Float) infos.get(3)).floatValue() > C_GestionSeuils.getMax(idProc, idIndicateur).doubleValue())
				{
					STV_RCT.setFont(new java.awt.Font(STV_RCT.getFont().getFontName(), java.awt.Font.BOLD, STV_RCT.getFont().getSize()));
					STV_RCT.setForeground(Color.red);
				}
			}
			catch (SeuilNonDefiniException s)
			{
				cpt_seuilNonDefini++;
			}

			String rur = df.format(((Float) infos.get(5)).doubleValue());
			STV_RUR.setText(" " + rur + " %");
			idIndicateur = ((Integer) infos.get(4)).intValue();
			try
			{
				if (((Float) infos.get(5)).floatValue() < C_GestionSeuils.getMin(idProc, idIndicateur).doubleValue() || ((Float) infos.get(5)).floatValue() > C_GestionSeuils.getMax(idProc, idIndicateur).doubleValue())
				{
					STV_RUR.setFont(new java.awt.Font(STV_RUR.getFont().getFontName(), java.awt.Font.BOLD, STV_RUR.getFont().getSize()));
					STV_RUR.setForeground(Color.red);
				}
			}
			catch (SeuilNonDefiniException s)
			{
				cpt_seuilNonDefini++;
			}

			String rup = df.format(((Float) infos.get(7)).doubleValue());
			STV_RUP.setText(" " + rup + " %");
			idIndicateur = ((Integer) infos.get(6)).intValue();
			try
			{
				if (((Float) infos.get(7)).floatValue() < C_GestionSeuils.getMin(idProc, idIndicateur).doubleValue() || ((Float) infos.get(7)).floatValue() > C_GestionSeuils.getMax(idProc, idIndicateur).doubleValue())
				{
					STV_RUP.setFont(new java.awt.Font(STV_RUP.getFont().getFontName(), java.awt.Font.BOLD, STV_RUP.getFont().getSize()));
					STV_RUP.setForeground(Color.red);
				}
			}
			catch (SeuilNonDefiniException s)
			{
				cpt_seuilNonDefini++;
			}
		}
		catch (IdentifiantInconnuException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("Erreur"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		//initialiser la liste d'activités
		OrdreDesTaches = (HashMap) infos.get(8);
		//LS_OA.setModel(C_Evaluation.getModeleTable(idProc, OrdreDesActivites));
		// initialisation de la table des charges pas composant
		LS_ChargeComposant.setModel(C_Evaluation.getModeleTableChargeParComposant(idProc, C_Evaluation.getRepartitionDesChargesParComposant(idProc, idExec, idEval)));

		SimpleDateFormat sfDate = new SimpleDateFormat(FORMAT_DATE);

		STV_Fin.setText(Bundle.getText("OO_TableauDeBord_fin") + sfDate.format((Date) infos.get(10)));
		STV_Debut.setText(Bundle.getText("OO_TableauDeBord_debut") + sfDate.format((Date) infos.get(9)));

		if (cpt_seuilNonDefini > 0)
		{
			STV_SeuilNonDefinis.setForeground(Color.RED);
			STV_SeuilNonDefinis.setText(Bundle.getText("OO_EvaluationQuantitative_SeuilNonDefini") /* + C_Processus.getProcessus(idProc).getNom() +"."*/
			);
		}
	}

	private void doAfficherOA()
	{
		try
		{
			new BM_OrdreDesActivites(laFenetre, idProc, new Integer(idExec).toString(), OrdreDesTaches).setVisible(true);
		}
		catch (StopActionException e)
		{}
	}

	private void doAfficherDetails()
	{
		ArrayList identifiants = new ArrayList();
		identifiants.add(idProc);
		identifiants.add(new Integer(idExec));
		identifiants.add(new Integer(idEval));
		new BM_DetailCalculDesIndicateurs(laFenetre, identifiants).setVisible(true);
	}
}
