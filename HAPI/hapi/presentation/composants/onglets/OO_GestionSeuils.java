/*
 * Created on 1 févr. 2005
 *
 */
package hapi.presentation.composants.onglets;

import hapi.application.C_GestionSeuils;
import hapi.application.C_Indicateur;
import hapi.application.modele.DecimalModel;
import hapi.application.ressources.Bundle;
import hapi.exception.SeuilNonDefiniException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Natalia
 *
 */
public class OO_GestionSeuils extends JPanel implements FenetreHAPI
{
	private static final long serialVersionUID = -3702140373267585039L;
    // panels
	private JPanel SC_Principal = null;
	private JPanel SC_Centre = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Valeurs = null;
	private JPanel SC_Indicateurs = null;
	private JPanel SC_Blanc = null;

	// layouts
	private BorderLayout L_Fond = null;
	private BorderLayout L_Principal = null;
	private BorderLayout L_Tableau = null;
	private GridLayout L_Indicateurs = null;
	private GridLayout L_Valeurs = null;

	// labels
	private JLabel STC_Min = null;
	private JLabel STC_Max = null;

	// boutons
	private JButton BP_Modifier = null;

	// identifiant du processus courant
	private String idProcessusCourant = null;
	// liste d'indicateurs du processus courant
	private ArrayList indicateurs = null;

	public OO_GestionSeuils(String idProcessus)
	{
		super();
		idProcessusCourant = idProcessus;
		try
		{
			this.creationElements();
			this.operationSurBoutons();
			this.operationSurComposants();
			this.operationSurPanel();
			this.updateTexte();
			this.operationMoteur();
			this.operationSurFenetre();

		}
		catch (Exception e)
		{}
	}

	public void creationElements()
	{
		// création des panels
		SC_Principal = new JPanel();
		SC_Centre = new JPanel();
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();
		SC_Boutons = new JPanel();
		SC_Valeurs = new JPanel();
		SC_Indicateurs = new JPanel();
		SC_Blanc = new JPanel();

		// création des layouts
		L_Fond = new BorderLayout();
		L_Indicateurs = new GridLayout(1, 1);
		L_Principal = new BorderLayout();
		L_Tableau = new BorderLayout();
		L_Valeurs = new GridLayout(1, 2);
		// création des labels (entête du tableau des indicateurs)
		STC_Min = new JLabel();
		STC_Max = new JLabel();
		// création des boutons
		BP_Modifier = new JButton();
	}

	public void operationSurBoutons()
	{
		//ajout des texts et mnemoniques aux boutons
		BP_Modifier.setMnemonic('M');
		BP_Modifier.setText(Bundle.getText("Modifier"));
		BP_Modifier.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				modifier(e);
			}
		});
	}

	public void operationSurComposants()
	{
		// configuration des labels
		STC_Min.setText(Bundle.getText("OO_GestionSeuils_min"));
		STC_Min.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Max.setText(Bundle.getText("OO_GestionSeuils_max"));
		STC_Max.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void operationSurPanel()
	{
		SC_Principal.setLayout(L_Principal);
		SC_Principal.add(SC_Centre, BorderLayout.NORTH);

		SC_Centre.setLayout(L_Tableau); // borderlayout
		SC_Valeurs.setLayout(L_Valeurs); // gridlayout
		SC_Indicateurs.setLayout(L_Indicateurs); // gridlayout		

		SC_Centre.add(SC_Indicateurs, BorderLayout.CENTER);
		SC_Centre.add(SC_Valeurs, BorderLayout.EAST);
		SC_Centre.add(new JPanel(), BorderLayout.SOUTH);
		SC_Indicateurs.add(SC_Blanc);

		SC_Valeurs.add(STC_Min);
		SC_Valeurs.add(STC_Max);

		SC_Boutons.add(BP_Modifier);

	}

	public void operationSurFenetre()
	{
		this.setEnabled(true);
		this.setLocale(java.util.Locale.getDefault());

		this.setLayout(L_Fond);
		this.add(SC_Droit, BorderLayout.EAST);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Principal, BorderLayout.CENTER);
		this.add(SC_Boutons, BorderLayout.SOUTH);
	}

	public void operationMoteur() throws Exception
	{
		// affichage des indicateurs dans le tableau
		indicateurs = C_Indicateur.getIdIndicateurs();

		for (int i = 0; i < indicateurs.size(); i++)
		{
			// ajout d'un ligne au layout
			L_Valeurs.setRows(L_Valeurs.getRows() + 1);
			L_Indicateurs.setRows(L_Indicateurs.getRows() + 1);

			// affichage du nom de l'indicateur
			int id = ((Integer) indicateurs.get(i)).intValue();
			boolean afficher = C_GestionSeuils.existe(idProcessusCourant, id);

			JTextField lbl = new JTextField();
			lbl.setName("STC_Nom" + i);

			lbl.setPreferredSize(new Dimension(275, 23));
			lbl.setText(C_Indicateur.getNom(id) + " " + Bundle.getText(C_Indicateur.getFormule(id) + "_nom") + " :  ");
			lbl.setEditable(false);
			lbl.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			lbl.setToolTipText(Bundle.getText(C_Indicateur.getFormule(id)));
			SC_Indicateurs.add(lbl);

			// affichage du seuil minimum
			JFormattedTextField min = new JFormattedTextField();
			min.setName("ES_Min" + i);
			min.setPreferredSize(new Dimension(90, 23));
			min.setHorizontalAlignment(SwingConstants.RIGHT);
			DecimalModel df = new DecimalModel(Bundle.formatDecimal);
			min.setFormatterFactory(new DefaultFormatterFactory(df));
			SC_Valeurs.add(min);

			// affichage du seuil maximum
			JFormattedTextField max = new JFormattedTextField();
			max.setName("ES_Max" + i);
			max.setPreferredSize(new Dimension(90, 23));
			max.setHorizontalAlignment(SwingConstants.RIGHT);
			max.setFormatterFactory(new DefaultFormatterFactory(df));
			SC_Valeurs.add(max);

			if (afficher)
			{
				try
				{
					Double dMin = C_GestionSeuils.getMin(idProcessusCourant, id);
					if (dMin.doubleValue() != -1)
						min.setText(dMin.toString());
					Double dMax = C_GestionSeuils.getMax(idProcessusCourant, id);
					if (dMax.doubleValue() != -1)
						max.setText(dMax.toString());
				}
				catch (SeuilNonDefiniException e)
				{
					// ne rien faire
				}
			}
		}

	}

	public void updateTexte()
	{}

	private void modifier(ActionEvent e)
	{
		for (int i = 0, j = 2; i < indicateurs.size(); i++, j += 2)
		{
			int id_ind = ((Integer) indicateurs.get(i)).intValue();

			Double min = null, max = null;

			if (!((JTextField) SC_Valeurs.getComponent(j)).getText().equals(""))
			{
				if (((JFormattedTextField) SC_Valeurs.getComponent(j)).getText().indexOf(".") > 0)
					min = new Double((((JFormattedTextField) SC_Valeurs.getComponent(j)).getText()));
				else
					min = (Double) ((((JFormattedTextField) SC_Valeurs.getComponent(j)).getValue()));
			}
			else
			{
				min = new Double(-1.0);
			}
			if (!((JTextField) SC_Valeurs.getComponent(j + 1)).getText().equals(""))
			{
				if (((JFormattedTextField) SC_Valeurs.getComponent(j + 1)).getText().indexOf(".") > 0)
					max = new Double((((JFormattedTextField) SC_Valeurs.getComponent(j + 1)).getText()));
				else
					max = (Double) ((((JFormattedTextField) SC_Valeurs.getComponent(j + 1)).getValue()));
			}
			else
			{
				max = new Double(-1.0);
			}

			if (min.doubleValue() != -1 && max.doubleValue() != -1)
			{
				// si les valeurs sont supérieures à 100			
				if (min.doubleValue() > 100)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_GestionSeuils_erreurTropEleveDebut") + "du minimum pour l'indicateur " + C_Indicateur.getNom(id_ind) + Bundle.getText("OO_GestionSeuils_erreurTropEleveFin"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (max.doubleValue() > 100)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("OO_GestionSeuils_erreurTropEleveDebut") + "du maximum pour l'indicateur " + C_Indicateur.getNom(id_ind) + Bundle.getText("OO_GestionSeuils_erreurTropEleveFin"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
					return;
				}

				// si les valeurs min et max sont saisies :
				// vérification : max > min
				if (min.doubleValue() != -1 && max.doubleValue() != -1)
				{
					if (min.doubleValue() > max.doubleValue())
					{
						JOptionPane.showMessageDialog(this, Bundle.getText("OO_GestionSeuils_erreurSaisieMinMaxDebut") + " " + C_Indicateur.getNom(id_ind) + " " + Bundle.getText("OO_GestionSeuils_erreurSaisieMinMaxFin"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			C_GestionSeuils.modifierSeuil(id_ind, idProcessusCourant, min, max);
		}
		// sauvagarde en base
		try
		{
			C_GestionSeuils.sauvegarderSeuil(idProcessusCourant);
		}
		catch (SQLException e1)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
	}

}
