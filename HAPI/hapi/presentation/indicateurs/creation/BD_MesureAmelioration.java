/*
 * Créé le 1 oct. 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_MesureAmelioration;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.exception.NiveauInsuffisantException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * @author Cédric
 */
public class BD_MesureAmelioration extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 95962187998250585L;
    //Panel
	private JPanel SC_Composants = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Labels = null;
	private JPanel SC_Champs = null;
	private JPanel SC_CadreFixe = null;
	private JPanel SC_LabelsFixes = null;
	private JPanel SC_ChampsFixes = null;

	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private GridLayout layoutLabels = null;
	private GridLayout layoutChamps = null;
	private GridLayout layoutLabelsFixes = null;
	private BorderLayout layoutCadreFixe = null;
	private GridLayout layoutChampsFixes = null;

	//Labels
	private JLabel STC_Processus = null;
	private JLabel STC_Presentation = null;
	private JLabel STC_Documentation = null;
	private JLabel STC_Modele = null;
	private JLabel STC_NomrePris = null;
	private JLabel STC_NombrePropose = null;
	private JLabel STC_Version = null;

	//Champs
	private JTextField ES_Processus = null;
	private JSpinner ES_Modele = null;
	private JSpinner ES_NombrePropose = null;
	private JSpinner ES_NombrePris = null;
	private JSpinner ES_Presentation = null;
	private JSpinner ES_Documentation = null;
	private JComboBox LD_Version = null;

	//Boutons
	private JButton BP_Valider = null;
	private JButton BP_Annuler = null;

	//Variables
	private String idProcessus = null;
	private String dateExport = null;
	private boolean annulerVisible = true;

	//Ecouteur
	private ActionListener actionValider = new ActionListener()
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
			doAnnuler();
		}
	};
	private ActionListener actionVersion = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doVersion();
		}
	};

	public BD_MesureAmelioration(JDialog parent, String id_processus,boolean annulerVisible) throws NiveauInsuffisantException
	{
		super(parent, true);
		initialisation(id_processus,annulerVisible);
	}

	public BD_MesureAmelioration(JFrame parent, String id_processus,boolean annulerVisible) throws NiveauInsuffisantException
	{
		super(parent, true);
		initialisation(id_processus,annulerVisible);
	}

	private void initialisation(String id_processus,boolean annulerVisible) throws NiveauInsuffisantException
	{
		if (C_Processus.getProcessus(id_processus).getNiveauMaturite() < 2)
			throw new NiveauInsuffisantException(Bundle.getText("OO_MaturiteProcessus_insuffisant"));
		
		setTitle(Bundle.getText("BD_MesureAmelioration_caption"));

		idProcessus = id_processus;
		dateExport = C_Processus.getProcessus(idProcessus).getDateExport();
		this.annulerVisible = annulerVisible;

		this.creationElements();
		this.operationSurBoutons();
		this.operationSurComposants();
		this.operationSurPanel();
		this.operationSurFenetre();
		this.operationMoteur();
	}

	public void creationElements()
	{
		//Panel
		SC_Composants = new JPanel();
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Labels = new JPanel();
		SC_Champs = new JPanel();
		SC_CadreFixe = new JPanel();
		SC_LabelsFixes = new JPanel();
		SC_ChampsFixes = new JPanel();

		//Layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutLabels = new GridLayout(6, 0);
		layoutChamps = new GridLayout(6, 0);
		layoutLabelsFixes = new GridLayout(1, 0);
		layoutCadreFixe = new BorderLayout();
		layoutChampsFixes = new GridLayout(1, 0);

		//Labels
		STC_Processus = new JLabel();
		STC_Presentation = new JLabel();
		STC_Documentation = new JLabel();
		STC_Modele = new JLabel();
		STC_NomrePris = new JLabel();
		STC_NombrePropose = new JLabel();
		STC_Version = new JLabel();

		//Champs
		ES_Processus = new JTextField();
		ES_Modele = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		ES_NombrePropose = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		ES_NombrePris = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		ES_Presentation = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		ES_Documentation = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
		LD_Version = new JComboBox(C_MesureAmelioration.getModeleVersion(idProcessus));

		//Boutons
		BP_Valider = new JButton();
		BP_Annuler = new JButton();
	}

	public void operationSurBoutons()
	{
		BP_Valider.setText(Bundle.getText("Valider"));
		BP_Valider.setMnemonic(Bundle.getChar("Valider_mne"));
		BP_Valider.addActionListener(actionValider);

		BP_Annuler.setText(Bundle.getText("Annuler"));
		BP_Annuler.setMnemonic(Bundle.getChar("Annuler_mne"));
		BP_Annuler.addActionListener(actionAnnuler);
		BP_Annuler.setVisible(annulerVisible);
	}

	public void operationSurComposants()
	{
		STC_Processus.setText(Bundle.getText("BD_MesureAmelioration_processus")+ " : ");
		STC_Presentation.setText(Bundle.getText("BD_MesureAmelioration_presentation")+ " : ");
		STC_Documentation.setText(Bundle.getText("BD_MesureAmelioration_documentation")+ " : ");
		STC_Modele.setText(Bundle.getText("BD_MesureAmelioration_modele")+ " : ");
		STC_NomrePris.setText(Bundle.getText("BD_MesureAmelioration_nombrePris")+ " : ");
		STC_NombrePropose.setText(Bundle.getText("BD_MesureAmelioration_nombrePropose")+ " : ");
		STC_Version.setText(Bundle.getText("BD_MesureAmelioration_version")+ " : ");

		ES_Processus.setPreferredSize(new Dimension(10, 25));
		ES_Processus.setEditable(false);

		LD_Version.addActionListener(actionVersion);
	}

	public void operationSurPanel()
	{
		SC_Composants.setLayout(layoutComposant);
		SC_Composants.add(SC_CadreFixe, BorderLayout.NORTH);
		SC_Composants.add(SC_Labels, BorderLayout.WEST);
		SC_Composants.add(SC_Champs, BorderLayout.CENTER);

		SC_Labels.setLayout(layoutLabels);
		SC_Labels.add(STC_Version);
		SC_Labels.add(STC_NombrePropose);
		SC_Labels.add(STC_NomrePris);
		SC_Labels.add(STC_Presentation);
		SC_Labels.add(STC_Modele);
		SC_Labels.add(STC_Documentation);

		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(LD_Version);
		SC_Champs.add(ES_NombrePropose);
		SC_Champs.add(ES_NombrePris);
		SC_Champs.add(ES_Presentation);
		SC_Champs.add(ES_Modele);
		SC_Champs.add(ES_Documentation);

		SC_CadreFixe.setLayout(layoutCadreFixe);
		SC_CadreFixe.add(SC_ChampsFixes, BorderLayout.CENTER);
		SC_CadreFixe.add(SC_LabelsFixes, BorderLayout.WEST);

		SC_LabelsFixes.setLayout(layoutLabelsFixes);
		SC_ChampsFixes.add(ES_Processus);

		SC_ChampsFixes.setLayout(layoutChampsFixes);
		SC_LabelsFixes.add(STC_Processus);

		SC_Boutons.add(BP_Valider);
		SC_Boutons.add(BP_Annuler);
	}

	public void operationSurFenetre()
	{
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimmentionner
		this.setResizable(false);
		//Affectation du layout
		this.getContentPane().setLayout(layoutFond);
		//Affectation des panels
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		//packaging
		this.pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{
		ES_Processus.setText(C_Processus.getProcessus(idProcessus).getNomSansVersion());
		C_MesureAmelioration.selectionnerModele(dateExport);
		if (C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport) != null)
		{
			ES_NombrePropose.setValue(new Integer(C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport).getNombrePropose()));
			ES_NombrePris.setValue(new Integer(C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport).getNombrePris()));
			ES_Presentation.setValue(new Integer(C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport).getPresentation()));
			ES_Modele.setValue(new Integer(C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport).getModele()));
			ES_Documentation.setValue(new Integer(C_Processus.getProcessus(idProcessus).getMesureAmelioration(dateExport).getDocumentation()));			
		}
		else
		{
			ES_NombrePropose.setValue(new Integer(0));
			ES_NombrePris.setValue(new Integer(0));
			ES_Presentation.setValue(new Integer(0));
			ES_Modele.setValue(new Integer(0));
			ES_Documentation.setValue(new Integer(0));
		}
	}

	public void updateTexte()
	{}

	private void doValider()
	{
		try
		{
			// Récupération de la date du jour
			Calendar date = Calendar.getInstance();
			Date dateJour = new java.sql.Date(date.getTimeInMillis());
			
			C_MesureAmelioration.ajouterMesure(idProcessus, dateExport, dateJour, ((Integer) ES_NombrePropose.getValue()).intValue(), ((Integer) ES_NombrePris.getValue()).intValue(), ((Integer) ES_Presentation.getValue()).intValue(), ((Integer) ES_Modele.getValue()).intValue(), ((Integer) ES_Documentation.getValue()).intValue());
			dispose();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void doAnnuler()
	{
		dispose();
	}

	private void doVersion()
	{
		boolean trouve = false;
		int i = -1;
		ArrayList lesVersions = C_Processus.getProcessus(idProcessus).getListeDesVersion();
		while (++i < lesVersions.size() && !trouve)
			if (((ArrayList) lesVersions.get(i)).get(1).equals(LD_Version.getSelectedItem().toString()))
			{
				trouve = true;
				dateExport = (String) ((ArrayList) lesVersions.get(i)).get(0);
			}

		operationMoteur();
	}

}
