/*
 * Auteur Cédric
 *
 */
package hapi.presentation;

import hapi.application.C_Configuration;
import hapi.application.modele.FiltreFichier;
import hapi.application.ressources.Bundle;
import hapi.exception.ChampsVideException;
import hapi.exception.FichierConfInexistantException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Fenêtre permettant de configurer l'accès à la base sur le poste local
 */
public class BD_ConfigurationCheminLocal extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = -6743631449559017024L;
    //Panel
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Composant = null;
	private TitledBorder SC_ComposantTitre = null;
	private JPanel SC_Bouton = null;
	private JPanel SC_Titres = null;
	private JPanel SC_Champs = null;
	private JPanel SC_Adresse = null;
	private JPanel SC_Login = null;
	private JPanel SC_PWD = null;
	private JPanel SC_NomBase = null;
	//Composants
	private JLabel STC_Login = null;
	private JLabel STC_PWD = null;
	private JLabel STC_Adresse = null;
	private JLabel STC_NomBase = null;
	private JTextField ES_Login = null;
	private JPasswordField ES_PWD = null;
	private JTextField ES_Adresse = null;
	private JTextField ES_NomBase = null;
	private JButton BP_Parcourir = null;
	//Boutons
	private JButton BP_Valider = null;
	private JButton BP_Annuler = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private FlowLayout layoutBouton = null;
	private GridLayout layoutTitres = null;
	private GridLayout layoutChamps = null;
	private BorderLayout layoutLogin = null;
	private BorderLayout layoutPWD = null;
	private BorderLayout layoutNomBase = null;
	private BorderLayout layoutAdresse = null;
	//Controleur
	C_Configuration cConfiguration = null;
	//Ecouteurs de la fenêtre
	private ActionListener actionOK = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doOK();
		}
	};
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};
	private ActionListener actionParcourir = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doParcourir();
		}
	};

	public BD_ConfigurationCheminLocal(JFrame parent)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_ConfigurationCheminLocal_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Création des Panel
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Composant = new JPanel();
		SC_ComposantTitre = new TitledBorder(Bundle.getText("ServeurBD"));
		SC_Bouton = new JPanel();
		SC_Titres = new JPanel();
		SC_Champs = new JPanel();
		SC_Adresse = new JPanel();
		SC_Login = new JPanel();
		SC_PWD = new JPanel();
		SC_NomBase = new JPanel();
		//Création des Composants
		STC_Login = new JLabel();
		STC_PWD = new JLabel();
		STC_Adresse = new JLabel();
		STC_NomBase = new JLabel();
		ES_Login = new JTextField();
		ES_PWD = new JPasswordField();
		ES_Adresse = new JTextField();
		ES_NomBase = new JTextField();
		BP_Parcourir = new JButton();
		//Création des Boutons
		BP_Valider = new JButton();
		BP_Annuler = new JButton();
		//Création des Layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutBouton = new FlowLayout();
		layoutTitres = new GridLayout(4, 0);
		layoutChamps = new GridLayout(4, 0);
		layoutLogin = new BorderLayout();
		layoutPWD = new BorderLayout();
		layoutNomBase = new BorderLayout();
		layoutAdresse = new BorderLayout();
	}
	public void operationSurBoutons()
	{
		//Valider
		BP_Valider.setMnemonic(Bundle.getChar("Valider_mne"));
		BP_Valider.setText(Bundle.getText("Valider"));
		//Annuler
		BP_Annuler.setMnemonic(Bundle.getChar("Annuler_mne"));
		BP_Annuler.setText(Bundle.getText("Annuler"));
	}

	public void operationSurComposants()
	{
		STC_Login.setText(Bundle.getText("login"));
		ES_Login.setPreferredSize(new Dimension(100, 21));
		ES_Login.addActionListener(actionOK);
		STC_PWD.setText(Bundle.getText("pwd"));
		ES_PWD.setPreferredSize(new Dimension(100, 21));
		ES_PWD.addActionListener(actionOK);
		STC_Adresse.setText(Bundle.getText("adresse"));
		STC_NomBase.setText(Bundle.getText("nomBase"));
		ES_Adresse.setPreferredSize(new Dimension(300, 21));
		ES_Adresse.addActionListener(actionOK);
		ES_NomBase.setPreferredSize(new Dimension(300, 21));
		ES_NomBase.addActionListener(actionOK);
		BP_Parcourir.setMnemonic(Bundle.getChar("parcourir_mne"));
		BP_Parcourir.setText(Bundle.getText("parcourir"));
		BP_Parcourir.addActionListener(actionParcourir);
	}

	public void operationSurPanel()
	{
		//Login
		SC_Login.setLayout(layoutLogin);
		SC_Login.add(ES_Login, BorderLayout.WEST);
		//PWD
		SC_PWD.setLayout(layoutPWD);
		SC_PWD.add(ES_PWD, BorderLayout.WEST);
		// Nom base
		SC_NomBase.setLayout(layoutNomBase);
		SC_NomBase.add(ES_NomBase, BorderLayout.WEST);
		//Adresse
		SC_Adresse.setLayout(layoutAdresse);
		SC_Adresse.add(ES_Adresse, BorderLayout.CENTER);
		SC_Adresse.add(BP_Parcourir, BorderLayout.EAST);
		//Champs
		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(SC_Login);
		SC_Champs.add(SC_PWD);
		SC_Champs.add(SC_NomBase);
		SC_Champs.add(SC_Adresse);
		//Titres
		SC_Titres.add(STC_Login);
		SC_Titres.add(STC_PWD);
		SC_Titres.add(STC_NomBase);
		SC_Titres.add(STC_Adresse);
		SC_Titres.setLayout(layoutTitres);
		//Composants
		SC_Composant.setBorder(SC_ComposantTitre);
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Titres, BorderLayout.WEST);
		SC_Composant.add(SC_Champs, BorderLayout.CENTER);
		//Boutons
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(BP_Valider);
		SC_Bouton.add(BP_Annuler);
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
		this.getContentPane().add(SC_Bouton, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Composant, BorderLayout.CENTER);
		//packaging
		this.pack();
		//Positionnement
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension tailleFenetre = this.getSize();
		this.setLocation((tailleEcran.width - tailleFenetre.width) / 2, (tailleEcran.height - tailleFenetre.height) / 2);
	}

	public void operationMoteur()
	{
		try
		{
			cConfiguration = new C_Configuration(false);
		}
		catch (Exception e)
		{
			//Exception normale à ce niveau, ne rien faire
		}
		//Remplissage des champs, dans le cas où les infos existent

		ES_Login.setText(cConfiguration.getLoginBase());
		ES_PWD.setText(cConfiguration.getPWDBase());
		ES_Adresse.setText(cConfiguration.getCheminBase());
		ES_NomBase.setText(cConfiguration.getNomBase());

		//Branchement des listeners
		BP_Valider.addActionListener(actionOK);
		BP_Annuler.addActionListener(actionAnnuler);
	}

	//Réalisation du OK pour la config de la base
	private void doOK()
	{
		try
		{
			StringBuffer PWD = new StringBuffer("");
			for (int i = 0; i < ES_PWD.getPassword().length; i++)
				PWD.append(ES_PWD.getPassword()[i]);

			cConfiguration.setConfigurationBase(ES_Login.getText(), PWD.toString(), ES_Adresse.getText(), ES_NomBase.getText());
			//Fermeture de la fenêtre
			dispose();
		}
		catch (ChampsVideException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("ManqueChamps"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (FichierConfInexistantException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("BD_ConfigurationCheminLocal_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
		catch (IOException e)
		{}
	}

	//Réalisation du annuler pour la config de la base
	private void doAnnuler()
	{
		//Fermeture de la fenêtre
		dispose();
	}

	private void doParcourir()
	{
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogTitle(Bundle.getText("BD_ConfigurationCheminLocal_parcourir"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FiltreFichier("*", Bundle.getText("touslesfichiers")));
		int res = fileChooser.showDialog(this, Bundle.getText("Valider"));
		if (res == JFileChooser.APPROVE_OPTION)
		{
			//Vérification si le chemin contient le nom du fichier
			if (fileChooser.getSelectedFile().isFile())
				ES_Adresse.setText(fileChooser.getSelectedFile().getAbsolutePath().substring(0, fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
			else
				ES_Adresse.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void updateTexte()
	{}
}
