package hapi.presentation.composants.onglets;

import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * @author Stéphanie
 */

public abstract class OO_TableauDeBord extends JPanel implements FenetreHAPI
{
	//Panels
	protected JPanel SC_OrdreActivites = null;
	private JPanel SC_Role = null;
	private JPanel SC_Produits = null;
	private JPanel SC_Ratios = null;
	private JPanel SC_RUA = null;
	private JPanel SC_RCT = null;
	//private JPanel SC_OA = null;
	private JPanel SC_Composant = null;
	private JPanel SC_PanelComposant = null;
	private JPanel SC_DateExport = null;
	private JPanel SC_ChampsDateExport = null;

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
	private JLabel STC_DateExport = null;
	protected JLabel STV_RUA = null;
	protected JLabel STV_RCT = null;
	protected JLabel STV_RUR = null;
	protected JLabel STV_RUP = null;
	protected JLabel STV_DateExport = null;
	//Liste des activités
	//protected JTable LS_OA = null;
	//private JScrollPane SC_Scroll = null;	
	//Layouts
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutFond = null;
	private BorderLayout layoutPanelComposant = null;
	protected BorderLayout layoutOrdreActivites = null;
	private BorderLayout layoutRole = null;
	private BorderLayout layoutProduit = null;
	private GridLayout layoutRatios = null;
	private BorderLayout layoutRUA = null;
	private BorderLayout layoutRCT = null;
	private BorderLayout layoutDateExport = null;
	private BorderLayout layoutChampsDateExport = null;
	//Titres
	private TitledBorder SC_TitreRatios = null;
	//private TitledBorder SC_TitreOrdreActivites = null;	

	// identifiant du processus courant
	protected String identifiant = null;

	public OO_TableauDeBord(String identifiant)
	{
		super();
		//Affectation de l'identifiant
		this.identifiant = identifiant;
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
		SC_OrdreActivites = new JPanel();
		SC_Role = new JPanel();
		SC_Produits = new JPanel();

		SC_Ratios = new JPanel();
		SC_RUA = new JPanel();
		SC_RCT = new JPanel();
		//SC_OA = new JPanel();
		SC_Composant = new JPanel();
		SC_PanelComposant = new JPanel();
		SC_DateExport = new JPanel();
		SC_ChampsDateExport = new JPanel();
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
		STC_DateExport = new JLabel();
		STV_RUA = new JLabel();
		STV_RCT = new JLabel();
		STV_RUR = new JLabel();
		STV_RUP = new JLabel();
		STV_DateExport = new JLabel();
		//LS_OA = new JTable();
		//SC_Scroll = new JScrollPane();	
		//Layout
		layoutComposant = new BorderLayout();
		layoutFond = new BorderLayout();
		layoutPanelComposant = new BorderLayout();
		layoutOrdreActivites = new BorderLayout();
		layoutRole = new BorderLayout();
		layoutProduit = new BorderLayout();
		layoutRUA = new BorderLayout();
		layoutRCT = new BorderLayout();
		layoutRatios = new GridLayout(4, 0);
		layoutDateExport = new BorderLayout();
		layoutChampsDateExport = new BorderLayout();
		//titre
		SC_TitreRatios = new TitledBorder(Bundle.getText("OO_TableauDeBord_ratios"));
		//SC_TitreOrdreActivites= new TitledBorder(Bundle.getText("OO_TableauDeBord_ordre_activites"));
	}

	public abstract void operationSurBoutons();

	public void operationSurComposants()
	{
		// affichage des labels statiques
		STC_RUA.setText(Bundle.getText("OO_TableauDeBord_RUA"));
		STC_RUR.setText(Bundle.getText("OO_TableauDeBord_RUR"));
		STC_RCT.setText(Bundle.getText("OO_TableauDeBord_RCT"));
		STC_RUP.setText(Bundle.getText("OO_TableauDeBord_RUP"));
		STC_DateExport.setText(Bundle.getText("OO_TableauDeBord_Version"));
	}

	public void operationSurPanel()
	{
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_PanelComposant, BorderLayout.NORTH);

		SC_PanelComposant.setLayout(layoutPanelComposant);
		SC_PanelComposant.add(SC_OrdreActivites, BorderLayout.CENTER);
		SC_PanelComposant.add(SC_DateExport, BorderLayout.NORTH);

		//SC_OrdreActivites.setLayout(layoutOrdreActivites);
		//SC_OrdreActivites.add(BP_Detail,BorderLayout.CENTER);
		//SC_OrdreActivites.setBorder(SC_TitreOrdreActivites);

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

		//SC_OA.setLayout(layoutOA);
		//SC_OA.add(SC_Scroll,BorderLayout.CENTER);

		//SC_Scroll.setViewportView(LS_OA);
		//LS_OA.setPreferredScrollableViewportSize(new Dimension(40, 100));

		SC_Role.setLayout(layoutRole);
		SC_Role.add(STC_RUR, BorderLayout.WEST);
		SC_Role.add(STV_RUR, BorderLayout.CENTER);

		SC_Produits.setLayout(layoutProduit);
		SC_Produits.add(STC_RUP, BorderLayout.WEST);
		SC_Produits.add(STV_RUP, BorderLayout.CENTER);
		
		SC_DateExport.setLayout(layoutDateExport);
		SC_DateExport.add(SC_ChampsDateExport, BorderLayout.NORTH);
		SC_DateExport.add(SC_Ratios, BorderLayout.CENTER);
		
		SC_ChampsDateExport.setLayout(layoutChampsDateExport);
		SC_ChampsDateExport.add(STC_DateExport, BorderLayout.WEST);
		SC_ChampsDateExport.add(STV_DateExport, BorderLayout.CENTER);

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

	public abstract void updateTexte();
}