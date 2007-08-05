/*
 * Created on 9 mars 2005
 */
package hapi.presentation.indicateurs.creation;

import hapi.application.C_Mesures;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.modele.DecimalModel;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Natalia
 */
public class OO_RTP extends FenetreAssistee implements FenetreHAPI
{
	private static final long serialVersionUID = 4259592068087580555L;
    private JPanel SC_Composants = null;
	private JPanel SC_Tableau = null;
	private JPanel SC_NomsComposants = null;
	private JPanel SC_Charge = null;

	// panels de mise en forme
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droit = null;

	private JLabel STC_NomDuComposant = null;
	private JLabel STC_ChargeEnMinute = null;

	private BorderLayout layoutfont = null;
	private BorderLayout layoutcomposants = null;
	private GridLayout layoutnomscomposants = null;
	private GridLayout layoutcharge = null;
	private BorderLayout layouttableau = null;

	private JScrollPane SC_Scroll = null;

	private int idMesureP = -1;
	private boolean utiliseCourantP = false;
	private String idProcessusP = null;
	private HashMap listeDesComposantsP = null;

	public OO_RTP()
	{
		this.creationElements();
		this.operationSurComposants();
		this.operationSurPanel();
		this.operationSurBoutons();
		this.operationMoteur();
		this.updateTexte();
		this.operationSurFenetre();

	}

	public void creationElements()
	{
		SC_Composants = new JPanel();
		SC_Tableau = new JPanel();
		SC_Charge = new JPanel();
		SC_NomsComposants = new JPanel();

		// panels de mise en forme
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droit = new JPanel();

		STC_NomDuComposant = new JLabel();
		STC_ChargeEnMinute = new JLabel();

		// layouts
		layoutfont = new BorderLayout();
		layoutnomscomposants = new GridLayout();
		layoutcharge = new GridLayout();
		layoutcomposants = new BorderLayout();
		layouttableau = new BorderLayout();

		SC_Scroll = new JScrollPane();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		layoutnomscomposants.setColumns(1);
		layoutcharge.setColumns(2);

		STC_NomDuComposant.setText(Bundle.getText("OO_TableauDeBord_NomDuComposant"));
		STC_NomDuComposant.setBorder(BorderFactory.createTitledBorder(""));
		STC_NomDuComposant.setHorizontalAlignment(SwingConstants.CENTER);
		STC_NomDuComposant.setPreferredSize(new Dimension(350, 21));

		STC_ChargeEnMinute.setText(Bundle.getText("OO_TableauDeBord_Charge"));
		STC_ChargeEnMinute.setBorder(BorderFactory.createTitledBorder(""));
		STC_ChargeEnMinute.setHorizontalAlignment(SwingConstants.CENTER);
		STC_ChargeEnMinute.setPreferredSize(new Dimension(100, 21));

		//SC_Scroll.setBorder(SC_TitreScroll);
		SC_Scroll.setViewportView(SC_Composants);
	}

	public void operationSurPanel()
	{
		SC_NomsComposants.setLayout(layoutnomscomposants);
		SC_Charge.setLayout(layoutcharge);

		SC_NomsComposants.add(STC_NomDuComposant);
		SC_Charge.add(STC_ChargeEnMinute);
		SC_Charge.add(new JPanel());

		SC_Tableau.setLayout(layouttableau);
		SC_Tableau.add(SC_NomsComposants, BorderLayout.CENTER);
		SC_Tableau.add(SC_Charge, BorderLayout.EAST);

		SC_Composants.setLayout(layoutcomposants);
		SC_Composants.add(SC_Tableau, BorderLayout.NORTH);
	}

	public void operationSurFenetre()
	{
		this.setLayout(layoutfont);
		this.add(SC_Scroll, BorderLayout.CENTER);
		this.add(SC_Haut, BorderLayout.NORTH);
		this.add(SC_Bas, BorderLayout.SOUTH);
		this.add(SC_Gauche, BorderLayout.WEST);
		this.add(SC_Droit, BorderLayout.EAST);
	}

	public void operationMoteur()
	{

	}

	public void updateTexte()
	{}

	private HashMap compileRTP()
	{
		// liste des composants et de la charge de temps associée
		// structure <idComposant, charge >
		HashMap chargeParComposant = new HashMap();
		// récupération des valeurs des champs
		int i = 2;
		// recherche du temps passé par composant
		for (Iterator it = listeDesComposantsP.keySet().iterator(); it.hasNext(); i += 2)
		{
			String idComp = it.next().toString();

			// si rien n'est saisi ou si la caleur saisie est 0, on n'enregistre
			// pas le composant et sa charge

			Double chargeComposant = (Double) ((JFormattedTextField) SC_Charge.getComponent(i)).getValue();
			//transformation en minutes
			if (chargeComposant != null)
			{
				switch (((JComboBox) SC_Charge.getComponent(i + 1)).getSelectedIndex())
				{
					//Heures
					case 1:
						chargeComposant = new Double(chargeComposant.floatValue() * new Double(60).floatValue());
						break;
					//Jours
					case 2:
						chargeComposant = new Double(chargeComposant.floatValue() * new Double(60).floatValue() * new Double(24).floatValue());
						break;
				}

				if (chargeComposant.floatValue() > 0.0)
				{
					chargeParComposant.put(idComp, chargeComposant);
				}
			}
		}
		return chargeParComposant;
	}

	public HashMap getListeDesComposants()
	{
		return compileRTP();
	}

	public ArrayList getParametresSaisis()
	{
		ArrayList lesComposants = new ArrayList();

		for (int i = 1; i < SC_NomsComposants.getComponentCount(); i++)
		{
			ArrayList uneLigne = new ArrayList();
			uneLigne.add(SC_NomsComposants.getComponent(i));
			uneLigne.add(SC_Charge.getComponent(2 * i));
			uneLigne.add(SC_Charge.getComponent(2 * i + 1));

			lesComposants.add(uneLigne);
		}

		ArrayList retour = new ArrayList();

		retour.add(idProcessusP);
		retour.add(new Integer(idMesureP));
		retour.add(new Boolean(utiliseCourantP));
		retour.add(lesComposants);
		retour.add(compileRTP());

		return retour;
	}

	public void setParametres(ArrayList lesParametres)
	{
		if (lesParametres.size() != 5)
		{
			idProcessusP = (String) lesParametres.get(0);
			idMesureP = ((Integer) lesParametres.get(1)).intValue();
			utiliseCourantP = ((Boolean) lesParametres.get(2)).booleanValue();

			if (idMesureP == -1)
			{
				// récupération de la liste des composants du processus courant
				if (utiliseCourantP)
					listeDesComposantsP = C_Composant.getComposantsDuProcessus(idProcessusP);
				else
					listeDesComposantsP = C_ComposantTemporaire.getComposants();
			}
			else
			{
				HashMap chargesDesTachesDesComposants_tmp = new HashMap();
				// parcours de la liste des activites de l'itération
				// construction d'une liste de taches pas composant
				for (Iterator it = C_Mesures.getMesure(idMesureP).getListeActivites().keySet().iterator(); it.hasNext();)
				{
					String idAct = it.next().toString();
					if (!idAct.equals("-1")) // -1 cas des tâches hors processus
					{
						String idDef = C_Activite.getActivite(C_ProcessusTemporaire.get().getIdentifiant(), idAct).getAgregatDefinitionTravail();
						String idComp = C_Definition.getDefinition(C_ProcessusTemporaire.get().getIdentifiant(), idDef).getAgregatComposant();
						// si aucune entrée pour le composant n'existe ...
						if (!chargesDesTachesDesComposants_tmp.containsKey(idComp))
						{
							// ... la créer
							chargesDesTachesDesComposants_tmp.put(idComp, new ArrayList());
						}
						// dans tous les cas ajouter la liste des tâches
						((ArrayList) chargesDesTachesDesComposants_tmp.get(idComp)).addAll((ArrayList) C_Mesures.getMesure(idMesureP).getListeActivites().get(idAct));
					}
				}

				// construction de la hashmap finale
				listeDesComposantsP = new HashMap();

				// pour chaque composant de la hashmap précédente
				for (Iterator it = chargesDesTachesDesComposants_tmp.keySet().iterator(); it.hasNext();)
				{
					String idComp = it.next().toString();
					// calcul de la charge
					float chargeDuComposant = 0;
					for (Iterator ittache = ((ArrayList) chargesDesTachesDesComposants_tmp.get(idComp)).iterator(); ittache.hasNext();)
					{
						chargeDuComposant += C_Mesures.getTempsPasse(ittache.next());
					}
					listeDesComposantsP.put(idComp, new Float(chargeDuComposant));
				}

				//Il faut travaille avec les temporaires, car ce sont eux qui
				// contiennt les infos du parsage
				for (Iterator it = C_ComposantTemporaire.getComposants().keySet().iterator(); it.hasNext();)
				{
					String cle = it.next().toString();
					if (!listeDesComposantsP.containsKey(cle))
					{
						listeDesComposantsP.put(cle, new Float(-1));
					}
				}
			}

			int i = 0;
			layoutnomscomposants.setRows(listeDesComposantsP.size() + 1);

			layoutcharge.setRows(listeDesComposantsP.size() + 1);

			// pour chaque composant ...
			for (Iterator it = listeDesComposantsP.keySet().iterator(); it.hasNext(); i++)
			{
				String idComp = it.next().toString();

				// création du composant contenant le nom du composant
				JTextField comp = new JTextField();
				comp.setName("STV_Comp" + i);
				if (utiliseCourantP && idMesureP == -1)
					comp.setText(C_Composant.getComposant(idProcessusP, idComp).getNom() + " :  ");
				else
					comp.setText(C_ComposantTemporaire.getComposant(idComp).getNom() + " :  ");
				comp.setEditable(false);
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
				// ajout du composant au panel
				SC_NomsComposants.add(comp);

				// création du composant contenant la charge du composant
				JFormattedTextField charge = new JFormattedTextField();
				charge.setName("STV_Charge" + i);
				charge.setHorizontalAlignment(SwingConstants.RIGHT);
				DecimalModel df = new DecimalModel(Bundle.formatDecimal);
				charge.setFormatterFactory(new DefaultFormatterFactory(df));
				// ajout du composant au panel
				SC_Charge.add(charge);

				// création du composant contenant l'unité de charge
				JComboBox unite = new JComboBox(C_Mesures.createModeleUnite());
				unite.setName("LD_Unite" + i);
				// ajout du composant au panel
				SC_Charge.add(unite);

				if (idMesureP != -1)
				{
					float valeur = ((Float) listeDesComposantsP.get(idComp)).floatValue();
					if (valeur != -1)
					{
						charge.setValue(new Double(valeur));
						unite.getModel().setSelectedItem(Bundle.getText("BD_CreerEvaluation_Minute"));
					}
				}
			}
		}
		else
		{
			idProcessusP = (String) lesParametres.get(0);
			idMesureP = ((Integer) lesParametres.get(1)).intValue();
			utiliseCourantP = ((Boolean) lesParametres.get(2)).booleanValue();

			if (idMesureP == -1)
			{
				// récupération de la liste des composants du processus courant
				if (utiliseCourantP)
					listeDesComposantsP = C_Composant.getComposantsDuProcessus(idProcessusP);
				else
					listeDesComposantsP = C_ComposantTemporaire.getComposants();
			}
			else
			{
				HashMap chargesDesTachesDesComposants_tmp = new HashMap();
				// parcours de la liste des activites de l'itération
				// construction d'une liste de taches pas composant
				for (Iterator it = C_Mesures.getMesure(idMesureP).getListeActivites().keySet().iterator(); it.hasNext();)
				{
					String idAct = it.next().toString();
					if (!idAct.equals("-1")) // -1 cas des tâches hors processus
					{
						String idDef = C_Activite.getActivite(C_ProcessusTemporaire.get().getIdentifiant(), idAct).getAgregatDefinitionTravail();
						String idComp = C_Definition.getDefinition(C_ProcessusTemporaire.get().getIdentifiant(), idDef).getAgregatComposant();
						// si aucune entrée pour le composant n'existe ...
						if (!chargesDesTachesDesComposants_tmp.containsKey(idComp))
						{
							// ... la créer
							chargesDesTachesDesComposants_tmp.put(idComp, new ArrayList());
						}
						// dans tous les cas ajouter la liste des tâches
						((ArrayList) chargesDesTachesDesComposants_tmp.get(idComp)).addAll((ArrayList) C_Mesures.getMesure(idMesureP).getListeActivites().get(idAct));
					}
				}

				// construction de la hashmap finale
				listeDesComposantsP = new HashMap();

				// pour chaque composant de la hashmap précédente
				for (Iterator it = chargesDesTachesDesComposants_tmp.keySet().iterator(); it.hasNext();)
				{
					String idComp = it.next().toString();
					// calcul de la charge
					float chargeDuComposant = 0;
					for (Iterator ittache = ((ArrayList) chargesDesTachesDesComposants_tmp.get(idComp)).iterator(); ittache.hasNext();)
					{
						chargeDuComposant += C_Mesures.getTempsPasse(ittache.next());
					}
					listeDesComposantsP.put(idComp, new Float(chargeDuComposant));
				}

				//Il faut travaille avec les temporaires, car ce sont eux qui
				// contiennt les infos du parsage
				for (Iterator it = C_ComposantTemporaire.getComposants().keySet().iterator(); it.hasNext();)
				{
					String cle = it.next().toString();
					if (!listeDesComposantsP.containsKey(cle))
					{
						listeDesComposantsP.put(cle, new Float(-1));
					}
				}
			}

			ArrayList listeDesLignes = (ArrayList) lesParametres.get(3);

			int i = 0;
			layoutnomscomposants.setRows(listeDesLignes.size() + 1);

			layoutcharge.setRows(listeDesLignes.size() + 1);

			for (Iterator it = listeDesLignes.iterator(); it.hasNext(); i++)
			{
				ArrayList uneLigne = (ArrayList) it.next();

				// ajout du composant au panel
				SC_NomsComposants.add((JTextField) uneLigne.get(0));

				// ajout du composant au panel
				SC_Charge.add((JFormattedTextField) uneLigne.get(1));

				// ajout du composant au panel
				SC_Charge.add((JComboBox) uneLigne.get(2));
			}
		}
	}

}
