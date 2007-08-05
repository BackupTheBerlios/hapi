package hapi.presentation.composants.onglets;

import hapi.application.C_HistoriqueModificationsProcessus;
import hapi.application.C_Utilisateur;
import hapi.application.ressources.Bundle;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author Robin EYSSERIC 
 */
public class OO_HistoriqueModificationsProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -6398727266726129500L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;

	private JPanel SC_Composants = null;
	private JPanel SC_Description = null;
	private JScrollPane SC_Scroll = null;
	private JScrollPane SC_ScrollDescription = null;

	private JPanel SC_Detail = null;
	private JPanel SC_Labels = null;
	private JPanel SC_Champs = null;
	private JPanel SC_Supprimer = null;
	private JPanel SC_BoutonsTable = null;
	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private TitledBorder SC_AjoutTitre = null;
	
	private JPanel SC_ChampsBas = null;
	
	private JSplitPane horizontalSplit = null;

	//Composants
	private JLabel STC_DescriptionModification = null;
	private JLabel STC_Composant = null;
	private JLabel STC_DateDemande = null;
	private JLabel STC_VersionModification = null;
	private JLabel STC_Type = null;
	private JLabel STC_Severite = null;

	private JTable LS_Modifications = null;
	private JTextField STV_VersionModification = null;
	private JTextField STV_Composant = null;
	private JTextField STV_DateDemande = null;
	private JTextField STV_Type = null;
	private JTextField STV_Severite = null;
	private JTextArea STV_DescriptionModification = null;

	private JButton BP_Supprimer = null;

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	private BorderLayout layoutDetail = null;
	private GridLayout layoutLabels = null;
	private GridLayout layoutChamps = null;
	private BorderLayout layoutListe = null;
	private BorderLayout layoutBoutonsTable = null;
	private BorderLayout layoutDescription = null;
	private FlowLayout layoutSupprimer = null;
	private BorderLayout layoutChampsBas = null;

	// Identifiant du processus courant
	private String idProcessus = null;

	// Permet de savoir si le modèle de la table est en cours de modification
	// Utile pour savoir s'il faut ou pas afficher la modification en cours sur la table
	private boolean actionSurTable = false;

	//Ecouteur de la fenêtre
	private FocusAdapter ongletListener = new FocusAdapter()
	{
		public void focusGained(FocusEvent e)
		{
			doMAJTable();
		}
	};
	
	TableModelListener modelListener = new TableModelListener()
	{
		public void tableChanged(TableModelEvent e)
		{
			if(!actionSurTable)
				doMAJTable();
		}
	};	

	// Ecouteur de bouton
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
	public OO_HistoriqueModificationsProcessus(String identifiantProcessus)
	{
		super();
		C_HistoriqueModificationsProcessus.reinitModele();
		idProcessus = identifiantProcessus;

		//Création des éléments
		creationElements();

		//Mise à jour des textes
		updateTexte();

		//Appels de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	/**
	 * Création des éléments composant l'onglet.
	 */
	public void creationElements()
	{
		//Création des Panel
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Composants = new JPanel();
		SC_Scroll = new JScrollPane();
		SC_ScrollDescription = new JScrollPane();
		SC_Detail = new JPanel();
		SC_Champs = new JPanel();
		SC_Labels = new JPanel();
		SC_Supprimer = new JPanel();
		SC_BoutonsTable = new JPanel();
		SC_Liste = new JPanel();
		SC_Description = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_HistoriqueModificationProcessus_liste"));
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_HistoriqueModificationProcessus_detail"));
		
		SC_ChampsBas = new JPanel();		
		
		horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

		//Création des composants
		LS_Modifications = new JTable();
				
		LS_Modifications.setModel(C_HistoriqueModificationsProcessus.creerModeleHistoriqueModification(idProcessus));
		LS_Modifications.getModel().addTableModelListener(modelListener);

		STV_Composant = new JTextField();
		STV_DateDemande = new JTextField();
		STV_VersionModification = new JTextField();
		STV_Type = new JTextField();
		STV_Severite = new JTextField();

		STV_DescriptionModification = new JTextArea(6, 100);
		STC_DescriptionModification = new JLabel();
		STC_Composant = new JLabel();
		STC_DateDemande = new JLabel();
		STC_VersionModification = new JLabel();
		STC_Type = new JLabel();
		STC_Severite = new JLabel();
		BP_Supprimer = new JButton();

		//Création des Layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutDetail = new BorderLayout();
		layoutChamps = new GridLayout(5, 0);
		layoutLabels = new GridLayout(5, 0);
		layoutListe = new BorderLayout();
		layoutBoutonsTable = new BorderLayout();
		layoutDescription = new BorderLayout();
		layoutSupprimer = new FlowLayout();
		layoutChampsBas = new BorderLayout();
	}

	public void operationSurBoutons()
	{}

	/**
	 * Initialisation des éléments composant l'onglet.
	 */
	public void operationSurComposants()
	{
		//Detail de la demande
		STC_VersionModification.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_versionModification") + " : ");
		STC_Composant.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_composant") + " : ");
		STC_DescriptionModification.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_description") + " : ");
		STC_DateDemande.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_date") + " : ");
		STC_Type.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_type") + " : ");
		STC_Severite.setText(Bundle.getText("OO_HistoriqueModificationProcessus_champ_severite") + " : ");

		//Bouton de suppression de demande
		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);

		this.addFocusListener(ongletListener);

		LS_Modifications.addMouseListener(tableMouseListener);
		LS_Modifications.getSelectionModel().addListSelectionListener(ListenerSurTable);
		
		//Seul le responsable peut modifier la maturite et les commentaires
		try
		{
			BP_Supprimer.setEnabled(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}

		// Les champs sont en consultation uniquement
		STV_Composant.setEditable(false);
		STV_DateDemande.setEditable(false);
		STV_VersionModification.setEditable(false);
		STV_DescriptionModification.setEditable(false);
		STV_Type.setEditable(false);
		STV_Severite.setEditable(false);
		
		//Split
		horizontalSplit.setContinuousLayout(true);
		horizontalSplit.setLeftComponent(SC_Liste);
		horizontalSplit.setRightComponent(SC_ChampsBas);
		horizontalSplit.setDividerLocation(90);
	}

	/**
	 * Mise en place des éléments.
	 */
	public void operationSurPanel()
	{
		//Description de la demande de modification
		SC_Description.setLayout(layoutDescription);
		SC_Description.add(STC_DescriptionModification, BorderLayout.NORTH);
		SC_Description.add(SC_ScrollDescription, BorderLayout.CENTER);
		SC_ScrollDescription.setViewportView(STV_DescriptionModification);

		// Détail de la demande
		SC_Labels.setLayout(layoutLabels);
		SC_Labels.add(STC_VersionModification);
		SC_Labels.add(STC_Composant);
		SC_Labels.add(STC_DateDemande);
		SC_Labels.add(STC_Type);
		SC_Labels.add(STC_Severite);

		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(STV_VersionModification);
		SC_Champs.add(STV_Composant);
		SC_Champs.add(STV_DateDemande);
		SC_Champs.add(STV_Type);
		SC_Champs.add(STV_Severite);

		SC_Detail.setLayout(layoutDetail);
		SC_Detail.add(SC_Labels, BorderLayout.WEST);
		SC_Detail.add(SC_Champs, BorderLayout.CENTER);
		
		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		SC_BoutonsTable.setLayout(layoutBoutonsTable);
		SC_BoutonsTable.add(BP_Supprimer, BorderLayout.NORTH);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_Scroll, BorderLayout.CENTER);
		SC_Liste.add(SC_BoutonsTable, BorderLayout.EAST);
		SC_Scroll.setViewportView(LS_Modifications);
		
		SC_ChampsBas.setBorder(SC_AjoutTitre);
		SC_ChampsBas.setLayout(layoutChampsBas);
		SC_ChampsBas.add(SC_Detail, BorderLayout.NORTH);
		SC_ChampsBas.add(SC_Description, BorderLayout.CENTER);

		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(horizontalSplit, BorderLayout.CENTER);
		//SC_Composants.add(SC_Liste, BorderLayout.CENTER);
		//SC_Composants.add(SC_Detail, BorderLayout.SOUTH);
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
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		// Evite la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = true;
		
		if(LS_Modifications.getSelectedRowCount() == 1)
		{
			try
			{
				C_HistoriqueModificationsProcessus.supprimerModification(LS_Modifications.getSelectedRow(), idProcessus);
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_HistoriqueModificationProcessus_ErreurSupprModif"), Bundle.getText("OO_HistoriqueModificationProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			for (int i = 0; i < LS_Modifications.getSelectedRowCount(); i++)
			{
				try
				{
					C_HistoriqueModificationsProcessus.supprimerModification(LS_Modifications.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_HistoriqueModificationProcessus_ErreurSupprModif"), Bundle.getText("OO_HistoriqueModificationProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		}
		// Remise à zéro des zones de saisie
		doRAZ();
		
		// Déselectionne le ou les elts de la table
		LS_Modifications.clearSelection();
		
		//Remise à jour du modèle
		C_HistoriqueModificationsProcessus.reinitModele();
		LS_Modifications.setModel(C_HistoriqueModificationsProcessus.creerModeleHistoriqueModification(idProcessus));
		LS_Modifications.getModel().addTableModelListener(modelListener);
		
		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}

	/**
	 * Affiche dans la zone de saisie/consultation du detail de la demande
	 * de modification sélectionnée dans la table.
	 */
	private void doAfficheDetailDemande()
	{
		if (LS_Modifications.getModel().getRowCount() > 0 && LS_Modifications.getSelectedRowCount()>0)
		{
			STV_VersionModification.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 0).toString());
			STV_Composant.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 1).toString());
			STV_DescriptionModification.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 2).toString());
			STV_DateDemande.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 3).toString());
			STV_Type.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 4).toString());
			STV_Severite.setText(LS_Modifications.getModel().getValueAt(LS_Modifications.getSelectedRow(), 5).toString());
		}
		else
		{
			doRAZ();
			LS_Modifications.clearSelection();
		}
	}

	/**
	 * Remise à zéro des zones de saisie.
	 */
	private void doRAZ()
	{
		STV_Composant.setText("");
		STV_DescriptionModification.setText("");
		STV_DateDemande.setText("");
		STV_VersionModification.setText("");
		STV_Type.setText("");
		STV_Severite.setText("");
	}

	/**
	 * Mise à jour du modèle de la table à partir des données contenues en base.
	 */
	private void doMAJTable()
	{
		// Evite la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = true;

		//Remise à jour du modèle
		LS_Modifications.setModel(C_HistoriqueModificationsProcessus.getModeleHistoriqueModification(idProcessus));

		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}
}