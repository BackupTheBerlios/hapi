/*
 * Created on 6 mars 2005
 *
 */
package hapi.presentation.indicateurs;

import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author Natalia
 *
 */
public class BM_DetailCalculDesIndicateurs extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 4735802930135764031L;
    private JPanel SC_Composants = null;
	// panels de mise en forme
	private JPanel SC_Haut = null;
	private JPanel SC_Bas = null;
	private JPanel SC_Droit = null;
	private JPanel SC_Bouton = null;

	private JButton BP_Fermer = null;

	private BorderLayout layoutfond = null;
	private BorderLayout layoutcomposant = null;

	private JTabbedPane SC_Onglets = null;

	// listeners
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doFermer();
		}
	};

	private String idProcessus = null;
	private int idExecution = 0;
	private int idEvaluation = 0;

	public BM_DetailCalculDesIndicateurs(Frame parent, ArrayList identifiants)
	{
		super(parent, Bundle.getText("BM_DetailCalculDesIndicateurs_Caption"), true);
		// affectation des identifiants
		idProcessus = identifiants.get(0).toString();
		idExecution = ((Integer) identifiants.get(1)).intValue();
		idEvaluation = ((Integer) identifiants.get(2)).intValue();

		creationElements();
		operationSurBoutons();
		operationMoteur();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();

	}

	public void creationElements()
	{
		SC_Composants = new JPanel();
		SC_Bouton = new JPanel();
		// panels de mise en forme
		SC_Haut = new JPanel();
		SC_Bas = new JPanel();
		SC_Droit = new JPanel();

		layoutfond = new BorderLayout();
		layoutcomposant = new BorderLayout();

		BP_Fermer = new JButton();

		SC_Onglets = new JTabbedPane();
	}

	public void operationSurBoutons()
	{
		BP_Fermer.setText(Bundle.getText("BM_DetailCalculDesIndicateurs_Fermer"));
		BP_Fermer.setMnemonic(Bundle.getText("BM_DetailCalculDesIndicateurs_Fermer_mnemonic").charAt(0));
		BP_Fermer.addActionListener(actionFermer);
	}

	public void operationSurComposants()
	{}

	public void operationSurPanel()
	{
		ArrayList identifiants = new ArrayList();
		identifiants.add(idProcessus);
		identifiants.add(new Integer(idExecution));
		identifiants.add(new Integer(idEvaluation));
		SC_Onglets.add(new OO_DetailParIndicateur(identifiants, 0), Bundle.getText("BM_DetailCalculDesIndicateurs_CaptionRUA"));
		SC_Onglets.add(new OO_DetailParIndicateur(identifiants, 2), Bundle.getText("BM_DetailCalculDesIndicateurs_CaptionRUP"));
		SC_Onglets.add(new OO_DetailParIndicateur(identifiants, 3), Bundle.getText("BM_DetailCalculDesIndicateurs_CaptionRUR"));

		SC_Bouton.add(BP_Fermer);

		SC_Composants.setLayout(layoutcomposant);
		SC_Composants.add(SC_Onglets, BorderLayout.CENTER);

		SC_Bas.add(SC_Bouton);
	}

	public void operationSurFenetre()
	{
		//this.setTitle(Bundle.getText("BM_DetailCalculDesIndicateurs_Caption"));

		this.getContentPane().setLayout(layoutfond);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Bouton, BorderLayout.SOUTH);
		this.getContentPane().add(SC_Droit, BorderLayout.EAST);
		this.getContentPane().add(SC_Bas, BorderLayout.WEST);
		this.setModal(true);

		this.pack();

		//Positionnement
		this.setSize(650, 400);
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}

	private void doFermer()
	{
		this.dispose();
	}
}
