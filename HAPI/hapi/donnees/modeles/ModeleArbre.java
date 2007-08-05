/*
 * Fichier ModeleArbreFaceArbre.java
 * Auteur Cédric
 *
 */
package hapi.donnees.modeles;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


/**
 * Modele standard d'arbre à trois niveau (sans compter la racine)
 */
public class ModeleArbre extends Observable implements TreeModel
{
	//Arbre contenant les composants et les définitions de travail
	private DefaultTreeModel tree;
	//Liste des noeuds
	private ArrayList listeNiveau = new ArrayList();

	//Liste des noeuds
	private NoeudArbre racine = null;

	public ModeleArbre(String nomRacine)
	{
		super();
		racine = new NoeudArbre(new Racine(nomRacine));
		tree = new DefaultTreeModel(racine);
	}

	public void clear()
	{
		listeNiveau.clear();
		racine.removeAllChildren();
		this.notifyChange();
	}

	public void addNoeud(NoeudArbre leNoeud)
	{
		racine.add(leNoeud);

		this.notifyChange();
	}

	public void addNoeud(NoeudArbre lePere, NoeudArbre leNoeud)
	{
		NoeudArbre pointAppui = search(racine, lePere);
		if (pointAppui != null)
			pointAppui.add(leNoeud);

		this.notifyChange();
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

	public void notifyChange()
	{
		this.setChanged();
		this.notifyObservers();
	}

	private NoeudArbre search(NoeudArbre pointAppui, NoeudArbre recherche)
	{
		if (pointAppui.getInfos().getIdentifiant().equals(recherche.getInfos().getIdentifiant()))
			return pointAppui;
		else
		{
			if (pointAppui.getChildCount() == 0)
				return null;

			for (int i = 0; i < pointAppui.getChildCount(); i++)
			{
				NoeudArbre retour = search((NoeudArbre) pointAppui.getChildAt(i), recherche);
				if (retour != null)
					return retour;
			}

			return null;
		}
	}

	public void listeFeuille(HashMap laListe, int leNiveau)
	{
		listeFeuille(racine, laListe, leNiveau);
	}

	private void listeFeuille(NoeudArbre pointAppui, HashMap laListe, int leNiveau)
	{
		if (pointAppui.isLeaf() && pointAppui.getLevel() == leNiveau)
			laListe.put(pointAppui.getInfos().getIdentifiant(), pointAppui.getInfos());
		else
		{
			for (int i = 0; i < pointAppui.getChildCount(); i++)
			{
				listeFeuille((NoeudArbre) pointAppui.getChildAt(i), laListe, leNiveau);
			}
		}
	}
	
	static public void rempliUnModlele(ModeleArbre unModele, HashMap uneListe)
	{
		for (Iterator niveau1 = uneListe.keySet().iterator(); niveau1.hasNext();)
		{
			InterfaceMetier clef = (InterfaceMetier) niveau1.next();
			NoeudArbre unNoeud = new NoeudArbre(clef);
			unModele.addNoeud(unNoeud);
			HashMap listeSuivante = (HashMap) uneListe.get(clef);
			if (listeSuivante != null)
			{
				for (Iterator niveau2 = listeSuivante.keySet().iterator(); niveau2.hasNext();)
				{
					InterfaceMetier clef2 = (InterfaceMetier) niveau2.next();
					NoeudArbre unNoeud2 = new NoeudArbre(clef2);
					unModele.addNoeud(unNoeud, unNoeud2);
					HashMap listeSuivante2 = (HashMap) listeSuivante.get(clef2);
					if (listeSuivante2 != null)
					{
						for (Iterator niveau3 = listeSuivante2.keySet().iterator(); niveau3.hasNext();)
						{
							unModele.addNoeud(unNoeud2, new NoeudArbre((InterfaceMetier) niveau3.next()));
						}
					}
				}
			}
		}
	}	

}

class Racine implements InterfaceMetier
{
	private String leNom;

	public Racine(String nom)
	{
		leNom = nom;
	}

	public String getNom()
	{
		return leNom;
	}

	public String getIdentifiant()
	{
		return "-1";
	}

	public ImageIcon getIcone()
	{
		return null;
	}
}
