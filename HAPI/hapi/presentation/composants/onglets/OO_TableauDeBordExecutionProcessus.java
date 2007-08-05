package hapi.presentation.composants.onglets;

import hapi.application.indicateurs.C_TableauDeBordExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.presentation.indicateurs.BM_RegroupementCalculIndicateurs;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * @author Vincent
 */
public class OO_TableauDeBordExecutionProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -5303173722215932983L;
    //Panels
	private JPanel SC_OrdreActivites = null;
	private JPanel SC_Role = null;
	private JPanel SC_Produits = null;
	private JPanel SC_Ratios = null;
	private JPanel SC_RUA = null;
	private JPanel SC_RCT = null;
	private JPanel SC_Composant = null;
	private JPanel SC_PanelComposant = null;
	private JPanel SC_Periode = null;
	private JPanel SC_Description = null;
	private JPanel SC_Detail = null;

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

	private JLabel STV_DateDebut = null;
	private JLabel STV_DateFin = null;

	private JTextArea STV_Description = null;
	private JScrollPane SC_ScrollDescription = null;

	//Layouts
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutFond = null;
	private BorderLayout layoutPanelComposant = null;
	private BorderLayout layoutOrdreActivites = null;
	private BorderLayout layoutRole = null;
	private BorderLayout layoutProduit = null;
	private GridLayout layoutRatios = null;
	private BorderLayout layoutRUA = null;
	private BorderLayout layoutRCT = null;
	private BorderLayout layoutDetail = null;
	private GridLayout layoutPeriode = null;
	private BorderLayout layoutDescription = null;

	//Titres
	private TitledBorder SC_TitreRatios = null;
	private TitledBorder SC_TitrePeriode = null;
	private TitledBorder SC_TitreDescription = null;

	// identifiant du processus courant
	private String identifiant = null;

	private int idExecution = 0;
	private String FORMAT_INDICATEUR = "##0.0";
	//Bouton 
	private JButton BP_Detail = new JButton();
	;

	private JFrame laFenetre = null;

	private ActionListener actionDetail = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			doAfficherDetail();
		}
	};

	public OO_TableauDeBordExecutionProcessus(JFrame parent, String lIdExecution, String identifiantProcess)
	{
		super();
		//Affectation de l'identifiant
		this.identifiant = identifiantProcess;
		//Création des élémnts
		creationElements();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
		this.idExecution = Integer.parseInt(lIdExecution);
		laFenetre = parent;
		this.operationSurBoutons();
		this.updateTexte();
	}

	public void creationElements()
	{
		//Panels
		SC_OrdreActivites = new JPanel();
		SC_Role = new JPanel();
		SC_Produits = new JPanel();

		SC_Detail = new JPanel();
		SC_Ratios = new JPanel();
		SC_RUA = new JPanel();
		SC_RCT = new JPanel();
		SC_Composant = new JPanel();
		SC_PanelComposant = new JPanel();
		SC_Periode = new JPanel();
		SC_Description = new JPanel();

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

		STV_DateDebut = new JLabel();
		STV_DateFin = new JLabel();

		STV_Description = new JTextArea();
		SC_ScrollDescription = new JScrollPane();

		//Layout
		layoutComposant = new BorderLayout();
		layoutFond = new BorderLayout();
		layoutPanelComposant = new BorderLayout();
		layoutOrdreActivites = new BorderLayout();
		layoutRole = new BorderLayout();
		layoutProduit = new BorderLayout();
		layoutRUA = new BorderLayout();
		layoutRCT = new BorderLayout();
		layoutDetail = new BorderLayout();
		layoutRatios = new GridLayout(4, 0);
		layoutPeriode = new GridLayout(0, 2);
		layoutDescription = new BorderLayout();

		//titre
		SC_TitreRatios = new TitledBorder(Bundle.getText("OO_TableauDeBord_ratios"));
		SC_TitrePeriode = new TitledBorder(Bundle.getText("OO_TableauDeBord_periode"));
		SC_TitreDescription = new TitledBorder(Bundle.getText("OO_TableauDeBord_description"));
	}

	public void operationSurComposants()
	{
		// affichage des labels statiques
		STC_RUA.setText(Bundle.getText("OO_TableauDeBord_RUA"));
		STC_RUR.setText(Bundle.getText("OO_TableauDeBord_RUR"));
		STC_RCT.setText(Bundle.getText("OO_TableauDeBord_RCT"));
		STC_RUP.setText(Bundle.getText("OO_TableauDeBord_RUP"));

		SC_ScrollDescription.setViewportView(STV_Description);
		SC_ScrollDescription.setBorder(SC_TitreDescription);
		STV_Description.setLineWrap(true);
		SC_ScrollDescription.setPreferredSize(new Dimension(0, 120));
	}

	public void operationSurPanel()
	{
		SC_Description.setLayout(layoutDescription);
		SC_Description.add(SC_Periode, BorderLayout.NORTH);
		SC_Description.add(SC_ScrollDescription, BorderLayout.CENTER);

		SC_Periode.setLayout(layoutPeriode);
		SC_Periode.add(STV_DateDebut);
		SC_Periode.add(STV_DateFin);
		SC_Periode.setBorder(SC_TitrePeriode);

		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Description, BorderLayout.NORTH);
		SC_Composant.add(SC_PanelComposant, BorderLayout.CENTER);

		SC_PanelComposant.setLayout(layoutPanelComposant);
		SC_PanelComposant.add(SC_Detail, BorderLayout.CENTER);
		SC_PanelComposant.add(SC_Ratios, BorderLayout.NORTH);

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

		SC_OrdreActivites.setLayout(layoutOrdreActivites);
		SC_OrdreActivites.add(BP_Detail, BorderLayout.WEST);

		SC_Detail.setLayout(layoutDetail);
		SC_Detail.add(SC_OrdreActivites, BorderLayout.NORTH);

		SC_Role.setLayout(layoutRole);
		SC_Role.add(STC_RUR, BorderLayout.WEST);
		SC_Role.add(STV_RUR, BorderLayout.CENTER);

		SC_Produits.setLayout(layoutProduit);
		SC_Produits.add(STC_RUP, BorderLayout.WEST);
		SC_Produits.add(STV_RUP, BorderLayout.CENTER);

	}

	public void operationSurFenetre()
	{
		//Affectation du layout
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
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		Date deb = C_TableauDeBordExecutionProcessus.getDateDebut(identifiant, new Integer(idExecution).toString());
		Date fin = C_TableauDeBordExecutionProcessus.getDateFin(identifiant, new Integer(idExecution).toString());

		STV_DateFin.setText(Bundle.getText("OO_TableauDeBord_fin") + sfDate.format(fin));
		STV_DateDebut.setText(Bundle.getText("OO_TableauDeBord_debut") + sfDate.format(deb));

		STV_Description.setText(C_TableauDeBordExecutionProcessus.getDescription(identifiant, new Integer(idExecution).toString()));

		DecimalFormat df = new DecimalFormat(FORMAT_INDICATEUR);
		this.STV_RUR.setText(" " + df.format(C_TableauDeBordExecutionProcessus.getMoyenneRUR(this.identifiant, idExecution)) + " %");
		this.STV_RCT.setText(" " + df.format(C_TableauDeBordExecutionProcessus.getMoyenneRCT(this.identifiant, idExecution)) + " %");
		this.STV_RUA.setText(" " + df.format(C_TableauDeBordExecutionProcessus.getMoyenneRUA(this.identifiant, idExecution)) + " %");
		this.STV_RUP.setText(" " + df.format(C_TableauDeBordExecutionProcessus.getMoyenneRUP(this.identifiant, idExecution)) + " %");
	}

	public void operationSurBoutons()
	{
		BP_Detail.setText(Bundle.getText("OO_TableauDeBord_BP_Detail"));
		BP_Detail.addActionListener(actionDetail);
		BP_Detail.setMnemonic('D');
	}

	private void doAfficherDetail()
	{
		ArrayList identifiants = new ArrayList();
		identifiants.add(identifiant);
		identifiants.add(new Integer(idExecution));
		new BM_RegroupementCalculIndicateurs(laFenetre, identifiants).setVisible(true);
	}

}
