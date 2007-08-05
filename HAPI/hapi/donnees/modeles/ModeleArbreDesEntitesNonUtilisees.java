/*
 * Created on 6 mars 2005
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
public class ModeleArbreDesEntitesNonUtilisees implements TreeModel
{
	//Arbre contenant les composants et les définitions de travail
	private DefaultTreeModel tree;
	//Liste des noeuds
	private ArrayList listeComposants = new ArrayList();
	private ArrayList listeDefinitionsTravail = new ArrayList();
	private ArrayList listeActivites = new ArrayList();
	private ArrayList listeRoles = new ArrayList();
	private ArrayList listeProduits = new ArrayList();

	//Liste des noeuds
	private DefaultMutableTreeNode racine = null;

	public ModeleArbreDesEntitesNonUtilisees(String nomRacine)
	{
		racine = new DefaultMutableTreeNode(nomRacine);
		tree = new DefaultTreeModel(racine);
	}

	public void clear()
	{
		listeComposants.clear();
		listeDefinitionsTravail.clear();
		listeActivites.clear();
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

	public void addActivite(NoeudArbre laDefinition, NoeudArbre lActivite)
	{
		listeActivites.add(lActivite);
		((NoeudArbre) listeDefinitionsTravail.get(listeDefinitionsTravail.indexOf(laDefinition))).add(lActivite);
	}

	public void addRole(NoeudArbre leComposant, NoeudArbre leRole)
	{
		listeRoles.add(leRole);
		((NoeudArbre) listeComposants.get(listeComposants.indexOf(leComposant))).add(leRole);
	}

	public void addProduit(NoeudArbre leComposant, NoeudArbre leProduit)
	{
		listeProduits.add(leProduit);
		((NoeudArbre) listeComposants.get(listeComposants.indexOf(leComposant))).add(leProduit);
	}

	public int getNombreDActivites()
	{
		return listeActivites.size();
	}

	public int getNombreDeRoles()
	{
		return listeRoles.size();
	}

	public int getNombreDeProduits()
	{
		return listeProduits.size();
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
