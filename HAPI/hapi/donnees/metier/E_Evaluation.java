/**
 * Creé le 12 fevrier 2005
 * 
 */
package hapi.donnees.metier;

import hapi.application.ressources.Bundle;
import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Stéphane ANRIGO
 * Classe de definition des evaluations quantitatives 
 */

public class E_Evaluation implements InterfaceMetier
{
	private int id; // id unique de l'evaluation quantitative
	private int idExecutionProcessus; // id de l'exécution de processus associée 
	private int numIteration; // numero d'itération
	private float RUA; // Ratio d'Utilisation des Activités
	private float RCT; // Ratio de Charge des Tâches
	private float RUR; // Ratio d'Utilisation des Rôles
	private float RUP; // Ratio d'Utilisation des Produits
	private HashMap ordreDesTaches = new HashMap();
	private String idProcessus = null;
	private String evalQualitative = null;
	private Date dateFin = null;
	private Date dateDebut = null;

	private HashMap repartitionDeschargesParComposant = new HashMap();

	private ArrayList rolesHorsProcessus = new ArrayList();
	private ArrayList rolesNonUtilises = new ArrayList();

	private ArrayList produitsHorsProcessus = new ArrayList();
	private ArrayList produitsNonUtilises = new ArrayList();

	private ArrayList activitesHorsProcessus = new ArrayList();
	private ArrayList activitesNonUtilisees = new ArrayList();

	private ImageIcon icone;

	public String getEvalQualitative()
	{
		return evalQualitative;
	}
	public void setEvalQualitative(String evalQualitative)
	{
		this.evalQualitative = evalQualitative;
	}
	public String getIdProcessus()
	{
		return idProcessus;
	}
	public void setIdProcessus(String idProcessus)
	{
		this.idProcessus = idProcessus;
	}

	public E_Evaluation(ImageIcon lIcone)
	{
		icone = lIcone;
	}

	public E_Evaluation(String idProc, int idExecProc, int numIT, float rua, float rct, float rur, float rup, HashMap ordreActivites, ImageIcon lIcone)
	{
		idProcessus = idProc;
		idExecutionProcessus = idExecProc;
		numIteration = numIT;
		RUA = rua;
		RCT = rct;
		RUR = rur;
		RUP = rup;
		ordreDesTaches = ordreActivites;
		icone = lIcone;
	}

	/*
	 * Getters
	 */

	/**
	 * Méthode redéfinie de InterfaceMetier
	 * Ne pas utiliser hormis pour l'arbre de l'appli
	 */

	public String getIdentifiant()
	{
		return new Integer(numIteration).toString();
	}

	public int getIdExecutionProcessus()
	{
		return idExecutionProcessus;
	}

	public int getNumIteration()
	{
		return numIteration;
	}

	public float getRUA()
	{
		return RUA;
	}

	public float getRCT()
	{
		return RCT;
	}

	public float getRUR()
	{
		return RUR;
	}

	public float getRUP()
	{
		return RUP;
	}

	public String getNom()
	{
		return Bundle.getText("FP_HAPI_iteration") + this.numIteration;
	}

	public HashMap getOrdreTaches()
	{
		return ordreDesTaches;
	}

	public int getId()
	{
		return id;
	}

	/*
	 * Setters
	 */

	public void setId(int ident)
	{
		id = ident;
	}

	public void setIdExecutionProcessus(int idExecProc)
	{
		idExecutionProcessus = idExecProc;
	}

	public void setNumIteration(int numIT)
	{
		numIteration = numIT;
	}

	public void setRUA(float rua)
	{
		RUA = rua;
	}

	public void setRCT(float rct)
	{
		RCT = rct;
	}

	public void setRUR(float rur)
	{
		RUR = rur;
	}

	public void setRUP(float rup)
	{
		RUP = rup;
	}

	/**
	* Ajout des activités dans la liste représentation l'ordre d'exécution
	* Attention le rang doit commencer à 1
	*/
	public void classerActivite(int rang, String nomTache, String idActivite)
	{
		ArrayList infos = new ArrayList();
		infos.add(nomTache);
		infos.add(idActivite);
		ordreDesTaches.put(new Integer(rang), infos);
	}

	public String getDateFinFormatee()
	{
		return formaterDateEvaluation(dateFin.toString());
	}

	public String getDateDebutFormatee()
	{
		return formaterDateEvaluation(dateDebut.toString());
	}

	private String formaterDateEvaluation(String dExport)
	{
		return (dExport.substring(6, 8) + "/" + dExport.substring(4, 6) + "/" + dExport.substring(0, 4) + " " + dExport.substring(8, 10) + ":" + dExport.substring(10, 12) + ":" + dExport.substring(12, 14));
	}

	public Date getDateDebut()
	{
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}
	public Date getDateFin()
	{
		return dateFin;
	}
	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}
	public ArrayList getActivitesHorsProcessus()
	{
		return activitesHorsProcessus;
	}
	public void setActivitesHorsProcessus(ArrayList activitesHorsProcessus)
	{
		this.activitesHorsProcessus = activitesHorsProcessus;
	}
	public void addActivitesHorsProcessus(String activitesHorsProcessus)
	{
		this.activitesHorsProcessus.add(activitesHorsProcessus);
	}
	public ArrayList getActivitesNonUtilisees()
	{
		return activitesNonUtilisees;
	}
	public void setActivitesNonUtilisees(ArrayList activitesNonUtilisees)
	{
		this.activitesNonUtilisees = activitesNonUtilisees;
	}
	public void addActivitesNonUtilisees(String activitesNonUtilisees)
	{
		this.activitesNonUtilisees.add(activitesNonUtilisees);
	}
	public ArrayList getProduitsHorsProcessus()
	{
		return produitsHorsProcessus;
	}
	public void setProduitsHorsProcessus(ArrayList produitsHorsProcessus)
	{
		this.produitsHorsProcessus = produitsHorsProcessus;
	}
	public void addProduitsHorsProcessus(String produitsHorsProcessus)
	{
		this.produitsHorsProcessus.add(produitsHorsProcessus);
	}
	public ArrayList getProduitsNonUtilises()
	{
		return produitsNonUtilises;
	}
	public void setProduitsNonUtilises(ArrayList produitsNonUtilises)
	{
		this.produitsNonUtilises = produitsNonUtilises;
	}
	public void addProduitsNonUtilises(String produitsNonUtilises)
	{
		this.produitsNonUtilises.add(produitsNonUtilises);
	}
	public ArrayList getRolesHorsProcessus()
	{
		return rolesHorsProcessus;
	}
	public void setRolesHorsProcessus(ArrayList rolesHorsProcessus)
	{
		this.rolesHorsProcessus = rolesHorsProcessus;
	}
	public void addRolesHorsProcessus(String rolesHorsProcessus)
	{
		this.rolesHorsProcessus.add(rolesHorsProcessus);
	}
	public ArrayList getRolesNonUtilises()
	{
		return rolesNonUtilises;
	}
	public void setRolesNonUtilises(ArrayList rolesNonUtilises)
	{
		this.rolesNonUtilises = rolesNonUtilises;
	}
	public void addRolesNonUtilises(String rolesNonUtilises)
	{
		this.rolesNonUtilises.add(rolesNonUtilises);
	}
	public HashMap getRepartitionDeschargesParComposant()
	{
		return repartitionDeschargesParComposant;
	}
	public void setRepartitionDeschargesParComposant(HashMap repartitionDeschargesParComposant)
	{
		this.repartitionDeschargesParComposant = repartitionDeschargesParComposant;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
}