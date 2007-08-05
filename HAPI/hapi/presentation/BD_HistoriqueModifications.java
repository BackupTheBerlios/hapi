package hapi.presentation;

import hapi.application.indicateurs.C_MesureAmelioration;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.presentation.composants.onglets.OO_DemandesModificationProcessus;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * @author Robin EYSSERIC
 */
public class BD_HistoriqueModifications extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 6040414312921145885L;
    //Panels
	private OO_DemandesModificationProcessus SC_Central = null;
	private JPanel SC_PanelBouton = null;

	private JButton BP_Fermer = null;

	private String idProcessus = null;

	//Ecouteurs de la fenêtre
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doFermer();
		}
	};

	public BD_HistoriqueModifications(JDialog parent, String idProc)
	{
		//Association au parent
		super(parent, Bundle.getText("BD_HistoriqueModifications_caption"), true);

		idProcessus = idProc;

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
		SC_PanelBouton = new JPanel();
		SC_Central = new OO_DemandesModificationProcessus(idProcessus,true);

		BP_Fermer = new JButton(Bundle.getText("Fermer"));
	}

	public void operationSurBoutons()
	{
		BP_Fermer.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Fermer.addActionListener(actionFermer);
	}

	public void operationSurComposants()
	{}

	public void operationSurPanel()
	{
		SC_PanelBouton.add(BP_Fermer);
	}

	public void operationSurFenetre()
	{
		//Composants
		this.getContentPane().add(SC_Central, BorderLayout.CENTER);
		this.getContentPane().add(SC_PanelBouton, BorderLayout.SOUTH);

		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.setSize(600, 400);

		//Impossibilité de redimentionnement
		this.setResizable(false);

		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));

		this.setVisible(true);
	}

	public void operationMoteur()
	{}

	public void updateTexte()
	{}
	
	private void doFermer()
	{
		//Création d'une amélioration
		// Récupération de la date du jour
		Calendar date = Calendar.getInstance();
		Date dateJour = new java.sql.Date(date.getTimeInMillis());
		
		C_MesureAmelioration.ajouterMesureSansBD(idProcessus,C_Processus.getProcessus(idProcessus).getDateExport(),dateJour, SC_Central.getNombrePropose(),SC_Central.getNombrePris(),SC_Central.getPresentation(),SC_Central.getModele(),SC_Central.getDocumentation());
		dispose();
	}
}
