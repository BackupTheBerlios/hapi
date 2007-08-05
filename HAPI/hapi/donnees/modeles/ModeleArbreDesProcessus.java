package hapi.donnees.modeles;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Robin & Cécile
 */
public class ModeleArbreDesProcessus extends Observable implements TreeModel
{
	//Arbre contenant les définitions processus et les composants
	private DefaultTreeModel tree;
	//Liste des noeuds
	private ArrayList listeProcessus = new ArrayList();
	private ArrayList listeExec = new ArrayList();

	//Liste des noeuds
	private DefaultMutableTreeNode racine = null;

	public ModeleArbreDesProcessus(String nomRacine)
	{
		super();
		createTreeModel(nomRacine);
	}

	public void createTreeModel(String nomRacine)
	{

		racine = new DefaultMutableTreeNode(nomRacine);

		this.tree = new DefaultTreeModel(racine);

		this.notifyChange();
	}

	public void clear()
	{
		listeExec.clear();
		listeProcessus.clear();
		racine.removeAllChildren();
		this.notifyChange();
	}

	public void addProcessus(NoeudArbre leNoeud)
	{
		listeProcessus.add(leNoeud);
		racine.add(leNoeud);
		this.notifyChange();
	}

	public void addExecutionProcessus(NoeudArbre leProcessus, NoeudArbre lExec)
	{
		listeExec.add(lExec);
		((NoeudArbre) listeProcessus.get(listeProcessus.indexOf(leProcessus))).add(lExec);
		this.notifyChange();
	}

	public void addEvaluation(NoeudArbre lExec, NoeudArbre lEval)
	{
		((NoeudArbre) listeExec.get(listeExec.indexOf(lExec))).add(lEval);
		this.notifyChange();
	}

	public void notifyChange()
	{
		this.setChanged();
		this.notifyObservers();
	}

	public void addTreeModelListener(TreeModelListener listener)
	{
		this.getTree().addTreeModelListener(listener);
	}

	public Object getChild(Object parent, int index)
	{
		return this.getTree().getChild(parent, index);
	}

	public int getChildCount(Object obj)
	{
		return this.getTree().getChildCount(obj);
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		return this.getTree().getIndexOfChild(parent, child);
	}

	public Object getRoot()
	{
		return this.getTree().getRoot();
	}

	public boolean isLeaf(Object obj)
	{
		return this.getTree().isLeaf(obj);
	}

	public void removeTreeModelListener(TreeModelListener ecouteur)
	{
		this.getTree().removeTreeModelListener(ecouteur);
	}

	public void valueForPathChanged(TreePath path, Object valeur)
	{
		this.getTree().valueForPathChanged(path, valeur);
	}

	public DefaultTreeModel getTree()
	{
		return tree;
	}

}
