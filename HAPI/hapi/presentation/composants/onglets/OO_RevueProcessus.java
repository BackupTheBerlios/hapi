/*
 * Créé le 21 sept. 2005
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_RevueProcessus;
import hapi.application.C_Utilisateur;
import hapi.application.modele.DateModel;
import hapi.application.ressources.Bundle;
import hapi.exception.ChampsVideException;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Cédric
 */
public class OO_RevueProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 829994683770666825L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;
	
	private JPanel SC_Ajout = null;
	private TitledBorder SC_AjoutTitre = null;
	private JPanel SC_Saisie = null;
	private JPanel SC_Action = null;
	private JPanel SC_Decision = null;
	private JPanel SC_LabelDate = null;
	private JPanel SC_ChampsDate = null;
	private JPanel SC_GroupeDate = null;
	private JPanel SC_GroupeDateBouton = null;
	private JPanel SC_Bouton = null;
	private JPanel SC_ProchaineRevue = null;
	private JPanel SC_LabelProchaineRevue = null;
	private JPanel SC_ChampProchaineRevue = null;
	private JPanel SC_GroupeProchaineRevue = null;
	
	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private JPanel SC_Supprimer = null;
	
	private JSplitPane horizontalSplit = null;
	
	//Composants
	private JScrollPane SC_ScrollAction = null;
	private JScrollPane SC_ScrollDecision = null;
	private JScrollPane SC_ScrollRevues = null;
	
	private JTable LS_Revues = null;
	private JButton BP_Supprimer = null;
	
	private JLabel STC_Decision = null;
	private JTextArea ES_Decision = null;
	private JLabel STC_Action = null;
	private JTextArea ES_Action = null;
	private JButton BP_Ajouter = null;
	private JButton BP_Modifier = null;
	private JLabel STC_Date = null;
	private JFormattedTextField ES_Date = null;
	private JLabel STC_ProchaineDate = null;
	private JFormattedTextField ES_ProchaineDate = null;	

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	
	private BorderLayout layoutAjout = null;
	private GridLayout layoutSaisie = null;
	private BorderLayout layoutDecision = null;
	private BorderLayout layoutAction = null;
	private GridLayout layoutLabelDate = null;
	private GridLayout layoutChampsDate = null;
	private BorderLayout layoutGroupeDate = null;
	private BorderLayout layoutGroupeDateBouton = null;
	private BorderLayout layoutBouton = null;
	
	private BorderLayout layoutListe = null;
	private BorderLayout layoutSupprimer = null;
	
	private BorderLayout layoutProchaineRevue = null;
	private BorderLayout layoutLabelProchaineRevue = null;
	private BorderLayout layoutChampProchaineRevue = null;
	private BorderLayout layoutGroupeProchaineRevue = null;

	// Identifiant du processus courant
	private String idProcessus = null;
	private int indiceRevue = -1;

	// Permet de savoir si le modèle de la table est en cours de modification
	// Utile pour savoir s'il faut ou pas afficher la modification en cours sur la table
	private boolean actionSurTable = false;

	//Ecouteurs de la fenêtre
	private ActionListener actionAjouter = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAjouter();
		}
	};
	
	private ActionListener actionModifier = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doModifier();
		}
	};	

	private ActionListener actionSupprimer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSupprimer();
		}
	};

	// Ecouteur de la JTable
	private MouseAdapter tableMouseListener = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			doAfficheDetailDemande();
		}
	};

	private ListSelectionListener ListenerSurTable = new ListSelectionListener()
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if (actionSurTable == false)
			{
				doAfficheDetailDemande();
			}
		}
	};

	/** 
	 * Création de l'onglet premettant de gérer les demandes de modification
	 * concernant un processus.
	 * 
	 * @param identifiantProcessus : l'id du processus sélectionné.
	 */
	public OO_RevueProcessus(String identifiantProcessus)
	{
		super();

		idProcessus = identifiantProcessus;

		//Création des éléments
		creationElements();

		//Mise à jour des textes
		updateTexte();

		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	/**
	 * Création des éléments composant l'onglet.
	 */
	public void creationElements()
	{
		//Panel
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Composants = new JPanel();
		
		SC_Ajout = new JPanel();
		SC_LabelDate = new JPanel();
		SC_ChampsDate = new JPanel();
		SC_GroupeDate = new JPanel();
		SC_GroupeDateBouton = new JPanel();
		SC_Bouton = new JPanel();
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_RevueProcessus_detail"));
		SC_Saisie = new JPanel();
		SC_Action = new JPanel();
		SC_Decision = new JPanel();	
		
		SC_Liste = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_RevueProcessus_liste"));
		SC_Supprimer = new JPanel();
		
		SC_ProchaineRevue = new JPanel();
		SC_LabelProchaineRevue = new JPanel();
		SC_ChampProchaineRevue = new JPanel();
		SC_GroupeProchaineRevue = new JPanel();
		
		horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		
		//Composants
		SC_ScrollAction = new JScrollPane();
		SC_ScrollDecision = new JScrollPane();
		SC_ScrollRevues = new JScrollPane();
		
		LS_Revues = new JTable(C_RevueProcessus.getModeleRevuesProcessus(idProcessus));
		BP_Supprimer = new JButton();
		
		STC_Decision = new JLabel();
		ES_Decision = new JTextArea(4, 100);
		STC_Action = new JLabel();
		ES_Action = new JTextArea(4, 100);
		BP_Ajouter = new JButton();
		BP_Modifier = new JButton();
		STC_Date = new JLabel();
		STC_ProchaineDate = new JLabel();
		ES_Date = new JFormattedTextField();
		ES_ProchaineDate = new JFormattedTextField();

		//Layouts
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		
		layoutAjout = new BorderLayout();
		layoutLabelDate = new GridLayout(1,0);
		layoutChampsDate = new GridLayout(1,0);
		layoutGroupeDate = new BorderLayout();
		layoutGroupeDateBouton = new BorderLayout();
		layoutBouton = new BorderLayout();		
		layoutSaisie = new GridLayout(2,0);
		layoutDecision = new BorderLayout();
		layoutAction = new BorderLayout();
		
		layoutListe = new BorderLayout();
		layoutSupprimer = new BorderLayout();
		
		layoutProchaineRevue = new BorderLayout();
		layoutLabelProchaineRevue = new BorderLayout();
		layoutChampProchaineRevue = new BorderLayout();
		layoutGroupeProchaineRevue = new BorderLayout();
	}

	public void operationSurBoutons()
	{
		//Boutons
		BP_Ajouter.setText(Bundle.getText("Ajouter"));
		BP_Ajouter.setMnemonic(Bundle.getChar("Ajouter_mne"));
		BP_Ajouter.addActionListener(actionAjouter);
		
		BP_Modifier.setText(Bundle.getText("Modifier"));
		BP_Modifier.setMnemonic(Bundle.getChar("Modifier_mne"));
		BP_Modifier.addActionListener(actionModifier);		
		
		//Bouton de suppression
		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);
	}

	/**
	 * Initialisation des éléments composant l'onglet.
	 */
	public void operationSurComposants()
	{
		//Detail
		STC_Decision.setText(Bundle.getText("OO_RevueProcessus_champ_decision") + " : ");
		STC_Action.setText(Bundle.getText("OO_RevueProcessus_champ_action") + " : ");
		STC_Date.setText(Bundle.getText("OO_RevueProcessus_champ_date") + " : ");
		STC_ProchaineDate.setText(Bundle.getText("OO_RevueProcessus_champ_dateProchaine") + " : ");

		LS_Revues.addMouseListener(tableMouseListener);
		LS_Revues.getSelectionModel().addListSelectionListener(ListenerSurTable);
		
		DateModel df = new DateModel(Bundle.formatDate);
		ES_Date.setFormatterFactory(new DefaultFormatterFactory(df));
		ES_Date.setPreferredSize(new Dimension(100,25));
		
		ES_ProchaineDate.setFormatterFactory(new DefaultFormatterFactory(df));
		ES_ProchaineDate.setPreferredSize(new Dimension(100,25));
		
		//BP_Ajouter.setPreferredSize(BP_Supprimer.getPreferredSize());

		try
		{
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1);
			BP_Modifier.setEnabled(C_Utilisateur.getRole() == 1 && indiceRevue != -1);
			BP_Supprimer.setEnabled(C_Utilisateur.getRole() == 1);
			ES_Action.setEditable(C_Utilisateur.getRole() == 1);
			ES_Decision.setEditable(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
		
		//Split
		horizontalSplit.setContinuousLayout(true);
		horizontalSplit.setLeftComponent(SC_Liste);
		horizontalSplit.setRightComponent(SC_Ajout);
		horizontalSplit.setDividerLocation(90);
	}

	/**
	 * Mise en place des éléments.
	 */
	public void operationSurPanel()
	{
		//Decision
		SC_Decision.setLayout(layoutDecision);
		SC_Decision.add(STC_Decision, BorderLayout.NORTH);
		SC_ScrollDecision.setViewportView(ES_Decision);
		SC_Decision.add(SC_ScrollDecision, BorderLayout.CENTER);
		
		//Prochaine revue
		SC_GroupeProchaineRevue.setLayout(layoutGroupeProchaineRevue);
		SC_GroupeProchaineRevue.add(SC_ProchaineRevue, BorderLayout.WEST);
		
		SC_ProchaineRevue.setLayout(layoutProchaineRevue);
		SC_ProchaineRevue.add(SC_LabelProchaineRevue, BorderLayout.WEST);
		SC_ProchaineRevue.add(SC_ChampProchaineRevue, BorderLayout.CENTER);

		SC_LabelProchaineRevue.setLayout(layoutLabelProchaineRevue);
		SC_LabelProchaineRevue.add(STC_ProchaineDate, BorderLayout.CENTER);
		
		SC_ChampProchaineRevue.setLayout(layoutChampProchaineRevue);
		SC_ChampProchaineRevue.add(ES_ProchaineDate, BorderLayout.CENTER);
		
		//Action
		SC_Action.setLayout(layoutAction);
		SC_Action.add(STC_Action, BorderLayout.NORTH);
		SC_ScrollAction.setViewportView(ES_Action);
		SC_Action.add(SC_ScrollAction, BorderLayout.CENTER);
		
		//Saisie
		SC_Saisie.setLayout(layoutSaisie);
		SC_Saisie.add(SC_Decision);
		SC_Saisie.add(SC_Action);
		
		//Date
		SC_LabelDate.setLayout(layoutLabelDate);
		SC_LabelDate.add(STC_Date);
		//SC_LabelDate.add(STC_ProchaineDate);
		
		SC_ChampsDate.setLayout(layoutChampsDate);
		SC_ChampsDate.add(ES_Date);
		//SC_ChampsDate.add(ES_ProchaineDate);
		
		SC_GroupeDate.setLayout(layoutGroupeDate);
		SC_GroupeDate.add(SC_LabelDate, BorderLayout.WEST);
		SC_GroupeDate.add(SC_ChampsDate, BorderLayout.CENTER);
		
		//Groupe date
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(BP_Ajouter, BorderLayout.EAST);
		SC_Bouton.add(BP_Modifier, BorderLayout.WEST);
		
		
		SC_GroupeDateBouton.setLayout(layoutGroupeDateBouton);
		SC_GroupeDateBouton.add(SC_GroupeDate, BorderLayout.WEST);
		SC_GroupeDateBouton.add(SC_Bouton, BorderLayout.EAST);
		
		//Ajout
		SC_Ajout.setBorder(SC_AjoutTitre);
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.add(SC_GroupeDateBouton, BorderLayout.NORTH);
		SC_Ajout.add(SC_Saisie, BorderLayout.CENTER);
		SC_Ajout.add(SC_GroupeProchaineRevue, BorderLayout.SOUTH);
		
		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_ScrollRevues, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);
		SC_ScrollRevues.setViewportView(LS_Revues);

		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(horizontalSplit, BorderLayout.CENTER);
		//SC_Composants.add(SC_Liste, BorderLayout.CENTER);
		//SC_Composants.add(SC_Ajout, BorderLayout.SOUTH);
	}

	/**
	 * Ajout des éléments dans l'onglet
	 */
	public void operationSurFenetre()
	{
		//Affectation du layout de fond
		this.setLayout(layoutFond);

		//Bordures
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);

		//Composants
		this.add(SC_Composants, BorderLayout.CENTER);
	}

	/**
	 * Permet l'ajout d'une nouvelle demande de modification à partir 
	 * des informations données par l'utilisateur.
	 */
	private void doAjouter()
	{
		// Evite la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = true;
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);

		try
		{
			C_RevueProcessus.ajouterRevue(idProcessus,new Date(sfDate.parse(ES_Date.getText()).getTime()),ES_ProchaineDate.getText().equals("")?null:new Date(sfDate.parse(ES_ProchaineDate.getText()).getTime()),ES_Decision.getText(), ES_Action.getText());

			// Réinitialisation des champs
			doRAZ();

			//Remise à jour du modèle
			LS_Revues.setModel(C_RevueProcessus.getModeleRevuesProcessus(idProcessus));
		}
		catch (ParseException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_RevueProcessus_DateNonRemplie"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_RevueProcessus_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}
	
	private void doModifier()
	{
		if (indiceRevue != -1)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;
			SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
	
			try
			{
				C_RevueProcessus.modifierRevue(indiceRevue,idProcessus,new Date(sfDate.parse(ES_Date.getText()).getTime()),ES_ProchaineDate.getText().equals("")?null:new Date(sfDate.parse(ES_ProchaineDate.getText()).getTime()),ES_Decision.getText(), ES_Action.getText());
	
				// Réinitialisation des champs
				doRAZ();
	
				//Remise à jour du modèle
				LS_Revues.setModel(C_RevueProcessus.getModeleRevuesProcessus(idProcessus));
			}
			catch (ParseException e)
			{
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_RevueProcessus_DateNonRemplie"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			}
			catch (ChampsVideException e)
			{
				JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_RevueProcessus_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			}
	
			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}	

	/**
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		if (LS_Revues.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;

			for (int i = 0; i < LS_Revues.getSelectedRowCount(); i++)
			{
				try
				{
					C_RevueProcessus.supprimerRevue(LS_Revues.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_RevueProcessus_ErreurSupprRevue"), Bundle.getText("OO_RevueProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

			// Remise à zéro des zones de saisie
			doRAZ();

			// Déselectionne le ou les elts de la table
			LS_Revues.clearSelection();

			//Remise à jour du modèle
			LS_Revues.setModel(C_RevueProcessus.getModeleRevuesProcessus(idProcessus));

			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}

	/**
	 * Affiche dans la zone de saisie/consultation du detail de la demande
	 * de modification sélectionnée dans la table.
	 */
	private void doAfficheDetailDemande()
	{
		if (LS_Revues.getModel().getRowCount() > 0 && !actionSurTable)
		{
			indiceRevue = LS_Revues.getSelectedRow();
			try
			{
				BP_Modifier.setEnabled(C_Utilisateur.getRole() == 1 && indiceRevue != -1);
			}
			catch (UtilisateurNonIdentifieException e)
			{}
			ES_Date.setText(LS_Revues.getModel().getValueAt(LS_Revues.getSelectedRow(), 0).toString());
			ES_ProchaineDate.setText(LS_Revues.getModel().getValueAt(LS_Revues.getSelectedRow(), 1).toString());
			ES_Decision.setText(LS_Revues.getModel().getValueAt(LS_Revues.getSelectedRow(), 2).toString());
			ES_Action.setText(LS_Revues.getModel().getValueAt(LS_Revues.getSelectedRow(), 3).toString());
		}
		else
		{
			doRAZ();
			LS_Revues.clearSelection();
		}
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}
	
	/**
	 * Remise à zéro des zones de saisie.
	 */
	private void doRAZ()
	{
		indiceRevue = -1;
		try
		{
			BP_Modifier.setEnabled(C_Utilisateur.getRole() == 1 && indiceRevue != -1);
		}
		catch (UtilisateurNonIdentifieException e)
		{}
		ES_Date.setText("");
		ES_ProchaineDate.setText("");
		ES_Action.setText("");
		ES_Decision.setText("");
	}	
}