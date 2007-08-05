package hapi.presentation;

import hapi.application.C_Identification;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Natalia Boursier
 */
public class BD_Identification extends JDialog implements FenetreHAPI
{

	private static final long serialVersionUID = 4663045771462470149L;
    //Panels
	private JPanel SC_Boutons = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Champs = null;
	private JPanel SC_Titres = null;
	private JPanel SC_Saisies = null;
	//Boutons
	private JButton BP_Ok = null;
	private JButton BP_Quitter = null;
	//Contenu
	private JLabel STC_Login = null;
	private JLabel STC_MotDePasse = null;
	private JTextField ES_Login = null;
	private JPasswordField ES_MotDePasse = null;
	private JLabel STC_Connecter = null;
	private JComboBox LD_Roles = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutChamp = null;
	private GridLayout layoutTitres = null;
	private GridLayout layoutSaisies = null;
	private FlowLayout layoutBoutons = null;
	//Controleur
	private C_Identification cIdentification = null;
	//Ecouteurs de la fenêtre
	private ActionListener actionOK = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doValider();
		}
	};
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doQuitter();
		}
	};
	private MouseWheelListener actionScroll = new MouseWheelListener()
	{
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			doScroll(arg0.getWheelRotation());
		}
	};
	//Constructeur
	public BD_Identification(JFrame parent) throws SQLException
	{
		//Association au parent
		super(parent, Bundle.getText("BD_Identification_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationMoteur();
		operationSurFenetre();
	}

	public void operationSurBoutons()
	{
		//Bouton valider
		BP_Ok.setMnemonic(Bundle.getChar("Valider_mne"));
		BP_Ok.setText(Bundle.getText("Valider"));
		BP_Ok.addMouseWheelListener(actionScroll);
		//Bouter quitter
		BP_Quitter.setMnemonic(Bundle.getChar("Quitter_mne"));
		BP_Quitter.setText(Bundle.getText("Quitter"));
		BP_Quitter.addMouseWheelListener(actionScroll);
	}

	public void operationSurComposants()
	{
		//Ligne de rôle
		STC_Connecter.setDisplayedMnemonic(Bundle.getChar("BD_Identification_role_mne"));
		STC_Connecter.setHorizontalAlignment(SwingConstants.RIGHT);
		STC_Connecter.setLabelFor(LD_Roles);
		STC_Connecter.setText(Bundle.getText("BD_Identification_role"));
		//Ligne de login
		STC_Login.setDisplayedMnemonic(Bundle.getChar("login_mne"));
		STC_Login.setHorizontalAlignment(SwingConstants.RIGHT);
		STC_Login.setLabelFor(ES_Login);
		STC_Login.setText(Bundle.getText("login"));
		ES_Login.setText("");
		ES_Login.addActionListener(actionOK);
		ES_Login.addMouseWheelListener(actionScroll);
		//Ligne de mot de passe
		STC_MotDePasse.setDisplayedMnemonic(Bundle.getChar("pwd_mne"));
		STC_MotDePasse.setHorizontalAlignment(SwingConstants.RIGHT);
		STC_MotDePasse.setLabelFor(ES_MotDePasse);
		STC_MotDePasse.setText(Bundle.getText("pwd"));
		ES_MotDePasse.setText("");
		ES_MotDePasse.addActionListener(actionOK);
		ES_MotDePasse.addMouseWheelListener(actionScroll);
	}

	public void operationSurPanel()
	{
		//Panel contenant les infos
		SC_Champs.setLayout(layoutChamp);
		SC_Titres.setLayout(layoutTitres);
		SC_Saisies.setLayout(layoutSaisies);
		SC_Titres.add(STC_Connecter);
		SC_Saisies.add(LD_Roles);
		SC_Titres.add(STC_Login);
		SC_Saisies.add(ES_Login);
		SC_Titres.add(STC_MotDePasse);
		SC_Saisies.add(ES_MotDePasse);
		SC_Champs.add(SC_Titres, BorderLayout.WEST);
		SC_Champs.add(SC_Saisies, BorderLayout.CENTER);
		//Panel des boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Ok);
		SC_Boutons.add(BP_Quitter);
	}

	public void operationSurFenetre()
	{
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimmentionner
		this.setResizable(false);
		//Affectation du layout
		this.getContentPane().setLayout(layoutFond);
		//Ajout des panels
		this.getContentPane().add(SC_Champs, BorderLayout.CENTER);
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		//packaging
		this.pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
		//Ajout du listener
		this.addMouseWheelListener(actionScroll);
		//Affectation du focus
		ES_Login.requestFocus();
	}

	public void operationMoteur() throws SQLException
	{
		//Branchement des listeners
		BP_Ok.addActionListener(actionOK);
		BP_Quitter.addActionListener(actionAnnuler);
		//Création du contrôleur
		cIdentification = new C_Identification();
		LD_Roles.setModel(cIdentification.getModeleRoles());
		LD_Roles.addMouseWheelListener(actionScroll);
	}

	public void creationElements()
	{
		//Création des panels
		SC_Champs = new JPanel();
		SC_Titres = new JPanel();
		SC_Saisies = new JPanel();
		SC_Boutons = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		//Création des boutons
		BP_Ok = new JButton();
		BP_Quitter = new JButton();
		//Création des composants
		STC_Login = new JLabel();
		STC_MotDePasse = new JLabel();
		ES_Login = new JTextField();
		ES_MotDePasse = new JPasswordField();
		STC_Connecter = new JLabel();
		LD_Roles = new JComboBox();
		//Création des layout
		layoutFond = new BorderLayout();
		layoutChamp = new BorderLayout();
		layoutTitres = new GridLayout(3, 1);
		layoutSaisies = new GridLayout(3, 1);
		layoutBoutons = new FlowLayout();
	}

	//Réalisation du ok pour l'identification
	private void doValider()
	{
		//Transformation du mot de passe
		StringBuffer PWD = new StringBuffer("");
		for (int i = 0; i < ES_MotDePasse.getPassword().length; i++)
			PWD.append(ES_MotDePasse.getPassword()[i]);
		//this.toBack();
		//Vérification de l'identification
		try
		{
			if (cIdentification.verifierIdentification(ES_Login.getText(), PWD.toString(), LD_Roles.getSelectedIndex() + 1))
			{
				//Chargement de la configuration de l'utilisateur
				cIdentification.chargerConfiguration();
				//Fermeture de la fenêtre
				dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(this, Bundle.getText("BD_Identification_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
				//this.toFront();
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			//this.toFront();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(this, Bundle.getText("probleme"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			//this.toFront();
		}
	}

	//Réalisation du annuler pour l'identification
	private void doQuitter()
	{
		//Fermeture de la fenêtre
		dispose();
	}

	//Réalisation du scroll de la souris sur la combo
	private void doScroll(int nbLignes)
	{
		try
		{
			if (LD_Roles.getSelectedIndex() + nbLignes < 0)
				LD_Roles.setSelectedIndex(0);
			else
				LD_Roles.setSelectedIndex(LD_Roles.getSelectedIndex() + nbLignes);
		}
		catch (IllegalArgumentException e)
		{
			if (nbLignes > 0)
				LD_Roles.setSelectedIndex(LD_Roles.getItemCount() - 1);
			else
				LD_Roles.setSelectedIndex(0);
		}
	}

	public void updateTexte()
	{}

}
