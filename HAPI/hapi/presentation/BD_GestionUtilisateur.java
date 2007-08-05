package hapi.presentation;

import hapi.application.C_Configuration;
import hapi.application.ressources.Bundle;
import hapi.exception.ChampsVideException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author Cédric
 */
public class BD_GestionUtilisateur extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 1526599299396790269L;
    //Panel
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Composants = null;
	private JScrollPane SC_Scroll = null;
	private JPanel SC_Ajout = null;
	private JPanel SC_BoutonAjout = null;
	private JPanel SC_Labels = null;
	private JPanel SC_Champs = null;
	private JPanel SC_Supprimer = null;
	private JPanel SC_Liste = null;
	private TitledBorder SC_ListeTitre = null;
	private TitledBorder SC_AjoutTitre = null;
	//Boutons
	private JButton BP_Fermer = null;
	//Composants
	private JTable LS_Utilisateurs = null;
	private JTextField ES_LoginUtilisateur = null;
	private JTextField ES_NomUtilisateur = null;
	private JTextField ES_PrenomUtilisateur = null;
	private JComboBox LD_RolesUtilisateur = null;
	private JLabel STC_NomUtilisateur = null;
	private JLabel STC_PrenomUtilisateur = null;
	private JLabel STC_LoginUtilisateur = null;
	private JLabel STC_RoleUtilisateur = null;
	private JButton BP_Supprimer = null;
	private JButton BP_Ajouter = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	private BorderLayout layoutAjout = null;
	private BorderLayout layoutBoutonAjout = null;
	private GridLayout layoutLabels = null;
	private GridLayout layoutChamps = null;
	private BorderLayout layoutListe = null;
	private FlowLayout layoutSupprimer = null;
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
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			dispose();
		}
	};
	private MouseWheelListener actionScroll = new MouseWheelListener()
	{
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			doScroll(arg0.getWheelRotation());
		}
	};

	public BD_GestionUtilisateur(JFrame parent)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_GestionUtilisateur_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appels de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	public void creationElements()
	{
		//Création des Panel
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Boutons = new JPanel();
		SC_Composants = new JPanel();
		SC_Scroll = new JScrollPane();
		SC_Ajout = new JPanel();
		SC_BoutonAjout = new JPanel();
		SC_Champs = new JPanel();
		SC_Labels = new JPanel();
		SC_Supprimer = new JPanel();
		SC_Liste = new JPanel();
		SC_ListeTitre = new TitledBorder(Bundle.getText("OO_GestionUtilisateur_liste"));
		SC_AjoutTitre = new TitledBorder(Bundle.getText("OO_GestionUtilisateur_champ"));
		//Création des composants
		LS_Utilisateurs = new JTable(C_Configuration.getModeleUtilisateurs());
		ES_NomUtilisateur = new JTextField();
		ES_LoginUtilisateur = new JTextField();
		ES_PrenomUtilisateur = new JTextField();
		LD_RolesUtilisateur = new JComboBox(C_Configuration.getModeleRoles());
		STC_NomUtilisateur = new JLabel();
		STC_PrenomUtilisateur = new JLabel();
		STC_LoginUtilisateur = new JLabel();
		STC_RoleUtilisateur = new JLabel();
		BP_Supprimer = new JButton();
		BP_Ajouter = new JButton();
		//Bouton
		BP_Fermer = new JButton();
		//Création des Layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutAjout = new BorderLayout();
		layoutBoutonAjout = new BorderLayout();
		layoutChamps = new GridLayout(4, 1);
		layoutLabels = new GridLayout(4, 1);
		layoutListe = new BorderLayout();
		layoutSupprimer = new FlowLayout();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		//Nom du responsable
		STC_LoginUtilisateur.setText(Bundle.getText("OO_GestionUtilisateur_champ_login"));
		STC_NomUtilisateur.setText(Bundle.getText("OO_GestionUtilisateur_champ_nom"));
		STC_PrenomUtilisateur.setText(Bundle.getText("OO_GestionUtilisateur_champ_prenom"));
		STC_RoleUtilisateur.setText(Bundle.getText("OO_GestionUtilisateur_champ_role"));
		//Bouton d'ajout
		BP_Ajouter.setText(Bundle.getText("Ajouter"));
		BP_Ajouter.setMnemonic(Bundle.getChar("Ajouter_mne"));
		BP_Ajouter.addActionListener(actionAjouter);
		BP_Ajouter.addMouseWheelListener(actionScroll);
		ES_LoginUtilisateur.addActionListener(actionAjouter);
		ES_LoginUtilisateur.addMouseWheelListener(actionScroll);
		ES_NomUtilisateur.addActionListener(actionAjouter);
		ES_NomUtilisateur.addMouseWheelListener(actionScroll);
		ES_PrenomUtilisateur.addActionListener(actionAjouter);
		ES_PrenomUtilisateur.addMouseWheelListener(actionScroll);
		LD_RolesUtilisateur.addMouseWheelListener(actionScroll);
		//Bouton de suppression
		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);
		//Bouton
		BP_Fermer.setText(Bundle.getText("Fermer"));
		BP_Fermer.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Fermer.addActionListener(actionFermer);
	}

	public void operationSurPanel()
	{
		//Ajout
		SC_Ajout.setLayout(layoutAjout);
		SC_Ajout.setBorder(SC_AjoutTitre);
		SC_Labels.setLayout(layoutLabels);
		SC_Labels.add(STC_LoginUtilisateur);
		SC_Labels.add(STC_NomUtilisateur);
		SC_Labels.add(STC_PrenomUtilisateur);
		SC_Labels.add(STC_RoleUtilisateur);
		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(ES_LoginUtilisateur);
		SC_Champs.add(ES_NomUtilisateur);
		SC_Champs.add(ES_PrenomUtilisateur);
		SC_Champs.add(LD_RolesUtilisateur);
		SC_Ajout.add(SC_Labels, BorderLayout.WEST);
		SC_Ajout.add(SC_Champs, BorderLayout.CENTER);
		SC_Ajout.add(SC_BoutonAjout, BorderLayout.EAST);
		SC_BoutonAjout.setLayout(layoutBoutonAjout);
		SC_BoutonAjout.add(BP_Ajouter, BorderLayout.SOUTH);
		//Suppression
		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer);
		//Bouton
		SC_Boutons.add(BP_Fermer);
		//Liste
		SC_Liste.setBorder(SC_ListeTitre);
		SC_Liste.setLayout(layoutListe);
		SC_Liste.add(SC_Scroll, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);
		SC_Scroll.setViewportView(LS_Utilisateurs);
		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Liste, BorderLayout.CENTER);
		SC_Composants.add(SC_Ajout, BorderLayout.NORTH);
	}

	public void operationSurFenetre()
	{
		//Affectation du layout de fond
		this.getContentPane().setLayout(layoutFond);
		//Bordures
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		//Composants
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimentionnement
		this.setResizable(true);
		//packaging
		pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}

	private void doAjouter()
	{
		try
		{
			if (!C_Configuration.addUtilisateur(ES_LoginUtilisateur.getText(), ES_NomUtilisateur.getText(), ES_PrenomUtilisateur.getText(), LD_RolesUtilisateur.getSelectedIndex()))
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_GestionUtilisateur_AjoutImpossible"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			else
			{
				ES_LoginUtilisateur.setText("");
				ES_NomUtilisateur.setText("");
				ES_PrenomUtilisateur.setText("");
				try
				{
					LD_RolesUtilisateur.setSelectedIndex(0);
				}
				catch (Exception e)
				{
					//Rien du tout
				}
				//Remise à jour du modèle
				LS_Utilisateurs.setModel(C_Configuration.getModeleUtilisateurs());
			}
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("ManqueChamps"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
	}

	private void doSupprimer()
	{
		if (JOptionPane.showConfirmDialog(this, Bundle.getText("BD_GestionUtilisateur_ConfirmationSuppression"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
		{
			for (int i = 0; i < LS_Utilisateurs.getSelectedRowCount(); i++)
			{
				String login = (String) LS_Utilisateurs.getModel().getValueAt(LS_Utilisateurs.getSelectedRows()[i], 0);
				String nom = (String) LS_Utilisateurs.getModel().getValueAt(LS_Utilisateurs.getSelectedRows()[i], 1);
				String prenom = (String) LS_Utilisateurs.getModel().getValueAt(LS_Utilisateurs.getSelectedRows()[i], 2);
				String role = (String) LS_Utilisateurs.getModel().getValueAt(LS_Utilisateurs.getSelectedRows()[i], 3);
				int leRole = C_Configuration.convertRole(role);
				if (C_Configuration.verifieSuppression(login, nom, prenom, leRole))
				{
					try
					{
						C_Configuration.supprimeUtilisateur(login, leRole);
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(this, Bundle.getText("BD_GestionUtilisateur_ErreurSuppression"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//Remise à jour du modèle
			LS_Utilisateurs.setModel(C_Configuration.getModeleUtilisateurs());
		}

	}

	//Réalisation du scroll de la souris sur la combo
	private void doScroll(int nbLignes)
	{
		try
		{
			if (LD_RolesUtilisateur.getSelectedIndex() + nbLignes < 0)
				LD_RolesUtilisateur.setSelectedIndex(0);
			else
				LD_RolesUtilisateur.setSelectedIndex(LD_RolesUtilisateur.getSelectedIndex() + nbLignes);
		}
		catch (IllegalArgumentException e)
		{
			if (nbLignes > 0)
				LD_RolesUtilisateur.setSelectedIndex(LD_RolesUtilisateur.getItemCount() - 1);
			else
				LD_RolesUtilisateur.setSelectedIndex(0);
		}
	}
}
