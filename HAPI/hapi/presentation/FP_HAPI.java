package hapi.presentation;

import hapi.application.C_ArbreDesProcessus;
import hapi.application.C_Configuration;
import hapi.application.C_Hapi;
import hapi.application.C_Utilisateur;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.exception.FichierConfInexistantException;
import hapi.exception.UtilisateurNonIdentifieException;
import hapi.presentation.composants.BarreDeMenu;
import hapi.presentation.composants.BarreOutils;
import hapi.presentation.composants.MC_ExecutionProcessus;
import hapi.presentation.composants.MC_Iteration;
import hapi.presentation.composants.MC_Processus;
import hapi.presentation.composants.onglets.OO_CommentaireProjet;
import hapi.presentation.composants.onglets.OO_DemandesModificationProcessus;
import hapi.presentation.composants.onglets.OO_DescriptifProcessus;
import hapi.presentation.composants.onglets.OO_EvaluationQualitative;
import hapi.presentation.composants.onglets.OO_EvaluationQuantitative;
import hapi.presentation.composants.onglets.OO_GestionSeuils;
import hapi.presentation.composants.onglets.OO_HistoriqueModificationsProcessus;
import hapi.presentation.composants.onglets.OO_ListeProcessus;
import hapi.presentation.composants.onglets.OO_MaturiteProcessus;
import hapi.presentation.composants.onglets.OO_MesureAcces;
import hapi.presentation.composants.onglets.OO_MesureFormation;
import hapi.presentation.composants.onglets.OO_MesureUtilisation;
import hapi.presentation.composants.onglets.OO_RevueProcessus;
import hapi.presentation.composants.onglets.OO_TableauDeBordExecutionProcessus;
import hapi.presentation.composants.onglets.OO_TableauDeBordProcessus;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Fenêtre principale de l'application
 * @author Robin EYSSERIC
 */
public class FP_HAPI extends JFrame implements FenetreHAPI
{

	private static final long serialVersionUID = 7204818422395261914L;
    //Actions sur les items du menu et sur les boutons la barre d'outils */
	public AbstractAction quitterAction = new AbstractAction(Bundle.getText("MENUITEM_Quitter"), C_Hapi.QUITTER_ICONE)
	{
		private static final long serialVersionUID = -5096240270944951150L;

        public void actionPerformed(ActionEvent e)
		{
			doQuitter();
		}
	};
	public AbstractAction importerProcessusAction = new AbstractAction(Bundle.getText("MENUITEM_ImporterProcessus"), C_Hapi.IMPORTER_PROCESSUS_ICONE)
	{
		private static final long serialVersionUID = 8504882443794501480L;

        public void actionPerformed(ActionEvent e)
		{
			doImporterProcessus();
		}
	};
	public AbstractAction importerMesuresAction = new AbstractAction(Bundle.getText("MENUITEM_ImporterMesures"), C_Hapi.IMPORTER_MESURES_ICONE)
	{
		private static final long serialVersionUID = -6960790046347930048L;

        public void actionPerformed(ActionEvent e)
		{
			doImporterMesures();
		}
	};
	public AbstractAction gestionUtilisateurAction = new AbstractAction(Bundle.getText("MENUITEM_GestionUtilisateur"))
	{
		private static final long serialVersionUID = 1558475939767361525L;

        public void actionPerformed(ActionEvent e)
		{
			doGestionUtilisateur();
		}
	};
	public AbstractAction motDePasseAction = new AbstractAction(Bundle.getText("MENUITEM_Utilisateur"))
	{
		private static final long serialVersionUID = 3859333930914071439L;

        public void actionPerformed(ActionEvent e)
		{
			doInfosUtilisateur();
		}
	};
	public AbstractAction aProposAction = new AbstractAction(Bundle.getText("MENUITEM_APropos"), C_Hapi.A_PROPOS_ICONE)
	{
		private static final long serialVersionUID = 7388861488667810386L;

        public void actionPerformed(ActionEvent e)
		{
			doAPropos();
		}
	};
	public AbstractAction afficherProcessusComposantsAction = new AbstractAction(Bundle.getText("MENUITEM_Comp_Proc"))
	{
		private static final long serialVersionUID = -5364358765286583877L;

        public void actionPerformed(ActionEvent e)
		{
			doAfficherCompProc();
		}
	};
	public AbstractAction associerDescriptionsAction = new AbstractAction(Bundle.getText("MENUITEM_AssocierDescription"))
	{
		private static final long serialVersionUID = -4295451629522995676L;

        public void actionPerformed(ActionEvent e)
		{
			doAssocierDescription();
		}
	};		
	//Ecouteur de souris
	private MouseAdapter actionArbre = new MouseAdapter()
	{
		public void mousePressed(MouseEvent arg0)
		{
			if (arg0.getButton() == MouseEvent.BUTTON1) //Si bouton de gauche
			{
				doSelectionArbre(false);
			}
			//Si bouton de droite
			if (arg0.getButton() == MouseEvent.BUTTON3)
			{
				try
				{
					if (C_Utilisateur.getRole() == 1)
					{
						doMenuDeroulantArbre(arg0.getX(), arg0.getY());
					}
				}
				catch (UtilisateurNonIdentifieException e)
				{
					System.err.println("-----ERREUR GRAVE N°3-----");
				}
			}
		}
	};
	//Fermeture de la fenêtre
	private WindowAdapter actionFenetre = new WindowAdapter()
	{
		public void windowClosing(WindowEvent arg0)
		{
			doQuitter();
		}
	};

	//Panel
	private JSplitPane verticalSplit = null;
	private JScrollPane SC_Scroll = null;
	private JTabbedPane SC_Onglet = null;
	//Barres
	private BarreDeMenu barreMenu = null;
	private BarreOutils barreOutils = null;
	//Composants
	private C_ArbreDesProcessus LS_Arbre = null;
	//Variable
	private int roleUtilisateur = -1;
	private C_Hapi cHapi = null;

	public FP_HAPI()
	{
		//Appel au parent
		super();
		//Création des éléments
		creationElements();
		//Appels de l'interface
		operationSurComposants();
		operationSurFenetre();
		operationMoteur();
		//Mise à jour des textes
		updateTexte();
		chargeOngletRacine();
		setVisible(true);
	}

	public void creationElements()
	{
		//Création des panels
		SC_Scroll = new JScrollPane();
		SC_Onglet = new JTabbedPane();
		verticalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		//Création des barres
		barreMenu = new BarreDeMenu(this);
		barreOutils = new BarreOutils(this);
		//Création des composants
		LS_Arbre = new C_ArbreDesProcessus();
	}

	public void operationSurBoutons()
	{}

	public void operationSurComposants()
	{
		//Arbre
		LS_Arbre.setMinimumSize(new Dimension(300, 100));
		LS_Arbre.addMouseListener(actionArbre);
		//Scroll
		SC_Scroll.setAutoscrolls(true);
		SC_Scroll.setViewportView(LS_Arbre);
		//Split
		verticalSplit.setContinuousLayout(true);
		verticalSplit.setLeftComponent(SC_Scroll);
		verticalSplit.setRightComponent(SC_Onglet);
		verticalSplit.setDividerLocation(190);
	}

	public void operationSurPanel()
	{}

	public void operationSurFenetre()
	{
		//Affectation du role
		setRole(roleUtilisateur);
		//Affectation de la barre de menu
		this.setJMenuBar(this.barreMenu);
		//Affectation des panel
		this.getContentPane().add(this.barreOutils, BorderLayout.NORTH);
		this.getContentPane().add(this.verticalSplit, BorderLayout.CENTER);
		//Possibilité de redimensionnement
		this.setResizable(true);
		//Affectation des dimensions de depart
		this.setSize(640, 480);
		//Operation par défaut
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(actionFenetre);
	}

	public void setRole(int leRole)
	{
		roleUtilisateur = leRole;
		//Choix du titre en fonction du role
		switch (roleUtilisateur)
		{
			case 1 : //Responsable processus
				setTitle(Bundle.getText("FP_HAPI_caption") + Bundle.getText("FP_HAPI_caption_resp"));
				importerMesuresAction.setEnabled(true);
				gestionUtilisateurAction.setEnabled(false);
				afficherProcessusComposantsAction.setEnabled(false);
				associerDescriptionsAction.setEnabled(false);
				break;
			case 2 : //Directeur processus
				setTitle(Bundle.getText("FP_HAPI_caption") + Bundle.getText("FP_HAPI_caption_dir"));
				importerMesuresAction.setEnabled(false);
				importerProcessusAction.setEnabled(false);
				gestionUtilisateurAction.setEnabled(true);
				afficherProcessusComposantsAction.setEnabled(true);
				associerDescriptionsAction.setEnabled(true);
				break;
			case 3 : //Ingenieur processus
				setTitle(Bundle.getText("FP_HAPI_caption") + Bundle.getText("FP_HAPI_caption_ing"));
				importerMesuresAction.setEnabled(false);
				importerProcessusAction.setEnabled(false);
				gestionUtilisateurAction.setEnabled(false);
				afficherProcessusComposantsAction.setEnabled(false);
				associerDescriptionsAction.setEnabled(false);
				break;
			default : //par defaut
				setTitle(Bundle.getText("FP_HAPI_caption"));
				break;
		}
	}

	public void operationMoteur()
	{
		//Positionnement
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension tailleFenetre = getSize();
		setLocation((tailleEcran.width - tailleFenetre.width) / 2, (tailleEcran.height - tailleFenetre.height) / 2);
		//Fenêtre d'attente		
		//BM_Splash splash = new BM_Splash(this);
		//splash.show();
		//Création du contrôleur
		try
		{
			try
			{
				cHapi = new C_Hapi();
			}
			catch (FichierConfInexistantException E)
			{
				new BD_ConfigurationCheminLocal(this).setVisible(true);
				cHapi = new C_Hapi();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		//Mise à jour des informations
		updateTexte();
		LS_Arbre.reset();

		//Tentative d'ouverture de la fenêtre d'identification
		try
		{
			new BD_Identification(this).setVisible(true);
		}
		catch (SQLException e1)
		{
			//En cas de problème, modification de la configuration d'accès à la base
			new BD_ConfigurationCheminLocal(this).setVisible(true);
			try
			{
				//Recréation du contrôleur avec les nouveaux paramètres
				cHapi = new C_Hapi();
				//Dernière tentative d'ouverture de la fenêtre d'identification
				new BD_Identification(this).setVisible(true);
			}
			catch (Exception e3)
			{
				//Fin de l'histoire
				System.err.println(e3.getMessage());
				JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		catch (Exception e2)
		{
			//Fin de l'histoire
			System.err.println(e2.getMessage());
			JOptionPane.showMessageDialog(this, Bundle.getText("probleme"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		//A ce niveau, tout va bien, la base est reconnue,
		//Affectation du role
		try
		{
			setRole(C_Utilisateur.getRole());
		}
		catch (UtilisateurNonIdentifieException e2)
		{
			System.exit(0);
		}
		//Remplissage de l'arbre		
		cHapi.refreshArbre(LS_Arbre.getActivityModel());
		//splash.dispose();
	}

	public void updateTexte()
	{
		//Modification du LookAndFeel
		SwingUtilities.updateComponentTreeUI(this);
		new Thread()
		{
			public void run()
			{
				//Caption
				setRole(roleUtilisateur);
				/*Caption des Items de menu et de la barre d'outils */
				importerProcessusAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_ImporterProcessus"));
				importerMesuresAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_ImporterMesures"));
				quitterAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_Quitter"));
				//configurationAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_Configuration"));
				gestionUtilisateurAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_GestionUtilisateur"));
				motDePasseAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_Utilisateur"));
				aProposAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_APropos"));
				afficherProcessusComposantsAction.putValue(AbstractAction.NAME, Bundle.getText("MENUITEM_Comp_Proc"));
				barreMenu.updateTexte();
				barreOutils.updateTexte();
				//Positionnement
				Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension tailleFenetre = getSize();
				setLocation((tailleEcran.width - tailleFenetre.width) / 2, (tailleEcran.height - tailleFenetre.height) / 2);
			}
		}
		.start();
	}

	/* Traitement des actions sur les items de la barre de menu et les boutons de la barre d'outils */
	public void doQuitter()
	{
		//Sauvegarde de la config locale
		try
		{
			new C_Configuration(false).saveConfiguration();
		}
		catch (Exception e)
		{}
		//Fermeture
		dispose();
		//Inutile par l'action défaut, mais juste au cas où
		System.exit(0);
	}

	public void doImporterProcessus()
	{
		new BD_ImporterProcessus(this,true).setVisible(true);
		//Recherche des processus parsés
		cHapi.refreshArbre(LS_Arbre.getActivityModel());
		doSelectionArbre(true);
	}

	public void doImporterMesures()
	{
		new BD_ImporterMesures(this).setVisible(true);
		cHapi.refreshArbre(LS_Arbre.getActivityModel());
		doSelectionArbre(true);
	}

	public void doGestionUtilisateur()
	{
		new BD_GestionUtilisateur(this).setVisible(true);
		selectionnerRacineArbre();
		updateTexte();
		cHapi.refreshArbre(LS_Arbre.getActivityModel());
	}

	public void doInfosUtilisateur()
	{
		new BD_ChangerInformations(this).setVisible(true);
	}

	public void doAPropos()
	{
		new BM_APropos(this).setVisible(true);
	}

	public void doAfficherCompProc()
	{
		new BM_AfficherComposantsProcessus(this).setVisible(true);
	}

	public void doSelectionArbre(boolean reinit)
	{
		this.update(this.getGraphics());
		if (reinit)
				chargeOngletRacine();
		else
		{
			switch (cHapi.getSelectionArbre(LS_Arbre))
			{
				//---------------------------------------------------------
				//Dans le cas où l'utilisateur clique sur la racine
				//--------------------------------------------------------
				case -1 :
					chargeOngletRacine();
					break;
	
					//---------------------------------------------------------
					//Dans le cas où l'utilisateur clique sur un processus
					//--------------------------------------------------------			
				case 0 :
					chargeOngletProcessus(cHapi.getElementSelectionne(LS_Arbre));
					break;
					//---------------------------------------------------------
					//Dans le cas où l'utilisateur clique sur un projet
					//--------------------------------------------------------			
				case 1 :
					chargeOngletExecution();
					break;
					//---------------------------------------------------------
					//Dans le cas où l'utilisateur clique sur une evaluation
					//--------------------------------------------------------								 
				case 2 :
					chargeOngletEvaluation();
					break;
			}
		}
	}

	public void doMenuDeroulantArbre(int x, int y)
	{
		if (cHapi.getSelectionArbre(LS_Arbre) == 0)
			new MC_Processus(this, cHapi.getElementSelectionne(LS_Arbre)).show(LS_Arbre, x, y);
		if (cHapi.getSelectionArbre(LS_Arbre) == 1)
			new MC_ExecutionProcessus(this, cHapi.getElementSelectionne(LS_Arbre), cHapi.getCheminElementSelectionne(LS_Arbre)).show(LS_Arbre, x, y);
		if (cHapi.getSelectionArbre(LS_Arbre) == 2)
			new MC_Iteration(this, cHapi.getElementSelectionne(LS_Arbre), cHapi.getCheminElementSelectionne(LS_Arbre)).show(LS_Arbre, x, y);

	}
	
	public void doAssocierDescription()
	{
		try
		{
			new BD_AssocierDescriptionMaturite(this).setVisible(true);
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public void refreshArbre()
	{
		LS_Arbre.reset();
		cHapi.refreshArbre(LS_Arbre.getActivityModel());
		LS_Arbre.selectionRacine();
		doSelectionArbre(true);
	}

	public void selectionnerRacineArbre()
	{
		LS_Arbre.selectionRacine();
		doSelectionArbre(true);
	}

	/**
	 * Effacement des onglets à distance
	 * Utile pour le changement de langue
	 */
	public void clearOnglets()
	{
		SC_Onglet.removeAll();
	}

	/**
	 * Chargement des onglets associés à la racine de l'arbre
	 */
	public void chargeOngletRacine()
	{
		SC_Onglet.removeAll();
		SC_Onglet.add(new OO_ListeProcessus(), Bundle.getText("OO_ListeProcessus_caption"));
	}

	/**
	 * Chargement des onglets associés au processus
	 */
	public void chargeOngletProcessus(String idProcessus)
	{
		SC_Onglet.removeAll();

		//Mise en place des onglets, dans leur ordre d'apparation
		SC_Onglet.add(null, Bundle.getText("OO_DescriptifProcessus_caption"));
		SC_Onglet.add(null, Bundle.getText("OO_TableauDeBord_caption"));
		SC_Onglet.add(null, Bundle.getText("OO_MaturiteProcessus_caption"));
		SC_Onglet.add(null, Bundle.getText("OO_RevueProcessus_caption"));

		//Création des onglets
		creerDescriptifProcessus(0);
		creerTableauDeBordProcessus(1);
		creerMaturite(2);
		creerRevue(3);

		if (roleUtilisateur != 3)
		{
			SC_Onglet.add(null, Bundle.getText("OO_DemandeModificationProcessus_caption"));
			creerDemandesModifications(4);
			SC_Onglet.add(null, Bundle.getText("OO_HistoriqueModificationProcessus_caption"));
			creerHistoriqueModifications(5);
		}

		//Si l'utilisateur est le responsable du processus
		if (roleUtilisateur == 1)
		{
			//Mise en place des onglets dans leur ordre d'apparation					
			SC_Onglet.add(null, Bundle.getText("OO_GestionSeuils_caption"));

			//Création des onglets
			creerGestionSeuils(6);
		}
		
		if (C_Processus.getProcessus(idProcessus).getNiveauMaturite() > 1)
		{
			if (roleUtilisateur != 3)
			{
				SC_Onglet.add(null, Bundle.getText("OO_MesureAcces_caption"));
				creerMesureAcces((roleUtilisateur==1)?7:6);
				SC_Onglet.add(null, Bundle.getText("OO_MesureFormation_caption"));
				creerMesureFormation((roleUtilisateur==1)?8:7);
				SC_Onglet.add(null, Bundle.getText("OO_MesureUtilisation_caption"));
				creerMesureUtilisation((roleUtilisateur==1)?9:8);			
			}		
		}

	}

	/**
	 * Chargement des onglets associés au projet
	 */
	public void chargeOngletExecution()
	{
		SC_Onglet.removeAll();

		//Mise en place des onglets, dans leur ordre d'apparation
		SC_Onglet.add(null, Bundle.getText("OO_TableauDeBord_caption"));
		SC_Onglet.add(null, Bundle.getText("OO_CommentaireProjet_caption"));

		//Création des onglets
		creerTableauDeBordExecution(0);
		creerCommentaireProjet(1);
	}

	public void chargeOngletEvaluation()
	{
		SC_Onglet.removeAll();
		//Mise en place de l'onglet
		SC_Onglet.add(null, Bundle.getText("OO_Quantitative_caption"));
		SC_Onglet.add(null, Bundle.getText("OO_Qualitative_caption"));

		//Création de l'onglet
		creerQuantitative(0);
		creerQualitative(1);
	}

	/*********************************************************************
	 * 
	 *                    CREATION DES ONGLETS
	 * 
	 *********************************************************************/

	/**
	 * Onglet Tableau de bord de processus
	 */
	public void creerTableauDeBordProcessus(final int numOnglet)
	{
		OO_TableauDeBordProcessus onglet = new OO_TableauDeBordProcessus(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet Tableau de bord d'une exécution de processus
	 */
	public void creerTableauDeBordExecution(final int numOnglet)
	{
		OO_TableauDeBordExecutionProcessus onglet = new OO_TableauDeBordExecutionProcessus(this, cHapi.getElementSelectionne(LS_Arbre), (String) cHapi.getCheminElementSelectionne(LS_Arbre).get(0));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet Descriptif processus
	 */
	public void creerDescriptifProcessus(final int numOnglet)
	{
		OO_DescriptifProcessus onglet = new OO_DescriptifProcessus(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet Demandes de moficiations
	 */
	public void creerDemandesModifications(final int numOnglet)
	{
		OO_DemandesModificationProcessus onglet = new OO_DemandesModificationProcessus(cHapi.getElementSelectionne(LS_Arbre),false);
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet Demandes de moficiations
	 */
	public void creerHistoriqueModifications(final int numOnglet)
	{
		OO_HistoriqueModificationsProcessus onglet = new OO_HistoriqueModificationsProcessus(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet Maturité
	 */
	public void creerMaturite(final int numOnglet)
	{
		OO_MaturiteProcessus onglet = new OO_MaturiteProcessus(this,cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet commentaire projet
	 */
	public void creerCommentaireProjet(final int numOnglet)
	{
		OO_CommentaireProjet onglet = new OO_CommentaireProjet(cHapi.getElementSelectionne(LS_Arbre), cHapi.getCheminElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet gestion des seuils
	 */
	public void creerGestionSeuils(final int numOnglet)
	{
		OO_GestionSeuils onglet = new OO_GestionSeuils(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet évaluation quantitative
	 */
	public void creerQuantitative(final int numOnglet)
	{
		OO_EvaluationQuantitative onglet = new OO_EvaluationQuantitative(this, new Integer(cHapi.getElementSelectionne(LS_Arbre)).intValue(), cHapi.getCheminElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}

	/**
	 * Onglet évaluation qualitative
	 */
	public void creerQualitative(final int numOnglet)
	{
		OO_EvaluationQualitative onglet = new OO_EvaluationQualitative(cHapi.getElementSelectionne(LS_Arbre), cHapi.getCheminElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}
	
	/**
	 * Onglet revue
	 */
	public void creerRevue(final int numOnglet)
	{
		OO_RevueProcessus onglet = new OO_RevueProcessus(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}
	
	/**
	 * Onglet Mesure Acces
	 */
	public void creerMesureAcces(final int numOnglet)
	{
		OO_MesureAcces onglet = new OO_MesureAcces(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}
	
	/**
	 * Onglet Mesure Formation
	 */
	public void creerMesureFormation(final int numOnglet)
	{
		OO_MesureFormation onglet = new OO_MesureFormation(cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}
	
	/**
	 * Onglet Mesure Utilisation
	 */
	public void creerMesureUtilisation(final int numOnglet)
	{
		OO_MesureUtilisation onglet = new OO_MesureUtilisation(this,cHapi.getElementSelectionne(LS_Arbre));
		SC_Onglet.setComponentAt(numOnglet, onglet);
	}	
	/*********************************************************************
	 * 
	 *   A PART DES ONGLETS NE RIEN AJOUTER AU DESSUS DE CE BLOC
	 * 
	 *********************************************************************/
}
