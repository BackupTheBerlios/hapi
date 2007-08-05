/*
 * Created on 5 mars 2005
 *
 */
package hapi.presentation.indicateurs;

import hapi.application.indicateurs.C_ArbreComposantsDefinitionsDeTravail;
import hapi.application.indicateurs.C_OrdonnerActivites;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.exception.StopActionException;
import hapi.presentation.indicateurs.creation.BD_DemanderDPE;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 * @author Natalia
 *
 */
public class BM_OrdreDesActivites extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = -930594111803084L;
    private JPanel SC_Composants = null;
	private JPanel SC_Detail = null; // panel d'affichage des activités ordonnées
	// panels de mise en forme
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Droit = null;
	private JPanel SC_Bouton = null;

	private BorderLayout layoutfond = null;
	private BorderLayout layoutcomposant = null;
	private BorderLayout layoutdetails = null;

	private JScrollPane SC_ScrollArbre = null;
	private JScrollPane SC_ScrollTable = null;
	private JButton BP_Fermer = null;

	private TitledBorder SC_TitreDetails = new TitledBorder(Bundle.getText("BM_OrdreDesActivites_STC_OrdreActivites"));

	// arbre 
	private C_ArbreComposantsDefinitionsDeTravail LS_Arbre = null;
	// liste ordonnée
	private JTable LS_OrdreActivites = null;

	// listeners
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doFermer();
		}
	};

	// écouteur d'une action sur l'arbre
	private MouseAdapter actionArbre = new MouseAdapter()
	{
		public void mousePressed(MouseEvent arg0)
		{
			doSelectionArbre();
		}
	};

	private C_OrdonnerActivites cOrdonnerActivites = null;

	private String idProcessus = null;
	private String idExecution = null;
	// liste ordonnée des identifiants d'activités
	// < ordre, id_activite >
	private HashMap ordreDesTaches = null;

	public BM_OrdreDesActivites(Frame parent, String idProcessus, String idExecution, HashMap ordreTaches) throws StopActionException
	{
		//Association au parent
		super(parent, Bundle.getText("BD_OrdreDesActivites_caption"), true);
		this.idProcessus = idProcessus;
		this.idExecution = idExecution;
		this.ordreDesTaches = ordreTaches;
		cOrdonnerActivites = new C_OrdonnerActivites(idProcessus, ordreDesTaches);

		this.creationElements();
		this.operationSurBoutons();
		this.operationMoteur();
		this.operationSurComposants();
		this.operationSurPanel();
		this.operationSurFenetre();
	}

	public void creationElements()
	{
		SC_Composants = new JPanel();
		SC_Detail = new JPanel(); // panel d'affichage des activités ordonnées
		// panels de mise en forme
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Droit = new JPanel();
		SC_Bouton = new JPanel();

		layoutfond = new BorderLayout();
		layoutcomposant = new BorderLayout();
		layoutdetails = new BorderLayout();

		SC_ScrollArbre = new JScrollPane();
		SC_ScrollTable = new JScrollPane();

		// liste ordonnée
		LS_OrdreActivites = new JTable();

		BP_Fermer = new JButton();
	}

	public void operationSurBoutons()
	{
		BP_Fermer.setText(Bundle.getText("BM_OrdreDesActivites_BP_Fermer"));
		BP_Fermer.addActionListener(actionFermer);
		BP_Fermer.setMnemonic(Bundle.getText("BM_OrdreDesActivites_BP_Fermer_mnemonic").charAt(0));
	}

	public void operationSurComposants()
	{

		SC_ScrollArbre.setViewportView(LS_Arbre);
		SC_ScrollTable.setViewportView(LS_OrdreActivites);
		LS_OrdreActivites.setPreferredScrollableViewportSize(new Dimension(50, 200));
		SC_ScrollTable.setBorder(SC_TitreDetails);
		LS_Arbre.addMouseListener(actionArbre);
	}

	public void operationSurPanel()
	{
		SC_Bouton.add(BP_Fermer);

		SC_Detail.setLayout(layoutdetails);
		SC_Detail.add(SC_ScrollTable, BorderLayout.CENTER);
		SC_Detail.add(new JPanel(), BorderLayout.WEST);

		SC_Composants.setLayout(layoutcomposant);
		SC_Composants.add(SC_Detail, BorderLayout.CENTER);
		SC_Composants.add(SC_ScrollArbre, BorderLayout.WEST);

		SC_Bas.add(SC_Bouton);
	}

	public void operationSurFenetre()
	{
		this.getContentPane().setLayout(layoutfond);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Bouton, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Droit, BorderLayout.EAST);
		this.getContentPane().add(SC_Bas, BorderLayout.WEST);

		pack();
		//Positionnement
		this.setSize(650, 350);
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur() throws StopActionException
	{
		//chargement de l'arbre
		LS_Arbre = new C_ArbreComposantsDefinitionsDeTravail(idProcessus,C_ExecutionProcessus.getExecutionProcessus(idProcessus,idExecution).getIdCycleDeVie(),BD_DemanderDPE.doitUtiliserDPECourant((JFrame) this.getParent(), idProcessus, idExecution));
		LS_Arbre.setSelectionRow(0);
		LS_Arbre.deployerLArbre();

		// creation du modele pour la table
		cOrdonnerActivites.creerLeModele();
		// affichage des noms des colonnes
		LS_OrdreActivites.setModel(cOrdonnerActivites.getModeleVide());
	}

	public void updateTexte()
	{}

	private void doFermer()
	{
		this.dispose();
	}

	private void doSelectionArbre()
	{
		// récupération de la selection dans l'arbre
		this.update(this.getGraphics());
		// dans le cas où la selection est une définition ...
		if (LS_Arbre.isSelectionUneActivite())
		{
			// ... affichage de la liste des tâches ordonnée
			LS_OrdreActivites.setModel(cOrdonnerActivites.getModelePourLaDefinition(LS_Arbre.getSelection().getIdentifiant()));
		}
		else
		{
			// ... réinitialisation de la table
			cOrdonnerActivites.reinitialiserLeModele();
			LS_OrdreActivites.removeAll();
		}
	}

}
