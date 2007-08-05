package hapi.presentation;

import hapi.application.C_Utilisateur;
import hapi.application.importation.C_ImportationProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.modele.FiltreFichier;
import hapi.application.ressources.Bundle;
import hapi.exception.ConnexionException;
import hapi.exception.LoginFTPException;
import hapi.exception.NiveauInsuffisantException;
import hapi.exception.RecupererFichierException;
import hapi.exception.StopActionException;
import hapi.presentation.indicateurs.creation.BD_MesureAmelioration;
import hapi.presentation.indicateurs.creation.BD_MesureVersion;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.xml.sax.SAXException;

/**
 * @author Vincent
 */
public class BD_ImporterProcessus extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 6277360583259434129L;
    //Panels
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Composants = null;
	private JPanel SC_Chemin = null;
	private JPanel SC_Fichiers = null;
	private JPanel SC_Rechercher = null;
	private JScrollPane SC_Scroll = null;
	//Composants
	private JLabel STC_Chemin = null;
	private JButton BP_Parcourir = null;
	private JComboBox LD_Chemin = null;
	private JButton BP_Rechercher = null;
	private JLabel STC_Fichiers = null;
	private JList LS_Fichiers = null;
	//Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	private FlowLayout layoutBoutons = null;
	private BorderLayout layoutChemin = null;
	private FlowLayout layoutRechercher = null;
	private BorderLayout layoutFichier = null;
	//Boutons
	private JButton BP_Importer = null;
	private JButton BP_Annuler = null;
	private JButton BP_Fermer = null; 
	//Variable
	private boolean importationComplete = true;
	private boolean annulationComplete = false;
	private String nomDuFichier = null;
	//Controleur
	private C_ImportationProcessus cImportation = null;
	//Ecouteurs de la fenêtre
	private WindowAdapter actionFermerFenetre = new WindowAdapter()
	{
		public void windowClosing(WindowEvent arg0)
		{
			doFermer();			
		}
	};
	private ActionListener actionImporter = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doImporter();
		}
	};
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doFermer();
		}
	};	
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};
	private ActionListener actionRechercher = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doRechercher();
		}
	};
	private ActionListener actionParcourir = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doParcourir();
		}
	};

	public BD_ImporterProcessus(JFrame parent, boolean importationComplete)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_ImporterProcessus_caption"), true);
		if (! importationComplete)
			setTitle(Bundle.getText("BD_ImporterProcessus_caption2"));
		this.importationComplete = importationComplete;
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
	}
	
	public BD_ImporterProcessus(JDialog parent, boolean importationComplete)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_ImporterProcessus_caption"), true);
		if (! importationComplete)
			setTitle(Bundle.getText("BD_ImporterProcessus_caption2"));		
		this.importationComplete = importationComplete;
		//Création des éléments
		creationElements();
		//Mise à jour des textes
		updateTexte();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Création des Panels
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Boutons = new JPanel();
		SC_Composants = new JPanel();
		SC_Chemin = new JPanel();
		SC_Fichiers = new JPanel();
		SC_Rechercher = new JPanel();
		SC_Scroll = new JScrollPane();
		//Création des Composants
		STC_Chemin = new JLabel();
		BP_Parcourir = new JButton();
		LD_Chemin = new JComboBox();
		BP_Rechercher = new JButton();
		STC_Fichiers = new JLabel();
		LS_Fichiers = new JList();
		//Création des Layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutBoutons = new FlowLayout();
		layoutChemin = new BorderLayout();
		layoutRechercher = new FlowLayout();
		layoutFichier = new BorderLayout();
		//Création des Boutons
		BP_Importer = new JButton();
		BP_Annuler = new JButton();
		BP_Fermer = new JButton();
	}

	public void operationSurBoutons()
	{
		//Annuler
		if (! importationComplete)
		{
			BP_Annuler.setMnemonic(Bundle.getChar("BD_ImporterProcessus_UtiliserCourant_mne"));
			BP_Annuler.setText(Bundle.getText("BD_ImporterProcessus_UtiliserCourant"));			
		}
		else
		{
			BP_Annuler.setMnemonic(Bundle.getChar("Fermer_mne"));
			BP_Annuler.setText(Bundle.getText("Fermer"));
		}
		BP_Annuler.addActionListener(actionAnnuler);
		//Fermer
		if (! importationComplete)
		{
			BP_Fermer.setMnemonic(Bundle.getChar("Annuler_mne"));
			BP_Fermer.setText(Bundle.getText("Annuler"));
			BP_Fermer.addActionListener(actionFermer);
		}		
		//Importer
		BP_Importer.setMnemonic(Bundle.getChar("Importer_mne"));
		BP_Importer.setText(Bundle.getText("Importer"));
		BP_Importer.addActionListener(actionImporter);
	}

	public void operationSurComposants()
	{
		//Liste des fichiers
		STC_Fichiers.setText(Bundle.getText("BD_ImporterProcessus_liste"));
		//Parcourir
		BP_Parcourir.setMnemonic(Bundle.getChar("parcourir_mne"));
		BP_Parcourir.setText(Bundle.getText("parcourir"));
		BP_Parcourir.addActionListener(actionParcourir);
		//Chemin
		STC_Chemin.setText(Bundle.getText("BD_ImporterProcessus_chemin"));
		LD_Chemin.setPreferredSize(new Dimension(350, 24));
		LD_Chemin.setEditable(true);
		LD_Chemin.addActionListener(actionRechercher);
		//Rechercher
		BP_Rechercher.setText(Bundle.getText("BD_ImporterProcessus_rechercher"));
		BP_Rechercher.addActionListener(actionRechercher);
	}

	public void operationSurPanel()
	{
		//Bordures
		//Boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Importer);
		SC_Boutons.add(BP_Annuler);
		if (! importationComplete)
		{
			SC_Boutons.add(BP_Fermer);
		}
		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Fichiers, BorderLayout.CENTER);
		SC_Composants.add(SC_Chemin, BorderLayout.NORTH);
		//Chemin
		SC_Chemin.setLayout(layoutChemin);
		SC_Rechercher.setLayout(layoutRechercher);
		SC_Rechercher.add(BP_Rechercher);
		SC_Chemin.add(STC_Chemin, BorderLayout.NORTH);
		SC_Chemin.add(BP_Parcourir, BorderLayout.EAST);
		SC_Chemin.add(LD_Chemin, BorderLayout.CENTER);
		SC_Chemin.add(SC_Rechercher, BorderLayout.SOUTH);
		//Fichiers
		SC_Fichiers.setLayout(layoutFichier);
		SC_Fichiers.add(STC_Fichiers, BorderLayout.NORTH);
		SC_Fichiers.add(SC_Scroll, BorderLayout.CENTER);
		SC_Scroll.setViewportView(LS_Fichiers);
	}

	public void operationSurFenetre()
	{
		//Affectation du fond
		this.getContentPane().setLayout(layoutFond);
		this.addWindowListener(actionFermerFenetre);
		//Bordures
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		//Composants
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Impossibilité de redimentionnement
		this.setResizable(false);
		//packaging
		pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{
		cImportation = new C_ImportationProcessus();
		if (! importationComplete)
			LS_Fichiers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		else
			LS_Fichiers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		LS_Fichiers.setModel(cImportation.getModeleFichiers());
		LD_Chemin.setModel(cImportation.getModeleChemin());
		doRechercher();
	}

	public void updateTexte()
	{}

	private void doImporter()
	{
		final String lAdresse = (String) LD_Chemin.getSelectedItem();
		cImportation.addChemin(lAdresse);
		cImportation.sauvegarderSelectionChemin(lAdresse);

		//Récupération des fichiers sélectionnés
		final Object lesFichiers[] = LS_Fichiers.getSelectedValues();

		//Création de la fenêtre de téléchargement
		final BM_Telechargement bmTelechargement = new BM_Telechargement(this, Bundle.getText("BD_ImporterProcessus_encours"), cImportation);

		try
		{
			final BD_LoginPwd leLogin = new BD_LoginPwd(this, lAdresse, "DPE");

			//Préparation du traitement			
			Thread leTraitement = new Thread()
			{
				public void run()
				{
					ArrayList idProcMAJ = null;

					for (int i = 0; i < lesFichiers.length; i++)
					{
						//Importation
						File leChemin = new File(lAdresse);
						try
						{
							if (leChemin.isDirectory())
							{
								if (importationComplete)
									idProcMAJ = cImportation.chargerFichier(lesFichiers[i].toString(), lAdresse);
								else
									cImportation.memoriserFichier(lesFichiers[i].toString(), lAdresse);
							}
							else
							{
								try
								{
									if (importationComplete)
										idProcMAJ = cImportation.chargerFichier(lesFichiers[i].toString(), lAdresse, C_Utilisateur.findServeurDPE(lAdresse).getLogin(), C_Utilisateur.findServeurDPE(lAdresse).getMotDePasse());
									else
										cImportation.memoriserFichier(lesFichiers[i].toString(), lAdresse, C_Utilisateur.findServeurDPE(lAdresse).getLogin(), C_Utilisateur.findServeurDPE(lAdresse).getMotDePasse());
								}
								catch (LoginFTPException e)
								{
									leLogin.setVisible(true);
									if (importationComplete)
										idProcMAJ = cImportation.chargerFichier(lesFichiers[i].toString(), (String) LD_Chemin.getSelectedItem(), C_Utilisateur.findServeurDPE(lAdresse).getLogin(), C_Utilisateur.findServeurDPE(lAdresse).getMotDePasse());
									else
										cImportation.memoriserFichier(lesFichiers[i].toString(), (String) LD_Chemin.getSelectedItem(), C_Utilisateur.findServeurDPE(lAdresse).getLogin(), C_Utilisateur.findServeurDPE(lAdresse).getMotDePasse());
								}
							}
						
							if (idProcMAJ != null)
							{
								if (((String) idProcMAJ.get(1)) != null)
								{								
									//Choix de la version du processus
									String numeroVersion = null;
									while (numeroVersion == null || numeroVersion.equals(""))
										numeroVersion = JOptionPane.showInputDialog(bmTelechargement, Bundle.getText("BD_ImporterProcessus_versionMessage")+" \""+C_Processus.getProcessus((String) idProcMAJ.get(1)).getNomSansVersion()+"\" :", Bundle.getText("BD_ImporterProcessus_versionCaption"), JOptionPane.QUESTION_MESSAGE);
									String dateExport = C_Processus.getProcessus((String) idProcMAJ.get(1)).getDateExport();
									C_Processus.enregistrerVersionProcessus((String) idProcMAJ.get(1),dateExport,numeroVersion);
									C_Processus.getProcessus((String) idProcMAJ.get(1)).setnumeroVersion(dateExport,numeroVersion);
									
									try
									{
										new BD_MesureVersion(bmTelechargement, (String) idProcMAJ.get(1));
									}
									catch (StopActionException e)
									{}
								}
							}
							
							if (idProcMAJ != null)
							{
								if (((Boolean) idProcMAJ.get(0)).booleanValue())
								{
									JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("BD_ImporterProcessus_MajModifsVersionProcessus1") + C_Processus.getProcessus((String) idProcMAJ.get(1)).getNomSansVersion() + Bundle.getText("BD_ImporterProcessus_MajModifsVersionProcessus2") + C_Processus.getProcessus((String) idProcMAJ.get(1)).getDateExportFormatee() + Bundle.getText("BD_ImporterProcessus_MajModifsVersionProcessus3"), Bundle.getText("BD_ImporterProcessus_ProcessusMisAJour"), JOptionPane.INFORMATION_MESSAGE);
									new BD_HistoriqueModifications(bmTelechargement, (String) idProcMAJ.get(1));
									try
									{
										new BD_MesureAmelioration(bmTelechargement,(String) idProcMAJ.get(1), false).setVisible(true);
									}
									catch (NiveauInsuffisantException e)
									{}
								}
							}
						}
						catch (ConnexionException e2)
						{
							JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("problemeFTP"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						}
						catch (SAXException saxe)
						{
							JOptionPane.showMessageDialog(bmTelechargement, saxe.getMessage(), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						}
						catch (RecupererFichierException rfe)
						{
							JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("problemeConnexion"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						}
						catch (LoginFTPException lfe)
						{
							JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("problemeConnexion"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						}
						catch (Exception e)
						{
							JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("BD_ImporterProcessus_ProcessusDejaAffecte"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
						}
					}
					//Attente de terminaison
					bmTelechargement.dispose();
				}
			};

			//Nommage du thread
			leTraitement.setName("ImportationProcessus");
			//Démarrage
			leTraitement.start();
			//Affichage
			bmTelechargement.setVisible(true);
			//Message d'avertissement pour la conservation du dpe
			if (importationComplete)
				JOptionPane.showMessageDialog(this, Bundle.getText("BD_ImporterProcessus_ConservationDPE"), Bundle.getText("Attention"), JOptionPane.INFORMATION_MESSAGE);

		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}

		//Femeture de la fenêtre
		bmTelechargement.dispose();
		dispose();
	}

	private void doAnnuler()
	{
		if (! importationComplete)
		{
			if (JOptionPane.showConfirmDialog(this, Bundle.getText("BD_ImporterProcessus_UtilisationDPE"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				dispose();
		}
		else
			dispose();
	}

	private void doRechercher()
	{
		String lAdresse = (String) LD_Chemin.getSelectedItem();
		if (lAdresse != null)
			if (!lAdresse.equals(""))
			{
				cImportation.addChemin(lAdresse);
				File leChemin = new File((String) LD_Chemin.getSelectedItem());
				cImportation.effacerListeFichiers();
				try
				{
					if (leChemin.isDirectory())
					{
						//récupération de la liste des fichiers
						cImportation.getListeFichiersLocal("dpe", lAdresse, "", "");
						cImportation.getListeFichiersLocal("DPE", lAdresse, "", "");
					}
					else
					{
						//récupération de la liste des fichiers
						try
						{
							cImportation.getListeFichiers("dpe", lAdresse, cImportation.getLogin(lAdresse), cImportation.getPWD(lAdresse));
							cImportation.getListeFichiers("DPE", lAdresse, cImportation.getLogin(lAdresse), cImportation.getPWD(lAdresse));
						}
						catch (LoginFTPException e)
						{
							new BD_LoginPwd(this, lAdresse, "DPE").setVisible(true);
							cImportation.getListeFichiers("dpe", lAdresse, cImportation.getLogin(lAdresse), cImportation.getPWD(lAdresse));
							cImportation.getListeFichiers("DPE", lAdresse, cImportation.getLogin(lAdresse), cImportation.getPWD(lAdresse));
						}

					}
				}
				catch (ConnexionException e2)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("problemeFTP"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e3)
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("problemeConnexion"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
				}
			}
		if (nomDuFichier != null)
			LS_Fichiers.setSelectedIndex(cImportation.getModeleFichiers().getIndexOf(nomDuFichier));
		nomDuFichier = null;
	}

	private void doParcourir()
	{
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogTitle(Bundle.getText("BD_ImporterProcessus_parcourir"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FiltreFichier("dpe", Bundle.getText("DPE")));
		int res = fileChooser.showDialog(this, Bundle.getText("Valider"));
		if (res == JFileChooser.APPROVE_OPTION)
		{
			//Vérification si le chemin contient le nom du fichier
			nomDuFichier = null;
			if (fileChooser.getSelectedFile().isFile())
			{
				cImportation.addChemin(fileChooser.getSelectedFile().getAbsolutePath().substring(0, fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
				LD_Chemin.setSelectedItem(fileChooser.getSelectedFile().getAbsolutePath().substring(0, fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
				nomDuFichier = fileChooser.getSelectedFile().getName();
			}
			else
			{
				cImportation.addChemin(fileChooser.getSelectedFile().getAbsolutePath());
				LD_Chemin.setSelectedItem(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}

		doRechercher();
	}
	
	private void doFermer()
	{
		annulationComplete = true;
		dispose();
	}
	
	public boolean getAnnulationComplete()
	{
		return annulationComplete;
	}
}