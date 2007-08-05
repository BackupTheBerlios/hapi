/*
 * Créé le 19 sept. 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_MesureVersion;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.ressources.Bundle;
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

/**
 * @author Cédric
 */
public class BD_MesureVersion extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 3216958384614822479L;
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

	public BD_MesureVersion(JFrame parent, String idProcessus) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		demarrage(idProcessus);
	}
	
	public BD_MesureVersion(JDialog parent, String idProcessus) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		demarrage(idProcessus);
	}	
	
	private void demarrage(String idProcessus) throws StopActionException
	{
		setTitle(Bundle.getText("BD_MesureVersion_caption"));

		id_processus = idProcessus;
		
		C_MesureVersion.setDateExportEnCours(null);

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
		SC_Composants.add(STV_Titre,BorderLayout.NORTH);
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
		this.setSize(560, 400);
		this.setResizable(false);
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
			C_MesureVersion.memoriseListesCourantes();
			SC_Composants.remove(leCentre);
		}
		
		leCentre = getFenetre(++numeroFenetreCourante);
		SC_Composants.add(leCentre, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_MesureVersion.getTitreFenetre(numeroFenetreCourante));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_MesureVersion.NOMBRE_FENETRES-1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);
	}
	
	private void doPrecedent()
	{
		if (leCentre != null)
		{
			C_MesureVersion.memoriseListesCourantes();
			SC_Composants.remove(leCentre);
		}
		
		leCentre = getFenetre(--numeroFenetreCourante);
		SC_Composants.add(leCentre, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_MesureVersion.getTitreFenetre(numeroFenetreCourante));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_MesureVersion.NOMBRE_FENETRES-1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);			
	}
	
	private void doAnnuler()
	{
		C_MesureVersion.clearListesCourantes();
		dispose();
	}
	
	private void doTerminer()
	{
		try
		{
			C_MesureVersion.memoriseListesCourantes();
			C_MesureVersion.saveListe();
			annulation = false;
			C_MesureVersion.clearListesCourantes();
			dispose();
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
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
				laFenetreCourante = new OO_RepresentationInterface();
				break;
			case 1:
				laFenetreCourante = new OO_RepresentationModele();
				break;
			case 2:
				laFenetreCourante = new OO_RepresentationDocument();
				break;
		}
		
		C_MesureVersion.initialiseFenetre(Numero,id_processus,laFenetreCourante);

		return laFenetreCourante;
	}	
}