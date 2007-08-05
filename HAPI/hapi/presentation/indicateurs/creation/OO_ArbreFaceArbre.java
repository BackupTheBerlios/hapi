/*
 * Fichier OO_ArbreFaceArbre.java
 * Auteur Cédric
 *
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_ArbreFaceArbre;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

public class OO_ArbreFaceArbre extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -2983350258596148269L;
    // Panels
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;
	private JPanel SC_Composants = null;
	private JPanel SC_Arbres = null;
	private JPanel SC_Utilise = null;
	private JPanel SC_NonUtilise = null;
	private JPanel SC_Passage = null;
	private JPanel SC_BoutonGauche = null;
	private JPanel SC_BoutonDroite = null;

	// Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private GridLayout layoutArbres = null;
	private GridLayout layoutPassage = null;

	//Scroll
	private JScrollPane SC_ScrollUtilise = null;
	private JScrollPane SC_ScrollNonUtilise = null;

	//Cadres
	private TitledBorder SC_TitreUtilise = null;
	private TitledBorder SC_TitreNonUtilise = null;

	//Arbre
	private JTree LS_ArbreUtilise = null;
	private JTree LS_ArbreNonUtilise = null;

	//Passages
	private JButton BP_VersDroite = null;
	private JButton BP_VersGauche = null;

	//Le contrôleur
	private C_ArbreFaceArbre cArbreFaceArbre = null;

	//Ecouteurs
	private ActionListener actionVersUtilise = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doVersUtilise();
		}
	};
	private ActionListener actionVersNonUtilise = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doVersNonUtilise();
		}
	};

	public OO_ArbreFaceArbre(C_ArbreFaceArbre lArbreFaceArbre)
	{
		cArbreFaceArbre = lArbreFaceArbre;
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
		// Panels
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();
		SC_Arbres = new JPanel();
		SC_Composants = new JPanel();
		SC_Utilise = new JPanel();
		SC_NonUtilise = new JPanel();
		SC_Passage = new JPanel();
		SC_BoutonDroite = new JPanel();
		SC_BoutonGauche = new JPanel();

		// Layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutArbres = new GridLayout(0,2);
		layoutPassage = new GridLayout(0,2);

		//Scroll
		SC_ScrollUtilise = new JScrollPane();
		SC_ScrollNonUtilise = new JScrollPane();

		//Cadres
		SC_TitreUtilise = new TitledBorder(cArbreFaceArbre.getTitreUtilise());
		SC_TitreNonUtilise = new TitledBorder(cArbreFaceArbre.getTitreNonUtilise());

		//Arbres
		LS_ArbreUtilise = new JTree(cArbreFaceArbre.getModeleUtilise());
		LS_ArbreNonUtilise = new JTree(cArbreFaceArbre.getModeleNonUtilise());
		cArbreFaceArbre.setToObserve(LS_ArbreUtilise, LS_ArbreNonUtilise);

		//Passages
		BP_VersDroite = new JButton();
		BP_VersGauche = new JButton();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		BP_VersDroite.setText(cArbreFaceArbre.getPasserNonUtilise());
		BP_VersDroite.addActionListener(actionVersNonUtilise);
		BP_VersDroite.setMaximumSize(new Dimension(23,23));
		
		BP_VersGauche.setText(cArbreFaceArbre.getPasserUtilise());
		BP_VersGauche.addActionListener(actionVersUtilise);
		BP_VersGauche.setMaximumSize(new Dimension(23,23));
	}

	public void operationSurPanel()
	{
		SC_ScrollUtilise.setViewportView(LS_ArbreUtilise);
		SC_ScrollNonUtilise.setViewportView(LS_ArbreNonUtilise);		

		SC_Utilise.setLayout(new BorderLayout());
		SC_Utilise.add(SC_ScrollUtilise, BorderLayout.CENTER);
		SC_Utilise.setBorder(SC_TitreUtilise);

		SC_NonUtilise.setLayout(new BorderLayout());
		SC_NonUtilise.add(SC_ScrollNonUtilise, BorderLayout.CENTER);
		SC_NonUtilise.setBorder(SC_TitreNonUtilise);		

		SC_Arbres.setLayout(layoutArbres);		
		SC_Arbres.add(SC_Utilise);
		//SC_Arbres.add(SC_Passage, BorderLayout.CENTER);
		SC_Arbres.add(SC_NonUtilise);

		SC_BoutonDroite.setLayout(new BorderLayout());
		SC_BoutonDroite.add(BP_VersGauche, BorderLayout.WEST);

		SC_BoutonGauche.setLayout(new BorderLayout());
		SC_BoutonGauche.add(BP_VersDroite, BorderLayout.EAST);

		SC_Passage.setLayout(layoutPassage);
		SC_Passage.add(SC_BoutonGauche);
		SC_Passage.add(SC_BoutonDroite);		

		SC_Composants.setLayout(layoutComposant);
		SC_Composants.add(SC_Passage, BorderLayout.NORTH);
		SC_Composants.add(SC_Arbres, BorderLayout.CENTER);
	}

	public void operationSurFenetre()
	{
		this.setLayout(layoutFond);

		this.add(SC_Composants, BorderLayout.CENTER);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
	}

	public void operationMoteur()
	{
		for (int i = 0; i < LS_ArbreNonUtilise.getRowCount(); i++)
		{
			LS_ArbreNonUtilise.expandRow(i);
		}

		for (int i = 0; i < LS_ArbreUtilise.getRowCount(); i++)
		{
			LS_ArbreUtilise.expandRow(i);
		}

	}

	public void updateTexte()
	{}

	private void doVersUtilise()
	{
		for (int i = 0; i < LS_ArbreNonUtilise.getSelectionCount(); i++)
		{
			cArbreFaceArbre.moveNonUtiliseToUtilise(LS_ArbreNonUtilise.getSelectionPaths()[i]);
		}

	}

	private void doVersNonUtilise()
	{
		for (int i = 0; i < LS_ArbreUtilise.getSelectionCount(); i++)
		{
			cArbreFaceArbre.moveUtiliseToNonUtilise(LS_ArbreUtilise.getSelectionPaths()[i]);
		}
	}
	
	public C_ArbreFaceArbre getCArbreFaceArbre()
	{
		return cArbreFaceArbre;
	}
}
