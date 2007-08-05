package hapi.presentation.composants;

import hapi.application.C_Configuration;
import hapi.application.ressources.Bundle;
import hapi.exception.FichierConfInexistantException;
import hapi.presentation.FP_HAPI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

/**
 * @author Robin & Cécile
 */
public class BarreDeMenu extends JMenuBar
{
	private static final long serialVersionUID = 7727314851903371986L;
    /* Les menus déroulants */
	private JMenu MD_Fichier = null;
	private JMenu MD_Preferences = null;
	private JMenu MD_Langue = null;
	private JMenu MD_Laf = null;
	private JMenu MD_Aide = null;

	/* Groupe des langues */
	private ButtonGroup SC_Radios = null;
	private ButtonGroup SC_RadioLaf = null;

	/* Les items de menu */
	private JMenuItem OM_ImporterProcessus = null;
	private JMenuItem OM_ImporterMesures = null;
	private JMenuItem OM_GestionUtilisateur = null;
	private JMenuItem OM_MotDePasse = null;
	private JMenuItem OM_Quitter = null;
	private JMenuItem OM_APropos = null;
	private JMenuItem OM_Comp_Proc = null;
	private JMenuItem OM_DescriptionMaturite = null;

	private FP_HAPI fenetrePrincipale = null;

	//Ecouteurs
	private ActionListener actionLangue = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSelectionLangue();
		}
	};
	private ActionListener actionLaf = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doSelectionLaf();
		}
	};

	public BarreDeMenu(FP_HAPI fp)
	{
		super();
		this.fenetrePrincipale = fp;
		createMenuBar();
	}

	/**
	 * Methode permettant de créer les éléments du menu et de les ajouter
	 */
	private void createMenuBar()
	{

		// File menu
		this.MD_Fichier = new JMenu();
		this.OM_Quitter = new JMenuItem(fenetrePrincipale.quitterAction);
		this.OM_ImporterProcessus = new JMenuItem(fenetrePrincipale.importerProcessusAction);
		this.OM_ImporterMesures = new JMenuItem(fenetrePrincipale.importerMesuresAction);
		this.OM_Comp_Proc = new JMenuItem(fenetrePrincipale.afficherProcessusComposantsAction);
		this.OM_DescriptionMaturite = new JMenuItem(fenetrePrincipale.associerDescriptionsAction);
		this.add(this.MD_Fichier);
		this.MD_Fichier.add(this.OM_ImporterProcessus);
		this.MD_Fichier.add(this.OM_ImporterMesures);
		this.MD_Fichier.addSeparator();
		this.MD_Fichier.add(this.OM_DescriptionMaturite);
		this.MD_Fichier.add(this.OM_Comp_Proc);
		this.MD_Fichier.addSeparator();
		this.MD_Fichier.add(this.OM_Quitter);

		// Preferences menu
		this.MD_Preferences = new JMenu();
		//this.OM_Configuration = new JMenuItem(fenetrePrincipale.configurationAction);
		this.MD_Langue = new JMenu();
		this.MD_Laf = new JMenu();
		this.OM_GestionUtilisateur = new JMenuItem(fenetrePrincipale.gestionUtilisateurAction);
		this.OM_MotDePasse = new JMenuItem(fenetrePrincipale.motDePasseAction);

		this.add(this.MD_Preferences);
		//this.MD_Preferences.add(this.OM_Configuration);
		this.MD_Preferences.add(this.OM_GestionUtilisateur);
		this.MD_Preferences.add(this.MD_Langue);
		this.MD_Preferences.add(this.MD_Laf);
		this.MD_Preferences.add(this.OM_MotDePasse);

		// Help menu
		this.MD_Aide = new JMenu();
		this.OM_APropos = new JMenuItem(fenetrePrincipale.aProposAction);

		this.add(this.MD_Aide);
		this.MD_Aide.add(this.OM_APropos);

		// Adds tooltip texts
		this.MD_Fichier.setText(Bundle.getText("MENU_Fichier"));
		this.MD_Aide.setText(Bundle.getText("MENU_Aide"));
		this.MD_Preferences.setText(Bundle.getText("MENU_Preferences"));

	} // End of createMenuBar method

	public void updateTexte()
	{
		this.MD_Fichier.setText(Bundle.getText("MENU_Fichier"));
		this.MD_Aide.setText(Bundle.getText("MENU_Aide"));
		this.MD_Preferences.setText(Bundle.getText("MENU_Preferences"));
		this.MD_Langue.setText(Bundle.getText("MENU_Langue"));
		this.MD_Laf.setText(Bundle.getText("MENU_Laf"));
		chargerLangue();
		chargerLaf();
	}

	public void chargerLangue()
	{
		MD_Langue.removeAll();
		//Groupe
		SC_Radios = new ButtonGroup();
		//Radios langue
		for (int i = 0; i < Bundle.getLanguesDisponibles().size(); i++)
		{
			//Récupération de la première des langues disponibles
			String langue = Bundle.getLanguesDisponibles().elementAt(i).toString();
			//Récupération de la langue courante
			String langueCourante = Bundle.getLangueCourante();
			//Création d'un bouton radio
			JRadioButtonMenuItem OO_Langue = new JRadioButtonMenuItem(langue);
			//Ajout au groupe
			SC_Radios.add(OO_Langue);
			//Ajout du listener
			OO_Langue.addActionListener(actionLangue);
			//Si la langue courante correspond à l'élément
			if (langueCourante.equals(langue))
				OO_Langue.setSelected(true);
			//Ajout au panel
			MD_Langue.add(OO_Langue);
		}
	}

	public void chargerLaf()
	{
		MD_Laf.removeAll();
		//Groupe
		SC_RadioLaf = new ButtonGroup();

		//Look And Feel
		for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++)
		{
			//Création d'un bouton radio
			JRadioButtonMenuItem OO_LookAndFeel = new JRadioButtonMenuItem(UIManager.getInstalledLookAndFeels()[i].getName());
			//Ajout au groupe
			SC_RadioLaf.add(OO_LookAndFeel);
			//Ajout du listener
			OO_LookAndFeel.addActionListener(actionLaf);
			//Si le look and feel courant correspond à l'élément
			if (OO_LookAndFeel.getText().equals(UIManager.getLookAndFeel().getName()))
				OO_LookAndFeel.setSelected(true);
			//Ajout au panel
			MD_Laf.add(OO_LookAndFeel);
		}
	}

	public void doSelectionLangue()
	{
		try
		{
			C_Configuration cConfiguration = new C_Configuration(false);

			Enumeration lEnumeration = SC_Radios.getElements();
			fenetrePrincipale.clearOnglets();
			while (lEnumeration.hasMoreElements())
			{
				JRadioButtonMenuItem lItem = (JRadioButtonMenuItem) (lEnumeration.nextElement());
				if (lItem.isSelected())
				{
					cConfiguration.setLangue(lItem.getText());
					break;
				}
			}
			fenetrePrincipale.updateTexte();
			fenetrePrincipale.refreshArbre();
			//fenetrePrincipale.doSelectionArbre();
		}
		catch (FichierConfInexistantException e)
		{
			System.err.println("-----ERREUR GRAVE N°4-----");
		}
	}

	public void doSelectionLaf()
	{
		try
		{
			C_Configuration cConfiguration = new C_Configuration(false);

			Enumeration lEnumeration = SC_RadioLaf.getElements();

			//fenetrePrincipale.clearOnglets();
			while (lEnumeration.hasMoreElements())
			{
				JRadioButtonMenuItem lItem = (JRadioButtonMenuItem) (lEnumeration.nextElement());
				if (lItem.isSelected())
				{
					cConfiguration.setLaf(lItem.getText());
					break;
				}
			}
			fenetrePrincipale.updateTexte();
			//fenetrePrincipale.doSelectionArbre();
		}
		catch (FichierConfInexistantException e)
		{
			System.err.println("-----ERREUR GRAVE N°5-----");
		}
	}

}
