/*
 * Fichier BM_afficherCompProc.java
 * Auteur Fabien Allanic, Yannick Goutaudier, Fabien Puyssegur
 *
 */
package hapi.presentation;

import hapi.application.C_Hapi;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.presentation.interfaces.FenetreHAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Boîte de message affichage des processus en fontcion des composants
 */
public class BM_AfficherComposantsProcessus extends JDialog implements FenetreHAPI
{
	private static final long serialVersionUID = -5745711011102175230L;
    //Layout
	private BorderLayout layoutFond = null;
	private FlowLayout layoutBoutons = null;
	private BorderLayout layoutComposants = null;

	//	Panel
	private JPanel SC_Bouton = null;
	private JPanel SC_Gauche = null;
	private JPanel SC_Haut = null;
	private JPanel SC_Droite = null;
	private JPanel SC_Composants = null;
	private JScrollPane SC_Scroll = null;

	//Boutons
	private JButton BP_Fermer = null;

	//Composants
	private JLabel STC_Chaine1 = null;
	private JTree LS_Arbre;

	//Ecouteurs de la fenêtre
	private ActionListener actionFermer = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			dispose();
		}
	};

	public BM_AfficherComposantsProcessus(JFrame parent)
	{
		//Association au parent
		super(parent, Bundle.getText("BM_afficherCompProc_caption"), true);
		//Création des éléments
		creationElements();
		//Appels de l'interface
		operationSurBoutons();
		operationSurComposants();
		operationSurArbre();
		operationSurPanel();
		operationSurFenetre();
	}

	public void creationElements()
	{
		//Layout
		layoutFond = new BorderLayout();
		layoutBoutons = new FlowLayout();
		layoutComposants = new BorderLayout();

		//	Panel
		SC_Bouton = new JPanel();
		SC_Gauche = new JPanel();
		SC_Haut = new JPanel();
		SC_Droite = new JPanel();
		SC_Composants = new JPanel();
		SC_Scroll = new JScrollPane();

		//Boutons
		BP_Fermer = new JButton();

		//Composants
		STC_Chaine1 = new JLabel();

	}

	public void operationSurBoutons()
	{
		//Bouton de fermeture
		BP_Fermer.setText(Bundle.getText("Fermer"));
		BP_Fermer.setMnemonic(Bundle.getChar("Fermer_mne"));
		BP_Fermer.addActionListener(actionFermer);
	}

	public void operationSurComposants()
	{
		//Texte
		STC_Chaine1.setText(Bundle.getText("BM_afficherCompProc_texte1"));
		STC_Chaine1.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void operationSurPanel()
	{
		SC_Bouton.setLayout(layoutBoutons);
		SC_Bouton.add(BP_Fermer);

		SC_Scroll.setViewportView(LS_Arbre);
		SC_Scroll.setAutoscrolls(true);

		SC_Composants.setLayout(layoutComposants);
		SC_Composants.add(STC_Chaine1, BorderLayout.NORTH);
		SC_Composants.add(SC_Scroll, BorderLayout.CENTER);
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
		this.getContentPane().add(SC_Haut, BorderLayout.NORTH);
		this.getContentPane().add(SC_Gauche, BorderLayout.WEST);
		this.getContentPane().add(SC_Droite, BorderLayout.EAST);
		this.getContentPane().add(SC_Composants, BorderLayout.CENTER);
		this.getContentPane().add(SC_Bouton, BorderLayout.SOUTH);

		//Packaging
		pack();
		this.setBounds(this.getX(), this.getY(), this.getWidth(), 400);
		//Positionnement
		Rectangle PositionParent = this.getParent().getBounds();
		this.setLocation(PositionParent.x + Math.round(PositionParent.width / 2 - this.getWidth() / 2), Math.round(PositionParent.y + PositionParent.height / 2 - this.getHeight() / 2));
	}

	public void operationSurArbre()
	{
		//recuperation des composants		
		HashMap compoProc = C_Composant.getProcessusComposants();

		//creation de la hierarchie de l'arbre		
		Object[] hierarchy = new Object[compoProc.size() + 1];
		hierarchy[0] = Bundle.getText("BM_afficherCompProc_texte2");

		int iterateur = 1;
		//Liste les clés
		for (Iterator i = compoProc.keySet().iterator(); i.hasNext();)
		{
			Object key = i.next();
			HashMap proc_temp = (HashMap) compoProc.get(key);
			Object temp[] = new Object[proc_temp.size() + 1];
			int iterateur2 = 1;
			temp[0] = key;
			for (Iterator j = proc_temp.keySet().iterator(); j.hasNext();)
			{
				Object key2 = j.next();
				temp[iterateur2] = C_Processus.getProcessus((String) key2).getNomSansVersion();
				iterateur2++;
			}
			hierarchy[iterateur] = temp;
			iterateur++;
		}

		DefaultMutableTreeNode root = processHierarchy(hierarchy);
		LS_Arbre = new JTree(root);
		LS_Arbre.setCellRenderer(new TreeRendering());
		LS_Arbre.setAutoscrolls(true);
		LS_Arbre.setScrollsOnExpand(true);
	}

	/**
	 * Renderer de l'arbre
	 */
	protected class TreeRendering extends DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = 3316084895863617181L;

        /**
		 * Constructeur du renderer
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			//Le renderer par défaut est un Label, on peut donc lui associer une icone
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (((DefaultMutableTreeNode) value).isLeaf())
			{
				this.setIcon(C_Hapi.PROCESSUS_ICONE);
			}
			else
			{
				if (((DefaultMutableTreeNode) value).isRoot())
				{
					this.setIcon(C_Hapi.LISTE_ICONE);
				}
				else
				{
					this.setIcon(C_Hapi.COMPOSANT_ICONE);
				}
			}

			this.setToolTipText(null);
			this.setBackgroundSelectionColor(new Color(255, 51, 51));
			// retourner le JLabel avec l'icone et le texte associé
			return this;
		}
	}

	//methode necessaire a l affichage de l'arbre
	private DefaultMutableTreeNode processHierarchy(Object[] hierarchy)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
		DefaultMutableTreeNode child;
		for (int i = 1; i < hierarchy.length; i++)
		{
			Object nodeSpecifier = hierarchy[i];
			if (nodeSpecifier instanceof Object[]) // Ie node with children
				child = processHierarchy((Object[]) nodeSpecifier);
			else
				child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
			node.add(child);
		}
		return (node);
	}

	public void operationMoteur() throws Exception
	{}

	public void updateTexte()
	{}
}
