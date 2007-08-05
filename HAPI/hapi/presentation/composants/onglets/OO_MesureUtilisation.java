/*
 * Créé le 30 sept. 2005
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_Utilisateur;
import hapi.application.indicateurs.C_MesureUtilisation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.modele.DecimalModel;
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
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Cédric
 */
public class OO_MesureUtilisation extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -8208414950845114971L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;

	private JPanel SC_Ajout = null;
	private TitledBorder SC_AjoutTitre = null;
	private JPanel SC_LabelUtilisation = null;
	private JPanel SC_ChampsUtilisation = null;
	private JPanel SC_GroupeUtilisation = null;
	private JPanel SC_GroupeBouton = null;
	private JPanel SC_GroupeUtilisationBouton = null;
	private JPanel SC_Bouton = null;

	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private JPanel SC_Supprimer = null;

	//Composants
	private JScrollPane SC_ScrollUtilisation = null;

	private JTable LS_Utilisation = null;
	private JButton BP_Supprimer = null;

	private JButton BP_Ajouter = null;
	private JButton BP_RAZ = null;
	private JLabel STC_Projet = null;
	private JComboBox LD_Projet = null;
	private JLabel STC_NombrePT = null;
	private JSpinner ES_NombrePT = null;
	private JLabel STC_PourPT = null;
	private JFormattedTextField ES_PourPT = null;
	private JLabel STC_NombreAct = null;
	private JSpinner ES_NombreAct = null;
	private JLabel STC_PourAct = null;
	private JFormattedTextField ES_PourAct = null;
	private JTextField ES_Projet = null;

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;

	private BorderLayout layoutAjout = null;
	private GridLayout layoutLabelUtilisation = null;
	private GridLayout layoutChampsUtilisation = null;
	private BorderLayout layoutGroupeUtilisation = null;
	private BorderLayout layoutGroupeUtilisationBouton = null;
	private GridLayout layoutGroupeBouton = null;
	private BorderLayout layoutBouton = null;

	private BorderLayout layoutListe = null;
	private BorderLayout layoutSupprimer = null;

	// Identifiant du processus courant
	private String idProcessus = null;

	// Permet de savoir si le modèle de la table est en cours de modification
	// Utile pour savoir s'il faut ou pas afficher la modification en cours sur
	// la table
	private boolean actionSurTable = false;

	//Ecouteurs de la fenêtre
	private ActionListener actionAjouter = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAjouter();
		}
	};
	private ActionListener actionRAZ = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doRAZ();
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
			doAfficheDetailMesure();
		}
	};

	private ListSelectionListener ListenerSurTable = new ListSelectionListener()
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if (actionSurTable == false)
			{
				doAfficheDetailMesure();
			}
		}
	};
	private ChangeListener actionChangerPT = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChangePT();
		}
	};
	private ChangeListener actionChangerAct = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChangeAct();
		}
	};

	/**
	 * Création de l'onglet premettant de gérer les demandes de modification
	 * concernant un processus.
	 * 
	 * @param identifiantProcessus : l'id du processus sélectionné.
	 */
	public OO_MesureUtilisation(JFrame fenetreAppel, String identifiantProcessus)
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
		SC_LabelUtilisation = new JPanel();
		SC_ChampsUtilisation = new JPanel();
		SC_GroupeUtilisation = new JPanel();
		SC_GroupeUtilisationBouton = new JPanel();
		SC_Bouton = new JPanel();
		SC_GroupeBouton = new JPanel();
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_MesureUtilisation_detail"));

		SC_Liste = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_MesureUtilisation_liste"));
		SC_Supprimer = new JPanel();

		//Composants
		SC_ScrollUtilisation = new JScrollPane();

		LS_Utilisation = new JTable(C_MesureUtilisation.getModeleMesureUtilisationProcessus(idProcessus));
		BP_Supprimer = new JButton();

		BP_Ajouter = new JButton();
		BP_RAZ = new JButton();
		STC_Projet = new JLabel();
		LD_Projet = new JComboBox();
		STC_NombrePT = new JLabel();
		ES_NombrePT = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		STC_PourPT = new JLabel();
		ES_PourPT = new JFormattedTextField(new DecimalModel(Bundle.formatDecimalCourt));
		STC_NombreAct = new JLabel();
		ES_NombreAct = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		STC_PourAct = new JLabel();
		ES_PourAct = new JFormattedTextField(new DecimalModel(Bundle.formatDecimalCourt));
		ES_Projet = new JTextField();

		//Layouts
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();

		layoutAjout = new BorderLayout();
		layoutLabelUtilisation = new GridLayout(5, 0);
		layoutChampsUtilisation = new GridLayout(5, 0);
		layoutGroupeUtilisation = new BorderLayout();
		layoutGroupeUtilisationBouton = new BorderLayout();
		layoutGroupeBouton = new GridLayout(2,0);
		layoutBouton = new BorderLayout();

		layoutListe = new BorderLayout();
		layoutSupprimer = new BorderLayout();
	}

	public void operationSurBoutons()
	{
		//Boutons
		BP_Ajouter.setText(Bundle.getText("Ajouter"));
		BP_Ajouter.setMnemonic(Bundle.getChar("Ajouter_mne"));
		BP_Ajouter.addActionListener(actionAjouter);

		//Bouton de suppression
		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);
		
		BP_RAZ.setText(Bundle.getText("Reinit"));
		BP_RAZ.setMnemonic(Bundle.getChar("Reinit_mne"));
		BP_RAZ.addActionListener(actionRAZ);
	}

	/**
	 * Initialisation des éléments composant l'onglet.
	 */
	public void operationSurComposants()
	{
		//Detail
		STC_Projet.setText(Bundle.getText("OO_MesureUtilisation_champ_projet") + " : ");
		STC_NombrePT.setText(Bundle.getText("OO_MesureUtilisation_champ_nombrePT") + " : ");
		STC_PourPT.setText(Bundle.getText("OO_MesureUtilisation_champ_pourPT") + " : ");
		STC_NombreAct.setText(Bundle.getText("OO_MesureUtilisation_champ_nombreAct") + " : ");
		STC_PourAct.setText(Bundle.getText("OO_MesureUtilisation_champ_pourAct") + " : ");

		LS_Utilisation.addMouseListener(tableMouseListener);
		LS_Utilisation.getSelectionModel().addListSelectionListener(ListenerSurTable);

		LD_Projet.setModel(C_MesureUtilisation.getModeleProjets(idProcessus));

		ES_PourAct.setEditable(false);
		ES_PourPT.setEditable(false);
		
		ES_NombreAct.addChangeListener(actionChangerAct);
		ES_NombrePT.addChangeListener(actionChangerPT);

		//BP_Ajouter.setPreferredSize(BP_Supprimer.getPreferredSize());
		//BP_RAZ.setPreferredSize(BP_Supprimer.getPreferredSize());
		BP_Supprimer.setPreferredSize(BP_RAZ.getPreferredSize());
		
		ES_Projet.setPreferredSize(LD_Projet.getPreferredSize());
		ES_Projet.setEditable(false);

		try
		{
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1 && LD_Projet.getModel().getSize() != 0);
			BP_Supprimer.setEnabled(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
		
		doRAZ();

	}

	/**
	 * Mise en place des éléments.
	 */
	public void operationSurPanel()
	{
		//Date
		SC_LabelUtilisation.setLayout(layoutLabelUtilisation);
		SC_LabelUtilisation.add(STC_Projet);
		SC_LabelUtilisation.add(STC_NombrePT);
		SC_LabelUtilisation.add(STC_PourPT);
		SC_LabelUtilisation.add(STC_NombreAct);
		SC_LabelUtilisation.add(STC_PourAct);

		SC_ChampsUtilisation.setLayout(layoutChampsUtilisation);
		SC_ChampsUtilisation.add(LD_Projet);
		SC_ChampsUtilisation.add(ES_NombrePT);
		SC_ChampsUtilisation.add(ES_PourPT);
		SC_ChampsUtilisation.add(ES_NombreAct);
		SC_ChampsUtilisation.add(ES_PourAct);

		SC_GroupeUtilisation.setLayout(layoutGroupeUtilisation);
		SC_GroupeUtilisation.add(SC_LabelUtilisation, BorderLayout.WEST);
		SC_GroupeUtilisation.add(SC_ChampsUtilisation, BorderLayout.CENTER);

		//Groupe date
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(SC_GroupeBouton, BorderLayout.NORTH);
		
		SC_GroupeBouton.setLayout(layoutGroupeBouton);
		SC_GroupeBouton.add(BP_Ajouter);
		SC_GroupeBouton.add(BP_RAZ);

		SC_GroupeUtilisationBouton.setLayout(layoutGroupeUtilisationBouton);
		SC_GroupeUtilisationBouton.add(SC_GroupeUtilisation, BorderLayout.WEST);
		SC_GroupeUtilisationBouton.add(SC_Bouton, BorderLayout.EAST);

		//Ajout
		SC_Ajout.setBorder(SC_AjoutTitre);
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.add(SC_GroupeUtilisationBouton, BorderLayout.CENTER);

		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_ScrollUtilisation, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);
		SC_ScrollUtilisation.setViewportView(LS_Utilisation);

		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Liste, BorderLayout.CENTER);
		SC_Composants.add(SC_Ajout, BorderLayout.SOUTH);
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

	private void doAjouter()
	{
		// Evite la mise à jour de la zone de consultation lors de la
		// modification de la table
		actionSurTable = true;

		try
		{
			String valPT = ES_PourPT.getText();
			valPT = valPT.replaceAll(",",".");
			
			String valAct = ES_PourAct.getText();
			valAct = valAct.replaceAll(",",".");
			
			C_MesureUtilisation.ajouterMesure(idProcessus, LD_Projet.getSelectedIndex(), ES_NombrePT.getValue().toString(), new Double(valPT).doubleValue(), ES_NombreAct.getValue().toString(), new Double(valAct).doubleValue());

			// Réinitialisation des champs
			doRAZ();

			//Remise à jour du modèle
			LS_Utilisation.setModel(C_MesureUtilisation.getModeleMesureUtilisationProcessus(idProcessus));
			LD_Projet.setModel(C_MesureUtilisation.getModeleProjets(idProcessus));
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1 && LD_Projet.getModel().getSize() != 0);
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureUtilisation_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		// Réactive la mise à jour de la zone de consultation lors de la
		// modification de la table
		actionSurTable = false;
	}

	/**
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		if (LS_Utilisation.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la
			// modification de la table
			actionSurTable = true;

			for (int i = 0; i < LS_Utilisation.getSelectedRowCount(); i++)
			{
				try
				{
					C_MesureUtilisation.supprimerMesureUtilisation(LS_Utilisation.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureUtilisation_ErreurSupprMesure"), Bundle.getText("OO_RevueProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

			// Déselectionne le ou les elts de la table
			LS_Utilisation.clearSelection();

			//Remise à jour du modèle
			LS_Utilisation.setModel(C_MesureUtilisation.getModeleMesureUtilisationProcessus(idProcessus));
			LD_Projet.setModel(C_MesureUtilisation.getModeleProjets(idProcessus));
			try
			{
				BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1 && LD_Projet.getModel().getSize() != 0);
			}
			catch (UtilisateurNonIdentifieException e)
			{}
			
			// Remise à zéro des zones de saisie
			doRAZ();

			// Réactive la mise à jour de la zone de consultation lors de la
			// modification de la table
			actionSurTable = false;
		}
	}

	private void doAfficheDetailMesure()
	{
		if (LS_Utilisation.getModel().getRowCount() > 0 && !actionSurTable && LS_Utilisation.getSelectedRowCount() > 0)
		{
			SC_ChampsUtilisation.removeAll();
			SC_ChampsUtilisation.add(ES_Projet);
			SC_ChampsUtilisation.add(ES_NombrePT);
			SC_ChampsUtilisation.add(ES_PourPT);
			SC_ChampsUtilisation.add(ES_NombreAct);
			SC_ChampsUtilisation.add(ES_PourAct);
			BP_RAZ.setEnabled(true);
			BP_Ajouter.setEnabled(false);			
			updateUI();
			ES_Projet.setText(LS_Utilisation.getModel().getValueAt(LS_Utilisation.getSelectedRow(), 0).toString());
			ES_NombrePT.setValue(LS_Utilisation.getModel().getValueAt(LS_Utilisation.getSelectedRow(), 1));
			ES_PourPT.setValue(LS_Utilisation.getModel().getValueAt(LS_Utilisation.getSelectedRow(), 2));
			ES_NombreAct.setValue(LS_Utilisation.getModel().getValueAt(LS_Utilisation.getSelectedRow(), 3));
			ES_PourAct.setValue(LS_Utilisation.getModel().getValueAt(LS_Utilisation.getSelectedRow(), 4));
		}
		else
		{
			doRAZ();
			LS_Utilisation.clearSelection();
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
		SC_ChampsUtilisation.removeAll();
		SC_ChampsUtilisation.add(LD_Projet);
		SC_ChampsUtilisation.add(ES_NombrePT);
		SC_ChampsUtilisation.add(ES_PourPT);
		SC_ChampsUtilisation.add(ES_NombreAct);
		SC_ChampsUtilisation.add(ES_PourAct);
		LS_Utilisation.clearSelection();
		BP_RAZ.setEnabled(false);
		try
		{
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1 && LD_Projet.getModel().getSize() != 0);
		}
		catch (UtilisateurNonIdentifieException e)
		{}
		updateUI();
		if (LD_Projet.getModel().getSize() > 0)
			LD_Projet.setSelectedIndex(0);
		ES_NombreAct.setValue(new Integer(0));
		ES_NombrePT.setValue(new Integer(0));
		ES_PourAct.setValue(new Double(0));
		ES_PourPT.setValue(new Double(0));
	}

	private void doChangeAct()
	{
		/*boolean doitUtiliserCourant;
		try
		{
			doitUtiliserCourant = BD_DemanderDPE.doitUtiliserDPECourant(fenetreAppel, idProcessus, C_MesureUtilisation.getClefProjet(LD_Projet.getSelectedIndex()));

			if (((Integer) ES_NombrePT.getValue()).intValue() > 0)
			{
				double Val1 = ((Integer) ES_NombrePT.getValue()).intValue();
				double Val2 = 0;
				if (doitUtiliserCourant)
				{
					Val2 = C_Activite.getActivitesDuProcessus(idProcessus).size();
				}
				else
				{
					Val2 = C_ActiviteTemporaire.size();
				}

				double Multi = Val1 / Val2;
				ES_PourAct.setText((new Double(Multi * 100)).toString());
			}
		}
		catch (StopActionException e)
		{}*/
		
		if (LD_Projet.getModel().getSize() > 0)
		{
			double val1 = ((Integer) ES_NombreAct.getValue()).intValue();
			double val2 = C_Processus.getMesureRepresentation(idProcessus,C_ExecutionProcessus.getExecutionProcessus(idProcessus,C_MesureUtilisation.getClefProjet(LD_Projet.getSelectedIndex())).getVersionProcessus()).getNbAct();
			double multi = val1 / val2; 
			ES_PourAct.setValue(new Double(multi * 100));
		}
		else
			ES_PourAct.setText("");
	}

	private void doChangePT()
	{
		/*boolean doitUtiliserCourant;
		try
		{
			doitUtiliserCourant = BD_DemanderDPE.doitUtiliserDPECourant(fenetreAppel, idProcessus, C_MesureUtilisation.getClefProjet(LD_Projet.getSelectedIndex()));

			//Calcul du nombre d'interfaces requies
			Collection lesInterfaces = null;
			//Récupération des interfaces
			if (doitUtiliserCourant)
				lesInterfaces = C_Interface.getInterfacesDuProcessus(idProcessus).values();
			else
				lesInterfaces = C_InterfaceTemporaire.getInterfaces().values();
			//Recherche des interfaces pour chaque composant
			int valReq = 0;
			for (Iterator it = lesInterfaces.iterator(); it.hasNext();)
			{
				E_Interface uneInterface = (E_Interface) it.next();

				if (!uneInterface.getInterfaceRequiseComposant().equals(""))
					valReq += uneInterface.getInterfaceProduit().size();
			}

			if (((Integer) ES_NombrePT.getValue()).intValue() > 0)
			{
				double Val1 = ((Integer) ES_NombrePT.getValue()).intValue();
				double Val2 = 0;
				if (doitUtiliserCourant)
				{
					Val2 = C_Produit.getProduitsDuProcessus(idProcessus).size() - valReq;
				}
				else
				{
					Val2 = C_ProduitTemporaire.size() - valReq;
				}

				double Multi = Val1 / Val2;
				ES_PourPT.setText((new Double(Multi * 100)).toString());
			}
		}
		catch (StopActionException e)
		{}*/
		
		if (LD_Projet.getModel().getSize() > 0)
		{
			double val1 = ((Integer) ES_NombrePT.getValue()).intValue();
			double val2 = C_Processus.getMesureRepresentation(idProcessus,C_ExecutionProcessus.getExecutionProcessus(idProcessus,C_MesureUtilisation.getClefProjet(LD_Projet.getSelectedIndex())).getVersionProcessus()).getNbProd(); 
			double multi = (val1/val2); 
			ES_PourPT.setValue(new Double(multi * 100));
		}
		else
			ES_PourPT.setText("");
	}
}