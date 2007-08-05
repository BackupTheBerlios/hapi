/*
 * Fichier C_ArbreFaceArbre.java
 * Auteur Cédric
 *
 */
package hapi.application.indicateurs;

import hapi.application.C_Hapi;
import hapi.donnees.modeles.ModeleArbre;
import hapi.donnees.modeles.NoeudArbre;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * Contrôleur permettant de faire interagir 2 arbres face à face
 */
public class C_ArbreFaceArbre implements Observer
{
	private ModeleArbre modeleUtilise = null;
	private ModeleArbre modeleNonUtilise = null;
	private String titreUtilise = null;
	private String titreNonUtilise = null;
	private String passerUtilise = null;
	private String passerNonUtilise = null;
	private int niveauMax = 0;
	private JTree arbreUtilise = null;
	private JTree arbreNonUtilise = null;

	public C_ArbreFaceArbre(String racineUtilise, String racineNonUtilise, String tUilise, String tNonUtilise, String pUtilise, String pNonUtilise, HashMap lesUtilises, HashMap lesNonUtilises, int leNiveauMax)
	{
		modeleUtilise = new ModeleArbre(racineUtilise);
		ModeleArbre.rempliUnModlele(modeleUtilise, lesUtilises);
		modeleUtilise.addObserver(this);
		modeleNonUtilise = new ModeleArbre(racineNonUtilise);
		ModeleArbre.rempliUnModlele(modeleNonUtilise, lesNonUtilises);
		modeleNonUtilise.addObserver(this);

		titreUtilise = tUilise;
		titreNonUtilise = tNonUtilise;
		passerUtilise = pUtilise;
		passerNonUtilise = pNonUtilise;
		niveauMax = leNiveauMax;
	}

	public void setToObserve(JTree aUtilise, JTree aNonUtilise)
	{
		arbreUtilise = aUtilise;
		arbreUtilise.setCellRenderer(new TreeRendering());

		arbreNonUtilise = aNonUtilise;
		arbreNonUtilise.setCellRenderer(new TreeRendering());
	}

	public ModeleArbre getModeleUtilise()
	{
		return modeleUtilise;
	}

	public ModeleArbre getModeleNonUtilise()
	{
		return modeleNonUtilise;
	}

	private boolean insert(ModeleArbre leModele, NoeudArbre lePere, NoeudArbre lEntite)
	{
		if (lEntite.isLeaf())
		{
			if (lEntite.getLevel() >= niveauMax)
			{
				leModele.addNoeud(lePere, lEntite);
				return true;
			}
			else
				return false;
		}
		else
		{
			int i = 0;
			while (i < lEntite.getChildCount())
			{
				if (!insert(leModele, lEntite, (NoeudArbre) lEntite.getChildAt(i)))
					i++;
			}

			return false;
		}
	}

	public void moveUtiliseToNonUtilise(TreePath leChemin)
	{
		NoeudArbre lEntite = (NoeudArbre) leChemin.getLastPathComponent();
		NoeudArbre lPere = (NoeudArbre) lEntite.getParent();
		insert(modeleNonUtilise, lPere, lEntite);
	}

	public void moveNonUtiliseToUtilise(TreePath leChemin)
	{
		NoeudArbre lEntite = (NoeudArbre) leChemin.getLastPathComponent();
		NoeudArbre lPere = (NoeudArbre) lEntite.getParent();
		insert(modeleUtilise, lPere, lEntite);
	}

	public String getTitreUtilise()
	{
		return titreUtilise;
	}

	public String getTitreNonUtilise()
	{
		return titreNonUtilise;
	}

	public String getPasserUtilise()
	{
		return passerUtilise;
	}

	public String getPasserNonUtilise()
	{
		return passerNonUtilise;
	}

	public void update(Observable arg0, Object arg1)
	{
		if (arbreUtilise != null)
		{
			arbreUtilise.updateUI();
			deployerLArbre(arbreUtilise);
		}

		if (arbreNonUtilise != null)
		{
			arbreNonUtilise.updateUI();
			deployerLArbre(arbreNonUtilise);
		}

	}

	private void deployerLArbre(JTree unArbre)
	{
		for (int i = 0; i < unArbre.getRowCount(); i++)
		{
			unArbre.expandRow(i);
		}
	}

	public HashMap getListeUtilise()
	{
		HashMap retour = new HashMap();
		modeleUtilise.listeFeuille(retour, niveauMax);

		return retour;
	}

	public HashMap getListeNonUtilise()
	{
		HashMap retour = new HashMap();
		modeleNonUtilise.listeFeuille(retour, niveauMax);

		return retour;
	}

	/**
	 * Renderer de l'arbre
	 */
	protected class TreeRendering extends DefaultTreeCellRenderer
	{
        private static final long serialVersionUID = -6373523290075718017L;

        /**
		 * Constructeur du renderer
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			//Le renderer par défaut est un Label, on peut donc lui associer une icone
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value instanceof NoeudArbre)
			{
				if (((NoeudArbre) value).getInfos().getIcone() != null)
					this.setIcon(((NoeudArbre) value).getInfos().getIcone());
				else
					this.setIcon(C_Hapi.LISTE_ICONE);
			}

			this.setToolTipText(null);
			this.setBackgroundSelectionColor(new Color(255, 51, 51));
			// retourner le JLabel avec l'icone et le texte associé
			return this;
		}
	}
}
