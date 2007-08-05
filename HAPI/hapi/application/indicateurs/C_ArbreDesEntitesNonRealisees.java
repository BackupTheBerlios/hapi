/*
 * Created on 6 mars 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.C_Hapi;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_Produit;
import hapi.application.metier.C_Role;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.modeles.ModeleArbreDesEntitesNonUtilisees;
import hapi.donnees.modeles.NoeudArbre;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Natalia
 *
 */
public class C_ArbreDesEntitesNonRealisees extends JTree
{
    private static final long serialVersionUID = -8053040100878250029L;
    // modele
	private ModeleArbreDesEntitesNonUtilisees modeleArbre = null;
	// identifiant du processus concerné
	private String idProcessus = null;
	private int idExec = 0;
	private int idEval = 0;

	public C_ArbreDesEntitesNonRealisees(String idProcessus, int idExec, int idEval, int indicateur)
	{
		// création de l'arbre
		super();
		// initialisation du modèle
		modeleArbre = new ModeleArbreDesEntitesNonUtilisees(Bundle.getText("BM_OrdreDesActivites_racine"));
		this.setCellRenderer(new TreeRendering());
		this.idProcessus = idProcessus;
		this.idExec = idExec;
		this.idEval = idEval;

		this.setSelectionRow(0);
		this.setAutoscrolls(true);
		this.setScrollsOnExpand(true);
		// remplissage de l'arbre en fonction de l'indicateur
		switch (indicateur)
		{
			case 0 : /* RUA */
				remplirArbreRUA();
				break;
			case 1 : /* RCT */
				remplirArbreRCT();
				break;
			case 2 : /* RUP */
				remplirArbreRUP();
				break;
			case 3 : /* RUR */
				remplirArbreRUR();
				break;
		}
		// affectation du modele
		this.setModel(modeleArbre);
	}

	private void remplirArbreRUA()
	{
		ArrayList listeDesActivitesNonRealisees = C_Evaluation.getEvaluation(idProcessus, idExec, idEval).getActivitesNonUtilisees();

		// récupération de la liste des définitions de travail par composant du processus
		// structure < idComposant, < idDefinition, E_Definition> >
		HashMap listeDefinitions = C_Definition.getDefinitionsDesComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeDefinitions.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre(((E_Composant) C_Composant.getComposant(idProcessus, idComp)));
			modeleArbre.addComposant(nc);

			// ... 2 : pour chaque définition associée
			for (Iterator itDef = ((HashMap) listeDefinitions.get(idComp)).keySet().iterator(); itDef.hasNext();)
			{
				String idDef = itDef.next().toString();
				// ... 3 : ajout de le définition de travail dans l'arbre
				NoeudArbre nd = new NoeudArbre(((E_Definition) ((HashMap) listeDefinitions.get(idComp)).get(idDef)));
				modeleArbre.addDefinitionDeTravail(nc, nd);

				// ... 4 : ajout des activites non réalisées de cette définition            
				for (Iterator itAct = listeDesActivitesNonRealisees.iterator(); itAct.hasNext();)
				{
					String idAct = itAct.next().toString();
					// ... 5 : si l'activité appartient à la définition de travail passée en paramètre ...
					if (C_Activite.getActivite(idProcessus, idAct).getAgregatDefinitionTravail().equals(idDef))
					{
						// ... alors on l'ajoute
						modeleArbre.addActivite(nd, new NoeudArbre(C_Activite.getActivite(idProcessus, idAct)));
					}
				}
			}
		}
	}

	private void remplirArbreRCT()
	{}

	private void remplirArbreRUR()
	{
		ArrayList listeDesRolesNonUtilises = C_Evaluation.getEvaluation(idProcessus, idExec, idEval).getRolesNonUtilises();

		// récupération de la liste des composants du processus
		// structure < idComposant, E_Composant >
		HashMap listeComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeComposants.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre((E_Composant) listeComposants.get(idComp));
			modeleArbre.addComposant(nc);

			// ... 2 : ajout des roles non réalisées de cette définition            
			for (Iterator itRole = listeDesRolesNonUtilises.iterator(); itRole.hasNext();)
			{
				String idRole = itRole.next().toString();
				// ... 3 : si le role appartient à la définition de travail passée en paramètre ...
				if (C_Role.getRole(idProcessus, idRole).getAgregatComposant().equals(idComp))
				{
					// ... alors on l'ajoute
					modeleArbre.addRole(nc, new NoeudArbre(C_Role.getRole(idProcessus, idRole)));
				}
			}
		}
	}

	private void remplirArbreRUP()
	{
		ArrayList listeDesProduitsNonRealises = C_Evaluation.getEvaluation(idProcessus, idExec, idEval).getProduitsNonUtilises();

		// récupération de la liste des composants du processus
		// structure < idComposant, E_Composant >
		HashMap listeComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeComposants.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre((E_Composant) listeComposants.get(idComp));
			modeleArbre.addComposant(nc);

			// ... 2 : ajout des roles non réalisées de cette définition            
			for (Iterator itProd = listeDesProduitsNonRealises.iterator(); itProd.hasNext();)
			{
				String idProd = itProd.next().toString();
				// ... 3 : si le role appartient à la définition de travail passée en paramètre ...
				if (C_Produit.getProduit(idProcessus, idProd).getAgregatComposant().equals(idComp))
				{
					// ... alors on l'ajoute
					modeleArbre.addProduit(nc, new NoeudArbre(C_Produit.getProduit(idProcessus, idProd)));
				}
			}
		}
	}

	public void deployerLArbre()
	{
		for (int i = 0; i < this.getRowCount(); i++)
		{
			this.expandRow(i);
		}
	}

	/**
	 * Renderer de l'arbre
	 */
	protected class TreeRendering extends DefaultTreeCellRenderer
	{
        private static final long serialVersionUID = -8171291450763932485L;

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
				this.setIcon(C_Hapi.LISTE_ICONE);

			this.setToolTipText(null);
			this.setBackgroundSelectionColor(new Color(255, 51, 51));
			// retourner le JLabel avec l'icone et le texte associé
			return this;
		}
	}
}
