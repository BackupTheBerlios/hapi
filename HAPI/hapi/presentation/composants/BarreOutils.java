package hapi.presentation.composants;

import hapi.application.ressources.Bundle;
import hapi.presentation.FP_HAPI;

import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * @author Robin & Cécile
 */
public class BarreOutils extends JToolBar
{
	private static final long serialVersionUID = -493493650435580806L;
    private JButton BP_ImporterProcessus = null;
	private JButton BP_ImporterMesures = null;
	//private JButton BP_Configuration = null;
	private JButton BP_APropos = null;

	private FP_HAPI fenetrePrincipale = null;

	public BarreOutils(FP_HAPI fp)
	{
		super();
		this.fenetrePrincipale = fp;
		createToolBar();
	}

	/**
	 * Méthode permettant de créer la barre d'outil contenant des boutons raccourcis
	 */
	private void createToolBar()
	{
		// Creates the buttons and add them to the toolBar
		this.BP_ImporterProcessus = new JButton(fenetrePrincipale.importerProcessusAction);
		this.BP_ImporterProcessus.setText("");
		this.add(this.BP_ImporterProcessus);

		this.BP_ImporterMesures = new JButton(fenetrePrincipale.importerMesuresAction);
		this.BP_ImporterMesures.setText("");
		this.add(this.BP_ImporterMesures);

		/*this.addSeparator();
		
		this.BP_Configuration = new JButton(fenetrePrincipale.configurationAction);
		this.BP_Configuration.setText("");
		this.add(this.BP_Configuration);*/

		/*this.BP_MotDePasse = new JButton(fenetrePrincipale.motDePasseAction);
		this.BP_MotDePasse.setText("");
		this.add(this.BP_MotDePasse);*/

		this.addSeparator();

		this.BP_APropos = new JButton(fenetrePrincipale.aProposAction);
		this.BP_APropos.setText("");
		this.add(this.BP_APropos);

		// Adds buttons tooltip texts
		this.BP_ImporterProcessus.setToolTipText(Bundle.getText("MENUITEM_ImporterProcessus"));
		this.BP_ImporterMesures.setToolTipText(Bundle.getText("MENUITEM_ImporterMesures"));
		//this.BP_Configuration.setToolTipText(Bundle.getText("MENUITEM_Configuration"));
		//this.BP_MotDePasse.setToolTipText(Bundle.getText("MENUITEM_MotDePasse"));
		this.BP_APropos.setToolTipText(Bundle.getText("MENUITEM_APropos"));

		this.setFloatable(false);

	} // End of createToolBar method

	public void updateTexte()
	{
		this.BP_ImporterProcessus.setText("");
		this.BP_ImporterMesures.setText("");
		//this.BP_Configuration.setText("");
		//this.BP_MotDePasse.setText("");
		this.BP_APropos.setText("");

		this.BP_ImporterProcessus.setToolTipText(Bundle.getText("MENUITEM_ImporterProcessus"));
		this.BP_ImporterMesures.setToolTipText(Bundle.getText("MENUITEM_ImporterMesures"));
		//this.BP_Configuration.setToolTipText(Bundle.getText("MENUITEM_Configuration"));
		//this.BP_MotDePasse.setToolTipText(Bundle.getText("MENUITEM_MotDePasse"));
		this.BP_APropos.setToolTipText(Bundle.getText("MENUITEM_APropos"));
	}
}
