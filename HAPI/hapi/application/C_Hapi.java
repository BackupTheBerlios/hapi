/*
 * Auteur Cédric
 *
 */
package hapi.application;

import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.donnees.metier.E_Processus;
import hapi.donnees.metier.interfaces.InterfaceMetier;
import hapi.donnees.modeles.ModeleArbreDesProcessus;
import hapi.donnees.modeles.NoeudArbre;
import hapi.exception.FichierConfInexistantException;
import hapi.exception.UtilisateurNonIdentifieException;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Contrôleur principal de l'application
 */
public class C_Hapi
{
	public final static ImageIcon QUITTER_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/quitter.gif"));
	public final static ImageIcon IMPORTER_PROCESSUS_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/importerProcessus.gif"));
	public final static ImageIcon IMPORTER_MESURES_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/importerMesures.gif"));
	public final static ImageIcon CONFIGURATION_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/configuration.gif"));
	public final static ImageIcon A_PROPOS_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/aPropos.gif"));
	public final static ImageIcon PROCESSUS_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/processus.gif"));
	public final static ImageIcon PROJET_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/projet.gif"));
	public final static ImageIcon EVALUATION_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/evaluation.gif"));
	public final static ImageIcon LISTE_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/liste.gif"));
	public final static ImageIcon COMPOSANT_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/composant.gif"));
	public final static ImageIcon ELEMENT_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/element.gif"));
	public final static ImageIcon ACTIVITE_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/activite.gif"));
	public final static ImageIcon PRODUIT_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/produit.gif"));
	public final static ImageIcon ROLE_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/role.gif"));
	public final static ImageIcon DEFINITION_ICONE = new ImageIcon(ClassLoader.getSystemResource("hapi/application/ressources/icones/definition.gif"));

	/**
	 * Constructeur
	 */
	public C_Hapi() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FichierConfInexistantException
	{
		//Création du controleur de configuration
		new C_Configuration(true);
	}

	/**
	 * Méthode rechargeant l'arbre
	 */
	public void refreshArbre(ModeleArbreDesProcessus leModele)
	{
		leModele.clear();

		//Pour chaque processus identifiés
		for (int i = 0; i < C_Processus.size(); i++)
		{
			NoeudArbre np = new NoeudArbre(C_Processus.get(i));
			leModele.addProcessus(np);
			String id_processus = C_Processus.get(i).getIdentifiant();
			try
			{
				int roleUtilisateur = C_Utilisateur.getRole();

				if (roleUtilisateur != 3)
				{
					for (int j = 0; j < C_ExecutionProcessus.size(id_processus); j++)
					{
						String id_exec = C_ExecutionProcessus.getClef(id_processus, j);
						NoeudArbre ne = new NoeudArbre(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_exec));
						leModele.addExecutionProcessus(np, ne);

						if (roleUtilisateur == 1)
						{
							for (int k = 0; k < C_Evaluation.size(id_processus, new Integer(id_exec).intValue()); k++)
							{
								int numIt = C_Evaluation.getClef(id_processus, new Integer(id_exec).intValue(), k);
								leModele.addEvaluation(ne, new NoeudArbre(C_Evaluation.getEvaluation(id_processus, new Integer(id_exec).intValue(), numIt)));
							}
						}
					}
				}
			}
			catch (UtilisateurNonIdentifieException e)
			{
				System.err.println("-----ERREUR GRAVE N° 2-----");
			}
		}
	}

	/**
	 * Récupération du code correspondant au type sélectionné dans l'arbre
	 * 0 : E_Processus
	 * 1 : E_ExecutionProcessus
	 * 2 : E_Evaluation
	 */
	public int getSelectionArbre(C_ArbreDesProcessus lArbre)
	{
		if (lArbre.getSelection() == null)
			return -1;
		else
		{
			if (lArbre.getSelection() instanceof E_Processus)
				return 0;
			if (lArbre.getSelection() instanceof E_ExecutionProcessus)
				return 1;
			if (lArbre.getSelection() instanceof E_Evaluation)
				return 2;
		}
		return -1;
	}

	/**
	 * Récupération de l'identificant de  l'entité sélectionnée
	 */
	public String getElementSelectionne(C_ArbreDesProcessus lArbre)
	{
		return lArbre.getSelection().getIdentifiant();
	}

	/**
	 * Récupération des identifiants des parents de la selection
	 * @param lArbre
	 * @return
	 */
	public ArrayList getCheminElementSelectionne(C_ArbreDesProcessus lArbre)
	{
		// liste des identifiants de tous les parents de l'élément sélectionné
		ArrayList id_parents = new ArrayList();
		for (Iterator it = lArbre.getChemin().iterator(); it.hasNext();)
		{
			id_parents.add(((InterfaceMetier) (it.next())).getIdentifiant());
		}
		return id_parents;
	}
}
