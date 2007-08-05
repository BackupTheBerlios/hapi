package hapi.presentation.composants.onglets;

import hapi.application.C_Commentaire;
import hapi.application.C_Utilisateur;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Cédric
 */
public class OO_CommentaireProjet extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 6712994060400887113L;
    //Panels
	private JPanel SC_Boutons = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Composant = null;
	private JScrollPane SC_Scroll = null;
	//Layout	
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private FlowLayout layoutBoutons = null;
	//Boutons
	private JButton BP_Modifier = null;
	//Composants
	private JTextArea EM_Commentaire = null;
	private JLabel STC_Titre = null;
	//Variables
	private String identifiantProjet = null;
	private String identifiantProcessus = null;
	//Controleur
	private C_Commentaire cCommentaire = null;
	//Ecouteur
	private ActionListener actionModificationCommentaire = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doModifierCommentaire();
		}
	};

	public OO_CommentaireProjet(String identifiant, ArrayList idProcessus)
	{
		super();
		//Création des éléments
		creationElements();
		//Affecation de l'identifiant
		identifiantProjet = identifiant;
		identifiantProcessus = (String) idProcessus.get(0);
		//Mise à jour des textes
		updateTexte();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurBoutons();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Création des panels
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Composant = new JPanel();
		SC_Scroll = new JScrollPane();
		//Création des composants
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutBoutons = new FlowLayout();
		//Création des boutons
		BP_Modifier = new JButton();
		//Création des layout
		EM_Commentaire = new JTextArea();
		STC_Titre = new JLabel();
	}

	public void operationSurBoutons()
	{
		BP_Modifier.setMnemonic(Bundle.getChar("Modifier_mne"));
		BP_Modifier.setText(Bundle.getText("Modifier"));
		BP_Modifier.addActionListener(actionModificationCommentaire);
	}

	public void operationSurComposants()
	{
		//Commentaire
		EM_Commentaire.setLineWrap(true);
		//Titre
		STC_Titre.setText(Bundle.getText("OO_CommentaireProjet_titre"));
	}

	public void operationSurPanel()
	{
		//Panel des composants
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(STC_Titre, BorderLayout.NORTH);
		SC_Scroll.setViewportView(EM_Commentaire);
		SC_Composant.add(SC_Scroll, BorderLayout.CENTER);
		//Boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Modifier);

	}

	public void operationSurFenetre()
	{
		//Layout du fond
		this.setLayout(layoutFond);
		//Layout des côtés
		this.add(SC_Boutons, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);
		//Layout des composants
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{
		//Création du contrôleur
		cCommentaire = new C_Commentaire();
		//Blocage du bouton si ce n'est pas le responsable
		try
		{
			BP_Modifier.setEnabled(C_Utilisateur.getRole() == 1);
			EM_Commentaire.setEditable(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException e)
		{
			System.err.println("-----ERREUR GRAVE N°7-----");
		}
		//Chargement du commentaire
		EM_Commentaire.setText(C_ExecutionProcessus.getCommentaireProjet(identifiantProcessus, identifiantProjet));
	}

	public void updateTexte()
	{}

	public void doModifierCommentaire()
	{
		try
		{
			cCommentaire.modifierCommentaire(identifiantProcessus, identifiantProjet, EM_Commentaire.getText());
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e)
		{}
	}
}