/*
 * Créé le 30 sept. 2005
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_Utilisateur;
import hapi.application.indicateurs.C_MesureFormation;
import hapi.application.modele.DateModel;
import hapi.application.modele.DecimalModel;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Cédric
 */
public class OO_MesureFormation extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 1615517389788529335L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;
	
	private JPanel SC_Ajout = null;
	private TitledBorder SC_AjoutTitre = null;
	private JPanel SC_LabelFormation = null;
	private JPanel SC_ChampsFormation = null;
	private JPanel SC_GroupeFormation = null;
	private JPanel SC_GroupeFormationBouton = null;
	private JPanel SC_Bouton = null;
	
	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private JPanel SC_Supprimer = null;
	
	//Composants
	private JScrollPane SC_ScrollFormation = null;
	
	private JTable LS_Formation = null;
	private JButton BP_Supprimer = null;
	
	private JButton BP_Ajouter = null;
	private JLabel STC_Date = null;
	private JFormattedTextField ES_Date = null;
	private JLabel STC_EffPro = null;
	private JFormattedTextField ES_EffPro = null;
	private JLabel STC_EffMes = null;
	private JFormattedTextField ES_EffMes = null;
	private JLabel STC_Note = null;
	private JSpinner ES_Note = null;	

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	
	private BorderLayout layoutAjout = null;
	private GridLayout layoutLabelFormation = null;
	private GridLayout layoutChampsFormation = null;
	private BorderLayout layoutGroupeFormation = null;
	private BorderLayout layoutGroupeFormationBouton = null;
	private BorderLayout layoutBouton = null;
	
	private BorderLayout layoutListe = null;
	private BorderLayout layoutSupprimer = null;

	// Identifiant du processus courant
	private String idProcessus = null;

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

	/** 
	 * Création de l'onglet premettant de gérer les demandes de modification
	 * concernant un processus.
	 * 
	 * @param identifiantProcessus : l'id du processus sélectionné.
	 */
	public OO_MesureFormation(String identifiantProcessus)
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
		SC_LabelFormation = new JPanel();
		SC_ChampsFormation = new JPanel();
		SC_GroupeFormation = new JPanel();
		SC_GroupeFormationBouton = new JPanel();
		SC_Bouton = new JPanel();
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_MesureFormation_detail"));
		
		SC_Liste = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_MesureFormation_liste"));
		SC_Supprimer = new JPanel();
		
		//Composants
		SC_ScrollFormation = new JScrollPane();
		
		LS_Formation = new JTable(C_MesureFormation.getModeleMesureFormationProcessus(idProcessus));
		BP_Supprimer = new JButton();
		
		BP_Ajouter = new JButton();
		STC_Date = new JLabel();
		ES_Date = new JFormattedTextField();
		STC_EffPro = new JLabel();
		ES_EffPro = new JFormattedTextField();
		STC_EffMes = new JLabel();
		ES_EffMes = new JFormattedTextField();
		STC_Note = new JLabel();
		ES_Note = new JSpinner(new SpinnerNumberModel(0,0,5,0.1));

		//Layouts
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		
		layoutAjout = new BorderLayout();
		layoutLabelFormation = new GridLayout(4,0);
		layoutChampsFormation = new GridLayout(4,0);
		layoutGroupeFormation = new BorderLayout();
		layoutGroupeFormationBouton = new BorderLayout();
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
	}

	/**
	 * Initialisation des éléments composant l'onglet.
	 */
	public void operationSurComposants()
	{
		//Detail
		STC_Date.setText(Bundle.getText("OO_MesureFormation_champ_date") + " : ");
		STC_EffPro.setText(Bundle.getText("OO_MesureFormation_champ_effPro") + " : ");
		STC_EffMes.setText(Bundle.getText("OO_MesureFormation_champ_effMes") + " : ");
		STC_Note.setText(Bundle.getText("OO_MesureFormation_champ_note") + " : ");

		LS_Formation.addMouseListener(tableMouseListener);
		LS_Formation.getSelectionModel().addListSelectionListener(ListenerSurTable);
		
		DateModel df = new DateModel(Bundle.formatDate);
		ES_Date.setFormatterFactory(new DefaultFormatterFactory(df));
		ES_Date.setPreferredSize(new Dimension(100,25));
		
		DecimalModel df2 = new DecimalModel(Bundle.naturelFormat);
		ES_EffPro.setFormatterFactory(new DefaultFormatterFactory(df2));
		ES_EffMes.setFormatterFactory(new DefaultFormatterFactory(df2));
		ES_EffMes.setHorizontalAlignment(JTextField.RIGHT);
		ES_EffPro.setHorizontalAlignment(JTextField.RIGHT);
		
		BP_Ajouter.setPreferredSize(BP_Supprimer.getPreferredSize());

		try
		{
			BP_Ajouter.setEnabled(C_Utilisateur.getRole() == 1);
			BP_Supprimer.setEnabled(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
		
	}

	/**
	 * Mise en place des éléments.
	 */
	public void operationSurPanel()
	{
		//Date
		SC_LabelFormation.setLayout(layoutLabelFormation);
		SC_LabelFormation.add(STC_Date);
		SC_LabelFormation.add(STC_EffPro);
		SC_LabelFormation.add(STC_EffMes);
		SC_LabelFormation.add(STC_Note);
		
		SC_ChampsFormation.setLayout(layoutChampsFormation);
		SC_ChampsFormation.add(ES_Date);
		SC_ChampsFormation.add(ES_EffPro);
		SC_ChampsFormation.add(ES_EffMes);
		SC_ChampsFormation.add(ES_Note);
		
		SC_GroupeFormation.setLayout(layoutGroupeFormation);
		SC_GroupeFormation.add(SC_LabelFormation, BorderLayout.WEST);
		SC_GroupeFormation.add(SC_ChampsFormation, BorderLayout.CENTER);
		
		//Groupe date
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(BP_Ajouter, BorderLayout.NORTH);
		
		
		SC_GroupeFormationBouton.setLayout(layoutGroupeFormationBouton);
		SC_GroupeFormationBouton.add(SC_GroupeFormation, BorderLayout.WEST);
		SC_GroupeFormationBouton.add(SC_Bouton, BorderLayout.EAST);
		
		//Ajout
		SC_Ajout.setBorder(SC_AjoutTitre);
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.add(SC_GroupeFormationBouton, BorderLayout.CENTER);
		
		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_ScrollFormation, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);
		SC_ScrollFormation.setViewportView(LS_Formation);

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
		// Evite la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = true;
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);

		try
		{
			DecimalFormat df = new DecimalFormat(Bundle.MODEL_DECIMAL_COURT);
			String laValeur = df.format(((Double)ES_Note.getValue()).doubleValue());
			laValeur = laValeur.replaceAll(",",".");
		
			C_MesureFormation.ajouterMesure(idProcessus,new Date(sfDate.parse(ES_Date.getText()).getTime()),ES_EffPro.getText(),ES_EffMes.getText(),new Double(laValeur));

			// Réinitialisation des champs
			doRAZ();

			//Remise à jour du modèle
			LS_Formation.setModel(C_MesureFormation.getModeleMesureFormationProcessus(idProcessus));
		}
		catch (ParseException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureFormation_DateNonRemplie"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureFormation_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}

	/**
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		if (LS_Formation.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;

			for (int i = 0; i < LS_Formation.getSelectedRowCount(); i++)
			{
				try
				{
					C_MesureFormation.supprimerMesureFormation(LS_Formation.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureFormation_ErreurSupprMesure"), Bundle.getText("OO_RevueProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

			// Remise à zéro des zones de saisie
			doRAZ();

			// Déselectionne le ou les elts de la table
			LS_Formation.clearSelection();

			//Remise à jour du modèle
			LS_Formation.setModel(C_MesureFormation.getModeleMesureFormationProcessus(idProcessus));

			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}

	private void doAfficheDetailMesure()
	{
		if (LS_Formation.getModel().getRowCount() > 0 && !actionSurTable)
		{
			ES_Date.setText(LS_Formation.getModel().getValueAt(LS_Formation.getSelectedRow(), 0).toString());
			ES_EffPro.setText(LS_Formation.getModel().getValueAt(LS_Formation.getSelectedRow(), 1).toString());
			ES_EffMes.setText(LS_Formation.getModel().getValueAt(LS_Formation.getSelectedRow(), 2).toString());
			ES_Note.setValue(LS_Formation.getModel().getValueAt(LS_Formation.getSelectedRow(), 3));
		}
		else
		{
			doRAZ();
			LS_Formation.clearSelection();
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
		ES_Date.setText("");
		ES_EffPro.setText("");
		ES_EffMes.setText("");
		ES_Note.setValue(new Double(0));
	}	
}