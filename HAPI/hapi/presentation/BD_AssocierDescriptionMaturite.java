/*
 * Créé le 23 août 2005
 */
package hapi.presentation;

import hapi.application.C_AssocierDescriptionMaturite;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Cédric
 */
public class BD_AssocierDescriptionMaturite extends JDialog implements FenetreHAPI
{
    private static final long serialVersionUID = -1160100943526316633L;
    //Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposants = null;
	private FlowLayout layoutBoutons = null;
	//Panels
	private JPanel SC_Haut = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Boutons = null;
	private JPanel SC_Composants = null;
	private JScrollPane SC_Scroll = null;
	//Composant
	private JTable LS_Associations = null;
	//Boutons
	private JButton BP_Modifier = null;
	private JButton BP_Annuler = null;
	//Ecouteurs
	private ActionListener actionAnnuler = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doAnnuler();
		}
	};
	private ActionListener actionModifier = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			doModifier();
		}
	};	
	//Variables
	private C_AssocierDescriptionMaturite leControleur = null;
		
	
	public BD_AssocierDescriptionMaturite(JFrame parent) throws SQLException
	{
		super(parent,true);
		this.setTitle(Bundle.getText("BD_AssocierDescriptionMaturite_Caption"));
		leControleur = new C_AssocierDescriptionMaturite();
		creationElements();
		operationSurBoutons();
		operationSurPanel();
		operationSurComposants();
		operationSurFenetre();
		operationMoteur();
	}

	public void creationElements()
	{
		//Layout
		layoutFond = new BorderLayout();
		layoutComposants = new BorderLayout();
		layoutBoutons = new FlowLayout();
		//Panels
		SC_Haut = new JPanel();
		SC_Gauche = new JPanel();
		SC_Droite = new JPanel();
		SC_Boutons = new JPanel();
		SC_Composants = new JPanel();
		SC_Scroll = new JScrollPane();
		//Composant
		LS_Associations = new JTable();
		//Boutons
		BP_Modifier = new JButton();
		BP_Annuler = new JButton();		
	}

	public void operationSurBoutons()
	{
		BP_Modifier.setText(Bundle.getText("Enregistrer"));
		BP_Modifier.setMnemonic(Bundle.getChar("Enregistrer_mne"));
		BP_Modifier.addActionListener(actionModifier);
		
		BP_Annuler.setText(Bundle.getText("Annuler"));
		BP_Annuler.setMnemonic(Bundle.getChar("Annuler_mne"));
		BP_Annuler.addActionListener(actionAnnuler);		
	}

	public void operationSurComposants()
	{}

	public void operationSurPanel()
	{
		SC_Boutons.setLayout(layoutBoutons);
		SC_Boutons.add(BP_Modifier);
		SC_Boutons.add(BP_Annuler);
		
		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(SC_Scroll,BorderLayout.CENTER);
		
		SC_Scroll.setViewportView(LS_Associations);
		SC_Scroll.setWheelScrollingEnabled(true);
		SC_Scroll.setPreferredSize(new Dimension(400,150));
	}

	public void operationSurFenetre()
	{
		//Fermeture par défaut
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimmentionner
		this.setResizable(true);
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

	public void operationMoteur() throws SQLException
	{
		LS_Associations.setModel(leControleur.getModele());
		LS_Associations.getColumnModel().getColumn(0).setWidth(50);
		LS_Associations.getColumnModel().getColumn(0).setMaxWidth(50);
		LS_Associations.getColumnModel().getColumn(0).setMinWidth(50);
		LS_Associations.getColumnModel().getColumn(0).setCellRenderer(new TableRenderer()); 
	}

	public void updateTexte()
	{}
	
	private void doAnnuler()
	{
		dispose();
	}
	
	private void doModifier()
	{
		try
		{
			LS_Associations.editCellAt(0,0);
			leControleur.SauvegarderAssociations();
			dispose();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, Bundle.getText("problemeBD"), Bundle.getText("Erreur"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class TableRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = -8610539786648088284L;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			this.setHorizontalAlignment(JLabel.CENTER);

			return this;
		}

	}	
}
