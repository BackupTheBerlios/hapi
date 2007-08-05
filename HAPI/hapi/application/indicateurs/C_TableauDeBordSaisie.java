package hapi.application.indicateurs;

import hapi.application.C_Hapi;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Evaluation;
import hapi.application.ressources.Bundle;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.modeles.ModeleComboWithId;
import hapi.donnees.modeles.ModeleTableOrdonnerActivites;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class C_TableauDeBordSaisie
{
	ModeleTableOrdonnerActivites modeltable;
	ModeleComboWithId modelcombo;

	public void creerEvaluationQuantitative(String idProc, int idExec, int numIt, float rua, float rct, float rur, float rup, HashMap activites, Date deb, Date fin) throws Exception
	{
		// creer une instance de E_EvaluationQuantitative
		E_Evaluation eval = new E_Evaluation(idProc, idExec, numIt, rua, rct, rur, rup, activites, C_Hapi.EVALUATION_ICONE);
		eval.setDateDebut(deb);
		eval.setDateFin(fin);
		// enregistrement en base
		C_Evaluation.insererEvaluationEnBase(eval);

		//ajouter l'instance à la liste des évaluations
		C_Evaluation.ajouterEvaluationQuantitative(idProc, idExec, numIt, eval);
	}

	public void creerModeleTableOrdonnerActivites()
	{
		modeltable = new ModeleTableOrdonnerActivites();

		modeltable.addColumn(Bundle.getText("OO_TableauDeBordSaisie_Table_Col1_caption"));
		modeltable.addColumn(Bundle.getText("OO_TableauDeBordSaisie_Table_Col2_caption"));
	}

	public void creerModeleMemoriseId()
	{
		modelcombo = new ModeleComboWithId();
	}

	public void initialiserModelMemoriseId()
	{
		try
		{
			modelcombo.setSelectedItem(modelcombo.getElementAt(0));
		}
		catch (Exception e)
		{}
	}

	public int getSizeModeleMemoriseId()
	{
		return modelcombo.getSize();
	}

	public ArrayList traiterModeleMemoriseId()
	{
		ArrayList valeurs = new ArrayList();
		// récupération de l'élément sélectionné
		valeurs.add(modelcombo.getCleAt(modelcombo.getIndexOf(modelcombo.getSelectedItem())));
		valeurs.add(modelcombo.getSelectedItem().toString());
		// suppression de l'élément dans la liste déroulante
		modelcombo.removeElementAt(modelcombo.getIndexOf(modelcombo.getSelectedItem()));
		return valeurs;
	}

	public ModeleTableOrdonnerActivites getModeleTableOrdonnerActivites(String id, String valeur)
	{
		// ajout de l'identifiant
		modeltable.getIdActivites().add(id);

		// ajout de la ligne dans la table
		Vector uneligne = new Vector();
		uneligne.add(new Integer(modeltable.getIdActivites().size()));
		uneligne.add(valeur);
		modeltable.addRow(uneligne);

		return modeltable;
	}

	public ModeleComboWithId getModeleMemoriseId(HashMap activites)
	{
		// chargement de la liste des activites à ordonner
		for (Iterator it = activites.keySet().iterator(); it.hasNext();)
		{
			String cle = it.next().toString();
			modelcombo.addElement(cle, activites.get(cle));
		}
		modelcombo.setSelectedItem(modelcombo.getElementAt(0));

		return modelcombo;
	}

	public ArrayList getActivitesDuModele()
	{
		return modeltable.getIdActivites();
	}
	/**
	 *  renvoie la liste des activités d'un processus 
	 * */

	public HashMap getActivitesDuProcessus(String idProcessus)
	{
		return C_Activite.getActivitesDuProcessus(idProcessus);
	}

	public static String getRURProcessus(String idProcess)
	{
		return "RUR";
	}

	public static String getRCTProcessus(String idProcess)
	{
		return "RCT";
	}

	public static String getRUAProcessus(String idProcess)
	{
		return "RUA";
	}

	public static String getRUPProcessus(String idProcess)
	{
		return "RUP";
	}
}