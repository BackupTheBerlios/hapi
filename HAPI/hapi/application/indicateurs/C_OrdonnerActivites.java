/*
 * Created on 5 mars 2005
 *
 */
package hapi.application.indicateurs;

import hapi.application.ressources.Bundle;
import hapi.donnees.modeles.ModeleTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Natalia
 *
 */
public class C_OrdonnerActivites
{
	// liste des identifiants (ordonnés) des activités 
	HashMap ordreDesTaches = null;
	// identifiant du processus concerné
	String idProcessus = null;
	// modele de la table 
	ModeleTable lemodele = null;

	public C_OrdonnerActivites(String idProcessus, HashMap ordreTaches)
	{
		this.ordreDesTaches = ordreTaches;
		this.idProcessus = idProcessus;
	}

	public void creerLeModele()
	{
		lemodele = new ModeleTable();
		lemodele.addColumn(Bundle.getText("BM_OrdreDesActivites_Rang"));
		lemodele.addColumn(Bundle.getText("BM_OrdreDesActivites_Tache"));
	}

	public ModeleTable getModeleVide()
	{
		return lemodele;
	}

	public ModeleTable getModelePourLaDefinition(String idAct)
	{
		reinitialiserLeModele();

		// liste des activites de la définition de travail
		HashMap lesActivitesConcernees = new HashMap();
		int ordre = 1;
		for (int i = 1; i < ordreDesTaches.size() + 1; i++)
		{
			try
			{
				// si la tâche appartient à l'activité
				if (((ArrayList) ordreDesTaches.get(new Integer(i))).get(1).toString().equals(idAct))
				{
					// ... alors on l'insère dans la liste
					lesActivitesConcernees.put(new Integer(ordre++), ((ArrayList) ordreDesTaches.get(new Integer(i))).get(0));
				}
			}
			catch (Exception e)
			{
				// alors il s'agit d'une tâche hosr processus 
			}

		}

		// On met dans la table cette nouvelle liste des tâches
		for (int i = 1; i < lesActivitesConcernees.size() + 1; i++)
		{
			Vector uneLigne = new Vector();
			uneLigne.add(new Integer(i));
			uneLigne.add(lesActivitesConcernees.get(new Integer(i)));
			lemodele.addRow(uneLigne);
		}
		return lemodele;
	}

	public void reinitialiserLeModele()
	{
		while (lemodele.getRowCount() > 0)
		{
			lemodele.removeRow(0);
		}
	}

}
