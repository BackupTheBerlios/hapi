/*
 * Créé le 10 mai 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.indicateurs.C_CycleDeVie;
import hapi.application.ressources.Bundle;
import hapi.exception.ChampsVideException;
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
public class BD_CycleDeVie extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = -8904285464071675347L;
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
	private int id_CycleDeVie = -1;
	private boolean utiliseCourant = true;
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
	private OO_ArbreFaceArbre lesArbres = null;

	public BD_CycleDeVie(JFrame parent, String idProcessus, String leProjet, int idCycleDeVie, boolean utilise_Courant) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		if (idCycleDeVie != -1)
			setTitle(Bundle.getText("BD_CreerCycleDeVie_captionModification"));
		else
			setTitle(Bundle.getText("BD_CreerCycleDeVie_caption"));

		id_processus = idProcessus;
		id_execution = leProjet;
		id_CycleDeVie = idCycleDeVie;
		utiliseCourant = utilise_Courant;

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
	
	public BD_CycleDeVie(JDialog parent, String idProcessus, String leProjet, int idCycleDeVie, boolean utilise_Courant) throws StopActionException
	{
		//Association au parent
		super(parent, true);
		if (idCycleDeVie != -1)
			setTitle(Bundle.getText("BD_CreerCycleDeVie_captionModification"));
		else
			setTitle(Bundle.getText("BD_CreerCycleDeVie_caption"));

		id_processus = idProcessus;
		id_execution = leProjet;
		id_CycleDeVie = idCycleDeVie;
		utiliseCourant = utilise_Courant;

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
		this.setSize(600, 450);
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur() throws StopActionException
	{
		C_CycleDeVie.clearHashMap();
		doSuivant();
	}

	public void updateTexte()
	{}
	
	private void doSuivant()
	{
		if (lesArbres != null)
		{
			SC_Composants.remove(lesArbres);
			C_CycleDeVie.memoriseListesCourantes(numeroFenetreCourante);			
		}
		lesArbres = new OO_ArbreFaceArbre(C_CycleDeVie.getFenetre(++numeroFenetreCourante, id_processus,utiliseCourant,id_CycleDeVie));
		SC_Composants.add(lesArbres, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_CycleDeVie.getTitreFenetre(numeroFenetreCourante));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_CycleDeVie.NOMBRE_FENETRES-1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);
	}
	
	private void doPrecedent()
	{
		if (lesArbres != null)
		{
			SC_Composants.remove(lesArbres);
			C_CycleDeVie.memoriseListesCourantes(numeroFenetreCourante);			
		}		
		lesArbres = new OO_ArbreFaceArbre(C_CycleDeVie.getFenetre(--numeroFenetreCourante, id_processus,utiliseCourant,id_CycleDeVie));
		SC_Composants.add(lesArbres, BorderLayout.CENTER);
		BP_Precedent.setEnabled(false);
		STV_Titre.setEditable(false);
		STV_Titre.setBackground(getBackground());
		STV_Titre.setText(C_CycleDeVie.getTitreFenetre(numeroFenetreCourante));
		BP_Suivant.setEnabled(numeroFenetreCourante < C_CycleDeVie.NOMBRE_FENETRES-1);
		BP_Precedent.setEnabled(numeroFenetreCourante > 0);			
	}
	
	private void doAnnuler()
	{
		C_CycleDeVie.clearHashMap();
		dispose();
	}
	
	private void doTerminer()
	{
		try
		{
			C_CycleDeVie.memoriseListesCourantes(numeroFenetreCourante);
			C_CycleDeVie.saveListes(id_CycleDeVie,id_execution,id_processus,utiliseCourant);
			annulation = false;
			C_CycleDeVie.clearHashMap();
			dispose();
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		catch (ChampsVideException e)
		{
			switch (e.getNumero())
			{
				case 0 : JOptionPane.showMessageDialog(this, Bundle.getText("BD_CreerCycleDeVie_ListeVide_Activite"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);break;
				case 1 : JOptionPane.showMessageDialog(this, Bundle.getText("BD_CreerCycleDeVie_ListeVide_Produit"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);break;
				case 2 : JOptionPane.showMessageDialog(this, Bundle.getText("BD_CreerCycleDeVie_ListeVide_Role"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);break;
			}
			
		}
	}
	
	

}