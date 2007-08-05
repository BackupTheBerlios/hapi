package hapi.application;

import hapi.application.ressources.Bundle;
import hapi.donnees.metier.interfaces.InterfaceMetier;
import hapi.donnees.modeles.ModeleArbreDesProcessus;
import hapi.donnees.modeles.NoeudArbre;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Classe représentant le modèle de l'arbre
 */
public class C_ArbreDesProcessus extends JTree implements Observer, MouseListener
{
    private static final long serialVersionUID = -8092390119472044763L;
    //Modele de l'arbre
	private ModeleArbreDesProcessus modele = null;
	//Noeud de l'arbre sélectionné
	private NoeudArbre selection = null;
	private Object[] chemin = null;

	/**
	 * Constructeur du modèle de l'arbre
	 */
	public C_ArbreDesProcessus()
	{
		//Création de la racine
		super(new ModeleArbreDesProcessus(Bundle.getText("listeProcessus")));
		//Préparation des paramètes
		modele = (ModeleArbreDesProcessus) this.getModel();
		modele.addObserver(this);
		this.addMouseListener(this);
		this.setCellRenderer(new TreeRendering());
		this.expandAllRows();
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setSelectionRow(0);
		this.setAutoscrolls(true);
		this.setScrollsOnExpand(true);
	}

	/**
	 * Cette méthode reforme l'arbre depuis la racine
	 */
	public void reset()
	{
		getActivityModel().createTreeModel(Bundle.getText("listeProcessus"));
		this.setSelectionRow(0);
		this.expandAllRows();
		this.repaint();
	}

	/**
	 * Renderer de l'arbre
	 */
	protected class TreeRendering extends DefaultTreeCellRenderer
	{
        private static final long serialVersionUID = 7418250260516957748L;

        /**
		 * Constructeur du renderer
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			//Le renderer par défaut est un Label, on peut donc lui associer une icone
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value instanceof NoeudArbre)
			{
				this.setIcon(((NoeudArbre) value).getInfos().getIcone());
			}
			else
			{
				if (value instanceof DefaultMutableTreeNode)
				{
					this.setIcon(C_Hapi.LISTE_ICONE);
				}
			}

			this.setToolTipText(null);
			this.setBackgroundSelectionColor(new Color(255, 51, 51));
			// retourner le JLabel avec l'icone et le texte associé
			return this;
		}
	}

	/**
	 * Ouverture de tous les noeuds
	 */
	public void expandAllRows()
	{
		for (int i = 0; i < this.getRowCount(); i++)
		{
			this.expandRow(i);
		}
	}

	/**
	 * Evènement lorsque le bouton de la souris est enfoncé
	 */
	public void mousePressed(MouseEvent e)
	{
		//Récupération de la ligne sélectionnée
		int selRow = getRowForLocation(e.getX(), e.getY());

		//Récupération du chemin associé
		TreePath selPath = getPathForLocation(e.getX(), e.getY());

		//S'il y a vraiment un ligne sélectionnée
		if (selRow != -1)
		{
			if (selPath.getLastPathComponent() instanceof NoeudArbre)
			{
				//Récupération du noeud
				selection = (NoeudArbre) selPath.getLastPathComponent();
				chemin = selPath.getPath();
			}
			else
			{
				//Racine de l'arbre
				selection = null;
				chemin = null;
			}
			//Compatibilité OS
			if ((e.isPopupTrigger() || ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)))
			{
				e.consume();
				setSelectionPath(selPath);
			}
		}
	}

	//Méthodes de l'interface	
	public void mouseClicked(MouseEvent e)
	{}
	public void mouseReleased(MouseEvent e)
	{}
	public void mouseEntered(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}

	/**
	 * Mise à jour de l'arbre
	 */
	public void update(Observable o, Object arg)
	{
		this.updateUI();
		this.expandAllRows();
	}

	/**
	 * Récupération du modèle
	 */
	public ModeleArbreDesProcessus getActivityModel()
	{
		return modele;
	}

	/**
	 * Affectation du modèle
	 */
	public void setActivityModel(ModeleArbreDesProcessus model)
	{
		modele = model;
	}

	/**
	 * Récupération de l'objet sélectionné
	 * @return Objet métier sélectionné
	 */
	public InterfaceMetier getSelection()
	{
		try
		{
			return selection.getInfos();
		}
		catch (NullPointerException e)
		{
			//S'il n'y a pas de sélection, on renvoi null
			return null;
		}
	}

	public ArrayList getChemin()
	{
		ArrayList parents = new ArrayList();
		try
		{
			for (int i = 0; i < chemin.length; i++)
			{
				if (chemin[i] instanceof NoeudArbre)
				{
					parents.add(((NoeudArbre) (chemin[i])).getInfos());
				}
			}
			return parents;
		}
		catch (NullPointerException e)
		{
			//S'il n'y a pas de sélection, on renvoi null
			return null;
		}
	}

	public void selectionRacine()
	{
		selection = null;
		chemin = null;
	}
}