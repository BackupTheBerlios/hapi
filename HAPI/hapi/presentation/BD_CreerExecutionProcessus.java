package hapi.presentation;

import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.modele.DateModel;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Fabien Allanic Yannick Goutaudier Fabien Puyssegur
 */
public class BD_CreerExecutionProcessus extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = -3791726469109099090L;
    //Panels
	private JPanel SC_Boutons = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Composant = null;
	private JPanel SC_Informations = null;
	private JPanel SC_Titres = null;
	private JPanel SC_Champs = null;
	private JScrollPane SC_Scroll = null;

	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutInformations = null;
	private GridLayout layoutTitres = null;
	private GridLayout layoutChamps = null;

	//Composants	
	private JLabel STC_Titre = null;
	private JLabel STC_versionProcessus = null;
	private JLabel STC_nom = null;
	private JLabel STC_description = null;
	private JLabel STC_dateDebut = null;
	private JLabel STC_dateFin = null;

	//Champs
	private JComboBox LD_versionProcessus = null;
	private JTextField ES_nom = null;
	private JTextArea EM_description = null;
	private JFormattedTextField ES_dateDebut = null;
	private JFormattedTextField ES_dateFin = null;

	//Boutons
	private JButton BP_Creer = null;
	private JButton BP_Annuler = null;

	private String idProcessus = null;

	//Ecouteurs de la fenêtre
	private ActionListener actionCreer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doCreer();
		}
	};
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};

	public BD_CreerExecutionProcessus(JFrame parent, String lIdProcessus)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_Creer_Execution_titre_fenetre"), true);
		idProcessus = lIdProcessus;

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
		//Création des Panels
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Haut = new JPanel();
		SC_Droite = new JPanel();
		SC_Composant = new JPanel();
		SC_Informations = new JPanel();
		SC_Titres = new JPanel();
		SC_Champs = new JPanel();
		SC_Scroll = new JScrollPane();

		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutInformations = new BorderLayout();
		layoutTitres = new GridLayout(5, 1);
		layoutChamps = new GridLayout(5, 1);

		//Création des Composants
		STC_Titre = new JLabel();
		STC_versionProcessus = new JLabel();
		STC_nom = new JLabel();
		STC_description = new JLabel();
		STC_dateDebut = new JLabel();
		STC_dateFin = new JLabel();

		//Création des Boutons
		BP_Creer = new JButton();
		BP_Annuler = new JButton();

		//Champs
		ES_nom = new JTextField();
		EM_description = new JTextArea(6, 25);
		ES_dateDebut = new JFormattedTextField();
		DateModel df = new DateModel(Bundle.formatDate);
		ES_dateDebut.setFormatterFactory(new DefaultFormatterFactory(df));
		ES_dateFin = new JFormattedTextField();
		ES_dateFin.setFormatterFactory(new DefaultFormatterFactory(df));
	}

	public void operationSurBoutons()
	{
		//Annuler
		BP_Annuler.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Annuler.setText(Bundle.getText("Fermer"));
		BP_Annuler.addActionListener(actionAnnuler);
		//Creer
		BP_Creer.setMnemonic(Bundle.getChar("BD_Creer_Execution_mne"));
		BP_Creer.setText(Bundle.getText("BD_Creer_Execution"));
		BP_Creer.addActionListener(actionCreer);

	}

	public void operationSurComposants()
	{
		//Titre
		STC_Titre.setText(Bundle.getText("BD_Creer_Execution_titre"));

		//Version
		STC_versionProcessus.setText(Bundle.getText("BD_Creer_Execution_vproc"));

		//Nom
		STC_nom.setText(Bundle.getText("BD_Creer_Execution_nom"));

		//Description
		STC_description.setText(Bundle.getText("BD_Creer_Execution_description"));

		//Date de début	
		STC_dateDebut.setText(Bundle.getText("BD_Creer_Execution_datedebut"));

		//Date de fin
		STC_dateFin.setText(Bundle.getText("BD_Creer_Execution_datefin"));

		EM_description.setLineWrap(true);
		EM_description.setEditable(true);
		SC_Scroll.setViewportView(EM_description);

		//Chargement de la liste des processus
		ArrayList liste_des_versions = C_Processus.getListeDateExportFormatee(idProcessus);
		// inversion de la liste des versions de façon à avoir les dernières version en haut de liste
		ArrayList linversee = new ArrayList();
		for (int i = liste_des_versions.size() - 1; i >= 0; i--)
		{
			linversee.add(liste_des_versions.get(i));
		}
		LD_versionProcessus = new JComboBox(linversee.toArray());
	}

	public void operationSurPanel()
	{
		//Boutons
		SC_Boutons.setLayout(new FlowLayout());
		SC_Boutons.add(BP_Creer);
		SC_Boutons.add(BP_Annuler);
		//Champs
		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(ES_nom);
		SC_Champs.add(LD_versionProcessus);
		SC_Champs.add(ES_dateDebut);
		SC_Champs.add(ES_dateFin);
		SC_Champs.add(new JPanel());

		//Titres
		SC_Titres.setLayout(layoutTitres);
		SC_Titres.add(STC_nom);
		SC_Titres.add(STC_versionProcessus);
		SC_Titres.add(STC_dateDebut);
		SC_Titres.add(STC_dateFin);
		SC_Titres.add(STC_description);

		//Informations
		SC_Informations.setLayout(layoutInformations);
		//SC_Informations.add(STC_Titre,BorderLayout.NORTH);
		SC_Informations.add(SC_Titres, BorderLayout.WEST);
		SC_Informations.add(SC_Champs, BorderLayout.CENTER);

		//Composants
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Informations, BorderLayout.NORTH);
		SC_Composant.add(SC_Scroll, BorderLayout.CENTER);

	}

	public void operationSurFenetre()
	{
		//Affectation du fond
		this.getContentPane().setLayout(layoutFond);
		//Bordures
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Composant, BorderLayout.CENTER);
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Impossibilité de redimentionnement
		this.setResizable(false);
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

	private void doAnnuler()
	{
		dispose();
	}

	private void doCreer()
	{
		// tests : tous les champs sont pleins (hormis le commentaire)
		if (ES_nom.getText().equals("") || ES_dateFin.getText().equals("") || ES_dateDebut.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("BD_CreerExecutionProcessus_incomplet"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// formatage de la version de processus
		String dnf = ((String) LD_versionProcessus.getSelectedItem());
		String df = dnf.substring(6, 10) + dnf.substring(3, 5) + dnf.substring(0, 2) + dnf.substring(11, 13) + dnf.substring(14, 16) + dnf.substring(17, 19);

		// liste des informations sur la nouvelle execution de processus
		ArrayList lesParametres = new ArrayList();

		// identifiant de l'execution de processus inexistant, à -1 donc
		lesParametres.add(new Integer(-1)); // 0 : id
		lesParametres.add(ES_nom.getText()); // 1 : nom
		lesParametres.add(EM_description.getText()); // 2 : description
		lesParametres.add(""); // 3 : commentaire

		// formatage des dates de début et fin
		String temp = ES_dateDebut.getText();
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(temp.substring(6, 10)), Integer.parseInt(temp.substring(3, 5)) - 1, Integer.parseInt(temp.substring(0, 2)));

		lesParametres.add(date.getTime()); // 4 : date de début
		temp = ES_dateFin.getText();
		date.set(Integer.parseInt(temp.substring(6, 10)), Integer.parseInt(temp.substring(3, 5)) - 1, Integer.parseInt(temp.substring(0, 2)));

		lesParametres.add(date.getTime()); // 5 : date de fin
		
		if (((Date) lesParametres.get(4)).compareTo((Date) lesParametres.get(5)) > 0)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("BD_Creer_Execution_erreur_date"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}
		/*
		//Vérification si la date de début du projet est bien postérieure à la version du processus
		dnf = dnf.substring(0,dnf.indexOf(' '));
		date.set(Integer.parseInt(dnf.substring(6, 10)), Integer.parseInt(dnf.substring(3, 5)) - 1, Integer.parseInt(dnf.substring(0, 2)));
	
		if (((Date) lesParametres.get(4)).compareTo(date.getTime()) < 0)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("BD_Creer_Execution_erreur_date_processus"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}	*/	

		try
		{

			C_ExecutionProcessus.creerExecutionProcessus(idProcessus, df, lesParametres, -1);
			dispose();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("BD_Creer_Execution_erreur_id"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE | JOptionPane.OK_OPTION);
		}
	}

}