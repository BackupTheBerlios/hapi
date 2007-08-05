package hapi.presentation;

import hapi.application.C_ExportationCommentaire;
import hapi.application.C_Utilisateur;
import hapi.application.modele.FiltreFichier;
import hapi.application.ressources.Bundle;
import hapi.exception.ConnexionException;
import hapi.exception.LoginFTPException;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Vincent
 */
public class BD_Exporter extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 1746426574640062793L;
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
	private JButton BP_Creer = null;
	private JButton BP_Ecraser = null;
	private JButton BP_Annuler = null;
	//Controleur
	private C_ExportationCommentaire cExportation = null;
	private String idProjet = null;
	private String idProcessus = null;

	//Ecouteurs de la fenêtre
	private ActionListener actionCreer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doCreer();
		}
	};
	private ActionListener actionEcraser = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doEcraser();
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
	private ListSelectionListener actionClic = new ListSelectionListener()
	{
		public void valueChanged(ListSelectionEvent arg0)
		{
			doClic();
		}
	};

	public BD_Exporter(JFrame parent, String lIdProjet, String lIdProcessus)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_Exporter_caption"), true);
		idProjet = lIdProjet;
		idProcessus = lIdProcessus;
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
		BP_Creer = new JButton();
		BP_Ecraser = new JButton();
		BP_Annuler = new JButton();
	}

	public void operationSurBoutons()
	{
		//Annuler
		BP_Annuler.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Annuler.setText(Bundle.getText("Fermer"));
		BP_Annuler.addActionListener(actionAnnuler);
		//Creer
		BP_Creer.setMnemonic(Bundle.getChar("BD_Exporter_creer_mne"));
		BP_Creer.setText(Bundle.getText("BD_Exporter_creer"));
		BP_Creer.addActionListener(actionCreer);
		//Ecraser
		BP_Ecraser.setMnemonic(Bundle.getChar("BD_Exporter_ecraser_mne"));
		BP_Ecraser.setText(Bundle.getText("BD_Exporter_ecraser"));
		BP_Ecraser.addActionListener(actionEcraser);
	}

	public void operationSurComposants()
	{
		//Liste des fichiers
		STC_Fichiers.setText(Bundle.getText("BD_Exporter_liste"));
		//Parcourir
		BP_Parcourir.setMnemonic(Bundle.getChar("parcourir_mne"));
		BP_Parcourir.setText(Bundle.getText("parcourir"));
		BP_Parcourir.addActionListener(actionParcourir);
		//Chemin
		STC_Chemin.setText(Bundle.getText("BD_Exporter_chemin"));
		LD_Chemin.setPreferredSize(new Dimension(350, 24));
		LD_Chemin.setEditable(true);
		LD_Chemin.addActionListener(actionRechercher);
		//Rechercher
		BP_Rechercher.setText(Bundle.getText("BD_Exporter_rechercher"));
		BP_Rechercher.addActionListener(actionRechercher);
		//Liste des fichiers
		LS_Fichiers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		LS_Fichiers.addListSelectionListener(actionClic);
	}

	public void operationSurPanel()
	{
		//Bordures
		//Boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Creer);
		SC_Boutons.add(BP_Ecraser);
		SC_Boutons.add(BP_Annuler);
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
		cExportation = new C_ExportationCommentaire(idProjet, idProcessus);
		LS_Fichiers.setModel(cExportation.getModeleFichiers());
		LD_Chemin.setModel(cExportation.getModeleChemin());
		BP_Ecraser.setEnabled(false);
		doRechercher();
	}

	public void updateTexte()
	{}

	private void doExporter(final String nomDuFichier)
	{
		final String lAdresse = (String) LD_Chemin.getSelectedItem();
		cExportation.addChemin(lAdresse);
		cExportation.sauvegarderSelectionChemin(lAdresse);

		//Création de la fenêtre de téléchargement
		final BM_Telechargement bmTelechargement = new BM_Telechargement(this, Bundle.getText("BD_Exporter_encours"), cExportation);

		try
		{

			final BD_LoginPwd leLogin = new BD_LoginPwd(this, lAdresse, "DOM");

			//Préparation du traitement			
			Thread leTraitement = new Thread()
			{
				public void run()
				{
					//Exportation
					File leChemin = new File(lAdresse);
					try
					{
						if (leChemin.isDirectory())
						{
							cExportation.uploaderFichier(nomDuFichier, lAdresse);
						}
						else
						{
							try
							{
								cExportation.uploaderFichier(nomDuFichier, lAdresse, C_Utilisateur.findServeurDOM(lAdresse).getLogin(), C_Utilisateur.findServeurDOM(lAdresse).getMotDePasse());
							}
							catch (LoginFTPException e)
							{
								leLogin.setVisible(true);
								cExportation.uploaderFichier(nomDuFichier.toString(), (String) LD_Chemin.getSelectedItem(), C_Utilisateur.findServeurDOM(lAdresse).getLogin(), C_Utilisateur.findServeurDOM(lAdresse).getMotDePasse());
							}
						}
					}
					catch (ConnexionException e2)
					{
						JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("problemeFTP"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception e3)
					{
						JOptionPane.showMessageDialog(bmTelechargement, Bundle.getText("problemeConnexion"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
					}
					//Attente de terminaison
					bmTelechargement.dispose();
				}
			};

			//Nommage du thread
			leTraitement.setName("ExportationCommentaire");
			//Démarrage
			leTraitement.start();
			//Affichage
			bmTelechargement.setVisible(true);

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
		dispose();
	}

	private void doRechercher()
	{
		String lAdresse = (String) LD_Chemin.getSelectedItem();
		if (lAdresse != null)
			if (!lAdresse.equals(""))
			{
				cExportation.addChemin(lAdresse);
				File leChemin = new File((String) LD_Chemin.getSelectedItem());
				cExportation.effacerListeFichiers();
				try
				{
					if (leChemin.isDirectory())
					{
						//récupération de la liste des fichiers
						cExportation.getListeFichiersLocal("dom", lAdresse, "", "");
						cExportation.getListeFichiersLocal("DOM", lAdresse, "", "");
					}
					else
					{
						//récupération de la liste des fichiers
						try
						{
							cExportation.getListeFichiers("dom", lAdresse, cExportation.getLogin(lAdresse), cExportation.getPWD(lAdresse));
							cExportation.getListeFichiers("DOM", lAdresse, cExportation.getLogin(lAdresse), cExportation.getPWD(lAdresse));
						}
						catch (LoginFTPException e)
						{
							new BD_LoginPwd(this, lAdresse, "DOM").setVisible(true);
							cExportation.getListeFichiers("dom", lAdresse, cExportation.getLogin(lAdresse), cExportation.getPWD(lAdresse));
							cExportation.getListeFichiers("DOM", lAdresse, cExportation.getLogin(lAdresse), cExportation.getPWD(lAdresse));
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
	}

	private void doParcourir()
	{
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogTitle(Bundle.getText("BD_Exporter_parcourir"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FiltreFichier("dom", Bundle.getText("DOM")));
		int res = fileChooser.showDialog(this, Bundle.getText("Valider"));
		if (res == JFileChooser.APPROVE_OPTION)
		{
			//Vérification si le chemin contient le nom du fichier
			if (fileChooser.getSelectedFile().isFile())
			{
				cExportation.addChemin(fileChooser.getSelectedFile().getAbsolutePath().substring(0, fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
				LD_Chemin.setSelectedItem(fileChooser.getSelectedFile().getAbsolutePath().substring(0, fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
			}
			else
			{
				cExportation.addChemin(fileChooser.getSelectedFile().getAbsolutePath());
				LD_Chemin.setSelectedItem(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}

		doRechercher();
	}

	private void doCreer()
	{
		String nomDuFichier = JOptionPane.showInputDialog(this, Bundle.getText("BD_Exporter_input"));
		if (!((nomDuFichier == null) || (nomDuFichier.equals(""))))
		{
			nomDuFichier = nomDuFichier + ".dom";
			File unFichier = new File((String) LD_Chemin.getSelectedItem() + File.separator + nomDuFichier);
			if (unFichier.isFile())
			{
				if (JOptionPane.showConfirmDialog(this, Bundle.getText("BD_Exporter_existe"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
					doExporter(nomDuFichier);
			}
			else
				doExporter(nomDuFichier);
		}
	}

	private void doEcraser()
	{
		doExporter((String) LS_Fichiers.getSelectedValue());

	}

	private void doClic()
	{
		BP_Ecraser.setEnabled(LS_Fichiers.getSelectedIndex() != -1);
	}
}