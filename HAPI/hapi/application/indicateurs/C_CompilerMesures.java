/*
 * Created on 22 févr. 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.C_Mesures;
import hapi.donnees.E_Tache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Natalia
 *
 */
public class C_CompilerMesures
{

	float RUA, RUP, RUR, RCT;
	HashMap ordreDesTaches = null;

	public static float compilerRUA(HashMap lesActivitesDeLIteration, ArrayList listeDesActivitesDuProcessus)
	{
		int nbActivitesDansProcessus = 0, nbActivitesHorsProcessus = 0;
		// pour toutes les activités, vérifier si celle ci appartient ou non au processus 

		nbActivitesDansProcessus = lesActivitesDeLIteration.size() - 1;
		nbActivitesHorsProcessus = ((ArrayList) lesActivitesDeLIteration.get("-1")).size();

		//	calcul du ratio d'utilisation des activites
		return C_Mesures.calculRUA(nbActivitesHorsProcessus, nbActivitesDansProcessus, listeDesActivitesDuProcessus.size());
	}

	public static float compilerRUP(HashMap lesProduitsDeLIteration, ArrayList listeDesProduitsDuProcessus)
	{
		int nbProduitsHorsProcessus = 0, nbProduitsDansProcessus = 0;
		// pour tous les produits, vérifier s'ils sont ou non dans le processus

		nbProduitsDansProcessus = lesProduitsDeLIteration.size() - 1;
		nbProduitsHorsProcessus = ((ArrayList) lesProduitsDeLIteration.get("-1")).size();

		// calcul du ratio d'utilisation des produits
		return C_Mesures.calculRUP(nbProduitsHorsProcessus, nbProduitsDansProcessus, listeDesProduitsDuProcessus.size());
	}

	public static float compilerRUR(HashMap lesRolesDeLIteration, ArrayList listeDesRolesDuProcessus)
	{

		int nbRolesDansProcessus = 0, nbRolesHorsProcessus = 0;

		nbRolesDansProcessus = lesRolesDeLIteration.size() - 1;
		nbRolesHorsProcessus = ((ArrayList) lesRolesDeLIteration.get("-1")).size();

		// calcul du ratio d'utilisation des rôles
		return C_Mesures.calculRUR(nbRolesHorsProcessus, nbRolesDansProcessus, listeDesRolesDuProcessus.size());
	}

	public static float compilerRCT(HashMap lesActivitesDeLIteration, ArrayList lesActivitesDuCycleDeVie)
	{
		// 1 : charge des tâches liées à une activité
		// 2 : charge totale

		double chargeTacheLieesActivites = 0, chargeTotale = 0;
		double chargeTachesDUneActivite = 0;

		// parcours de la liste des activités de l'itération 
		for (Iterator it = lesActivitesDeLIteration.keySet().iterator(); it.hasNext();)
		{
			String cle = it.next().toString();
			for (Iterator ittache = ((ArrayList) lesActivitesDeLIteration.get(cle)).iterator(); ittache.hasNext();)
			{
				chargeTachesDUneActivite += ((E_Tache) ittache.next()).getTempsPasse();
			}

			// l'activité est une activité du processus ...
			if (lesActivitesDuCycleDeVie.contains(cle))
			{
				//	... on incremente la charge (1) de la somme des durées des tâches de l'activité
				chargeTacheLieesActivites += chargeTachesDUneActivite;
			}
			// dans tous les cas, on incrémente la charge totale (2)
			chargeTotale += chargeTachesDUneActivite;
		}

		// calcul du ratio de charge des tâches 
		return C_Mesures.calculRCT(new Float(chargeTacheLieesActivites).floatValue(), new Float(chargeTotale).floatValue());
	}

	public static HashMap OrdonnerLesTaches(HashMap lesActivitesDeLIteration)
	{
		// liste qui servira pour ordonner les taches
		// (liste de E_Tache - id_activite)
		ArrayList ordreDesTaches_tmp = new ArrayList();
		int i = 0; // compteur

		// parcours de la hashmap de la liste des activités
		for (Iterator it = lesActivitesDeLIteration.keySet().iterator(); it.hasNext();)
		{
			String cle = it.next().toString(); // identifiant de l'activité

			// pour chacune des tâches de cette activités
			for (Iterator ittache = ((ArrayList) lesActivitesDeLIteration.get(cle)).iterator(); ittache.hasNext();)
			{
				E_Tache tache = (E_Tache) ittache.next();
				// l'ordonner pas rapport aux autres taches
				int taille = ordreDesTaches_tmp.size();
				for (i = 0; i < taille; i += 2)
				{
					// si la date est antérieurs à la tache courante ...
					if ((tache.getDateFinReelle()).compareTo((Date) ((E_Tache) ordreDesTaches_tmp.get(i)).getDateFinReelle()) < 0)
					{
						// ... ajout de la tâche à cet endroit de la liste
						ordreDesTaches_tmp.add(i, tache);
						ordreDesTaches_tmp.add(i + 1, cle);
					}
				}
				// si la tache est postérieure à toutes les autres ... 
				if (i == ordreDesTaches_tmp.size())
				{
					// ... ajout en fin de liste
					ordreDesTaches_tmp.add(tache);
					ordreDesTaches_tmp.add(cle);
				}
			}
		}

		// structure < rang (integer), infos (ArrayList) >
		// infos = nomTache + idActivites
		HashMap ordreDesTaches = new HashMap();
		// transformation de l'arraylist en hashmap 
		i = 1;
		for (Iterator it = ordreDesTaches_tmp.iterator(); it.hasNext(); i++)
		{
			ArrayList param = new ArrayList();
			param.add(((E_Tache) it.next()).getNom()); // nom de la tache
			param.add(it.next().toString()); // identifiant de l'activité
			ordreDesTaches.put(new Integer(i), param);
		}

		return ordreDesTaches;
	}

	public static HashMap compilerRTP(HashMap chargeParComposant)
	{
		return C_Mesures.calculRTP(chargeParComposant);
	}
}