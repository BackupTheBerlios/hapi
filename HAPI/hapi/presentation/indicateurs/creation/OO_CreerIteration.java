/*
 * Fichier OO_CreerIteration.java Auteur Cédric
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.interfaces.FenetreAssistee;
import hapi.application.modele.DateModel;
import hapi.application.modele.NaturelModel;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;

public class OO_CreerIteration extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 2160298042033995208L;
    //Panels
	private JPanel SC_Composant = null;
	private JPanel SC_PanelComposant = null;
	private JPanel SC_Sud = null;
	private JPanel SC_Iteration = null;

	//Espacement
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;

	//Composants
	private JLabel STC_Iteration = null;
	private JLabel STC_Debut = null;
	private JLabel STC_Fin = null;

	//Elements saisis
	private JFormattedTextField ES_Iteration = null;
	private JFormattedTextField ES_Debut = null;
	private JFormattedTextField ES_Fin = null;

	//Layouts
	private BorderLayout layoutComposant = null;
	private BorderLayout layoutFond = null;
	private BorderLayout layoutPanelComposant = null;
	private GridLayout layoutIteration = null;

	public OO_CreerIteration()
	{
		//Affectation de l'identifiant
		operationMoteur();
		//Création des élémnts
		creationElements();
		//Appel de l'interface
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();

		operationSurBoutons();
		// remplissage de la fenêtre
		updateTexte();
	}

	public void creationElements()
	{
		//Panels
		SC_Composant = new JPanel();
		SC_PanelComposant = new JPanel();
		SC_Sud = new JPanel();
		SC_Iteration = new JPanel();

		//Espacement
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		//Composants
		STC_Iteration = new JLabel();
		STC_Debut = new JLabel();
		STC_Fin = new JLabel();

		//Layout
		layoutComposant = new BorderLayout();
		layoutFond = new BorderLayout();
		layoutPanelComposant = new BorderLayout();
		layoutIteration = new GridLayout(3, 2);

		ES_Iteration = new JFormattedTextField();
		ES_Debut = new JFormattedTextField();
		ES_Fin = new JFormattedTextField();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		NaturelModel dn = new NaturelModel(Bundle.naturelFormat);
		ES_Iteration.setFormatterFactory(new DefaultFormatterFactory(dn));
		DateModel dd = new DateModel(Bundle.formatDate);
		ES_Debut.setFormatterFactory(new DefaultFormatterFactory(dd));
		ES_Fin.setFormatterFactory(new DefaultFormatterFactory(dd));

		// affichage des labels statiques
		STC_Iteration.setText(Bundle.getText("OO_TableauDeBordSaisie_Iteration"));
		STC_Debut.setText(Bundle.getText("OO_TableauDeBordSaisie_debut"));
		STC_Fin.setText(Bundle.getText("OO_TableauDeBordSaisie_fin"));
	}

	public void operationSurPanel()
	{
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_PanelComposant, BorderLayout.NORTH);

		SC_PanelComposant.setLayout(layoutPanelComposant);
		SC_PanelComposant.add(SC_Iteration, BorderLayout.NORTH);

		SC_Iteration.setLayout(layoutIteration);
		SC_Iteration.add(STC_Iteration);
		SC_Iteration.add(ES_Iteration);
		SC_Iteration.add(STC_Debut);
		SC_Iteration.add(ES_Debut);
		SC_Iteration.add(STC_Fin);
		SC_Iteration.add(ES_Fin);
	}

	public void operationSurFenetre()
	{
		//Affectation du fond
		this.setLayout(layoutFond);
		//Bordures
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Droite, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Sud, BorderLayout.SOUTH);
		this.add(SC_Composant, BorderLayout.CENTER);
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}

	public ArrayList getParametresSaisis()
	{
		ArrayList retour = new ArrayList();

		retour.add(ES_Iteration.getText());
		retour.add(ES_Debut.getText());
		retour.add(ES_Fin.getText());

		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		if (((Integer) lesParametres.get(0)).intValue() != -1)
		{
			ES_Fin.setEnabled(false);
			ES_Iteration.setEnabled(false);
			ES_Debut.setEnabled(false);
		}
		ES_Iteration.setText((String) lesParametres.get(1));
		ES_Debut.setText((String) lesParametres.get(2));
		ES_Fin.setText((String) lesParametres.get(3));
	}
}