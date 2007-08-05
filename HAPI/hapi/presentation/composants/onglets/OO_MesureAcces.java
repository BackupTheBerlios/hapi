/*
 * Créé le 30 sept. 2005
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_Utilisateur;
import hapi.application.indicateurs.C_MesureAcces;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Cédric
 */
public class OO_MesureAcces extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -1258453197264132907L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;
	
	private JPanel SC_Ajout = null;
	private TitledBorder SC_AjoutTitre = null;
	private JPanel SC_LabelAcces = null;
	private JPanel SC_ChampsAcces = null;
	private JPanel SC_GroupeAcces = null;
	private JPanel SC_GroupeAccesBouton = null;
	private JPanel SC_Bouton = null;
	
	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private JPanel SC_Supprimer = null;
	
	//Composants
	private JScrollPane SC_ScrollAcces = null;
	
	private JTable LS_Acces = null;
	private JButton BP_Supprimer = null;
	
	private JButton BP_Ajouter = null;
	private JLabel STC_Mois = null;
	private JFormattedTextField ES_Mois = null;
	private JLabel STC_Nombre = null;
	private JFormattedTextField ES_Nombre = null;	

	//Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	
	private BorderLayout layoutAjout = null;
	private GridLayout layoutLabelAcces = null;
	private GridLayout layoutChampsAcces = null;
	private BorderLayout layoutGroupeAcces = null;
	private BorderLayout layoutGroupeAccesBouton = null;
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
	public OO_MesureAcces(String identifiantProcessus)
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
		SC_LabelAcces = new JPanel();
		SC_ChampsAcces = new JPanel();
		SC_GroupeAcces = new JPanel();
		SC_GroupeAccesBouton = new JPanel();
		SC_Bouton = new JPanel();
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_MesureAcces_detail"));
		
		SC_Liste = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_MesureAcces_liste"));
		SC_Supprimer = new JPanel();
		
		//Composants
		SC_ScrollAcces = new JScrollPane();
		
		LS_Acces = new JTable(C_MesureAcces.getModeleMesureAccesProcessus(idProcessus));
		BP_Supprimer = new JButton();
		
		BP_Ajouter = new JButton();
		STC_Mois = new JLabel();
		STC_Nombre = new JLabel();
		ES_Mois = new JFormattedTextField();
		ES_Nombre = new JFormattedTextField();

		//Layouts
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		
		layoutAjout = new BorderLayout();
		layoutLabelAcces = new GridLayout(2,0);
		layoutChampsAcces = new GridLayout(2,0);
		layoutGroupeAcces = new BorderLayout();
		layoutGroupeAccesBouton = new BorderLayout();
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
		STC_Mois.setText(Bundle.getText("OO_MesureAcces_champ_mois") + " : ");
		STC_Nombre.setText(Bundle.getText("OO_MesureAcces_champ_nombre") + " : ");

		LS_Acces.addMouseListener(tableMouseListener);
		LS_Acces.getSelectionModel().addListSelectionListener(ListenerSurTable);
		
		DateModel df = new DateModel(Bundle.formatPetitMois);
		ES_Mois.setFormatterFactory(new DefaultFormatterFactory(df));
		ES_Mois.setPreferredSize(new Dimension(100,25));
		
		DecimalModel df2 = new DecimalModel(Bundle.naturelFormat);
		ES_Nombre.setFormatterFactory(new DefaultFormatterFactory(df2));	
		
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
		SC_LabelAcces.setLayout(layoutLabelAcces);
		SC_LabelAcces.add(STC_Mois);
		SC_LabelAcces.add(STC_Nombre);
		
		SC_ChampsAcces.setLayout(layoutChampsAcces);
		SC_ChampsAcces.add(ES_Mois);
		SC_ChampsAcces.add(ES_Nombre);
		
		SC_GroupeAcces.setLayout(layoutGroupeAcces);
		SC_GroupeAcces.add(SC_LabelAcces, BorderLayout.WEST);
		SC_GroupeAcces.add(SC_ChampsAcces, BorderLayout.CENTER);
		
		//Groupe date
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(BP_Ajouter, BorderLayout.NORTH);
		
		
		SC_GroupeAccesBouton.setLayout(layoutGroupeAccesBouton);
		SC_GroupeAccesBouton.add(SC_GroupeAcces, BorderLayout.WEST);
		SC_GroupeAccesBouton.add(SC_Bouton, BorderLayout.EAST);
		
		//Ajout
		SC_Ajout.setBorder(SC_AjoutTitre);
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.add(SC_GroupeAccesBouton, BorderLayout.CENTER);
		
		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);

		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_ScrollAcces, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);
		SC_ScrollAcces.setViewportView(LS_Acces);

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
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.PETITMOIS_MODEL);

		try
		{
			C_MesureAcces.ajouterMesure(idProcessus,new Date(sfDate.parse(ES_Mois.getText()).getTime()),ES_Nombre.getText());

			// Réinitialisation des champs
			doRAZ();

			//Remise à jour du modèle
			LS_Acces.setModel(C_MesureAcces.getModeleMesureAccesProcessus(idProcessus));
		}
		catch (ParseException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureAcces_DateNonRemplie"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureAcces_ErreurAjout"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		// Réactive la mise à jour de la zone de consultation lors de la modification de la table
		actionSurTable = false;
	}

	/**
	 * Supprime les demandes de modification séléctionnées dans la table.
	 */
	private void doSupprimer()
	{
		if (LS_Acces.getSelectedRowCount() > 0)
		{
			// Evite la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = true;

			for (int i = 0; i < LS_Acces.getSelectedRowCount(); i++)
			{
				try
				{
					C_MesureAcces.supprimerMesureAcces(LS_Acces.getSelectedRows()[i], idProcessus);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_MesureAcces_ErreurSupprMesure"), Bundle.getText("OO_RevueProcessus_ErreurComBD"), JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

			// Remise à zéro des zones de saisie
			doRAZ();

			// Déselectionne le ou les elts de la table
			LS_Acces.clearSelection();

			//Remise à jour du modèle
			LS_Acces.setModel(C_MesureAcces.getModeleMesureAccesProcessus(idProcessus));

			// Réactive la mise à jour de la zone de consultation lors de la modification de la table
			actionSurTable = false;
		}
	}

	private void doAfficheDetailMesure()
	{
		if (LS_Acces.getModel().getRowCount() > 0 && !actionSurTable)
		{
			ES_Mois.setText(LS_Acces.getModel().getValueAt(LS_Acces.getSelectedRow(), 0).toString());
			ES_Nombre.setText(LS_Acces.getModel().getValueAt(LS_Acces.getSelectedRow(), 1).toString());
		}
		else
		{
			doRAZ();
			LS_Acces.clearSelection();
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
		ES_Mois.setText("");
		ES_Nombre.setText("");
	}	
}