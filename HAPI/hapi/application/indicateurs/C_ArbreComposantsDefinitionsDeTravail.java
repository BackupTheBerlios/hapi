/*
 * Created on 5 mars 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_Hapi;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.interfaces.InterfaceMetier;
import hapi.donnees.modeles.ModeleArbre;
import hapi.donnees.modeles.NoeudArbre;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * @author Natalia
 */
public class C_ArbreComposantsDefinitionsDeTravail extends JTree implements Observer
{
    private static final long serialVersionUID = 4558937786801917289L;
    // modele
	private ModeleArbre modeleArbre = null;
	// identifiant du processus concerné
	private String idProcessus = null;
	private boolean utiliseCourant = false;
	private int idCycleDeVie = -1; 

	// noeud selectionné
	private NoeudArbre selection = null;

	// chemin du noeud selectionné
	private Object chemin[] = null;

	// écouteur de l'arbre
	private MouseListener actionSurClic = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent arg0)
		{}

		public void mouseEntered(MouseEvent arg0)
		{}

		public void mouseExited(MouseEvent arg0)
		{}

		public void mousePressed(MouseEvent e)
		{
			// Récupération de la ligne sélectionnée
			int selRow = getRowForLocation(e.getX(), e.getY());

			// Récupération du chemin associé
			TreePath selPath = getPathForLocation(e.getX(), e.getY());

			// S'il y a vraiment une ligne sélectionnée
			if (selRow != -1)
			{
				if (selPath.getLastPathComponent() instanceof NoeudArbre)
				{
					// Récupération du noeud
					selection = (NoeudArbre) selPath.getLastPathComponent();
					chemin = selPath.getPath();
				}
				else
				{
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

		public void mouseReleased(MouseEvent arg0)
		{}
	};

	public C_ArbreComposantsDefinitionsDeTravail(String idProcessus, int idCycleDeVie, boolean utiliseCourant)
	{
		// création de l'arbre
		super();
		// initialisation du modèle
		//modeleArbre = new ModeleArbreComposantsDefinitionsDeTravail(Bundle.getText("BM_OrdreDesActivites_racine"));
		modeleArbre = new ModeleArbre(Bundle.getText("BM_OrdreDesActivites_racine"));
		this.setCellRenderer(new TreeRendering());
		this.idProcessus = idProcessus;
		this.utiliseCourant = utiliseCourant;
		this.idCycleDeVie = idCycleDeVie;

		this.setSelectionRow(0);
		this.setAutoscrolls(true);
		this.setScrollsOnExpand(true);
		remplirArbre();
		ajouterListener();
		// affectation du modele
		modeleArbre.addObserver(this);
		this.setModel(modeleArbre);
	}

	private void remplirArbre()
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesDefinitions = null;
		HashMap lesActivites = null;
		//Les Roles
		HashMap lesEntites = null;
		HashMap lesEntites2 = null;

		//Récupératoin des composants et des définitions
		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(idProcessus);
			//Récupération de la liste des définitions de travail
			lesDefinitions = C_Definition.getDefinitionsDuProcessus(idProcessus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de la liste des définitions de travail
			lesDefinitions = C_DefinitionTemporaire.getDefinitions();
		}

			if (idCycleDeVie == -1)
			{
				if (utiliseCourant)
				{
					//Récupération de la liste des Activites
					lesActivites = C_Activite.getActivitesDuProcessus(idProcessus);
				}
				else
				{
					//Récupération de la liste des Activites
					lesActivites = C_ActiviteTemporaire.getActivites();
				}
			}
			else
			{
				lesActivites = new HashMap();
				//Récupération des Activites suivi par le cycle de vie
				ArrayList lesIdActivitesSuivies = (C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesActivites());
				//Construction de la hashmap <id_activité><activité>
				for (Iterator it = lesIdActivitesSuivies.iterator(); it.hasNext();)
				{
					String unIdActivite = (String) it.next();

					if (utiliseCourant)
					{
						//Récupération de la liste des Activites
						E_Activite uneActivite = C_Activite.getActivite(idProcessus, unIdActivite);

						if (uneActivite != null)
							lesActivites.put(unIdActivite, uneActivite);
					}
					else
					{
						//Récupération de la liste des Activites
						if (C_ActiviteTemporaire.getActivites().get(unIdActivite) != null)
							lesActivites.put(unIdActivite, C_ActiviteTemporaire.getActivites().get(unIdActivite));
					}

				}

		}

		//Construction de l'arborescence
		//Pour chaque composant
		for (Iterator it = lesComposants.keySet().iterator(); it.hasNext();)
		{
			String clef = (String) it.next();
			lesEntites2 = new HashMap();

			//Pour chaque définition
			for (Iterator it2 = lesDefinitions.keySet().iterator(); it2.hasNext();)
			{
				String clef2 = (String) it2.next();
				lesEntites = new HashMap();
				E_Definition uneDefinition = (E_Definition) lesDefinitions.get(clef2);

				//Si la définition apparatient au composant
				if (uneDefinition != null)
					if (uneDefinition.getAgregatComposant().equals(clef))
					{
						//Pour chaque Activites
						for (Iterator it3 = lesActivites.keySet().iterator(); it3.hasNext();)
						{
							String clef3 = (String) it3.next();
							E_Activite unActivite = (E_Activite) lesActivites.get(clef3);

							//Si le Activite apparatient au composant
							if (unActivite != null)
								if (unActivite.getAgregatDefinitionTravail().equals(clef2))
								{
									//Insertion dans la liste
									lesEntites.put(unActivite, null);
								}
						}
						lesEntites2.put(uneDefinition, lesEntites);
					}
			}

			//Création d'un niveau d'arborescence
			retour.put(lesComposants.get(clef), lesEntites2);
		}
		
		
		ModeleArbre.rempliUnModlele(modeleArbre, retour);		
		
		/*
		
		if (utiliseCourant)
		{
			// récupération de la liste des définitions de travail par composant
			// du processus
			// structure < idComposant, < idDefinition, E_Definition> >
			HashMap listeDefinitions = C_Definition.getDefinitionsDesComposantsDuProcessus(idProcessus);
			// structure < idDef, < idAct, E_Act > >
			HashMap listeDesActivites = null;
			if (idCycleDeVie != -1)
			{
				ArrayList listeIdActiviteCycle = C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesActivites();
				listeDesActivites = new HashMap();
				for (Iterator it = listeIdActiviteCycle.iterator();it.hasNext();)
				{
					String clef = it.next();
					listeDesActivites.put(clef,)
				}
			}
			
			HashMap listeDesActivites = C_Activite.getActivitesParDefinitionDeTravail(idProcessus);
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

					// 4 : pour chaque activite de l'iteration
					for (Iterator itAct = ((HashMap) listeDesActivites.get(idDef)).keySet().iterator(); itAct.hasNext();)
					{
						String idAct = itAct.next().toString();
						// 5 : ajout de l'activité
						modeleArbre.addActivite(nd, new NoeudArbre((E_Activite) ((HashMap) listeDesActivites.get(idDef)).get(idAct)));
					}
				}
			}
		}
		else
		{
			// récupération de la liste des définitions de travail par composant
			// du processus
			// structure < idComposant, < idDefinition, E_Definition> >
			HashMap listeDefinitions = C_DefinitionTemporaire.getDefinitions();
			// structure < idDef, < idAct, E_Act > >
			HashMap listeDesActivites = C_ActiviteTemporaire.getActivites();
			// parcours de cette liste, pour chaque composant ...
			for (Iterator it = listeDefinitions.keySet().iterator(); it.hasNext();)
			{
				String idComp = it.next().toString();
				// ... 1 : ajout d'un noeud le concernant
				NoeudArbre nc = new NoeudArbre(((E_Composant) C_ComposantTemporaire.getComposant(idComp)));
				modeleArbre.addComposant(nc);

				// ... 2 : pour chaque définition associée
				for (Iterator itDef = ((HashMap) listeDefinitions.get(idComp)).keySet().iterator(); itDef.hasNext();)
				{
					String idDef = itDef.next().toString();
					// ... 3 : ajout de le définition de travail dans l'arbre
					NoeudArbre nd = new NoeudArbre(((E_Definition) ((HashMap) listeDefinitions.get(idComp)).get(idDef)));
					modeleArbre.addDefinitionDeTravail(nc, nd);

					// 4 : pour chaque activite de l'iteration
					for (Iterator itAct = ((HashMap) listeDesActivites.get(idDef)).keySet().iterator(); itAct.hasNext();)
					{
						String idAct = itAct.next().toString();
						// 5 : ajout de l'activité
						modeleArbre.addActivite(nd, new NoeudArbre((E_Activite) ((HashMap) listeDesActivites.get(idDef)).get(idAct)));
					}
				}
			}		
		}*/
	}

	private void ajouterListener()
	{
		this.addMouseListener(actionSurClic);
	}

	/**
	 * Récupération de l'objet sélectionné
	 * 
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
			// S'il n'y a pas de sélection, on renvoie null
			return null;
		}
	}

	/**
	 * renvoie la liste des identifiants des objets appartenant au chemin de la
	 * sélection le premier élément étant du niveau juste au dessous de la
	 * racine et le dernier élément étant la selection elle même
	 * 
	 * @return
	 */
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
			// S'il n'y a pas de sélection, on renvoie null
			return null;
		}
	}

	public boolean isSelectionUneActivite()
	{
		InterfaceMetier ob = getSelection();
		if (ob instanceof E_Activite)
			return true;
		return false;
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
        private static final long serialVersionUID = 9134412615494029212L;

        /**
		 * Constructeur du renderer
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			//Le renderer par défaut est un Label, on peut donc lui associer
			// une icone
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

	public void update(Observable arg0, Object arg1)
	{
		this.updateUI();
		for (int i = 0; i < getRowCount(); i++)
		{
			expandRow(i);
		}
	}

}
