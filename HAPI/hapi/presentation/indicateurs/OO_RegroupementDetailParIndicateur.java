/*
 * Created on 7 mars 2005
 *
 */
package hapi.presentation.indicateurs;

import hapi.application.indicateurs.C_AfficherEntitesHorsProcessusDuProjet;
import hapi.application.indicateurs.C_ArbreDesEntitesNonRealiseesDuProjet;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * @author Natalia
 *
 */
public class OO_RegroupementDetailParIndicateur extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 4193503231504484279L;
    private JPanel SC_Composants = null;
	private JPanel SC_HorsProcessus = null;
	private JPanel SC_NonRealise = null;
	// panels de mise en forme
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;

	private JLabel STV_NbNonRealises = null;
	private JLabel STV_NbHorsProcessus = null;

	private BorderLayout layoutfond = null;
	private BorderLayout layoutcomposant = null;
	private BorderLayout layoutdetails = null;
	private BorderLayout layoutarbre = null;

	private JScrollPane SC_ScrollArbre = null;
	private JScrollPane SC_ScrollTable = null;

	private TitledBorder SC_TitreActivitesHorsProcessus = null;
	private TitledBorder SC_TitreActivitesNonRealisees = null;

	// arbre 
	private C_ArbreDesEntitesNonRealiseesDuProjet LS_Arbre = null;
	// liste ordonnée
	private JTable LS_HorsProcessus = null;

	private String idProcessus = null;
	private int idExecution = 0;
	int indicateur = 0;

	C_AfficherEntitesHorsProcessusDuProjet cAfficherEntitesHorsProcessus = null;

	public OO_RegroupementDetailParIndicateur(ArrayList identifiants, int indicateur)
	{
		// affectation des identifiants
		idProcessus = identifiants.get(0).toString();
		idExecution = ((Integer) identifiants.get(1)).intValue();
		this.indicateur = indicateur;

		cAfficherEntitesHorsProcessus = new C_AfficherEntitesHorsProcessusDuProjet(idProcessus, idExecution, indicateur);

		creationElements();
		operationSurBoutons();
		operationMoteur();
		operationSurComposants();
		operationSurPanel();
		updateTexte();
		operationSurFenetre();

	}

	public void creationElements()
	{
		SC_Composants = new JPanel();
		SC_HorsProcessus = new JPanel();
		SC_NonRealise = new JPanel();
		// panels de mise en forme
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();

		STV_NbHorsProcessus = new JLabel();
		STV_NbNonRealises = new JLabel();

		layoutfond = new BorderLayout();
		layoutcomposant = new BorderLayout();
		layoutdetails = new BorderLayout();
		layoutarbre = new BorderLayout();

		SC_ScrollArbre = new JScrollPane();
		SC_ScrollTable = new JScrollPane();

		switch (indicateur)
		{
			case 0 : /* RUA */
				SC_TitreActivitesHorsProcessus = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ActivitesHorsProcessus"));
				SC_TitreActivitesNonRealisees = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ActivitesNonRealisees"));
				break;
			case 1 : /* RCT */
				SC_TitreActivitesHorsProcessus = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ActivitesHorsProcessus"));
				SC_TitreActivitesNonRealisees = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ActivitesNonRealisees"));
				break;
			case 2 : /* RUP */
				SC_TitreActivitesHorsProcessus = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ProduitsHorsProcessus"));
				SC_TitreActivitesNonRealisees = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_ProduitsNonRealises"));
				break;
			case 3 : /* RUR */
				SC_TitreActivitesHorsProcessus = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_RolesHorsProcessus"));
				SC_TitreActivitesNonRealisees = new TitledBorder(Bundle.getText("BM_DetailCalculDesIndicateurs_RolesNonRealises"));
				break;

		}
		LS_HorsProcessus = new JTable();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		SC_ScrollArbre.setViewportView(LS_Arbre);
		SC_ScrollTable.setViewportView(LS_HorsProcessus);

		LS_HorsProcessus.setPreferredScrollableViewportSize(new Dimension(50, 200));

		SC_ScrollTable.setBorder(SC_TitreActivitesHorsProcessus);
		SC_ScrollArbre.setBorder(SC_TitreActivitesNonRealisees);

		STV_NbHorsProcessus.setHorizontalAlignment(SwingConstants.RIGHT);
		STV_NbNonRealises.setHorizontalAlignment(SwingConstants.RIGHT);

	}

	public void operationSurPanel()
	{
		SC_NonRealise.setLayout(layoutarbre);
		SC_NonRealise.add(add(STV_NbNonRealises), BorderLayout.SOUTH);
		SC_NonRealise.add(SC_ScrollArbre, BorderLayout.CENTER);

		SC_HorsProcessus.setLayout(layoutdetails);
		SC_HorsProcessus.add(SC_ScrollTable, BorderLayout.CENTER);
		SC_HorsProcessus.add(STV_NbHorsProcessus, BorderLayout.SOUTH);
		SC_HorsProcessus.add(new JPanel(), BorderLayout.WEST);

		SC_Composants.setLayout(layoutcomposant);
		SC_Composants.add(SC_HorsProcessus, BorderLayout.CENTER);
		SC_Composants.add(SC_NonRealise, BorderLayout.WEST);
	}

	public void operationSurFenetre()
	{
		this.setLayout(layoutfond);

		this.add(SC_Composants, BorderLayout.CENTER);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
	}

	public void operationMoteur()
	{
		// chargement de l'arbre
		LS_Arbre = new C_ArbreDesEntitesNonRealiseesDuProjet(idProcessus, idExecution, indicateur);
		LS_Arbre.setSelectionRow(0);
		LS_Arbre.deployerLArbre();

		// chargement de la table        
		switch (indicateur)
		{
			case 0 : /* RUA */
				LS_HorsProcessus.setModel(cAfficherEntitesHorsProcessus.getModeleDesActivitesHorsProcessus());
				break;
			case 2 : /* RUP */
				LS_HorsProcessus.setModel(cAfficherEntitesHorsProcessus.getModeleDesProduitsHorsProcessus());
				break;
			case 3 : /* RUR */
				LS_HorsProcessus.setModel(cAfficherEntitesHorsProcessus.getModeleDesRolesHorsProcessus());
				break;
		}
	}

	public void updateTexte()
	{
		switch (indicateur)
		{
			case 0 : /* RUA */
				STV_NbHorsProcessus.setText(LS_HorsProcessus.getRowCount() + Bundle.getText("BM_DetailCalculDesIndicateurs_NbActivitesHP"));
				STV_NbNonRealises.setText(cAfficherEntitesHorsProcessus.getNombreActivitesNonRealisees(LS_Arbre) + Bundle.getText("BM_DetailCalculDesIndicateurs_NbActivitesNU"));
				break;
			case 2 : /* RUP */
				STV_NbHorsProcessus.setText(LS_HorsProcessus.getRowCount() + Bundle.getText("BM_DetailCalculDesIndicateurs_NbProduitsHP"));
				STV_NbNonRealises.setText(cAfficherEntitesHorsProcessus.getNombreProduitsNonUtilises(LS_Arbre) + Bundle.getText("BM_DetailCalculDesIndicateurs_NbProduitsNU"));
				break;
			case 3 :
				STV_NbHorsProcessus.setText(LS_HorsProcessus.getRowCount() + Bundle.getText("BM_DetailCalculDesIndicateurs_NbRolesHP"));
				STV_NbNonRealises.setText(cAfficherEntitesHorsProcessus.getNombreRolesNonUtilises(LS_Arbre) + Bundle.getText("BM_DetailCalculDesIndicateurs_NbRolesNU"));
				break;
		}
	}

}
