/*
 * Created on 28 janv. 2005
 *
 */
package hapi.application;

import hapi.application.ressources.Bundle;
import hapi.donnees.E_Artefact;
import hapi.donnees.E_Membre;
import hapi.donnees.E_Mesures;
import hapi.donnees.E_Tache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

/**
 * @author Fabien Puysségur.
 *
 */
public class C_Mesures
{
	private static ArrayList lesEMesuresDuFichier = null;
	private static String idExecutionProcessus;
	private static Date dateDebut;
	private static Date dateFin;

	public static void reinitialiserLesMesures()
	{
		lesEMesuresDuFichier = new ArrayList();
	}

	public static void setIdExecutionProcessus(String idExec)
	{
		idExecutionProcessus = idExec;
	}

	public static String getIdExecutionProcessus()
	{
		return idExecutionProcessus;
	}

	public static void ajouterMesure(E_Mesures mesure)
	{
		lesEMesuresDuFichier.add(mesure);
	}

	public static E_Mesures getMesure(int indice)
	{
		return (E_Mesures) lesEMesuresDuFichier.get(indice);
	}

	public static int getNbMesures()
	{
		return lesEMesuresDuFichier.size();
	}

	/**
	 * RUA : renvoi un float qui est le pourcentage (ex : 69 %)
	 */
	public static float calculRUA(int nbActivitesHorsProcessus, int nbActivitesProcessus, int nbActivitesProcessusTotal)
	{
		return ((((float) nbActivitesProcessus - (float) nbActivitesHorsProcessus) / (float) nbActivitesProcessusTotal) * 100);
	}

	/**
	 * RCT : renvoi un float qui est le pourcentage (ex : 69 %)
	 */
	public static float calculRCT(float nbTacheActiviteProcessus, float nbTacheActiviteProcessusTotal)
	{
		if (nbTacheActiviteProcessusTotal == 0)
			return 0;
		return ((nbTacheActiviteProcessus / nbTacheActiviteProcessusTotal) * 100);
	}

	/**
	 * RUR : renvoi un float qui est le pourcentage (ex : 69 %)
	 */
	public static float calculRUR(int nbRoleHorsProcessus, int nbRoleProcessus, int nbRoleProcessusTotal)
	{
		return ((((float) nbRoleProcessus - (float) nbRoleHorsProcessus) / (float) nbRoleProcessusTotal) * 100);
	}

	/**
	 * RUP : renvoi un float qui est le pourcentage (ex : 69 %)
	 */
	public static float calculRUP(int nbProduitHorsProcessus, int nbProduitProcessus, int nbProduitProcessusTotal)
	{
		return ((((float) nbProduitProcessus - (float) nbProduitHorsProcessus) / (float) nbProduitProcessusTotal) * 100);
	}

	/**
	 * RTP : répartion du temps passé sur chaque composant en minute ou seconde (a voir)
	 * prend en paramètre une hashmap de structure : <idcomposant, tempspassé>
	 * et renvoi une hashMap de structure : <idComposant, pourcentage de temps> 
	 */
	public static HashMap calculRTP(HashMap chargesDesTachesDesComposants)
	{
		HashMap repartitionDeChargeParComposant = new HashMap();

		float chargeTotale = 0;
		// calcul de la charge totale
		for (Iterator it = chargesDesTachesDesComposants.values().iterator(); it.hasNext();)
		{
			chargeTotale += ((Double) it.next()).floatValue();
		}

		// calcul des pourcentages

		for (Iterator it = chargesDesTachesDesComposants.keySet().iterator(); it.hasNext();)
		{
			String idComp = it.next().toString();
			repartitionDeChargeParComposant.put(idComp, new Float(((Double) chargesDesTachesDesComposants.get(idComp)).floatValue() / chargeTotale));
		}

		return repartitionDeChargeParComposant;
	}

	/**
	* Méthode renvoyant la liste des noms des taches hors processus
	* @param lesActivitesIn identifiant des activités dans le processus
	*/
	public static ArrayList genereTachesHorsProcessus(E_Mesures uneMesure, ArrayList lesActivites)
	{
		// récupération de la liste des taches hors processus
		ArrayList listeDesNomsDesTaches = new ArrayList();
		for (Iterator it = ((ArrayList) uneMesure.getListeActivites().get("-1")).iterator(); it.hasNext();)
		{
			listeDesNomsDesTaches.add(((E_Tache) it.next()).getNom());
		}
		return listeDesNomsDesTaches;
	}

	/**
	* Méthode renvoyant la liste des noms des artefacts hors processus
	* @param lesProduitsIn identifiant des produits dans le processus
	*/
	public static ArrayList genereArtefactsHorsProcessus(E_Mesures uneMesure, ArrayList lesProduits)
	{
		ArrayList listeDesNomsDesArtefacts = new ArrayList();
		for (Iterator it = ((ArrayList) uneMesure.getListeProduits().get("-1")).iterator(); it.hasNext();)
		{
			listeDesNomsDesArtefacts.add(((E_Artefact) it.next()).getNom());
		}
		return listeDesNomsDesArtefacts;
	}

	/**
	* Méthode renvoyant la liste des noms des participants hors processus
	* @param lesRolesIn identifiant des rôles dans le processus
	*/
	public static ArrayList genereParticipantsHorsProcessus(E_Mesures uneMesure, ArrayList lesRoles)
	{
		ArrayList listeDesNomsDesParticipants = new ArrayList();
		for (Iterator it = ((ArrayList) uneMesure.getListeRoles().get("-1")).iterator(); it.hasNext();)
		{
			listeDesNomsDesParticipants.add(((E_Membre) it.next()).getNom());
		}
		return listeDesNomsDesParticipants;
	}

	/**
	* Méthode renvoyant la liste d'identifiants d'activités non réalisées
	* @param lesActivitesIn identifiant des activités dans le processus
	*/
	public static ArrayList genereActivitesNonRealisee(E_Mesures uneMesure, ArrayList lesActivites)
	{
		// récupération de la liste des activités de l'itération
		ArrayList activitesDeLEval = new ArrayList(uneMesure.getListeActivites().keySet());
		// calcul de la liste des activités non réalisées
		lesActivites.removeAll(activitesDeLEval);
		return lesActivites;
	}

	/**
	* Méthode renvoyant la liste d'identifiants produits non réalisés
	* @param lesProduitsIn identifiant des produits dans le processus
	*/
	public static ArrayList genereProduitsNonRealisee(E_Mesures uneMesure, ArrayList lesProduits)
	{
		// récupération des produits de l'itération	  
		ArrayList produitsDeLEval = new ArrayList(uneMesure.getListeRoles().keySet());
		// calcul des produits non réalisés
		lesProduits.removeAll(produitsDeLEval);
		return lesProduits;
	}

	/**
	* Méthode renvoyant la liste d'identifiants de rôles non réalisés
	* @param lesRolesIn identifiant des rôles dans le processus
	*/
	public static ArrayList genereRolesNonRealisee(E_Mesures uneMesure, ArrayList lesRoles)
	{
		// récupération des roles de l'itération
		ArrayList activitesDeLEval = new ArrayList(uneMesure.getListeActivites().keySet());
		// calcul des roles non réalisées
		lesRoles.removeAll(activitesDeLEval);
		return lesRoles;
	}

	/**
	 * @return
	 */
	public static Date getDateDebut()
	{
		return dateDebut;
	}

	/**
	 * @return
	 */
	public static Date getDateFin()
	{
		return dateFin;
	}

	/**
	 * @param date
	 */
	public static void setDateDebut(Date date)
	{
		dateDebut = date;
	}

	/**
	 * @param date
	 */
	public static void setDateFin(Date date)
	{
		dateFin = date;
	}

	public static float getTempsPasse(Object tache)
	{
		return new Float(((E_Tache) tache).getTempsPasse()).floatValue();
	}
	
	public static String getNumIterationParDefaut(String idProcessus,Integer idExecution)
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		ResultSet leResultat = null;
		int idStatement = 0;
		String retour ="";
		
		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(idProcessus);
			lesParametres.add(idExecution);
			
			idStatement = cBase.creerPreparedStatement("getIterationDefaut");
			leResultat = cBase.executerRequeteStockee(idStatement, "getIterationDefaut", lesParametres);
			leResultat.next();
			retour = new Integer(leResultat.getInt(1)+1).toString();
		}
		catch (Exception e)
		{}
		finally
		{
			try
			{
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		
		}
		
		return retour;	
	}
	
	public static DefaultComboBoxModel createModeleUnite()
	{
		DefaultComboBoxModel retour = new DefaultComboBoxModel();
		retour.addElement(Bundle.getText("BD_CreerEvaluation_Minute"));
		retour.addElement(Bundle.getText("BD_CreerEvaluation_Heure"));
		retour.addElement(Bundle.getText("BD_CreerEvaluation_Jour"));
		retour.setSelectedItem(Bundle.getText("BD_CreerEvaluation_Minute"));
		
		return retour;
	}

}
