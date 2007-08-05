package hapi.presentation;

import hapi.application.C_Utilisateur;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Cédric
 */
public class BD_LoginPwd extends JDialog implements FenetreHAPI
{

	private static final long serialVersionUID = 3551495624595028621L;
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
	//Contenu
	private JLabel STC_Login = null;
	private JLabel STC_MotDePasse = null;
	private JTextField ES_Login = null;
	private JPasswordField ES_MotDePasse = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutChamp = null;
	private GridLayout layoutTitres = null;
	private GridLayout layoutSaisies = null;
	private FlowLayout layoutBoutons = null;
	//Variable
	private String adresse = null;
	private String type;
	//Ecouteurs de la fenêtre
	private ActionListener actionOK = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doValider();
		}
	};

	//Constructeur
	public BD_LoginPwd(JDialog parent, String lAdresse, String leType) throws SQLException
	{
		//Association au parent
		super(parent, Bundle.getText("BD_Identification_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Affectation des variables
		adresse = lAdresse;
		type = leType;
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
	}

	public void operationSurComposants()
	{
		//Ligne de login
		STC_Login.setDisplayedMnemonic(Bundle.getChar("login_mne"));
		STC_Login.setHorizontalAlignment(SwingConstants.RIGHT);
		STC_Login.setLabelFor(ES_Login);
		ES_Login.setPreferredSize(new Dimension(100, 25));
		ES_MotDePasse.setPreferredSize(new Dimension(100, 25));
		STC_Login.setText(Bundle.getText("login"));
		//Ligne de mot de passe
		STC_MotDePasse.setDisplayedMnemonic(Bundle.getChar("pwd_mne"));
		STC_MotDePasse.setHorizontalAlignment(SwingConstants.RIGHT);
		STC_MotDePasse.setLabelFor(ES_MotDePasse);
		STC_MotDePasse.setText(Bundle.getText("pwd"));
	}

	public void operationSurPanel()
	{
		//Panel contenant les infos
		SC_Champs.setLayout(layoutChamp);
		SC_Titres.setLayout(layoutTitres);
		SC_Saisies.setLayout(layoutSaisies);
		SC_Titres.add(STC_Login);
		SC_Saisies.add(ES_Login);
		SC_Titres.add(STC_MotDePasse);
		SC_Saisies.add(ES_MotDePasse);
		SC_Champs.add(SC_Titres, BorderLayout.WEST);
		SC_Champs.add(SC_Saisies, BorderLayout.CENTER);
		//Panel des boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Ok);
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
	}

	public void operationMoteur() throws SQLException
	{
		//Branchement des listeners
		BP_Ok.addActionListener(actionOK);
		//Affectation des valeurs par défaut
		if (type.equals("DPE"))
		{
			ES_Login.setText(C_Utilisateur.findServeurDPE(adresse).getLogin());
			ES_MotDePasse.setText(C_Utilisateur.findServeurDPE(adresse).getMotDePasse());
		}
		if (type.equals("MES"))
		{
			ES_Login.setText(C_Utilisateur.findServeurMES(adresse).getLogin());
			ES_MotDePasse.setText(C_Utilisateur.findServeurMES(adresse).getMotDePasse());
		}
		if (type.equals("DOM"))
		{
			ES_Login.setText(C_Utilisateur.findServeurDOM(adresse).getLogin());
			ES_MotDePasse.setText(C_Utilisateur.findServeurDOM(adresse).getMotDePasse());
		}
		if (type.equals("HTM"))
		{
			ES_Login.setText(C_Utilisateur.findServeurHTML(adresse).getLogin());
			ES_MotDePasse.setText(C_Utilisateur.findServeurHTML(adresse).getMotDePasse());
		}		
		if (type.equals("CSS"))
		{
			ES_Login.setText(C_Utilisateur.findServeurCSS(adresse).getLogin());
			ES_MotDePasse.setText(C_Utilisateur.findServeurCSS(adresse).getMotDePasse());
		}

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
		//Création des composants
		STC_Login = new JLabel();
		STC_MotDePasse = new JLabel();
		ES_Login = new JTextField();
		ES_MotDePasse = new JPasswordField();
		//Création des layout
		layoutFond = new BorderLayout();
		layoutChamp = new BorderLayout();
		layoutTitres = new GridLayout(2, 1);
		layoutSaisies = new GridLayout(2, 1);
		layoutBoutons = new FlowLayout();
	}

	//Réalisation du ok pour l'identification
	private void doValider()
	{
		StringBuffer PWD = new StringBuffer("");
		for (int i = 0; i < ES_MotDePasse.getPassword().length; i++)
			PWD.append(ES_MotDePasse.getPassword()[i]);

		try
		{
			if (type.equals("DPE"))
				C_Utilisateur.setLoginMotDePasseDPE(adresse, ES_Login.getText(), PWD.toString());
			if (type.equals("DOM"))
				C_Utilisateur.setLoginMotDePasseDOM(adresse, ES_Login.getText(), PWD.toString());
			if (type.equals("MES"))
				C_Utilisateur.setLoginMotDePasseMES(adresse, ES_Login.getText(), PWD.toString());
			if (type.equals("HTM"))
				C_Utilisateur.setLoginMotDePasseHTML(adresse, ES_Login.getText(), PWD.toString());
			if (type.equals("CSS"))
				C_Utilisateur.setLoginMotDePasseCSS(adresse, ES_Login.getText(), PWD.toString());
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		dispose();
	}

	public void updateTexte()
	{}

}
