package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_CreerEvaluation;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.exception.ErreurCreationManuelleException;
import hapi.exception.StopActionException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class BD_CreerEvaluation extends JDialog implements FenetreHAPI
{

	private static final long serialVersionUID = -3961730694992995817L;
    //Panels
	private JPanel SC_Boutons;
	private JPanel SC_Gauche;
	private JPanel SC_Droite;
	private JPanel SC_Haut;
	private JPanel SC_Composants;
	//Boutons
	private JButton BP_Annuler;
	private JButton BP_Terminer;
	private JButton BP_Suivant;
	private JButton BP_Precedent;
	//Layout
	private BorderLayout layoutFond;
	private BorderLayout layoutComposants;
	private FlowLayout layoutBouton;
	//Texte
	private JTextPane STV_Titre;

	//Variables
	private String id_processus = null;
	private String id_execution = null;
	private int indice_Mesure = -1;
	private boolean utiliseCourant = false;
	private int numeroFenetreCourante = -1;
	private boolean annulation = true;

	//Ecouteurs de la fenêtre
	private ActionListener actionSuivant = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSuivant();
		}
	};
	private ActionListener actionPrecedent = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doPrecedent();
		}
	};
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};
	private ActionListener actionTerminer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doTerminer();
		}
	};

	//Central
	private FenetreAssistee leCentre = null;

	public BD_CreerEvaluation(JFrame parent, String idProcessus, String leProjet, int idMesure) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		setTitle(Bundle.getText("BD_CreerEvaluation_caption"));

		id_processus = idProcessus;
		id_execution = leProjet;
		indice_Mesure = idMesure;
		utiliseCourant = this.operationCycleDeVie();

		if (utiliseCourant)
		{
			this.creationElements();
			this.operationSurBoutons();
			this.operationSurComposants();
			this.operationSurPanel();
			this.operationSurFenetre();
			this.operationMoteur();

			setVisible(true);
			if (annulation)
				throw new StopActionException();
		}
	}

	public BD_CreerEvaluation(JDialog parent, String idProcessus, String leProjet, int idMesure) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		setTitle(Bundle.getText("BD_CreerEvaluation_caption"));

		id_processus = idProcessus;
		id_execution = leProjet;
		indice_Mesure = idMesure;
		utiliseCourant = this.operationCycleDeVie();

		if (utiliseCourant)
		{
			this.creationElements();
			this.operationSurBoutons();
			this.operationSurComposants();
			this.operationSurPanel();
			this.operationSurFenetre();
			this.operationMoteur();

			setVisible(true);
			if (annulation)
				throw new StopActionException();
		}
	}

	public void creationElements()
	{
		//Panels
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Composants = new JPanel();
		//Boutons
		BP_Annuler = new JButton();
		BP_Terminer = new JButton();
		BP_Suivant = new JButton();
		BP_Precedent = new JButton();
		//Layout
		layoutFond = new BorderLayout();
		layoutBouton = new FlowLayout();
		layoutComposants = new BorderLayout();
		//Texte
		STV_Titre = new JTextPane();
	}

	public void operationSurBoutons()
	{
		//Annuler
		BP_Annuler.setMnemonic(Bundle.getChar("Annuler_mne"));
		BP_Annuler.setText(Bundle.getText("Annuler"));
		BP_Annuler.addActionListener(actionAnnuler);
		//Terminer
		BP_Terminer.setMnemonic(Bundle.getChar("Terminer_mne"));
		BP_Terminer.setText(Bundle.getText("Terminer"));
		BP_Terminer.addActionListener(actionTerminer);
		//Suivant
		BP_Suivant.setMnemonic(Bundle.getChar("Suivant_mne"));
		BP_Suivant.setText(Bundle.getText("Suivant"));
		BP_Suivant.addActionListener(actionSuivant);
		//Précédent
		BP_Precedent.setMnemonic(Bundle.getChar("Precedent_mne"));
		BP_Precedent.setText(Bundle.getText("Precedent"));
		BP_Precedent.addActionListener(actionPrecedent);
	}

	public void operationSurComposants()
	{}

	public void operationSurPanel()
	{
		SC_Boutons.setLayout(layoutBouton);
		SC_Boutons.add(BP_Precedent);
		SC_Boutons.add(BP_Suivant);
		SC_Boutons.add(BP_Terminer);
		SC_Boutons.add(BP_Annuler);

		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(STV_Titre, BorderLayout.NORTH);
	}

	public void operationSurFenetre()
	{
		this.getContentPane().setLayout(layoutFond);
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		pack();
		//Positionnement
		this.setSize(640, 480);
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur() throws StopActionException
	{
		//leCentre = getFenetre(0);
		doSuivant();
	}

	public void updateTexte()
	{}

	private void doSuivant()
	{
		if (leCentre != null)
		{
			C_CreerEvaluation.memoriseListesCourantes();
			SC_Composants.remove(leCentre);
		}

		leCentre = getFenetre(++numeroFenetreCourante);
		SC_Composants.add(leCentre, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_CreerEvaluation.getTitreFenetre(numeroFenetreCourante, indice_Mesure));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_CreerEvaluation.NOMBRE_FENETRES - 1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);
	}

	private void doPrecedent()
	{
		if (leCentre != null)
		{
			C_CreerEvaluation.memoriseListesCourantes();
			SC_Composants.remove(leCentre);
		}

		leCentre = getFenetre(--numeroFenetreCourante);
		SC_Composants.add(leCentre, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_CreerEvaluation.getTitreFenetre(numeroFenetreCourante, indice_Mesure));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_CreerEvaluation.NOMBRE_FENETRES - 1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);
	}

	private void doAnnuler()
	{
		C_CreerEvaluation.clearListesCourantes();
		dispose();
	}

	private void doTerminer()
	{
		try
		{
			//Exceptionnel, car les paramètres de retour nécessitent la
			// compilation
			//appel de chaque fenêtre
			numeroFenetreCourante = -1;
			//Creer
			doSuivant();
			//RUA
			doSuivant();
			//RUP
			doSuivant();
			//RUR
			doSuivant();
			//RTP
			doSuivant();
			C_CreerEvaluation.memoriseListesCourantes();
			C_CreerEvaluation.saveListe(id_processus, id_execution, indice_Mesure, utiliseCourant);
			annulation = false;
			C_CreerEvaluation.clearListesCourantes();
			dispose();
		}
		catch (ErreurCreationManuelleException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_TableauDeBordSaisie_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_TableauDeBordSaisie_erreur_bd"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * Création d'une fenêtre
	 */
	private FenetreAssistee getFenetre(int Numero)
	{
		FenetreAssistee laFenetreCourante = null;

		switch (Numero)
		{
			case 0:
				laFenetreCourante = new OO_CreerIteration();
				break;
			case 1:
			case 2:
			case 3:
				laFenetreCourante = new OO_Indicateur();
				break;
			case 4:
				laFenetreCourante = new OO_RTP();
				break;
		}

		C_CreerEvaluation.initialiseFenetre(Numero, laFenetreCourante, id_processus, id_execution, indice_Mesure, utiliseCourant);

		return laFenetreCourante;
	}

	/**
	 * Gère le cycle de vie
	 * 
	 * @return false = ne pas continuer l'opération en cours
	 */
	private boolean operationCycleDeVie()
	{
		//Recherche si le pcv existe déjà
		int idCycleDeVie = C_ExecutionProcessus.getExecutionProcessus(id_processus, id_execution).getIdCycleDeVie();
		boolean passagePCV = true;

		if (indice_Mesure == -1) //Création manuelle
		{
			//Ce code ne doit pas être appelé dans le cas d'un import, car les
			// entités temporaires contiennent déjà le bon DPE
			try
			{
				if (this.getParent() instanceof JFrame)
					utiliseCourant = BD_DemanderDPE.doitUtiliserDPECourant((JFrame) this.getParent(), id_processus, id_execution);
				else
					utiliseCourant = BD_DemanderDPE.doitUtiliserDPECourant((JDialog) this.getParent(), id_processus, id_execution);
			}
			catch (StopActionException e)
			{
				return false;
			}
		}

		if (idCycleDeVie != -1)
		{
			passagePCV = JOptionPane.showConfirmDialog(this, Bundle.getText("BD_CreerEvaluation_Modification_PCV"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.NO_OPTION;
		}

		try
		{
			if (passagePCV)
				if (this.getParent() instanceof JFrame)
					new BD_CycleDeVie((JFrame) this.getParent(), id_processus, id_execution, idCycleDeVie, utiliseCourant);
				else
					new BD_CycleDeVie((JDialog) this.getParent(), id_processus, id_execution, idCycleDeVie, utiliseCourant);
		}
		catch (StopActionException e)
		{
			//S'il s'agit d'une modification du cycle de vie, l'annulation ne
			// ferme pas tout
			if (idCycleDeVie != -1)
				return true;
			else
				return false;
		}

		return true;
	}

}
