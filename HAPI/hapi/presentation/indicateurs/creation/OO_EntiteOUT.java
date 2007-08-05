package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_EntiteOUT;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

/**
 * @author Robin EYSSERIC
 */
public class OO_EntiteOUT extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 5031502478101097103L;
    // Bordures
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;

	// Panels
	private JPanel SC_Central = null;
	private JPanel SC_Saisie = null;
	private JPanel SC_Supprimer = null;
	private JPanel SC_Ajouter = null;
	private JPanel SC_Liste = null;
	private JScrollPane SC_ScrollTable = null;
	private TitledBorder SC_BordureSaisie = null;
	private TitledBorder SC_BordureTable = null;

	// Composants
	private JTable LS_Entites = null;
	private JButton BP_Ajouter = null;
	private JButton BP_Supprimer = null;
	private JTextField ES_Entite = null;

	// Layouts
	private BorderLayout layoutFond = null;
	private BorderLayout layoutCentral = null;
	private BorderLayout layoutSaisie = null;
	private BorderLayout layoutListe = null;

	private BorderLayout layoutSupprimer = null;
	private BorderLayout layoutAjouter = null;

	private C_EntiteOUT typeEntite = null;
	private int type;

	private ActionListener actionAjouter = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAjouterEntite();
		}
	};

	private ActionListener actionSupprimer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSupprimerEntite();
		}
	};

	public OO_EntiteOUT(int typeRequis, C_EntiteOUT entite)
	{
		super();
		typeEntite = entite;
		type = typeRequis;

		creationElements();
		operationSurBoutons();
		operationMoteur();
		updateTexte();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	public void creationElements()
	{
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();

		SC_Central = new JPanel();
		SC_Saisie = new JPanel();
		SC_Liste = new JPanel();
		SC_Supprimer = new JPanel();
		SC_Ajouter = new JPanel();
		SC_ScrollTable = new JScrollPane();

		layoutFond = new BorderLayout();
		layoutCentral = new BorderLayout();
		layoutSaisie = new BorderLayout();
		layoutListe = new BorderLayout();

		layoutSupprimer = new BorderLayout();
		layoutAjouter = new BorderLayout();

		SC_BordureSaisie = new TitledBorder("");
		SC_BordureTable = new TitledBorder("");

		LS_Entites = new JTable();
		BP_Ajouter = new JButton();
		BP_Supprimer = new JButton();
		ES_Entite = new JTextField(35);
	}

	public void operationSurBoutons()
	{
		BP_Ajouter.setText(Bundle.getText("Ajouter"));
		BP_Ajouter.setMnemonic(Bundle.getChar("Ajouter_mne"));
		BP_Ajouter.addActionListener(actionAjouter);
		BP_Ajouter.setPreferredSize(new Dimension(100, 23));

		BP_Supprimer.setText(Bundle.getText("Supprimer"));
		BP_Supprimer.setMnemonic(Bundle.getChar("Supprimer_mne"));
		BP_Supprimer.addActionListener(actionSupprimer);
		BP_Supprimer.setPreferredSize(new Dimension(100, 23));
	}

	public void operationSurComposants()
	{
		SC_ScrollTable.setViewportView(LS_Entites);
		LS_Entites.setPreferredScrollableViewportSize(new Dimension(50, 100));
	}

	public void operationSurPanel()
	{
		SC_Saisie.setLayout(layoutSaisie);
		SC_Saisie.add(ES_Entite, BorderLayout.CENTER);
		SC_Saisie.add(SC_Ajouter, BorderLayout.EAST);
		SC_Saisie.setBorder(SC_BordureSaisie);

		SC_Liste.setLayout(layoutListe);
		SC_Liste.setBorder(SC_BordureTable);
		SC_Liste.add(SC_ScrollTable, BorderLayout.CENTER);
		SC_Liste.add(SC_Supprimer, BorderLayout.EAST);

		SC_Supprimer.setLayout(layoutSupprimer);
		SC_Supprimer.add(BP_Supprimer, BorderLayout.NORTH);
		SC_Ajouter.setLayout(layoutAjouter);
		SC_Ajouter.add(BP_Ajouter, BorderLayout.NORTH);

		SC_Central.setLayout(layoutCentral);
		SC_Central.add(SC_Saisie, BorderLayout.NORTH);
		SC_Central.add(SC_Liste, BorderLayout.CENTER);
		//SC_Central.add(BP_Supprimer, BorderLayout.EAST);    		
	}

	public void operationSurFenetre()
	{
		this.setLayout(layoutFond);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Central, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{

		LS_Entites.setModel(typeEntite.getModeleTable());
		LS_Entites.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void updateTexte()
	{
		// Mise à jour des labels
		String valeur = null;

		switch (type)
		{
			case C_EntiteOUT.ACTIVITE :
				SC_BordureSaisie.setTitle(Bundle.getText("BD_CreerEvaluation_TachesOUT_tache_ajout"));
				SC_BordureTable.setTitle(Bundle.getText("BD_CreerEvaluation_TachesOUT_tache_liste"));
				/*				valeur = new String(Bundle.getText("Activite"));
								valeur2 = new String(Bundle.getText("Activites"));*/
				break;
			case C_EntiteOUT.ROLE :
				SC_BordureSaisie.setTitle(Bundle.getText("BD_CreerEvaluation_ParticipantOUT_participant_ajout"));
				SC_BordureTable.setTitle(Bundle.getText("BD_CreerEvaluation_ParticipantOUT_participant_liste"));

				/*				valeur = new String(Bundle.getText("Role"));
								valeur2 = new String(Bundle.getText("Roles"));*/
				break;
			case C_EntiteOUT.PRODUIT :
				SC_BordureSaisie.setTitle(Bundle.getText("BD_CreerEvaluation_ArtefactOUT_artefact_ajout"));
				SC_BordureTable.setTitle(Bundle.getText("BD_CreerEvaluation_ArtefactOUT_artefact_liste"));

				/*				valeur = new String(Bundle.getText("Produit"));
								valeur2 = new String(Bundle.getText("Produits"));*/
				break;
		}

		/*		SC_BordureSaisie.setTitle(valeur + " " + Bundle.getText("A_ajouter"));
				SC_BordureTable.setTitle(Bundle.getText("Liste_des") + " " + valeur2);*/

		// Mise à jour de la colonne de la table
		Vector titreColonne = new Vector();

		switch (type)
		{
			case C_EntiteOUT.ACTIVITE :
				valeur = new String(Bundle.getText("BD_CreerEvaluation_TachesOUT_tache"));
				break;
			case C_EntiteOUT.ROLE :
				valeur = new String(Bundle.getText("BD_CreerEvaluation_ParticipantOUT_participant"));
				break;
			case C_EntiteOUT.PRODUIT :
				valeur = new String(Bundle.getText("BD_CreerEvaluation_ArtefactOUT_artefact"));
				break;
		}

		titreColonne.add(valeur);
		typeEntite.getModeleTable().setColumnIdentifiers(titreColonne);

	}

	public void doAjouterEntite()
	{
		if (ES_Entite.getText().length() > 0)
		{
			typeEntite.ajouterEntite(ES_Entite.getText());
			ES_Entite.setText("");
		}
		else
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("ManqueChamps"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
		}
	}

	public void doSupprimerEntite()
	{
		for (int i = 0; i < LS_Entites.getSelectedRowCount(); i++)
		{
			typeEntite.supprimerEntite(LS_Entites.getSelectedRows()[i]);
		}
	}

}
