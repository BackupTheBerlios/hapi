package hapi.presentation.composants;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.presentation.FP_HAPI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Authors BOUHOURS
 */
public class MC_Iteration extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = -3072551586513112767L;
    private JFrame fenetreAppel;
	private JMenuItem OM_SupprimerIt;

	private String idProcessus;
	private int idExecution;
	private int numIteration;

	public MC_Iteration(JFrame fenetre, String leNumIteration, ArrayList leChemin)
	{
		super();

		fenetreAppel = fenetre;
		idProcessus = (String) leChemin.get(0);
		idExecution = new Integer((String) leChemin.get(1)).intValue();
		numIteration = new Integer(leNumIteration).intValue();
		OM_SupprimerIt = new JMenuItem(Bundle.getText("MENUITEM_Supprimer"));
		this.add(this.OM_SupprimerIt);

		this.OM_SupprimerIt.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == OM_SupprimerIt) // Suppression de l'itération
		{
			if (JOptionPane.showConfirmDialog(this, Bundle.getText("MC_Metier_ConfirmationSuppression_Eval"), Bundle.getText("Question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			{
				((FP_HAPI) fenetreAppel).selectionnerRacineArbre();
				C_AccesBaseDonnees base = new C_AccesBaseDonnees();

				try
				{
					base.ouvrirConnexion();
					base.setAutoCommit(false);

					C_Evaluation.supprimerUneEvaluationEnBase(base, idProcessus, idExecution, numIteration);
					// Suppression de l'entité processus
					C_Evaluation.supprimerUneEvaluation(idProcessus, idExecution, numIteration);
					base.commit();
				}
				catch (Exception e)
				{
					base.rollback();
					JOptionPane.showMessageDialog(this, Bundle.getText("problemeSuppressionEvaluation"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
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
