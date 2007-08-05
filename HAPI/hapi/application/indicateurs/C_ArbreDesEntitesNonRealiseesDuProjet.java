/*
 * Created on 7 mars 2005
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
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.modeles.ModeleArbreDesEntitesNonUtilisees;
import hapi.donnees.modeles.NoeudArbre;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Natalia
 *
 */
public class C_ArbreDesEntitesNonRealiseesDuProjet extends JTree
{
    private static final long serialVersionUID = -3158456985632575822L;
    // modele
	private ModeleArbreDesEntitesNonUtilisees modeleArbre = null;
	// identifiant du processus concern�
	private String idProcessus = null;

	private HashMap listeEvaluations = null;

	public C_ArbreDesEntitesNonRealiseesDuProjet(String idProcessus, int idExec, int indicateur)
	{
		// cr�ation de l'arbre
		super();
		// initialisation du mod�le
		modeleArbre = new ModeleArbreDesEntitesNonUtilisees(Bundle.getText("BM_OrdreDesActivites_racine"));
		this.setCellRenderer(new TreeRendering());
		this.idProcessus = idProcessus;

		//	r�cup�ration de la liste des �valuations de l'execution de processus
		listeEvaluations = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExec);

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
		HashSet listeDesActivitesNonRealisees = new HashSet();
		Iterator itEval = listeEvaluations.keySet().iterator();
		if (listeEvaluations.size() > 0)
		{
			// r�cup�ration de la premi�re �valuation
			listeDesActivitesNonRealisees.addAll(((E_Evaluation) listeEvaluations.get((Integer) itEval.next())).getActivitesNonUtilisees());
		}

		// pour chaque �valuation, rechercher les activit�s non r�alis�es
		// et faire l'intersection avec les activit�s non r�alis�es des autres �valuations
		while (itEval.hasNext())
		{
			Integer cle = (Integer) itEval.next();
			if (((E_Evaluation) listeEvaluations.get(cle)).getActivitesNonUtilisees() != null)
				listeDesActivitesNonRealisees.retainAll(((E_Evaluation) listeEvaluations.get(cle)).getActivitesNonUtilisees());
		}

		// r�cup�ration de la liste des d�finitions de travail par composant du processus
		// structure < idComposant, < idDefinition, E_Definition> >
		HashMap listeDefinitions = C_Definition.getDefinitionsDesComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeDefinitions.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre(((E_Composant) C_Composant.getComposant(idProcessus, idComp)));
			modeleArbre.addComposant(nc);

			// ... 2 : pour chaque d�finition associ�e
			for (Iterator itDef = ((HashMap) listeDefinitions.get(idComp)).keySet().iterator(); itDef.hasNext();)
			{
				String idDef = itDef.next().toString();
				// ... 3 : ajout de le d�finition de travail dans l'arbre
				NoeudArbre nd = new NoeudArbre(((E_Definition) ((HashMap) listeDefinitions.get(idComp)).get(idDef)));
				modeleArbre.addDefinitionDeTravail(nc, nd);

				// ... 4 : ajout des activites non r�alis�es de cette d�finition            
				for (Iterator itAct = listeDesActivitesNonRealisees.iterator(); itAct.hasNext();)
				{
					String idAct = itAct.next().toString();
					// ... 5 : si l'activit� appartient � la d�finition de travail pass�e en param�tre ...
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
		HashSet listeDesRolesNonUtilises = new HashSet();

		Iterator itEval = listeEvaluations.keySet().iterator();
		if (listeEvaluations.size() > 0)
		{
			listeDesRolesNonUtilises.addAll(((E_Evaluation) listeEvaluations.get((Integer) itEval.next())).getRolesNonUtilises());
		}

		// pour chaque �valuation, rechercher les r�les non r�alis�es
		while (itEval.hasNext())
		{
			Integer cle = (Integer) itEval.next();
			if (((E_Evaluation) listeEvaluations.get(cle)).getRolesNonUtilises() != null)
				listeDesRolesNonUtilises.retainAll(((E_Evaluation) listeEvaluations.get(cle)).getRolesNonUtilises());
		}

		// r�cup�ration de la liste des composants du processus
		// structure < idComposant, E_Composant >
		HashMap listeComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeComposants.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre((E_Composant) listeComposants.get(idComp));
			modeleArbre.addComposant(nc);

			// ... 2 : ajout des roles non r�alis�es de cette d�finition            
			for (Iterator itRole = listeDesRolesNonUtilises.iterator(); itRole.hasNext();)
			{
				String idRole = itRole.next().toString();
				// ... 3 : si le role appartient � la d�finition de travail pass�e en param�tre ...
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
		HashSet listeDesProduitsNonRealises = new HashSet();

		Iterator itEval = listeEvaluations.keySet().iterator();

		if (listeEvaluations.size() > 0)
		{
			listeDesProduitsNonRealises.addAll(((E_Evaluation) listeEvaluations.get((Integer) itEval.next())).getProduitsNonUtilises());
		}

		// pour chaque �valuation, rechercher la liste des produits non utilis�s
		while (itEval.hasNext())
		{
			Integer cle = (Integer) itEval.next();
			if (((E_Evaluation) listeEvaluations.get(cle)).getProduitsNonUtilises() != null)
				listeDesProduitsNonRealises.retainAll(((E_Evaluation) listeEvaluations.get(cle)).getProduitsNonUtilises());
		}

		// r�cup�ration de la liste des composants du processus
		// structure < idComposant, E_Composant >
		HashMap listeComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		// parcours de cette liste, pour chaque composant ... 
		for (Iterator it = listeComposants.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			// ... 1 : ajout d'un noeud le concernant
			NoeudArbre nc = new NoeudArbre((E_Composant) listeComposants.get(idComp));
			modeleArbre.addComposant(nc);

			// ... 2 : ajout des roles non r�alis�es de cette d�finition            
			for (Iterator itProd = listeDesProduitsNonRealises.iterator(); itProd.hasNext();)
			{
				String idProd = itProd.next().toString();
				// ... 3 : si le role appartient � la d�finition de travail pass�e en param�tre ...
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

	public int getNombreDActivites()
	{
		return modeleArbre.getNombreDActivites();
	}

	public int getNombreDeRoles()
	{
		return modeleArbre.getNombreDeRoles();
	}

	public int getNombreDeProduits()
	{
		return modeleArbre.getNombreDeProduits();
	}

	/**
	 * Renderer de l'arbre
	 */
	protected class TreeRendering extends DefaultTreeCellRenderer
	{
        private static final long serialVersionUID = 8596748439826082711L;

        /**
		 * Constructeur du renderer
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			//Le renderer par d�faut est un Label, on peut donc lui associer une icone
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value instanceof NoeudArbre)
			{
				this.setIcon(((NoeudArbre) value).getInfos().getIcone());
			}
			else
				this.setIcon(C_Hapi.LISTE_ICONE);

			this.setToolTipText(null);
			this.setBackgroundSelectionColor(new Color(255, 51, 51));
			// retourner le JLabel avec l'icone et le texte associ�
			return this;
		}
	}

}
