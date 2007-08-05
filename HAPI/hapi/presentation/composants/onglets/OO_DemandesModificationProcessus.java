package hapi.presentation.composants.onglets;

import hapi.application.C_DemandesModificationProcessus;
import hapi.application.C_Utilisateur;
import hapi.application.ressources.Bundle;
import hapi.exception.ChampsVideException;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Robin EYSSERIC 
 */
public class OO_DemandesModificationProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -7854952337936246098L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;
	
	private TitledBorder SC_AjoutTitre = null;
	private JPanel SC_ChampsBas = null;
	private JPanel SC_Ajout = null;
	private JPanel SC_Description = null;
	private JPanel SC_BoutonsTable = null;
	private JScrollPane SC_ScrollDescription = null;
	
	private JPanel SC_LabelDemande = null;
	private JPanel SC_ChampsDemande = null;
	private JPanel SC_GroupeDemande = null;
	private JPanel SC_GroupeBouton = null;
	private JPanel SC_GroupeDemandeBouton = null;
	private JPanel SC_Bouton = null;	
	

	private TitledBorder SC_ListeTitre = null;
	private JPanel SC_Liste = null;
	private JScrollPane SC_Scroll = null;
	private JPanel SC_Supprimer = null;
	private JPanel SC_Traiter = null;
	
	private JSplitPane horizontalSplit = null;

	//Composants
	private JTable LS_Demandes = null;
	private JButton BP_Supprimer = null;
	private JButton BP_Archiver = null;
	
	private JLabel STC_Description = null;
	private JTextArea ES_Description = null;
	
	private JButton BP_Ajouter = null;
	private JButton BP_RAZ = null;
	
	private JLabel STC_Composant = null;
	private JComboBox LD_Composant = null;
	private JLabel STC_Type = null;
	private JComboBox LD_Type = null;	
	private JLabel STC_Severite = null;
	private JSpinner ES_Severite = null;	

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;

	private BorderLayout layoutListe = null;
	private BorderLayout layoutBoutonsTable = null;
	private BorderLayout layoutSupprimer = null;	
	
	private BorderLayout layoutAjout = null;
	private BorderLayout layoutChampsBas = null;
	private BorderLayout layoutDescription = null;
	private GridLayout layoutLabelDemande = null;
	private GridLayout layoutChampsDemande = null;
	private BorderLayout layoutGroupeDemande = null;
	private BorderLayout layoutGroupeDemandeBouton = null;
	private GridLayout layoutGroupeBouton = null;
	private BorderLayout layoutBouton = null;	
	
	
	// Identifiant du processus courant
	private String idProcessus = null;

	// Permet de savoir si le modèle de la table est en cours de modification
	// Utile pour savoir s'il faut ou pas afficher la modification en cours sur la table
	private boolean actionSurTable = false;
	private boolean surveillanceAmelioration = false;
	private int nombrePropose = 0;
	private int nombrePris = 0;
	private int presentation = 0;
	private int modele = 0;
	private int documentation = 0;

	//Ecouteurs de la fenêtre
	private ActionListener actionAjouter = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAjouter();
		}
	};

	private ActionListener actionArchiver = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doArchiver();
		}
	};

	private ActionListener actionSupprimer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSupprimer();
		}
	};
	
	private ActionListener actionRAZ = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doRAZ();
		}
	};	

	private MouseWheelListener actionScroll = new MouseWheelListener()
	{
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			doScroll(arg0.getWheelRotation());
		}
	};
	
	private MouseWheelListener actionScroll2 = new MouseWheelListener()
	{
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			doScroll2(arg0.getWheelRotation());
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
	public OO_DemandesModificationProcessus(String identifiantProcessus,boolean surveillanceAmelioration)
	{
		super();

		idProcessus = identifiantProcessus;
		this.surveillanceAmelioration = surveillanceAmelioration;

		//Création des éléments
		creationElements();

		//Mise à jour des textes
		updateTexte();

		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		if (surveillanceAmelioration)
			nombrePropose = LS_Demandes.getModel().getRowCount();
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
		SC_BoutonsTable = new JPanel();
		SC_Scroll = new JScrollPane();
		SC_ScrollDescription = new JScrollPane();
		SC_Ajout = new JPanel();
		SC_Supprimer = new JPanel();
		SC_Traiter = new JPanel();
		SC_Liste = new JPanel();
		SC_Description = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_DemandeModificationProcessus_liste"));
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_DemandeModificationProcessus_detail"));
				
		SC_ChampsBas = new JPanel();
		
		SC_LabelDemande = new JPanel();
		SC_ChampsDemande = new JPanel();
		SC_GroupeDemande = new JPanel();
		SC_GroupeBouton = new JPanel();
		SC_GroupeDemandeBouton = new JPanel();
		SC_Bouton = new JPanel();			
		
		
		horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

		//Création des composants
		LS_Demandes = new JTable(C_DemandesModificationProcessus.getModeleDemandesModification(idProcessus));
		LD_Composant = new JComboBox(C_DemandesModificationProcessus.getModeleComposants(idProcessus));
		LD_Type = new JComboBox(C_DemandesModificationProcessus.getModeleType());

		ES_Description = new JTextArea(6, 100);
		STC_Description = new JLabel();
		STC_Composant = new JLabel();
		BP_Supprimer = new JButton();
		BP_Archiver = new JButton();
		BP_Ajouter = new JButton();
		BP_RAZ = new JButton();
		
		STC_Type = new JLabel();	
		STC_Severite = new JLabel();
		ES_Severite = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));;

		//Création des Layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutAjout = new BorderLayout();
		layoutListe = new BorderLayout();
		layoutBoutonsTable = new BorderLayout();
		layoutDescription = new BorderLayout();
		layoutSupprimer = new BorderLayout();
		layoutChampsBas = new BorderLayout();
		
		layoutLabelDemande = new GridLayout(3,0);
		layoutChampsDemande = new GridLayout(3,0);
		layoutGroupeDemande = new BorderLayout();
		layoutGroupeDemandeBouton = new BorderLayout();
		layoutGroupeBouton = new GridLayout(2,0);
		layoutBouton = new BorderLayout();		
	}

	public void operationSurBoutons()
	{
		//Boutons d'ajout de demande
		BP_Ajouter.setText(Bundle.getText("Ajouter"));
		BP_Ajouter.setMnemonic(Bundle.getChar("Ajouter_mne"));
		BP_Ajouter.addActionListener(actionAjouter);

		//Bouton de suppression de demande
		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);

		BP_Archiver.setText(Bundle.getText("Archiver"));
		BP_Archiver.setMnemonic(Bundle.getChar("Archiver_mne"));
		BP_Archiver.addActionListener(actionArchiver);
		
		BP_RAZ.setText(Bundle.getText("Reinit"));
		BP_RAZ.setMnemonic(Bundle.getChar("Reinit_mne"));
		BP_RAZ.addActionListener(actionRAZ);		
	}

	/**
	 * Initialisation des éléments composant l'onglet.
	 */
	public void operationSurComposants()
	{
		//Detail de la demande
		STC_Description.setText(Bundle.getText("OO_DemandeModificationProcessus_champ_description") + " : ");
		STC_Composant.setText(Bundle.getText("OO_DemandeModificationProcessus_champ_composant") + " : ");
		STC_Type.setText(Bundle.getText("OO_DemandeModificationProcessus_champ_type") + " : ");
		STC_Severite.setText(Bundle.getText("OO_DemandeModificationProcessus_champ_severite") + " : ");

		LD_Composant.addMouseWheelListener(actionScroll);
		LD_Type.addMouseWheelListener(actionScroll2);

		LS_Demandes.addMouseListener(tableMouseListener);
		LS_Demandes.getSelectionModel().addListSelectionListener(ListenerSurTable);

		//Seul le responsable peut modifier la maturite et les commentaires
		try
		{
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1);
			BP_Supprimer.setEnabled(C_Utilisateur.getRole() == 1);
			BP_Archiver.setEnabled(C_Utilisateur.getRole() == 1);
			LD_Composant.setEnabled(C_Utilisateur.getRole() == 1);
			LD_Type.setEnabled(C_Utilisateur.getRole() == 1);
			ES_Severite.setEnabled(C_Utilisateur.getRole() == 1);
			ES_Description.setEditable(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
		
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
		SC_LabelDemande.setLayout(layoutLabelDemande);
		SC_LabelDemande.add(STC_Composant);
		SC_LabelDemande.add(STC_Type);
		SC_LabelDemande.add(STC_Severite);

		SC_ChampsDemande.setLayout(layoutChampsDemande);
		SC_ChampsDemande.add(LD_Composant);
		SC_ChampsDemande.add(LD_Type);
		SC_ChampsDemande.add(ES_Severite);

		SC_GroupeDemande.setLayout(layoutGroupeDemande);
		SC_GroupeDemande.add(SC_LabelDemande, BorderLayout.WEST);
		SC_GroupeDemande.add(SC_ChampsDemande, BorderLayout.CENTER);
		
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(SC_GroupeBouton, BorderLayout.NORTH);
		
		SC_GroupeBouton.setLayout(layoutGroupeBouton);
		SC_GroupeBouton.add(BP_Ajouter);
		SC_GroupeBouton.add(BP_RAZ);

		SC_GroupeDemandeBouton.setLayout(layoutGroupeDemandeBouton);
		SC_GroupeDemandeBouton.add(SC_GroupeDemande, BorderLayout.WEST);
		SC_GroupeDemandeBouton.add(SC_Bouton, BorderLayout.EAST);
		
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.add(SC_GroupeDemandeBouton, BorderLayout.CENTER);
		
		//Description de la demande de modification
		SC_Description.setLayout(layoutDescription);
		SC_Description.add(STC_Description, BorderLayout.NORTH);
		SC_Description.add(SC_ScrollDescription, BorderLayout.CENTER);
		SC_ScrollDescription.setViewportView(ES_Description);

		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		SC_Traiter.setLayout(layoutSupprimer);
		SC_Traiter.add(BP_Archiver, BorderLayout.NORTH);

		SC_BoutonsTable.setLayout(layoutBoutonsTable);
		SC_BoutonsTable.add(BP_Supprimer, BorderLayout.NORTH);
		SC_BoutonsTable.add(SC_Traiter, BorderLayout.CENTER);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_Scroll, BorderLayout.CENTER);
		SC_Liste.add(SC_BoutonsTable, BorderLayout.EAST);
		SC_Scroll.setViewportView(LS_Demandes);
		
		SC_ChampsBas.setBorder(SC_AjoutTitre);
		SC_ChampsBas.setLayout(layoutChampsBas);
		SC_ChampsBas.add(SC_Ajout, BorderLayout.NORTH);
		SC_ChampsBas.add(SC_Description, BorderLayout.CENTER);

		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(horizontalSplit, BorderLayout.CENTER);
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

		try
		{
			C_DemandesModificationProcessus.ajouterDemande(idProcessus, LD_Composant.getSelectedItem().toString(), ES_Description.getText(), LD_Type.getSelectedItem().toString(), ((Integer)ES_Severite.getValue()).intValue());

			// Réinitialisation des champs
			doRAZ();

			//Remise à jour du modèle
			LS_Demandes.setModel(C_DemandesModificationProcessus.getModeleDemandesModification(idProcessus));
			
			if (surveillanceAmelioration)
				nombrePropose++;
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_DemandeModificationProcessus_DescriptionDemandeNonRemplie"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_DemandeModificationProcessus_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}

	private void doArchiver()
	{
		if (LS_Demandes.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;

			try
			{
				for (int i = 0; i < LS_Demandes.getSelectedRowCount(); i++)
				{
					// Ajout de la modification dans la table de l'historique des versions
					C_DemandesModificationProcessus.archiverDemande(idProcessus, LS_Demandes.getSelectedRows()[i]);
					if (surveillanceAmelioration)
					{
						nombrePris++;
						if (LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRows()[i], 3).toString().equals(Bundle.getText("OO_DemandeModificationProcessus_TypePresentation")))
							presentation++;
						if (LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRows()[i], 3).toString().equals(Bundle.getText("OO_DemandeModificationProcessus_TypeDocumentation")))
							documentation++;
						if (LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRows()[i], 3).toString().equals(Bundle.getText("OO_DemandeModificationProcessus_TypeModele")))
							modele++;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_DemandeModificationProcessus_ErreurArchivageDemandes"), Bundle.getText("OO_DemandeModificationProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
			}

			// Suppression des demandes sélectionnées dans la table des demandes de modification
			doSupprimer();

			// Remise à zéro des zones de saisie
			doRAZ();

			// Déselectionne le ou les elts de la table
			LS_Demandes.clearSelection();

			//Remise à jour du modèle
			LS_Demandes.setModel(C_DemandesModificationProcessus.getModeleDemandesModification(idProcessus));

			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}

	/**
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		if (LS_Demandes.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;

			for (int i = 0; i < LS_Demandes.getSelectedRowCount(); i++)
			{
				try
				{
					C_DemandesModificationProcessus.supprimerDemande(LS_Demandes.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_DemandeModificationProcessus_ErreurSupprDemandes"), Bundle.getText("OO_DemandeModificationProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

			// Remise à zéro des zones de saisie
			doRAZ();

			// Déselectionne le ou les elts de la table
			LS_Demandes.clearSelection();

			//Remise à jour du modèle
			LS_Demandes.setModel(C_DemandesModificationProcessus.getModeleDemandesModification(idProcessus));

			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}

	/**
	 * Réalisation du scroll de la souris sur la liste déroulante des composants.
	 */
	private void doScroll(int nbLignes)
	{
		try
		{
			if (LD_Composant.getSelectedIndex() + nbLignes < 0)
				LD_Composant.setSelectedIndex(0);
			else
				LD_Composant.setSelectedIndex(LD_Composant.getSelectedIndex() + nbLignes);
		}
		catch (IllegalArgumentException e)
		{
			if (nbLignes > 0)
				LD_Composant.setSelectedIndex(LD_Composant.getItemCount() - 1);
			else
				LD_Composant.setSelectedIndex(0);
		}
	}
	
	private void doScroll2(int nbLignes)
	{
		try
		{
			if (LD_Type.getSelectedIndex() + nbLignes < 0)
				LD_Type.setSelectedIndex(0);
			else
				LD_Type.setSelectedIndex(LD_Type.getSelectedIndex() + nbLignes);
		}
		catch (IllegalArgumentException e)
		{
			if (nbLignes > 0)
				LD_Type.setSelectedIndex(LD_Type.getItemCount() - 1);
			else
				LD_Type.setSelectedIndex(0);
		}
	}	

	/**
	 * Affiche dans la zone de saisie/consultation du detail de la demande
	 * de modification sélectionnée dans la table.
	 */
	private void doAfficheDetailDemande()
	{
		if (LS_Demandes.getModel().getRowCount() > 0 && !actionSurTable)
		{
			LD_Composant.setSelectedItem(LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRow(),1 ));
			ES_Description.setText(LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRow(), 2).toString());
			LD_Type.setSelectedItem(LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRow(), 3));
			ES_Severite.setValue(LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRow(), 4).toString().equals("")?new Integer(0):new Integer(LS_Demandes.getModel().getValueAt(LS_Demandes.getSelectedRow(), 4).toString()));
		}
		else
		{
			doRAZ();
			LS_Demandes.clearSelection();
		}
	}

	/**
	 * Remise à zéro des zones de saisie.
	 */
	private void doRAZ()
	{
		LD_Composant.setSelectedIndex(0);
		LD_Type.setSelectedIndex(0);
		ES_Description.setText("");
		ES_Severite.setValue(new Integer(0));
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}
	
	/**
	 * @return Renvoie documentation.
	 */
	public int getDocumentation()
	{
		return documentation;
	}
	
	/**
	 * @return Renvoie modele.
	 */
	public int getModele()
	{
		return modele;
	}
	
	/**
	 * @return Renvoie nombrePris.
	 */
	public int getNombrePris()
	{
		return nombrePris;
	}
	
	/**
	 * @return Renvoie nombrePropose.
	 */
	public int getNombrePropose()
	{
		return nombrePropose;
	}
	
	/**
	 * @return Renvoie presentation.
	 */
	public int getPresentation()
	{
		return presentation;
	}
}