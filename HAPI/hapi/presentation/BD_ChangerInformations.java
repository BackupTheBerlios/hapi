package hapi.presentation;

import hapi.application.C_ChangerInformations;
import hapi.application.C_Utilisateur;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Bouhours
 */
public class BD_ChangerInformations extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 7339303616251344412L;
    //Panels
	private JPanel SC_Composants = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Champs = null;
	private JPanel SC_Labels = null;
	//Composants
	private JLabel STC_Ancien = null;
	private JLabel STC_Nouveau = null;
	private JLabel STC_Redonne = null;
	private JLabel STC_Nom = null;
	private JLabel STC_Prenom = null;
	private JPasswordField ES_Ancien = null;
	private JPasswordField ES_Nouveau = null;
	private JPasswordField ES_Redonne = null;
	private JTextField ES_Nom = null;
	private JTextField ES_Prenom = null;
	//Boutons
	private JButton BP_Valider = null;
	private JButton BP_Annuler = null;
	//Layout
	private BorderLayout layoutFond = null;
	private FlowLayout layoutBoutons = null;
	private GridLayout layoutChamps = null;
	private GridLayout layoutLabels = null;
	private BorderLayout layoutComposants = null;
	//Ecouteurs de la fenêtre
	private ActionListener actionOK = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doValider();
		}
	};
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};

	public BD_ChangerInformations(Frame parent)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_ChangerInformations_caption"), true);
		//Création des éléments
		creationElements();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationMoteur();
		operationSurFenetre();
	}

	public void creationElements()
	{
		//Panels
		SC_Composants = new JPanel();
		SC_Boutons = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Haut = new JPanel();
		SC_Champs = new JPanel();
		SC_Labels = new JPanel();
		//Composants
		STC_Ancien = new JLabel();
		STC_Nouveau = new JLabel();
		STC_Redonne = new JLabel();
		STC_Nom = new JLabel();
		STC_Prenom = new JLabel();
		ES_Ancien = new JPasswordField();
		ES_Nouveau = new JPasswordField();
		ES_Redonne = new JPasswordField();
		ES_Nom = new JTextField();
		ES_Prenom = new JTextField();
		//Boutons
		BP_Valider = new JButton();
		BP_Annuler = new JButton();
		//Layout
		layoutFond = new BorderLayout();
		layoutBoutons = new FlowLayout();
		layoutChamps = new GridLayout(5, 1);
		layoutLabels = new GridLayout(5, 1);
		layoutComposants = new BorderLayout();
	}

	public void operationSurBoutons()
	{
		BP_Valider.setText(Bundle.getText("Valider"));
		BP_Valider.setMnemonic(Bundle.getChar("Valider_mne"));
		BP_Valider.addActionListener(actionOK);
		BP_Annuler.setText(Bundle.getText("Annuler"));
		BP_Annuler.setMnemonic(Bundle.getChar("Annuler_mne"));
		BP_Annuler.addActionListener(actionAnnuler);
	}

	public void operationSurComposants()
	{
		STC_Ancien.setText(Bundle.getText("BD_ChangerInformations_ancien"));
		ES_Ancien.setPreferredSize(new Dimension(100, 25));
		ES_Ancien.addActionListener(actionOK);
		STC_Nouveau.setText(Bundle.getText("BD_ChangerInformations_nouveau"));
		ES_Nouveau.setPreferredSize(new Dimension(100, 25));
		ES_Nouveau.addActionListener(actionOK);
		STC_Redonne.setText(Bundle.getText("BD_ChangerInformations_redonne"));
		ES_Redonne.setPreferredSize(new Dimension(100, 25));
		ES_Redonne.addActionListener(actionOK);
		STC_Nom.setText(Bundle.getText("BD_ChangerInformations_nom"));
		ES_Nom.setPreferredSize(new Dimension(100, 25));
		ES_Nom.addActionListener(actionOK);
		STC_Prenom.setText(Bundle.getText("BD_ChangerInformations_prenom"));
		ES_Prenom.setPreferredSize(new Dimension(100, 25));
		ES_Prenom.addActionListener(actionOK);
	}

	public void operationSurPanel()
	{
		//Boutons
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Valider);
		SC_Boutons.add(BP_Annuler);
		//Composants
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Champs, BorderLayout.CENTER);
		SC_Composants.add(SC_Labels, BorderLayout.WEST);
		//Champs
		SC_Champs.setLayout(layoutChamps);
		SC_Champs.add(ES_Nom);
		SC_Champs.add(ES_Prenom);
		SC_Champs.add(ES_Ancien);
		SC_Champs.add(ES_Nouveau);
		SC_Champs.add(ES_Redonne);
		//Labels
		SC_Labels.setLayout(layoutLabels);
		SC_Labels.add(STC_Nom);
		SC_Labels.add(STC_Prenom);
		SC_Labels.add(STC_Ancien);
		SC_Labels.add(STC_Nouveau);
		SC_Labels.add(STC_Redonne);
	}

	public void operationSurFenetre()
	{
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimmentionner
		this.setResizable(false);
		//Affectation du layout
		this.getContentPane().setLayout(layoutFond);
		//Affectation des panels
		this.getContentPane().add(SC_Boutons, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		//packaging
		this.pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{
		ES_Nom.setText(C_Utilisateur.getNom());
		ES_Prenom.setText(C_Utilisateur.getPrenom());
	}

	public void updateTexte()
	{}

	public void doAnnuler()
	{
		dispose();
	}

	public void doValider()
	{
		C_ChangerInformations cChangerInformations = new C_ChangerInformations();
		cChangerInformations.setNomPrenom(ES_Nom.getText(), ES_Prenom.getText());
		//Si l'ancien mot de passe est vide comme le nouveau, on ne fait rien, sinon, on change
		try
		{
			if ((ES_Ancien.getPassword().length != 0) || (ES_Nouveau.getPassword().length != 0))
			{
				if (cChangerInformations.verifieAncien(ES_Ancien.getPassword()))
				{
					if (cChangerInformations.verifieRedonne(ES_Nouveau.getPassword(), ES_Redonne.getPassword()))
					{
						try
						{
							cChangerInformations.modifieMotDePasse(ES_Nouveau.getPassword());
						}
						catch (SQLException e)
						{
							JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
							throw e;
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this, Bundle.getText("BD_ChangerInformations_redonne_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
						throw new Exception();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, Bundle.getText("BD_ChangerInformations_ancien_erreur"), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
					throw new Exception();
				}
			}
			//A ce niveau tout va bien
			dispose();
		}
		catch (Exception e)
		{
			//Juste pour empêcher de fermer la fenêtre
		}
	}
}
