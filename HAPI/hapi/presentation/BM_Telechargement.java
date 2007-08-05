package hapi.presentation;

import hapi.application.C_TransfertFichier;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Vincent
 */
public class BM_Telechargement extends JDialog implements FenetreHAPI
{

	private static final long serialVersionUID = 6402342863361511182L;
    // Panels
	private JPanel SC_Bas = null;
	private JPanel SC_Composants = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	//Composants
	private JLabel STC_Titre = null;
	private JList LS_Actions = null;
	private JScrollPane SC_Scroll = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	//Variable
	private String titreTelechargement = null;
	private C_TransfertFichier cControleur = null;
	//Ecouteur de la liste
	private ListDataListener ecouteurModele = new ListDataListener()
	{
		public void intervalAdded(ListDataEvent arg0)
		{}

		public void intervalRemoved(ListDataEvent arg0)
		{}

		public void contentsChanged(ListDataEvent arg0)
		{
			SC_Scroll.getVerticalScrollBar().setValue(SC_Scroll.getVerticalScrollBar().getMaximum());
		}
	};

	public BM_Telechargement(JFrame parent, String message, C_TransfertFichier controleur)
	{
		//Liaison avec le parent
		super(parent, Bundle.getText("BM_Telechargement_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Affectation de la variable
		titreTelechargement = message;
		cControleur = controleur;
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}	
	
	public BM_Telechargement(JDialog parent, String message, C_TransfertFichier controleur)
	{
		//Liaison avec le parent
		super(parent, Bundle.getText("BM_Telechargement_caption"), true);
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Affectation de la variable
		titreTelechargement = message;
		cControleur = controleur;
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		//Titre
		STC_Titre.setText(titreTelechargement);
		//Liste des actions
		SC_Scroll.setViewportView(LS_Actions);
		SC_Scroll.setWheelScrollingEnabled(true);
		//Modele de la liste
		LS_Actions.setModel(cControleur.getModeleTelechargement());
		cControleur.getModeleTelechargement().addListDataListener(ecouteurModele);
	}

	public void operationSurPanel()
	{
		//Panel des composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Scroll, BorderLayout.CENTER);
		SC_Composants.add(STC_Titre, BorderLayout.NORTH);
	}

	public void operationSurFenetre()
	{
		//Operation de fermeture
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimentionnement
		this.setResizable(false);
		//Affectation du layout
		this.getContentPane().setLayout(layoutFond);
		//Affectation des panels d'espacement
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Bas, BorderLayout.SOUTH);
		//Affectation des panels
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		//Impossible de faire pack à cause de la liste, donc dimension fixe
		this.setSize(new Dimension(250, 270));
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{}

	public void creationElements()
	{
		//Création des panels
		SC_Bas = new JPanel();
		SC_Composants = new JPanel();
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		//Création des composants
		STC_Titre = new JLabel();
		LS_Actions = new JList();
		SC_Scroll = new JScrollPane();

		//Création des layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		//Création de la variable
		titreTelechargement = new String();
	}

	public void updateTexte()
	{}
}
