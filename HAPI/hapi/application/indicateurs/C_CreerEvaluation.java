/*
 * Fichier C_CreerEvaluation.java Auteur Cédric
 */
package hapi.application.indicateurs;

import hapi.application.C_Hapi;
import hapi.application.C_Mesures;
import hapi.application.interfaces.FenetreAssistee;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Produit;
import hapi.application.metier.C_Role;
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.metier.temporaire.C_ProduitTemporaire;
import hapi.application.metier.temporaire.C_RoleTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_ArtefactHorsProcessus;
import hapi.donnees.E_CycleDeVie;
import hapi.donnees.E_ParticipantHorsProcessus;
import hapi.donnees.E_TacheHorsProcessus;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_Evaluation;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.exception.ErreurCreationManuelleException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Contrôleur de BD_CreerEvaluation
 */
public class C_CreerEvaluation
{
	//Mémorisation entre chaque fenêtres
	public final static int NOMBRE_FENETRES = 5;
	private static ArrayList lesParametresFen0 = new ArrayList();
	private static ArrayList lesParametresFen1 = new ArrayList();
	private static ArrayList lesParametresFen2 = new ArrayList();
	private static ArrayList lesParametresFen3 = new ArrayList();
	private static ArrayList lesParametresFen4 = new ArrayList();
	private static FenetreAssistee laFenetreCourante = null;

	public static String getTitreFenetre(int Numero, int indiceMesure)
	{
		switch (Numero)
		{
			case 0:
				if (indiceMesure == -1)
					return Bundle.getText("BD_CreerEvaluation_fen0");
				else
					return Bundle.getText("BD_CreerEvaluation_fen0auto");
			case 1:
				if (indiceMesure == -1)
					return Bundle.getText("BD_CreerEvaluation_fen1");
				else
					return Bundle.getText("BD_CreerEvaluation_fen1auto");
			case 2:
				if (indiceMesure == -1)
					return Bundle.getText("BD_CreerEvaluation_fen2");
				else
					return Bundle.getText("BD_CreerEvaluation_fen2auto");
			case 3:
				if (indiceMesure == -1)
					return Bundle.getText("BD_CreerEvaluation_fen3");
				else
					return Bundle.getText("BD_CreerEvaluation_fen3auto");
			case 4:
				if (indiceMesure == -1)
					return Bundle.getText("BD_CreerEvaluation_fen4");
				else
					return Bundle.getText("BD_CreerEvaluation_fen4auto");
		}
		return "Erreur grave n° 8 !!!!";
	}

	/**
	 * Création d'une fenêtre
	 */
	public static void initialiseFenetre(int Numero, FenetreAssistee fenetreCourante, String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant)
	{

		laFenetreCourante = fenetreCourante;

		laFenetreCourante.setParametres(prepareParametres(Numero, idProcessus, idProjet, indiceMesure, utiliseCourant));
		laFenetreCourante.setNumeroFenetre(Numero);
	}

	/**
	 * Récupération des paramètres des fenêtres
	 */
	private static ArrayList prepareParametres(int Numero, String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant)
	{
		ArrayList retour = null;

		switch (Numero)
		{
			case 0:
				retour = prepareParam0(idProcessus, idProjet, indiceMesure);
				break;
			case 1:
				retour = prepareParam1(idProcessus, idProjet, indiceMesure, utiliseCourant);
				break;
			case 2:
				retour = prepareParam2(idProcessus, idProjet, indiceMesure, utiliseCourant);
				break;
			case 3:
				retour = prepareParam3(idProcessus, idProjet, indiceMesure, utiliseCourant);
				break;
			case 4:
				retour = prepareParam4(idProcessus, indiceMesure, utiliseCourant);
				break;
		}

		return retour;
	}

	/**
	 * Préparation des paramètres de la fenêtre 0
	 */
	private static ArrayList prepareParam0(String idProcessus, String idProjet, int indiceMesure)
	{
		ArrayList retour = new ArrayList();
		if (lesParametresFen0.size() == 0)
		{
			//Affectation des champs si possible
			if (indiceMesure != -1)
			{
				retour.add(new Integer(indiceMesure));
				retour.add(new Integer(C_Mesures.getMesure(indiceMesure).getNumIT()).toString());
				retour.add(new SimpleDateFormat(Bundle.DATE_MODEL).format(C_Mesures.getMesure(indiceMesure).getDateDebutIT()));
				retour.add(new SimpleDateFormat(Bundle.DATE_MODEL).format(C_Mesures.getMesure(indiceMesure).getDateFinIT()));
			}
			else
			{
				retour.add(new Integer(indiceMesure));
				retour.add(C_Mesures.getNumIterationParDefaut(idProcessus, new Integer(idProjet)));
				retour.add("");
				retour.add("");
			}

			return retour;
		}
		else
		{
			retour.add(new Integer(indiceMesure));
			retour.addAll(lesParametresFen0);
			return retour;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 1
	 */
	private static ArrayList prepareParam1(String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant)
	{
		if (lesParametresFen1.size() == 0)
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUA_NR");
			retour.add("BD_CreerEvaluation_RUA_HP");
			//Arbre face à face
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUA_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ActivitesNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Realiser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPRealiser"));
			retour.add(CreerArborescenceActiviteUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(CreerArborescenceActiviteNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(3));
			retour.add(new Integer(C_EntiteOUT.ACTIVITE));
			retour.add(new C_EntiteOUT(recupererActivitesHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(new Boolean(false));
			retour.add(new Double(0));

			return retour;
		}
		else
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUA_NR");
			retour.add("BD_CreerEvaluation_RUA_HP");
			//Arbre face à face
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUA_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ActivitesNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Realiser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPRealiser"));
			retour.add(CreerArborescenceActiviteUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(CreerArborescenceActiviteNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(3));
			retour.add(new Integer(C_EntiteOUT.ACTIVITE));
			retour.add(new C_EntiteOUT(recupererActivitesHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(lesParametresFen1.get(2));
			retour.add(lesParametresFen1.get(3));

			return retour;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 2
	 */
	private static ArrayList prepareParam2(String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant)
	{
		if (lesParametresFen2.size() == 0)
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUP_NR");
			retour.add("BD_CreerEvaluation_RUP_HP");
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUP_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ProduitsNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Realiser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPRealiser"));
			retour.add(CreerArborescenceProduitUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(CreerArborescenceProduitNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(2));
			retour.add(new Integer(C_EntiteOUT.PRODUIT));
			retour.add(new C_EntiteOUT(recupererProduitsHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(new Boolean(false));
			retour.add(new Double(0));

			return retour;
		}
		else
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUP_NR");
			retour.add("BD_CreerEvaluation_RUP_HP");
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUP_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ProduitsNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Realiser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPRealiser"));
			retour.add(CreerArborescenceProduitUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(CreerArborescenceProduitNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(2));
			retour.add(new Integer(C_EntiteOUT.PRODUIT));
			retour.add(new C_EntiteOUT(recupererProduitsHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(lesParametresFen2.get(2));
			retour.add(lesParametresFen2.get(3));

			return retour;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 3
	 */
	private static ArrayList prepareParam3(String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant)
	{
		if (lesParametresFen3.size() == 0)
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUR_NR");
			retour.add("BD_CreerEvaluation_RUR_HP");
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUR_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RolesNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Utiliser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPUtiliser"));
			retour.add(C_CreerEvaluation.CreerArborescenceRoleUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(C_CreerEvaluation.CreerArborescenceRoleNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(2));
			retour.add(new Integer(C_EntiteOUT.ROLE));
			retour.add(new C_EntiteOUT(C_CreerEvaluation.recupererRolesHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(new Boolean(false));
			retour.add(new Double(0));

			return retour;
		}
		else
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie()));
			retour.add(new Integer(indiceMesure));
			retour.add("BD_CreerEvaluation_RUR_NR");
			retour.add("BD_CreerEvaluation_RUR_HP");
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_ListeComposant"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RUR_NR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_RolesNR"));
			retour.add(Bundle.getText("BD_CreerEvaluation_Utiliser"));
			retour.add(Bundle.getText("BD_CreerEvaluation_NPUtiliser"));
			retour.add(C_CreerEvaluation.CreerArborescenceRoleUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(C_CreerEvaluation.CreerArborescenceRoleNonUtilise(idProcessus, idProjet, indiceMesure, utiliseCourant));
			retour.add(new Integer(2));
			retour.add(new Integer(C_EntiteOUT.ROLE));
			retour.add(new C_EntiteOUT(C_CreerEvaluation.recupererRolesHorsProcessus(idProcessus, idProjet, indiceMesure)));
			retour.add(lesParametresFen3.get(2));
			retour.add(lesParametresFen3.get(3));

			return retour;
		}
	}

	/**
	 * Préparation des paramètres de la fenêtre 4
	 */
	private static ArrayList prepareParam4(String idProcessus, int indiceMesure, boolean utiliseCourant)
	{
		if (lesParametresFen4.size() == 0)
		{
			ArrayList retour = new ArrayList();

			retour.add(idProcessus);
			retour.add(new Integer(indiceMesure));
			retour.add(new Boolean(utiliseCourant));

			return retour;
		}
		else
		{
			return lesParametresFen4;
		}
	}

	public static void memoriseListesCourantes()
	{
		ArrayList lesParams = laFenetreCourante.getParametresSaisis();
		switch (laFenetreCourante.getNumeroFenetre())
		{
			case 0:
				lesParametresFen0.clear();
				lesParametresFen0.addAll(lesParams);
				break;
			case 1:
				lesParametresFen1.clear();
				lesParametresFen1.addAll(lesParams);
				break;
			case 2:
				lesParametresFen2.clear();
				lesParametresFen2.addAll(lesParams);
				break;
			case 3:
				lesParametresFen3.clear();
				lesParametresFen3.addAll(lesParams);
				break;
			case 4:
				lesParametresFen4.clear();
				lesParametresFen4.addAll(lesParams);
				break;
		}

	}

	public static void clearListesCourantes()
	{
		lesParametresFen0.clear();
		lesParametresFen1.clear();
		lesParametresFen2.clear();
		lesParametresFen3.clear();
		lesParametresFen4.clear();
	}

	private static int getNumIt() throws ErreurCreationManuelleException
	{
		try
		{
			return new Integer((String) lesParametresFen0.get(0)).intValue();
		}
		catch (Exception e)
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_numero"));
		}
	}

	private static Date getDateDebut() throws ErreurCreationManuelleException
	{
		try
		{
			// formatage des dates de début et fin
			String temp = (String) lesParametresFen0.get(1);
			Calendar date = Calendar.getInstance();
			date.set(Integer.parseInt(temp.substring(6, 10)), Integer.parseInt(temp.substring(3, 5)) - 1, Integer.parseInt(temp.substring(0, 2)));

			return date.getTime();
		}
		catch (Exception e)
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_nodate"));
		}
	}

	private static Date getDateFin() throws ErreurCreationManuelleException
	{
		try
		{
			// formatage des dates de début et fin
			String temp = (String) lesParametresFen0.get(2);
			Calendar date = Calendar.getInstance();
			date.set(Integer.parseInt(temp.substring(6, 10)), Integer.parseInt(temp.substring(3, 5)) - 1, Integer.parseInt(temp.substring(0, 2)));

			return date.getTime();
		}
		catch (Exception e)
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_nodate"));
		}
	}

	public static void saveListe(String idProcessus, String idProjet, int indiceMesure, boolean utiliseCourant) throws Exception
	{
		if (lesParametresFen0.size() == 0)
		{
			lesParametresFen0 = prepareParam0(idProcessus, idProjet, indiceMesure);
		}
		if (lesParametresFen1.size() == 0)
		{
			lesParametresFen1 = prepareParam1(idProcessus, idProjet, indiceMesure, utiliseCourant);
		}
		if (lesParametresFen2.size() == 0)
		{
			lesParametresFen2 = prepareParam2(idProcessus, idProjet, indiceMesure, utiliseCourant);
		}
		if (lesParametresFen3.size() == 0)
		{
			lesParametresFen3 = prepareParam3(idProcessus, idProjet, indiceMesure, utiliseCourant);
		}
		if (lesParametresFen4.size() == 0)
		{
			lesParametresFen4 = prepareParam4(idProcessus, indiceMesure, utiliseCourant);
		}

		creerEvaluation(indiceMesure, idProcessus, idProjet, C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getIdCycleDeVie(), getNumIt(), getDateDebut(), getDateFin(), ((Boolean) lesParametresFen1.get(2)).booleanValue(), (Double) lesParametresFen1.get(3), (HashMap) lesParametresFen1.get(0), ((Boolean) lesParametresFen2.get(2)).booleanValue(), (Double) lesParametresFen2.get(3), (HashMap) lesParametresFen2.get(0), ((Boolean) lesParametresFen3.get(2)).booleanValue(),
				(Double) lesParametresFen3.get(3), (HashMap) lesParametresFen3.get(0), (HashMap) lesParametresFen4.get(4), (HashMap) lesParametresFen1.get(1), (HashMap) lesParametresFen3.get(1), (HashMap) lesParametresFen2.get(1));
	}

	private static HashMap CreerArborescenceProduitUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesProduits = null;
		//Les produits
		HashMap lesEntites = null;
		ArrayList lesIdProduits = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen2.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des produits du
				// cycle
				// de vie
				lesIdProduits = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des produits
				// réalisés
				lesIdProduits = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeProduits().keySet());
				//On ne garde que les produits réalisés appartenant au cycle de
				// vie
				lesIdProduits.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits());
			}

			//Construction de la HashMap de produits
			lesProduits = new HashMap();
			if (utiliseCourant)
			{
				for (Iterator it = lesIdProduits.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_Produit.getProduit(id_processus, clef) != null)
							lesProduits.put(clef, C_Produit.getProduit(id_processus, clef));
				}
			}
			else
			{
				for (Iterator it = lesIdProduits.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_ProduitTemporaire.getProduit(clef) != null)
							lesProduits.put(clef, C_ProduitTemporaire.getProduit(clef));
				}
			}
		}
		else
		{
			lesProduits = new HashMap();
			lesProduits.putAll((HashMap) lesParametresFen2.get(0));
			lesProduits.remove("-1");
		}

		//Pour chaque composant
		for (Iterator it = lesComposants.keySet().iterator(); it.hasNext();)
		{
			String clef = (String) it.next();
			lesEntites = new HashMap();

			//Pour chaque produits
			for (Iterator it2 = lesProduits.keySet().iterator(); it2.hasNext();)
			{
				String clef2 = (String) it2.next();
				E_Produit unProduit = (E_Produit) lesProduits.get(clef2);

				if (unProduit != null)
					//Si le produit apparatient au composant
					if (unProduit.getAgregatComposant().equals(clef))
					{
						//Insertion dans la liste
						lesEntites.put(unProduit, null);
					}

			}

			//Création d'un niveau d'arborescence
			retour.put(lesComposants.get(clef), lesEntites);
		}

		return retour;
	}

	private static HashMap CreerArborescenceRoleUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesRoles = null;
		//Les Roles
		HashMap lesEntites = null;
		ArrayList lesIdRoles = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen3.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des Roles du cycle
				// de
				// vie
				lesIdRoles = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des Roles réalisés
				lesIdRoles = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeRoles().keySet());
				//On ne garde que les Roles réalisés appartenant au cycle de
				// vie
				lesIdRoles.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles());
			}

			//Construction de la HashMap de Roles
			lesRoles = new HashMap();
			if (utiliseCourant)
			{
				for (Iterator it = lesIdRoles.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_Role.getRole(id_processus, clef) != null)
							lesRoles.put(clef, C_Role.getRole(id_processus, clef));
				}
			}
			else
			{
				for (Iterator it = lesIdRoles.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_RoleTemporaire.getRole(clef) != null)
							lesRoles.put(clef, C_RoleTemporaire.getRole(clef));
				}
			}
		}
		else
		{
			lesRoles = new HashMap();
			lesRoles.putAll((HashMap) lesParametresFen3.get(0));
			lesRoles.remove("-1");
		}

		//Pour chaque composant
		for (Iterator it = lesComposants.keySet().iterator(); it.hasNext();)
		{
			String clef = (String) it.next();
			lesEntites = new HashMap();

			//Pour chaque Roles
			for (Iterator it2 = lesRoles.keySet().iterator(); it2.hasNext();)
			{
				String clef2 = (String) it2.next();
				E_Role unRole = (E_Role) lesRoles.get(clef2);

				//Si le Role apparatient au composant
				if (unRole != null)
					if (unRole.getAgregatComposant().equals(clef))
					{
						//Insertion dans la liste
						lesEntites.put(unRole, null);
					}

			}

			//Création d'un niveau d'arborescence
			retour.put(lesComposants.get(clef), lesEntites);
		}

		return retour;
	}

	private static HashMap CreerArborescenceActiviteUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesDefinitions = null;
		HashMap lesActivites = null;
		//Les Activites
		HashMap lesEntites = null;
		HashMap lesEntites2 = null;
		ArrayList lesIdActivites = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
			//Récupération de la liste des définitions
			lesDefinitions = C_Definition.getDefinitionsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de la liste des définitions
			lesDefinitions = C_DefinitionTemporaire.getDefinitions();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen1.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des Activites du
				// cycle
				// de vie
				lesIdActivites = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des Activites
				// réalisés
				lesIdActivites = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeActivites().keySet());
				//On ne garde que les Activites réalisés appartenant au cycle
				// de
				// vie
				lesIdActivites.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites());
			}

			//Construction de la HashMap de Activites
			lesActivites = new HashMap();
			if (utiliseCourant)
			{
				for (Iterator it = lesIdActivites.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_Activite.getActivite(id_processus, clef) != null)
							lesActivites.put(clef, C_Activite.getActivite(id_processus, clef));
				}
			}
			else
			{
				for (Iterator it = lesIdActivites.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					if (!clef.equals("-1"))
						if (C_ActiviteTemporaire.getActivite(clef) != null)
							lesActivites.put(clef, C_ActiviteTemporaire.getActivite(clef));
				}
			}
		}
		else
		{
			lesActivites = new HashMap();
			lesActivites.putAll((HashMap) lesParametresFen1.get(0));
			lesActivites.remove("-1");
		}

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

		return retour;
	}

	private static HashMap CreerArborescenceProduitNonUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesProduits = null;
		//Les produits
		HashMap lesEntites = null;
		ArrayList lesIdProduits = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen2.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des produits du
				// cycle
				// de vie
				lesIdProduits = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des produits
				// réalisés
				lesIdProduits = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeProduits().keySet());
				//On ne garde que les produits réalisés appartenant au cycle de
				// vie
				lesIdProduits.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits());
			}

			if (ind_mesure == -1) // Si création manuelle
			{
				//Récupération de la liste des produits
				lesProduits = new HashMap();
			}
			else
			{
				//HashMap des produits du cycle de vie
				HashMap lesProduitsDuCycleDeVie = new HashMap();
				//Construction de la HashMap de produits
				lesProduits = new HashMap();
				if (utiliseCourant)
				{
					for (Iterator it = lesIdProduits.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_Produit.getProduit(id_processus, clef) != null)
								lesProduits.put(clef, C_Produit.getProduit(id_processus, clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_Produit.getProduit(id_processus, clef) != null)
							lesProduitsDuCycleDeVie.put(clef, C_Produit.getProduit(id_processus, clef));
					}
				}
				else
				{
					for (Iterator it = lesIdProduits.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_ProduitTemporaire.getProduit(clef) != null)
								lesProduits.put(clef, C_ProduitTemporaire.getProduit(clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_ProduitTemporaire.getProduit(clef) != null)
							lesProduitsDuCycleDeVie.put(clef, C_ProduitTemporaire.getProduit(clef));
					}
				}

				lesProduits = doExclusion(lesProduits, lesProduitsDuCycleDeVie);
			}
		}
		else
		{
			lesProduits = (HashMap) lesParametresFen2.get(1);
		}

		//Pour chaque composant
		for (Iterator it = lesComposants.keySet().iterator(); it.hasNext();)
		{
			String clef = (String) it.next();
			lesEntites = new HashMap();

			//Pour chaque produits
			for (Iterator it2 = lesProduits.keySet().iterator(); it2.hasNext();)
			{
				String clef2 = (String) it2.next();
				E_Produit unProduit = (E_Produit) lesProduits.get(clef2);

				//Si le produit apparatient au composant
				if (unProduit != null)
					if (unProduit.getAgregatComposant().equals(clef))
					{
						//Insertion dans la liste
						lesEntites.put(unProduit, null);
					}

			}

			//Création d'un niveau d'arborescence
			retour.put(lesComposants.get(clef), lesEntites);
		}

		return retour;
	}

	private static HashMap CreerArborescenceRoleNonUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesRoles = null;
		//Les Roles
		HashMap lesEntites = null;
		ArrayList lesIdRoles = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen3.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des Roles du cycle
				// de
				// vie
				lesIdRoles = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des Roles réalisés
				lesIdRoles = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeRoles().keySet());
				//On ne garde que les Roles réalisés appartenant au cycle de
				// vie
				lesIdRoles.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles());
			}

			if (ind_mesure == -1) // Si création manuelle
			{
				//Récupération de la liste des Roles
				lesRoles = new HashMap();
			}
			else
			{
				//HashMap des Roles du cycle de vie
				HashMap lesRolesDuCycleDeVie = new HashMap();
				//Construction de la HashMap de Roles
				lesRoles = new HashMap();
				if (utiliseCourant)
				{
					for (Iterator it = lesIdRoles.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_Role.getRole(id_processus, clef) != null)
								lesRoles.put(clef, C_Role.getRole(id_processus, clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_Role.getRole(id_processus, clef) != null)
							lesRolesDuCycleDeVie.put(clef, C_Role.getRole(id_processus, clef));
					}
				}
				else
				{
					for (Iterator it = lesIdRoles.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_RoleTemporaire.getRole(clef) != null)
								lesRoles.put(clef, C_RoleTemporaire.getRole(clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_RoleTemporaire.getRole(clef) != null)
							lesRolesDuCycleDeVie.put(clef, C_RoleTemporaire.getRole(clef));
					}
				}

				lesRoles = doExclusion(lesRoles, lesRolesDuCycleDeVie);
			}
		}
		else
		{
			lesRoles = (HashMap) lesParametresFen3.get(1);
		}

		//Pour chaque composant
		for (Iterator it = lesComposants.keySet().iterator(); it.hasNext();)
		{
			String clef = (String) it.next();
			lesEntites = new HashMap();

			//Pour chaque Roles
			for (Iterator it2 = lesRoles.keySet().iterator(); it2.hasNext();)
			{
				String clef2 = (String) it2.next();
				E_Role unRole = (E_Role) lesRoles.get(clef2);

				//Si le Role apparatient au composant
				if (unRole != null)
					if (unRole.getAgregatComposant().equals(clef))
					{
						//Insertion dans la liste
						lesEntites.put(unRole, null);
					}

			}

			//Création d'un niveau d'arborescence
			retour.put(lesComposants.get(clef), lesEntites);
		}

		return retour;
	}

	private static HashMap CreerArborescenceActiviteNonUtilise(String id_processus, String id_projet, int ind_mesure, boolean utiliseCourant)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesDefinitions = null;
		HashMap lesActivites = null;
		//Les Activités
		HashMap lesEntites = null;
		HashMap lesEntites2 = null;
		ArrayList lesIdActivites = null;

		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(id_processus);
			//Récupération de la liste des définitions
			lesDefinitions = C_Definition.getDefinitionsDuProcessus(id_processus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
			//Récupération de la liste des définitions
			lesDefinitions = C_DefinitionTemporaire.getDefinitions();
			//Récupération de l'identifiant du processus
			id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		}

		if (lesParametresFen1.size() != 4)
		{
			if (ind_mesure == -1) //Si création manuelle
			{
				//Récupération de la liste des identifiants des Activites du
				// cycle
				// de vie
				lesIdActivites = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites();
			}
			else
			//Si modification
			{
				//Récupération de la liste des identifiants des Activites
				// réalisés
				lesIdActivites = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeActivites().keySet());
				//On ne garde que les Activites réalisés appartenant au cycle
				// de
				// vie
				lesIdActivites.retainAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites());
			}

			if (ind_mesure == -1) // Si création manuelle
			{
				//Récupération de la liste des Activites
				lesActivites = new HashMap();
			}
			else
			{
				//HashMap des Activites du cycle de vie
				HashMap lesActivitesDuCycleDeVie = new HashMap();
				//Construction de la HashMap de Activites
				lesActivites = new HashMap();
				if (utiliseCourant)
				{
					for (Iterator it = lesIdActivites.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_Activite.getActivite(id_processus, clef) != null)
								lesActivites.put(clef, C_Activite.getActivite(id_processus, clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_Activite.getActivite(id_processus, clef) != null)
							lesActivitesDuCycleDeVie.put(clef, C_Activite.getActivite(id_processus, clef));
					}
				}
				else
				{
					for (Iterator it = lesIdActivites.iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (!clef.equals("-1"))
							if (C_ActiviteTemporaire.getActivite(clef) != null)
								lesActivites.put(clef, C_ActiviteTemporaire.getActivite(clef));
					}

					for (Iterator it = ((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites().iterator(); it.hasNext();)
					{
						String clef = (String) it.next();
						if (C_ActiviteTemporaire.getActivite(clef) != null)
							lesActivitesDuCycleDeVie.put(clef, C_ActiviteTemporaire.getActivite(clef));
					}
				}

				lesActivites = doExclusion(lesActivites, lesActivitesDuCycleDeVie);
			}
		}
		else
		{
			lesActivites = (HashMap) lesParametresFen1.get(1);
		}

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

		return retour;
	}

	private static ArrayList recupererActivitesHorsProcessus(String id_processus, String id_projet, int ind_mesure)
	{
		if (lesParametresFen1.size() != 4)
		{
			if (ind_mesure == -1) // Si création manuelle
			{
				return null;
			}
			else
			{
				//Construction de la liste des tâches hors processus
				ArrayList lesActivites = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeActivites()).get("-1");

				//Récupération de la liste des identifiants des Activites
				// réalisés
				// (d'après le fichier de mesure
				ArrayList lesIdActivites = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeActivites().keySet());
				//Retrait du -1
				lesIdActivites.remove("-1");
				//On ne garde que les Activites réalisés n'appartenant pas au
				// cycle
				// de vie
				lesIdActivites.removeAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesActivites());

				for (Iterator it = lesIdActivites.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					ArrayList lesTachesDeLActivite = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeActivites()).get(clef);

					for (Iterator it2 = lesTachesDeLActivite.iterator(); it2.hasNext();)
					{
						lesActivites.add(it2.next());
					}
				}

				return lesActivites;
			}
		}
		else
		{
			ArrayList lesNomsDesTaches = ((ArrayList) ((HashMap) lesParametresFen1.get(0)).get("-1"));
			ArrayList lesTaches = new ArrayList();
			for (Iterator it = lesNomsDesTaches.iterator(); it.hasNext();)
			{
				lesTaches.add(new E_TacheHorsProcessus((String) it.next()));
			}

			return lesTaches;
		}
	}

	private static ArrayList recupererRolesHorsProcessus(String id_processus, String id_projet, int ind_mesure)
	{
		if (lesParametresFen3.size() != 4)
		{
			if (ind_mesure == -1) // Si création manuelle
			{
				return null;
			}
			else
			{
				//Construction de la liste des participants
				ArrayList lesRoles = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeRoles()).get("-1");

				//Récupération de la liste des identifiants des Roles réalisés
				ArrayList lesIdRoles = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeRoles().keySet());
				//Retrait du -1
				lesIdRoles.remove("-1");
				//On ne garde que les Roles réalisés n'appartenant pas au cycle
				// de
				// vie
				lesIdRoles.removeAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesRoles());

				for (Iterator it = lesIdRoles.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					ArrayList lesTachesDeLRole = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeRoles()).get(clef);

					for (Iterator it2 = lesTachesDeLRole.iterator(); it2.hasNext();)
					{
						lesRoles.add(it2.next());
					}
				}

				return lesRoles;
			}
		}
		else
		{
			ArrayList lesNomsDesRoles = ((ArrayList) ((HashMap) lesParametresFen3.get(0)).get("-1"));
			ArrayList lesRoles = new ArrayList();
			for (Iterator it = lesNomsDesRoles.iterator(); it.hasNext();)
			{
				lesRoles.add(new E_ParticipantHorsProcessus((String) it.next()));
			}

			return lesRoles;
		}
	}

	private static ArrayList recupererProduitsHorsProcessus(String id_processus, String id_projet, int ind_mesure)
	{
		if (lesParametresFen2.size() != 4)
		{
			if (ind_mesure == -1) // Si création manuelle
			{
				return null;
			}
			else
			{
				//Construction de la liste des artefacts
				ArrayList lesProduits = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeProduits()).get("-1");

				//Récupération de la liste des identifiants des Produits
				// réalisés
				ArrayList lesIdProduits = new ArrayList(C_Mesures.getMesure(ind_mesure).getListeProduits().keySet());
				//Retrait du -1
				lesIdProduits.remove("-1");
				//On ne garde que les Produits réalisés n'appartenant pas au
				// cycle
				// de vie
				lesIdProduits.removeAll(((E_CycleDeVie) C_CycleDeVie.getLesCyclesDeVie().get(new Integer(C_ExecutionProcessus.getExecutionProcessus(id_processus, id_projet).getIdCycleDeVie()))).getLesProduits());

				for (Iterator it = lesIdProduits.iterator(); it.hasNext();)
				{
					String clef = (String) it.next();
					ArrayList lesTachesDeLProduit = (ArrayList) ((HashMap) C_Mesures.getMesure(ind_mesure).getListeProduits()).get(clef);

					for (Iterator it2 = lesTachesDeLProduit.iterator(); it2.hasNext();)
					{
						lesProduits.add(it2.next());
					}
				}

				return lesProduits;
			}
		}
		else
		{
			ArrayList lesNomsDesProduits = ((ArrayList) ((HashMap) lesParametresFen2.get(0)).get("-1"));
			ArrayList lesProduits = new ArrayList();
			for (Iterator it = lesNomsDesProduits.iterator(); it.hasNext();)
			{
				lesProduits.add(new E_ArtefactHorsProcessus((String) it.next()));
			}

			return lesProduits;
		}
	}

	private static HashMap doExclusion(HashMap liste1, HashMap liste2)
	{
		HashMap listeLaPlusLongue = (liste1.size() > liste2.size()) ? liste1 : liste2;
		HashMap listeLaPlusCourte = (liste1.size() < liste2.size()) ? liste1 : liste2;
		HashMap listeRetour = new HashMap();

		for (Iterator it = listeLaPlusLongue.keySet().iterator(); it.hasNext();)
		{
			String uneClef = (String) it.next();
			if (!listeLaPlusCourte.containsKey(uneClef))
				listeRetour.put(uneClef, listeLaPlusLongue.get(uneClef));
		}

		//Fin de la petite liste
		for (Iterator it = listeLaPlusCourte.keySet().iterator(); it.hasNext();)
		{
			String uneClef = (String) it.next();
			if (!listeRetour.containsKey(uneClef) && !listeLaPlusLongue.containsKey(uneClef))
				listeRetour.put(uneClef, listeLaPlusCourte.get(uneClef));
		}

		return listeRetour;
	}

	private static void creerEvaluation(int idMesure, String idProcessus, String idExec, int idCycleDeVie, int numIt, Date dateDebut, Date dateFin, boolean valeurSaisieRUA, Double valeurRUA, HashMap listeDesActivitesDeLIteration, boolean valeurSaisieRUP, Double valeurRUP, HashMap listeDesProduitsDeLIteration, boolean valeurSaisieRUR, Double valeurRUR, HashMap listeDesRolesDeLIteration, HashMap chargesParComposant, HashMap activitesNONp, HashMap rolesNONp, HashMap produitNONp) throws Exception
	{
		ArrayList activitesNON = new ArrayList(activitesNONp.keySet());
		ArrayList rolesNON = new ArrayList(rolesNONp.keySet());
		ArrayList produitNON = new ArrayList(produitNONp.keySet());

		if (C_Evaluation.getEvaluationPourUneExecProc(idProcessus, Integer.parseInt(idExec)).containsKey(new Integer(numIt)))
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_existe"));
		}

		if (dateDebut.compareTo(dateFin) > 0)
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_date"));
		}

		if (idCycleDeVie == -1 && dateDebut.compareTo(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idExec).getDateDebut()) < 0)
		{
			throw new ErreurCreationManuelleException(Bundle.getText("BD_CreerEvaluation_erreur_date_projet"));
		}

		HashMap lesActivitesDeLIterationDansMesure = null;
		HashMap lesProduitsDeLIterationDansMesure = null;
		HashMap lesRolesDeLIterationDansMesure = null;

		if (idMesure != -1)
		{
			lesActivitesDeLIterationDansMesure = C_Mesures.getMesure(idMesure).getListeActivites();
			// modifier l'entité mesure en fonction des listes reçues
			// ajout des activités ajoutées
			for (Iterator it = listeDesActivitesDeLIteration.keySet().iterator(); it.hasNext();)
			{
				String id = it.next().toString();
				if (!lesActivitesDeLIterationDansMesure.containsKey(id))
				{
					lesActivitesDeLIterationDansMesure.put(id, new ArrayList());
				}
			}
			// retrait des activités supprimées
			ArrayList lesElements = new ArrayList(lesActivitesDeLIterationDansMesure.keySet());
			for (Iterator it = lesElements.iterator(); it.hasNext();)
			{
				String id = (String) it.next();
				if (!listeDesActivitesDeLIteration.containsKey(id))
				{
					lesActivitesDeLIterationDansMesure.remove(id);
				}
			}

			// ajout des produits ajoutés
			lesProduitsDeLIterationDansMesure = C_Mesures.getMesure(idMesure).getListeProduits();
			for (Iterator it = listeDesProduitsDeLIteration.keySet().iterator(); it.hasNext();)
			{
				String id = it.next().toString();
				if (!lesProduitsDeLIterationDansMesure.containsKey(id))
				{
					lesProduitsDeLIterationDansMesure.put(id, new ArrayList());
				}
			}

			// retrait des produits supprimés lors de la création manuelle
			lesElements.clear();
			lesElements = new ArrayList(lesProduitsDeLIterationDansMesure.keySet());
			for (Iterator it = lesElements.iterator(); it.hasNext();)
			{
				String id = (String) it.next();
				if (!listeDesProduitsDeLIteration.containsKey(id))
				{
					lesProduitsDeLIterationDansMesure.remove(id);
				}
			}

			// ajout des rôles ajoutés
			lesRolesDeLIterationDansMesure = C_Mesures.getMesure(idMesure).getListeRoles();
			for (Iterator it = listeDesRolesDeLIteration.keySet().iterator(); it.hasNext();)
			{
				String id = it.next().toString();
				if (!lesRolesDeLIterationDansMesure.containsKey(id))
				{
					lesRolesDeLIterationDansMesure.put(id, new ArrayList());
				}
			}

			// retrait des rôles supprimés lors de la création manuelle
			lesElements.clear();
			lesElements = new ArrayList(lesRolesDeLIterationDansMesure.keySet());
			for (Iterator it = lesElements.iterator(); it.hasNext();)
			{
				String id = (String) it.next();
				if (!listeDesRolesDeLIteration.containsKey(id))
				{
					lesRolesDeLIterationDansMesure.remove(id);
				}
			}

		}

		// enregistrement de l'eval quantitative
		float RUA;
		if ((!valeurSaisieRUA && valeurRUA != null) || valeurRUA == null)
		{
			if (idMesure == -1)
				RUA = C_CompilerMesures.compilerRUA(listeDesActivitesDeLIteration, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesActivites());
			else
				RUA = C_CompilerMesures.compilerRUA(lesActivitesDeLIterationDansMesure, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesActivites());
		}
		else
		{
			RUA = valeurRUA.floatValue();
		}

		float RUP;
		if ((!valeurSaisieRUP && valeurRUP != null) || valeurRUP == null)
		{
			if (idMesure == -1)
				RUP = C_CompilerMesures.compilerRUP(listeDesProduitsDeLIteration, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesProduits());
			else
				RUP = C_CompilerMesures.compilerRUP(lesProduitsDeLIterationDansMesure, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesProduits());
		}
		else
		{
			RUP = valeurRUP.floatValue();
		}

		float RUR;
		if ((!valeurSaisieRUR && valeurRUR != null) || valeurRUR == null)
		{
			if (idMesure == -1)
				RUR = C_CompilerMesures.compilerRUR(listeDesRolesDeLIteration, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesRoles());
			else
				RUR = C_CompilerMesures.compilerRUR(lesRolesDeLIterationDansMesure, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesRoles());
		}
		else
		{
			RUR = valeurRUR.floatValue();
		}

		float RCT;
		if (idMesure == -1)
			RCT = 0;
		else
			RCT = C_CompilerMesures.compilerRCT(lesActivitesDeLIterationDansMesure, C_CycleDeVie.getUnCycleDeVie(idCycleDeVie).getLesActivites());

		HashMap ordreDesTaches;
		if (idMesure == -1)
			ordreDesTaches = new HashMap();
		else
			ordreDesTaches = C_CompilerMesures.OrdonnerLesTaches(lesActivitesDeLIterationDansMesure);

		E_Evaluation eval = new E_Evaluation(idProcessus, Integer.parseInt(idExec), numIt, RUA, RCT, RUR, RUP, ordreDesTaches, C_Hapi.EVALUATION_ICONE);

		eval.setDateDebut(dateDebut);
		eval.setDateFin(dateFin);

		// affectation des listes des entités hors processus
		eval.setActivitesHorsProcessus((ArrayList) listeDesActivitesDeLIteration.get("-1"));
		eval.setRolesHorsProcessus((ArrayList) listeDesRolesDeLIteration.get("-1"));
		eval.setProduitsHorsProcessus((ArrayList) listeDesProduitsDeLIteration.get("-1"));

		HashMap repartitionDesChargesParComposant = C_CompilerMesures.compilerRTP(chargesParComposant);
		eval.setRepartitionDeschargesParComposant(repartitionDesChargesParComposant);

		// affectation des listes des entités non réalisées
		eval.setActivitesNonUtilisees(activitesNON);
		eval.setRolesNonUtilises(rolesNON);
		eval.setProduitsNonUtilises(produitNON);
		C_Evaluation.insererEvaluationEnBase(eval);
		// ajout de l'eval dans le controleur d'eval
		C_Evaluation.ajouterEvaluationQuantitative(idProcessus, Integer.parseInt(idExec), numIt, eval);

	}
}
