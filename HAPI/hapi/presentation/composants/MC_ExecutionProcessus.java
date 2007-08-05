package hapi.presentation.composants;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.ressources.Bundle;
import hapi.exception.StopActionException;
import hapi.presentation.BD_Exporter;
import hapi.presentation.FP_HAPI;
import hapi.presentation.indicateurs.creation.BD_CreerEvaluation;
import hapi.presentation.indicateurs.creation.BD_CycleDeVie;
import hapi.presentation.indicateurs.creation.BD_DemanderDPE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 */
public class MC_ExecutionProcessus extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = -1730073272335817327L;
    private JFrame fenetreAppel;
	private JMenuItem OM_Commentaire;
	private JMenuItem OM_Evaluation;
	private JMenuItem OM_Supprimer;
	private JMenuItem OM_CycleDeVie;
	private String idExecution;
	private String idProcessus;

	public MC_ExecutionProcessus(JFrame fenetre, String lIdExecution, ArrayList lIdProcessus)
	{
		super();

		fenetreAppel = fenetre;
		idExecution = lIdExecution;
		idProcessus = (String) lIdProcessus.get(0);
		OM_Commentaire = new JMenuItem(Bundle.getText("MENUITEM_ExporterCommentaire"));
		OM_Evaluation = new JMenuItem(Bundle.getText("MENUITEM_CreerEvaluation"));
		OM_Supprimer = new JMenuItem(Bundle.getText("MENUITEM_Supprimer"));

		if (C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExecution).getIdCycleDeVie() != -1)
			OM_CycleDeVie = new JMenuItem(Bundle.getText("MENUITEM_ModifierCycleDeVie"));
		else
			OM_CycleDeVie = new JMenuItem(Bundle.getText("MENUITEM_CreerCycleDeVie"));

		this.add(this.OM_Commentaire);
		this.addSeparator();
		this.add(this.OM_CycleDeVie);
		this.add(this.OM_Evaluation);
		this.addSeparator();
		this.add(this.OM_Supprimer);
		this.OM_Commentaire.addActionListener(this);
		this.OM_Evaluation.addActionListener(this);
		this.OM_Supprimer.addActionListener(this);
		this.OM_CycleDeVie.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == OM_Commentaire)
		{
			new BD_Exporter(fenetreAppel, idExecution, idProcessus).setVisible(true);
		}
		else if (event.getSource() == OM_Evaluation)
		{
			try
			{
				new BD_CreerEvaluation(fenetreAppel, idProcessus, idExecution, -1);
			}
			catch (StopActionException e)
			{}
		}
		else if (event.getSource() == OM_Supprimer) // Suppression du processus
													// sélectionné
		{
			if (JOptionPane.showConfirmDialog(this, Bundle.getText("MC_Metier_ConfirmationSuppression_Exec"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			{
				((FP_HAPI) fenetreAppel).selectionnerRacineArbre();

				C_AccesBaseDonnees base = new C_AccesBaseDonnees();

				try
				{
					base.ouvrirConnexion();
					base.setAutoCommit(false);

					C_ExecutionProcessus.supprimerExecutionProcessusEnBase(idProcessus, idExecution, base);
					// Suppression de l'entité execution processus
					C_ExecutionProcessus.supprimerExecutionProcessusEtDependances(idExecution, idProcessus);
					base.commit();

				}
				catch (Exception e)
				{
					base.rollback();
					JOptionPane.showMessageDialog(fenetreAppel, Bundle.getText("problemeSuppressionExecProcessus"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
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
		else if (event.getSource() == OM_CycleDeVie)
		{
			try
			{
				new BD_CycleDeVie(fenetreAppel, idProcessus, idExecution, C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExecution).getIdCycleDeVie(), BD_DemanderDPE.doitUtiliserDPECourant(fenetreAppel, idProcessus, idExecution));
			}
			catch (StopActionException ex)
			{}
		}

		((FP_HAPI) fenetreAppel).refreshArbre();
	}
}
