package hapi.presentation.composants;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.exception.NiveauInsuffisantException;
import hapi.exception.StopActionException;
import hapi.presentation.BD_CreerExecutionProcessus;
import hapi.presentation.BD_GenererHTML;
import hapi.presentation.FP_HAPI;
import hapi.presentation.indicateurs.creation.BD_MesureAmelioration;
import hapi.presentation.indicateurs.creation.BD_MesureVersion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Authors Fabien Allanic Yannick Goutaudier Fabien Puyssegur
 */
public class MC_Processus extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = -7726502618529033247L;
    private JFrame fenetreAppel;
	private JMenuItem OM_Creer_Exec;
	private JMenuItem OM_Supprimer_Proc;
	private JMenuItem OM_Mesures;
	private JMenuItem OM_GenererHTML;
	private JMenuItem OM_MesureAmelioration;
	private String idProcessus;

	public MC_Processus(JFrame fenetre, String lIdProcessus)
	{
		super();

		fenetreAppel = fenetre;
		idProcessus = lIdProcessus;
		OM_Creer_Exec = new JMenuItem(Bundle.getText("MENUITEM_CreerExecutionProcessus"));
		OM_Supprimer_Proc = new JMenuItem(Bundle.getText("MENUITEM_Supprimer"));
		OM_Mesures = new JMenuItem(Bundle.getText("MENUITEM_MesureSurVersion"));
		OM_GenererHTML = new JMenuItem(Bundle.getText("MENUITEM_GenererMesuresHTML"));
		OM_MesureAmelioration = new JMenuItem(Bundle.getText("MENUITEM_MesureAmelioration"));
		this.add(this.OM_Creer_Exec);
		this.addSeparator();
		this.add(this.OM_Mesures);
		this.add(this.OM_MesureAmelioration);
		this.add(this.OM_GenererHTML);
		this.addSeparator();
		this.add(this.OM_Supprimer_Proc);

		this.OM_Creer_Exec.addActionListener(this);
		this.OM_Supprimer_Proc.addActionListener(this);
		this.OM_Mesures.addActionListener(this);
		this.OM_GenererHTML.addActionListener(this);
		this.OM_MesureAmelioration.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == OM_Creer_Exec)
		{
			new BD_CreerExecutionProcessus(fenetreAppel, idProcessus).setVisible(true);
		}
		else if (event.getSource() == OM_Mesures)
		{
			try
			{
				new BD_MesureVersion(fenetreAppel, idProcessus);
			}
			catch (StopActionException e)
			{}
		}
		else if (event.getSource() == OM_GenererHTML)
		{
			new BD_GenererHTML(fenetreAppel, idProcessus).setVisible(true);
		}
		else if (event.getSource() == OM_MesureAmelioration)
		{
			try
			{
				new BD_MesureAmelioration(fenetreAppel,idProcessus,true).setVisible(true);
			}
			catch (NiveauInsuffisantException e)
			{
				JOptionPane.showMessageDialog(fenetreAppel, e.getMessage(), Bundle.getText("Attention"), JOptionPane.WARNING_MESSAGE);
			}
		}		
		else if (event.getSource() == OM_Supprimer_Proc) // Suppression du
														 // processus
														 // sélectionné
		{
			if (JOptionPane.showConfirmDialog(this, Bundle.getText("MC_Metier_ConfirmationSuppression_Proc"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			{
				((FP_HAPI) fenetreAppel).selectionnerRacineArbre();
				C_AccesBaseDonnees base = new C_AccesBaseDonnees();

				try
				{
					base.ouvrirConnexion();
					base.setAutoCommit(false);

					C_Processus.supprimerProcessusEnBase(idProcessus, base);
					// Suppression de l'entité processus
					C_Processus.supprimerProcessus(idProcessus);
					base.commit();
				}
				catch (Exception e)
				{
					base.rollback();
					JOptionPane.showMessageDialog(this, Bundle.getText("problemeSuppressionProcessus"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
				}
				finally
				{
					try
					{
						base.fermerConnexion();
					}
					catch (SQLException e1)
					{}
				}
			}
		}

		((FP_HAPI) fenetreAppel).refreshArbre();
	}
}
