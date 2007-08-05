/*
 * Fichier BM_APropos.java
 * Auteur Cédric
 *
 */
package hapi.presentation;

import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Boîte de message A Propos
 */
public class BM_APropos extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = 4997245366682469455L;
    //Layout
	private BorderLayout layoutFond = null;
	private BorderLayout layoutComposant = null;	
	private FlowLayout layoutIcones = null;
	private FlowLayout layoutPanelChaine = null;
	private GridLayout layoutEquipe = null;
	private GridLayout layoutChaine = null;
	private FlowLayout layoutPanelEquipe = null;
	private FlowLayout layoutBouton = null;	
	//Panel
	private JPanel SC_Bouton = null;
	private JPanel SC_Composant = null;
	//private JPanel SC_Gauche = null;
	//private JPanel SC_Droite = null;
	//private JPanel SC_Haut = null;
	private JPanel SC_PanelEquipe = null;
	private JPanel SC_PanelChaine = null;
	private JPanel SC_Equipe = null;
	private JPanel SC_Icones = null;	
	//Boutons
	private JButton BP_Fermer = null;	
	//Composants
	private JLabel SIC_HAPI = null;
	private JLabel STC_FabienPU = null;
	private JLabel STC_Stephanie = null;
	private JLabel STC_FabienPa = null;
	private JLabel STC_Natalia = null;
	private JLabel STC_Vincent = null;
	private JLabel STC_Cedric = null;
	private JLabel STC_Yannick = null;
	private JLabel STC_Stephane = null;
	private JLabel STC_Robin = null;
	private JLabel STC_FabienA = null;
	private JPanel SC_Chaine = null;
	private JLabel STC_Chaine4 = null;
	private JLabel STC_Chaine3 = null;
	private JLabel STC_Chaine2 = null;
	private JLabel STC_Chaine1 = null;
	private JLabel STC_Chaine5 = null;
	private JLabel STC_Chaine6 = null;
	private JLabel STC_Chaine7 = null;
	private JLabel STC_Chaine8 = null;	
	//Icones
		private final static ImageIcon HAPI_TOTAL_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/hapiTotal.gif"));
	//Ecouteurs de la fenêtre
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			dispose();;
		}
	};

	public BM_APropos(JFrame parent)
	{
		//Association au parent
		super(parent, Bundle.getText("BM_APropos_caption"), true);
		//Création des éléments
		creationElements();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurPanel();
		operationSurFenetre();
	}

	public void creationElements()
	{
		//Layout
		layoutFond = new BorderLayout();
		layoutComposant = new BorderLayout();	
		layoutIcones = new FlowLayout();
		layoutPanelChaine = new FlowLayout();
		layoutEquipe = new GridLayout(5,2);
		layoutChaine = new GridLayout(9,0);
		layoutPanelEquipe = new FlowLayout();
		layoutBouton = new FlowLayout();	
		//Panel
		SC_Bouton = new JPanel();
		SC_Composant = new JPanel();
		//SC_Gauche = new JPanel();
		//SC_Droite = new JPanel();
		//SC_Haut = new JPanel();
		SC_PanelEquipe = new JPanel();
		SC_PanelChaine = new JPanel();
		SC_Equipe = new JPanel();
		SC_Icones = new JPanel();	
		//Boutons
		BP_Fermer = new JButton();	
		//Composants
		SIC_HAPI = new JLabel();
		STC_FabienPU = new JLabel();
		STC_Stephanie = new JLabel();
		STC_FabienPa = new JLabel();
		STC_Natalia = new JLabel();
		STC_Vincent = new JLabel();
		STC_Cedric = new JLabel();
		STC_Yannick = new JLabel();
		STC_Stephane = new JLabel();
		STC_Robin = new JLabel();
		STC_FabienA = new JLabel();
		SC_Chaine = new JPanel();
		STC_Chaine4 = new JLabel();
		STC_Chaine3 = new JLabel();
		STC_Chaine2 = new JLabel();
		STC_Chaine1 = new JLabel();
		STC_Chaine5 = new JLabel();
		STC_Chaine6 = new JLabel();
		STC_Chaine7 = new JLabel();
		STC_Chaine8 = new JLabel();	
	}
	public void operationSurBoutons()
	{
		//Boutfon de fermeture
		BP_Fermer.setText(Bundle.getText("Fermer"));
		BP_Fermer.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Fermer.addActionListener(actionFermer);		
	}
	public void operationSurComposants()
	{
		//Logo
		//SIC_Logo.setHorizontalAlignment(SwingConstants.CENTER);
		//SIC_Logo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		//SIC_Logo.setIcon(LOGO_ICONE);
		//HAPI
		//SIC_HAPI.setHorizontalAlignment(SwingConstants.CENTER);
		//SIC_HAPI.setVerticalAlignment(SwingConstants.BOTTOM);		
		SIC_HAPI.setIcon(HAPI_TOTAL_ICONE);
		
		//Les Noms
		STC_FabienPU.setHorizontalAlignment(SwingConstants.CENTER);
		STC_FabienPU.setText("Fabien PUYSSEGUR");
		STC_FabienPU.setForeground(Color.RED);
		STC_FabienPU.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Stephanie.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Stephanie.setText("  Stéphanie COUTANCEAU");
		STC_Stephanie.setForeground(Color.RED);
		STC_Stephanie.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_FabienPa.setHorizontalAlignment(SwingConstants.CENTER);
		STC_FabienPa.setText("Fabien PASCAL");
		STC_FabienPa.setForeground(Color.RED);
		STC_FabienPa.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Natalia.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Natalia.setText("Natalia BOURSIER");
		STC_Natalia.setForeground(Color.RED);
		STC_Natalia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Vincent.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Vincent.setText("Vincent MARILLAUD");
		STC_Vincent.setForeground(Color.RED);		
		STC_Vincent.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Cedric.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Cedric.setText("Cédric BOUHOURS");
		STC_Cedric.setForeground(Color.RED);
		STC_Cedric.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Yannick.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Yannick.setText("Yannick GOUTAUDIER");
		STC_Yannick.setForeground(Color.RED);
		STC_Yannick.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Stephane.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Stephane.setText("Stéphane ANRIGO");
		STC_Stephane.setForeground(Color.RED);
		STC_Stephane.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		STC_Robin.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Robin.setText("Robin EYSSERIC");
		STC_Robin.setForeground(Color.RED);
		STC_Robin.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

		STC_FabienA.setHorizontalAlignment(SwingConstants.CENTER);
		STC_FabienA.setText("Fabien ALLANIC");
		STC_FabienA.setForeground(Color.RED);
		STC_FabienA.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		
		//Texte
		STC_Chaine1.setText(Bundle.getText("BM_APropos_texte1"));
		STC_Chaine1.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine2.setText(Bundle.getText("BM_APropos_texte2"));
		STC_Chaine2.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine3.setText(Bundle.getText("BM_APropos_texte3"));
		STC_Chaine3.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine4.setText(Bundle.getText("BM_APropos_texte4"));
		STC_Chaine4.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine5.setText(Bundle.getText("BM_APropos_GNU1"));
		STC_Chaine5.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine6.setText(Bundle.getText("BM_APropos_GNU2"));
		STC_Chaine6.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine7.setText(Bundle.getText("BM_APropos_GNU3"));
		STC_Chaine7.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Chaine8.setText(Bundle.getText("BM_APropos_GNU4"));
		STC_Chaine8.setHorizontalAlignment(SwingConstants.CENTER);
	}
	public void operationSurPanel()
	{
		
		//Espacement
		layoutEquipe.setHgap(20);
		layoutEquipe.setVgap(5);
		//layoutChaine.setVgap(10);
		
		//Composant		
		SC_Composant.setLayout(layoutComposant);
		SC_Composant.add(SC_Icones, BorderLayout.NORTH);
		SC_Composant.add(SC_PanelEquipe, BorderLayout.SOUTH);
		SC_Composant.add(SC_PanelChaine, BorderLayout.CENTER);		
		
		//Icones
		SC_Icones.setLayout(layoutIcones);
		//SC_Icones.add(SIC_Logo, BorderLayout.WEST);
		//SC_Icones.add(SIC_HAPI, BorderLayout.CENTER);
		//SC_Icones.add(SIC_Logo);
		SC_Icones.add(SIC_HAPI);		
		
		//Texte
		SC_PanelChaine.setLayout(layoutPanelChaine);
		SC_Chaine.setLayout(layoutChaine);
		SC_PanelChaine.add(SC_Chaine);
		SC_Chaine.add(STC_Chaine1);
		SC_Chaine.add(STC_Chaine2);
		SC_Chaine.add(STC_Chaine3);
		SC_Chaine.add(STC_Chaine4);
		SC_Chaine.add(new JLabel());
		SC_Chaine.add(STC_Chaine5);
		SC_Chaine.add(STC_Chaine6);
		SC_Chaine.add(STC_Chaine7);
		SC_Chaine.add(STC_Chaine8);
		
		//Equipe
		SC_Equipe.setBorder(BorderFactory.createEtchedBorder());
		SC_Equipe.setLayout(layoutEquipe);
		SC_PanelEquipe.setLayout(layoutPanelEquipe);
		SC_PanelEquipe.add(SC_Equipe);
		SC_Equipe.add(STC_FabienA);
		SC_Equipe.add(STC_Robin);
		SC_Equipe.add(STC_Stephane);
		SC_Equipe.add(STC_Yannick);
		SC_Equipe.add(STC_Cedric);
		SC_Equipe.add(STC_Vincent);
		SC_Equipe.add(STC_Natalia);
		SC_Equipe.add(STC_FabienPa);
		SC_Equipe.add(STC_Stephanie);
		SC_Equipe.add(STC_FabienPU);		
		
		//Boutons
		SC_Bouton.setLayout(layoutBouton);
		SC_Bouton.add(BP_Fermer);				
	}
	public void operationSurFenetre()
	{
		//Operation de fermeture
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Possibilité de redimentionnement
		this.setResizable(false);
		//Affectation du layout
		this.getContentPane().setLayout(layoutFond);
		//Affectation des panels d'espacement
		//this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		//this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		//this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Bouton, BorderLayout.SOUTH);
		//Affectation des panels
		this.getContentPane().add(SC_Composant, BorderLayout.CENTER);
		//Packaging
		pack();
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));		
	}
	public void operationMoteur() throws Exception
	{}
	public void updateTexte()
	{}
}
