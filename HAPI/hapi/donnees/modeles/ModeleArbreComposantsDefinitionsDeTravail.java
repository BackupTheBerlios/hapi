/*
 * Created on 5 mars 2005
 *
 */
package hapi.donnees.modeles;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Natalia
 *
 */
public class ModeleArbreComposantsDefinitionsDeTravail implements TreeModel
{
	//Arbre contenant les composants et les définitions de travail
	private DefaultTreeModel tree;
	//Liste des noeuds
	private ArrayList listeComposants = new ArrayList();
	private ArrayList listeDefinitionsTravail = new ArrayList();
	private ArrayList listeDesActivites = new ArrayList();

	//Liste des noeuds
	private DefaultMutableTreeNode racine = null;

	public ModeleArbreComposantsDefinitionsDeTravail(String nomRacine)
	{
		racine = new DefaultMutableTreeNode(nomRacine);
		tree = new DefaultTreeModel(racine);
	}

	public void clear()
	{
		listeComposants.clear();
		listeDefinitionsTravail.clear();
		racine.removeAllChildren();
	}

	public void addComposant(NoeudArbre leNoeud)
	{
		listeComposants.add(leNoeud);
		racine.add(leNoeud);
	}

	public void addDefinitionDeTravail(NoeudArbre leComposant, NoeudArbre laDefinition)
	{
		listeDefinitionsTravail.add(laDefinition);
		((NoeudArbre) listeComposants.get(listeComposants.indexOf(leComposant))).add(laDefinition);
	}

	public void addActivite(NoeudArbre laDefinition, NoeudArbre lactivite)
	{
		listeDesActivites.add(lactivite);
		((NoeudArbre) listeDefinitionsTravail.get(listeDefinitionsTravail.indexOf(laDefinition))).add(lactivite);
	}

	public Object getRoot()
	{
		return tree.getRoot();
	}

	public Object getChild(Object parent, int index)
	{
		return tree.getChild(parent, index);
	}

	public int getChildCount(Object parent)
	{
		return tree.getChildCount(parent);
	}

	public boolean isLeaf(Object noeud)
	{
		return tree.isLeaf(noeud);
	}

	public void valueForPathChanged(TreePath chemin, Object noeud)
	{
		tree.valueForPathChanged(chemin, noeud);
	}

	public int getIndexOfChild(Object parent, Object enfant)
	{
		return tree.getIndexOfChild(parent, enfant);
	}

	public void addTreeModelListener(TreeModelListener listener)
	{
		tree.addTreeModelListener(listener);
	}

	public void removeTreeModelListener(TreeModelListener listener)
	{
		tree.removeTreeModelListener(listener);
	}

}
