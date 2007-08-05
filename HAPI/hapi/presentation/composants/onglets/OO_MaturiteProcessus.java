package hapi.presentation.composants.onglets;

import hapi.application.C_DescriptionMaturite;
import hapi.application.C_NiveauMaturite;
import hapi.application.C_Utilisateur;
import hapi.application.metier.C_Processus;
import hapi.application.modele.DateModel;
import hapi.application.modele.ModeleSpinner;
import hapi.application.ressources.Bundle;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.FP_HAPI;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Vincent MARILLAUD
 */

//Attention, le parent doit être FP_HAPI
public class OO_MaturiteProcessus extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = 2822816266360052376L;
    //Panel
	private JPanel SC_Niveau = null;
	private JPanel SC_PanelNiveau = null;
	private JPanel SC_Composant = null;
	private JPanel SC_ComposantNord = null;
	private JPanel SC_DescriptNiveauAvecTitre = null;
	private JPanel SC_PanelDescriptNiveau = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Commentaire = null;
	private JPanel SC_LabelDate = null;
	private JPanel SC_ChampsDate = null;
	private JPanel SC_Date = null;
	private JPanel SC_Entete = null;
	private JScrollPane SC_Scroll = null;
	//Composant
	private JLabel STC_Niveau = null;
	private JLabel STC_Commentaire = null;
	private JLabel STC_DescriptionNiveau = null;
	private JLabel STC_Objectif = null;
	private JLabel STC_Passage = null;
	private JTextArea EM_Commentaire = null;
	private JTextField EM_DescriptionNiveau = null;
	private JSpinner ES_Niveau = null;
	private JButton BP_ModifierMaturite = null;
	private JFormattedTextField ES_Objectif = null;
	private JFormattedTextField ES_Passage = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutCommentaire = null;
	private BorderLayout layoutNiveau = null;
	private BorderLayout layoutPanelNiveau = null;
	private BorderLayout layoutComposantNord = null;
	private BorderLayout layoutDescEtTitre = null;
	private BorderLayout layoutDescriptionNiveau = null;
	private GridLayout layoutLabelDate = null;
	private GridLayout layoutChampsDate = null;
	private BorderLayout layoutDate = null;
	private BorderLayout layoutEntete = null;
	//Variables
	private JFrame laFenetre;

	// identifiant du processus sélectionné dans l'arbre
	private String identifiantProcessus;
	//Controleur
	C_NiveauMaturite cMaturiteProcessus = null;
	//Ecouteur
	private ActionListener actionModificationMaturite = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doModifierMaturite();
		}
	};

	private ChangeListener actionChangerNiveau = new ChangeListener()
	{
		public void stateChanged(ChangeEvent arg0)
		{
			doChange();			
		}
	};
	
	public OO_MaturiteProcessus(JFrame leParent,String idProcessus)
	{
		super();
		if (leParent instanceof FP_HAPI)
		{
			//On conserve l'identifiant du processus sélectionné dans l'arbre
			this.identifiantProcessus = idProcessus;
			this.laFenetre = leParent;
			//Création des éléments
			creationElements();
			//Mise à jour des textes
			updateTexte();
			//Appels de l'interface
			operationSurPanel();
			operationMoteur();
			operationSurComposants();
			operationSurFenetre();
			operationSurBoutons();
		}
	}

	public void creationElements()
	{
		//Création du panel
		SC_Niveau = new JPanel();
		SC_PanelNiveau = new JPanel();
		SC_Composant = new JPanel();
		SC_DescriptNiveauAvecTitre = new JPanel();
		SC_PanelDescriptNiveau = new JPanel();
		SC_ComposantNord = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Commentaire = new JPanel();
		SC_Scroll = new JScrollPane();
		SC_LabelDate = new JPanel();
		SC_ChampsDate = new JPanel();
		SC_Date = new JPanel();
		SC_Entete = new JPanel();
		//Création du composant
		STC_Niveau = new JLabel();
		//DM Précision de la maturité : ajout d'un titre à la zone de description ajouté
		STC_DescriptionNiveau = new JLabel();
		STC_Commentaire = new JLabel();
		EM_Commentaire = new JTextArea();
		//DM Précision de la maturité : ajout d'une zone de description (objet de la DM)
		EM_DescriptionNiveau = new JTextField();
		ES_Niveau = new JSpinner(new ModeleSpinner(1,5));
		BP_ModifierMaturite = new JButton();
		//Objectif
		STC_Objectif = new JLabel();
		STC_Passage = new JLabel();
		ES_Objectif = new JFormattedTextField();
		ES_Passage = new JFormattedTextField();
		//Création du layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();
		layoutCommentaire = new BorderLayout();
		layoutNiveau = new BorderLayout();
		layoutPanelNiveau = new BorderLayout();
		layoutComposantNord = new BorderLayout();
		layoutDescEtTitre = new BorderLayout();
		layoutDescriptionNiveau = new BorderLayout();
		layoutLabelDate = new GridLayout(2,0);
		layoutChampsDate = new GridLayout(2,0);
		layoutDate = new BorderLayout();
		layoutEntete = new BorderLayout();
	}

	public void operationSurBoutons()
	{
		BP_ModifierMaturite.addActionListener(actionModificationMaturite);
		//Bouton de modification de la maturite
		BP_ModifierMaturite.setText(Bundle.getText("OO_MaturiteProcessus_BP_ModifierMaturite"));	
	}

	public void operationSurComposants()
	{
		//Objectif
		STC_Objectif.setText(Bundle.getText("OO_MaturiteProcessus_objectif"));
		DateModel df = new DateModel(Bundle.formatDate);
		ES_Objectif.setFormatterFactory(new DefaultFormatterFactory(df));
		Date dateObjectif = C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,C_Processus.getProcessus(identifiantProcessus).getNiveauMaturite()-1).getDateObjectif();
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		ES_Objectif.setText((dateObjectif==null)?"":sfDate.format(dateObjectif));
		ES_Objectif.setPreferredSize(new Dimension(120,25));
		
		//Passage
		STC_Passage.setText(Bundle.getText("OO_MaturiteProcessus_passage"));
		DateModel df2 = new DateModel(Bundle.formatDate);
		ES_Passage.setFormatterFactory(new DefaultFormatterFactory(df2));
		Date datePassage = C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,C_Processus.getProcessus(identifiantProcessus).getNiveauMaturite()-1).getDatePassage();
		ES_Passage.setText((datePassage==null)?"":sfDate.format(datePassage));
		ES_Passage.setPreferredSize(new Dimension(120,25));
		
		//Niveau de maturite
		STC_Niveau.setText(Bundle.getText("OO_MaturiteProcessus_niveau"));
		ES_Niveau.setMinimumSize(new Dimension(35, 25));
		ES_Niveau.setPreferredSize(new Dimension(50, 25));
		ES_Niveau.setValue(new Integer(C_Processus.getNiveauMaturiteProcessus(this.identifiantProcessus)));
		ES_Niveau.addChangeListener(actionChangerNiveau);

		//DM Précision de la maturité : affectation du titre
		STC_DescriptionNiveau.setText(Bundle.getText("OO_MaturiteProcessus_decription_niveau"));
		EM_DescriptionNiveau.setText((String) C_DescriptionMaturite.getLesDescriptions().get(new Integer(((Integer)ES_Niveau.getValue()).intValue() - 1)));
		EM_DescriptionNiveau.setPreferredSize(new Dimension(100, 25));
		
		//Commentaire
		STC_Commentaire.setText(Bundle.getText("OO_MaturiteProcessus_commentaire"));
		EM_Commentaire.setText(C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,C_Processus.getProcessus(identifiantProcessus).getNiveauMaturite()-1).getCommentaire());
		EM_Commentaire.setLineWrap(true);

		SC_Scroll.setViewportView(EM_Commentaire);

		EM_DescriptionNiveau.setEditable(false);
		//Seul le responsable peut modifier la maturite et les commentaires
		try
		{
			ES_Niveau.setEnabled(C_Utilisateur.getRole() == 1);
			EM_Commentaire.setEditable(C_Utilisateur.getRole() == 1);
			ES_Objectif.setEditable(C_Utilisateur.getRole() == 1);
			ES_Passage.setEditable(C_Utilisateur.getRole() == 1);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
	}

	public void operationSurPanel()
	{
		//Composants
		SC_ComposantNord.setLayout(layoutComposantNord);
		SC_ComposantNord.add(SC_PanelNiveau, BorderLayout.WEST);
		SC_ComposantNord.add(SC_PanelDescriptNiveau, BorderLayout.CENTER);
		
		SC_Entete.setLayout(layoutEntete);
		SC_Entete.add(SC_ComposantNord, BorderLayout.NORTH);
		SC_Entete.add(SC_Date, BorderLayout.WEST);
		
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Entete, BorderLayout.NORTH);
		SC_Composant.add(SC_Commentaire, BorderLayout.CENTER);

		//Niveau
		SC_PanelNiveau.setLayout(layoutPanelNiveau);
		SC_PanelNiveau.add(SC_Niveau, BorderLayout.SOUTH);
		SC_Niveau.setLayout(layoutNiveau);
		SC_Niveau.add(STC_Niveau, BorderLayout.WEST);
		SC_Niveau.add(ES_Niveau, BorderLayout.CENTER);

		//DM Précision de la maturité : avec redécoupage
		SC_PanelDescriptNiveau.setLayout(layoutDescriptionNiveau);
		SC_PanelDescriptNiveau.add(SC_DescriptNiveauAvecTitre, BorderLayout.SOUTH);
		SC_DescriptNiveauAvecTitre.setLayout(layoutDescEtTitre);
		SC_DescriptNiveauAvecTitre.add(STC_DescriptionNiveau, BorderLayout.WEST);
		SC_DescriptNiveauAvecTitre.add(EM_DescriptionNiveau, BorderLayout.CENTER);

		//Commentaire
		SC_Commentaire.setLayout(layoutCommentaire);
		SC_Commentaire.add(SC_Scroll, BorderLayout.CENTER);
		SC_Commentaire.add(STC_Commentaire, BorderLayout.NORTH);
		
		//Objectif
		SC_LabelDate.setLayout(layoutLabelDate);
		SC_LabelDate.add(STC_Passage);
		SC_LabelDate.add(STC_Objectif);

		SC_Date.setLayout(layoutDate);
		SC_Date.add(SC_LabelDate, BorderLayout.WEST);
		SC_Date.add(SC_ChampsDate, BorderLayout.CENTER);
		
		//Passage
		SC_ChampsDate.setLayout(layoutChampsDate);
		SC_ChampsDate.add(ES_Passage);
		SC_ChampsDate.add(ES_Objectif);		

		//Bouton de modification uniquement pour le responsable de processus
		try
		{
			if (C_Utilisateur.getRole() == 1)
				SC_Bas.add(BP_ModifierMaturite);
		}
		catch (UtilisateurNonIdentifieException unie)
		{}
	}

	public void operationSurFenetre()
	{
		//Affectation du layout
		this.setLayout(layoutFond);
		//Ajout du panel des composants
		this.add(SC_Composant, BorderLayout.CENTER);
		//Ajout des bordures
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Haut, BorderLayout.NORTH);
	}

	public void operationMoteur()
	{
		//controleur
		cMaturiteProcessus = new C_NiveauMaturite();
	}

	public void updateTexte()
	{}

	private void doModifierMaturite()
	{
		try
		{
			SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
			if (ES_Passage.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this, Bundle.getText("OO_MaturiteProcessus_passageOblig"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			}
			else
				if (!ES_Objectif.getText().equals(""))
				{
					if (sfDate.parse(ES_Objectif.getText()).compareTo(sfDate.parse(ES_Passage.getText())) < 0)
							JOptionPane.showMessageDialog(this, Bundle.getText("OO_MaturiteProcessus_objectifAnt"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
					else
					{
						cMaturiteProcessus.modifierMaturiteProcessus(this.identifiantProcessus, ES_Niveau.getValue().toString(), EM_Commentaire.getText(),new Date(sfDate.parse(ES_Passage.getText()).getTime()),new Date(sfDate.parse(ES_Objectif.getText()).getTime()));
						((FP_HAPI) laFenetre).refreshArbre();
					}
				}
				else
				{
					cMaturiteProcessus.modifierMaturiteProcessus(this.identifiantProcessus, ES_Niveau.getValue().toString(), EM_Commentaire.getText(),new Date(sfDate.parse(ES_Passage.getText()).getTime()),null);
					((FP_HAPI) laFenetre).refreshArbre();
				}
			
			
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		catch (NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("OO_MaturiteProcessus_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			ES_Niveau.requestFocus();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void doChange()
	{
		EM_DescriptionNiveau.setText((String) C_DescriptionMaturite.getLesDescriptions().get(new Integer(((Integer)ES_Niveau.getValue()).intValue() - 1)));
		
		//Objectif
		Date dateObjectif = C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,((Integer) ES_Niveau.getValue()).intValue()-1).getDateObjectif();
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		ES_Objectif.setText((dateObjectif==null)?"":sfDate.format(dateObjectif));
		
		//Passage
		Date datePassage = C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,((Integer) ES_Niveau.getValue()).intValue()-1).getDatePassage();
		ES_Passage.setText((datePassage==null)?"":sfDate.format(datePassage));
		
		//DM Précision de la maturité : affectation du titre
		EM_DescriptionNiveau.setText((String) C_DescriptionMaturite.getLesDescriptions().get((new Integer(((Integer)ES_Niveau.getValue()).intValue() - 1))));
		
		//Commentaire
		EM_Commentaire.setText(C_NiveauMaturite.getNiveauMaturite(identifiantProcessus,((Integer) ES_Niveau.getValue()).intValue()-1).getCommentaire());		
	}
}