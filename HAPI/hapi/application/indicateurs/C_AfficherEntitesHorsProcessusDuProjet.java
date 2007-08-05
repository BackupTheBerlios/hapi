/*
 * Created on 7 mars 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.modeles.ModeleTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Natalia
 *
 */
public class C_AfficherEntitesHorsProcessusDuProjet
{
	private String idProcessus = null;

	private ModeleTable modele = null;
	private HashMap listeEvaluations = null;

	public C_AfficherEntitesHorsProcessusDuProjet(String idProcesuss, int idExec, int indicateur)
	{
		this.idProcessus = idProcesuss;

		//	récupération de la liste des évaluations de l'execution de processus
		listeEvaluations = C_Evaluation.getEvaluationPourUneExecProc(idProcessus, idExec);

		modele = new ModeleTable();
		switch (indicateur)
		{
			case 0 : /* RUA */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Activites"));
				break;
			case 2 : /* RUP */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Produits"));
				break;
			case 3 : /* RUR */
				modele.addColumn(Bundle.getText("BM_DetailCalculDesIndicateurs_Roles"));
				break;
		}

	}

	public ModeleTable getModeleDesActivitesHorsProcessus()
	{
		initialiserLeModele();

		// récupération des activités hors processus du projet
		HashSet activitesHorsProcessus = new HashSet();

		for (Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
			Integer cle = (Integer) it.next();
			activitesHorsProcessus.addAll(((E_Evaluation) listeEvaluations.get(cle)).getActivitesHorsProcessus());
		}

		for (Iterator it = activitesHorsProcessus.iterator(); it.hasNext();)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(it.next());
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public ModeleTable getModeleDesRolesHorsProcessus()
	{
		initialiserLeModele();

		// récupération des rôles hors processus du projet
		HashSet rolesHorsProcessus = new HashSet();
		for (Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
			Integer cle = (Integer) it.next();
			rolesHorsProcessus.addAll(((E_Evaluation) listeEvaluations.get(cle)).getRolesHorsProcessus());
		}

		for (Iterator it = rolesHorsProcessus.iterator(); it.hasNext();)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(it.next());
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public ModeleTable getModeleDesProduitsHorsProcessus()
	{
		initialiserLeModele();

		// récupération des produits hors processus du projet
		HashSet produitsHorsProcessus = new HashSet();
		for (Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
			Integer cle = (Integer) it.next();
			produitsHorsProcessus.addAll(((E_Evaluation) listeEvaluations.get(cle)).getProduitsHorsProcessus());
		}

		for (Iterator it = produitsHorsProcessus.iterator(); it.hasNext();)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(it.next());
			modele.addRow(uneLigne);
		}

		return modele;
	}

	public int getNombreActivitesNonRealisees(C_ArbreDesEntitesNonRealiseesDuProjet arbre)
	{
		/*int nb = 0;
		for(Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
		    Integer cle = (Integer)it.next();
		    nb += ((E_Evaluation)listeEvaluations.get(cle)).getActivitesHorsProcessus().size();
		}   */
		return arbre.getNombreDActivites();
	}

	public int getNombreProduitsNonUtilises(C_ArbreDesEntitesNonRealiseesDuProjet arbre)
	{
		/*int nb = 0;
		for(Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
		    Integer cle = (Integer)it.next();
		    nb += ((E_Evaluation)listeEvaluations.get(cle)).getProduitsHorsProcessus().size();
		}*/
		return arbre.getNombreDeProduits();
	}

	public int getNombreRolesNonUtilises(C_ArbreDesEntitesNonRealiseesDuProjet arbre)
	{
		/*int nb = 0;
		for(Iterator it = listeEvaluations.keySet().iterator(); it.hasNext();)
		{
		    Integer cle = (Integer)it.next();
		    nb += ((E_Evaluation)listeEvaluations.get(cle)).getRolesHorsProcessus().size();
		} */
		return arbre.getNombreDeRoles();
	}

	private void initialiserLeModele()
	{
		while (modele.getRowCount() > 0)
		{
			modele.removeRow(0);
		}
	}

}
