/*
 * Créé le 10 mai 2005
 */
package hapi.application.indicateurs;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Produit;
import hapi.application.metier.C_Role;
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.metier.temporaire.C_ProduitTemporaire;
import hapi.application.metier.temporaire.C_RoleTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_CycleDeVie;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.exception.ChampsVideException;
import hapi.exception.NoRowInsertedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Cédric
 */
public class C_CycleDeVie
{
	// structure <id_cycledevie (Integer), cycle de vie(E_CycleDeVie)>
	private static HashMap lesCyclesDeVie = new HashMap();
	//Mémorisation entre chaque fenêtres
	public final static int NOMBRE_FENETRES = 3;
	private static HashMap lesActivitesR = null;
	private static HashMap lesActivitesNR = null;
	private static HashMap lesProduitsR = null;
	private static HashMap lesProduitsNR = null;
	private static HashMap lesRolesR = null;
	private static HashMap lesRolesNR = null;
	private static C_ArbreFaceArbre lArbreCourant = null;

	public static String getTitreFenetre(int Numero)
	{
		switch (Numero)
		{
			case 0:
				return Bundle.getText("BD_CreerCycleDeVie_fen0");
			case 1:
				return Bundle.getText("BD_CreerCycleDeVie_fen1");
			case 2:
				return Bundle.getText("BD_CreerCycleDeVie_fen2");
		}
		return "Erreur grave n° 5 !!!!";
	}

	/**
	 * Création d'une fenêtre contenant deux arbres face à face
	 * 
	 * @param Numero Numéro de la fenêtre 0: Activités 1: Produits 2: Rôles
	 * @param idProcessus Identifiant du processus (utilisé QUE si
	 *            utilise_Courant == true)
	 * @param utiliseCourant Indique s'il faut utiliser le processus courant ou
	 *            le temporaire (false = < temporaire)
	 * @param idCycleDeVie Identifiant du cycle de vie (si -1 alors création
	 *            d'un nouveau cycle)
	 * @return
	 */
	public static C_ArbreFaceArbre getFenetre(int Numero, String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		lArbreCourant = null;
		switch (Numero)
		{
			case 0:
				lArbreCourant = getFenetreActivites(idProcessus, utiliseCourant, idCycleDeVie);
				break;
			case 1:
				lArbreCourant = getFenetreProduits(idProcessus, utiliseCourant, idCycleDeVie);
				break;
			case 2:
				lArbreCourant = getFenetreRoles(idProcessus, utiliseCourant, idCycleDeVie);
				break;
		}
		return lArbreCourant;
	}

	public static void memoriseListesCourantes(int Numero)
	{
		switch (Numero)
		{
			case 0:
				lesActivitesR = lArbreCourant.getListeUtilise();
				lesActivitesNR = lArbreCourant.getListeNonUtilise();
				break;
			case 1:
				lesProduitsR = lArbreCourant.getListeUtilise();
				lesProduitsNR = lArbreCourant.getListeNonUtilise();
				break;
			case 2:
				lesRolesR = lArbreCourant.getListeUtilise();
				lesRolesNR = lArbreCourant.getListeNonUtilise();
				break;
		}
	}

	public static void clearHashMap()
	{
		lesActivitesR = null;
		lesActivitesNR = null;
		lesProduitsR = null;
		lesProduitsNR = null;
		lesRolesR = null;
		lesRolesNR = null;
	}

	private static C_ArbreFaceArbre getFenetreActivites(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		return new C_ArbreFaceArbre(Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_Activites"), Bundle.getText("BD_CreerCycleDeVie_ActivitesNR"), Bundle.getText("BD_CreerCycleDeVie_Realiser"), Bundle.getText("BD_CreerCycleDeVie_NPRealiser"), CreerArborescenceActiviteUtilise(idProcessus, utiliseCourant, idCycleDeVie), CreerArborescenceActiviteNonUtilise(idProcessus, utiliseCourant, idCycleDeVie), 3);
	}

	private static C_ArbreFaceArbre getFenetreProduits(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		return new C_ArbreFaceArbre(Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_Produits"), Bundle.getText("BD_CreerCycleDeVie_ProduitsNR"), Bundle.getText("BD_CreerCycleDeVie_Realiser"), Bundle.getText("BD_CreerCycleDeVie_NPRealiser"), CreerArborescenceProduitUtilise(idProcessus, utiliseCourant, idCycleDeVie), CreerArborescenceProduitNonUtilise(idProcessus, utiliseCourant, idCycleDeVie), 2);
	}

	private static C_ArbreFaceArbre getFenetreRoles(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		return new C_ArbreFaceArbre(Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_ListeComposant"), Bundle.getText("BD_CreerCycleDeVie_Roles"), Bundle.getText("BD_CreerCycleDeVie_RolesNR"), Bundle.getText("BD_CreerCycleDeVie_Utiliser"), Bundle.getText("BD_CreerCycleDeVie_NPUtiliser"), CreerArborescenceRoleUtilise(idProcessus, utiliseCourant, idCycleDeVie), CreerArborescenceRoleNonUtilise(idProcessus, utiliseCourant, idCycleDeVie), 2);
	}

	private static HashMap CreerArborescenceProduitUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesProduits = null;
		//Les produits
		HashMap lesEntites = null;

		//Récupération des composants et des définitions
		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
		}

		if (lesProduitsR != null)
		{
			lesProduits = lesProduitsR;
		}
		else
		{
			if (idCycleDeVie == -1) //Création du cycle de vie
			{
				if (utiliseCourant)
				{
					//Récupération de la liste des Produits
					lesProduits = C_Produit.getProduitsDuProcessus(idProcessus);
				}
				else
				{
					//Récupération de la liste des Produits
					lesProduits = C_ProduitTemporaire.getProduits();
				}
			}
			else
			//Modification du cycle de vie
			{
				lesProduits = new HashMap();
				//Récupération des Produits suivis par le cycle de vie
				ArrayList lesIdProduitsSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesProduits();
				//Construction de la hashmap <id_Produit><Produit>
				for (Iterator it = lesIdProduitsSuivies.iterator(); it.hasNext();)
				{
					String unIdProduit = (String) it.next();

					if (utiliseCourant)
					{
						//Récupération de la liste des Produits
						E_Produit unProduit = C_Produit.getProduit(idProcessus, unIdProduit);

						if (unProduit != null)
							lesProduits.put(unIdProduit, unProduit);
					}
					else
					{
						//Récupération de la liste des Produits
						if (C_ProduitTemporaire.getProduits().get(unIdProduit) != null)
							lesProduits.put(unIdProduit, C_ProduitTemporaire.getProduits().get(unIdProduit));
					}
				}
			}
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

	private static HashMap CreerArborescenceRoleUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesRoles = null;
		//Les Roles
		HashMap lesEntites = null;

		//Récupération des composants et des définitions
		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
		}

		if (lesRolesR != null)
		{
			lesRoles = lesRolesR;
		}
		else
		{
			if (idCycleDeVie == -1) //Création du cycle de vie
			{
				if (utiliseCourant)
				{
					//Récupération de la liste des Roles
					lesRoles = C_Role.getRolesDuProcessus(idProcessus);
				}
				else
				{
					//Récupération de la liste des Roles
					lesRoles = C_RoleTemporaire.getRoles();
				}
			}
			else
			//Modification du cycle de vie
			{
				lesRoles = new HashMap();
				//Récupération des Roles suivis par le cycle de vie
				ArrayList lesIdRolesSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesRoles();
				//Construction de la hashmap <id_role><role>
				for (Iterator it = lesIdRolesSuivies.iterator(); it.hasNext();)
				{
					String unIdRole = (String) it.next();

					if (utiliseCourant)
					{
						//Récupération de la liste des Roles
						E_Role unRole = C_Role.getRole(idProcessus, unIdRole);

						if (unRole != null)
							lesRoles.put(unIdRole, unRole);
					}
					else
					{
						//Récupération de la liste des Roles
						if (C_RoleTemporaire.getRoles().get(unIdRole) != null)
							lesRoles.put(unIdRole, C_RoleTemporaire.getRoles().get(unIdRole));
					}
				}
			}
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

	private static HashMap CreerArborescenceActiviteUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
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

		if (lesActivitesR != null)
		{
			lesActivites = lesActivitesR;
		}
		else
		{
			if (idCycleDeVie == -1) //Création du cycle de vie
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
			//Modification du cycle de vie
			{
				lesActivites = new HashMap();
				//Récupération des Activites suivi par le cycle de vie
				ArrayList lesIdActivitesSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesActivites();
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

		return retour;
	}

	private static HashMap CreerArborescenceProduitNonUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesProduits = null;
		//Les produits
		HashMap lesEntites = null;

		//Récupération des composants et des définitions
		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
		}

		if (lesProduitsNR != null)
		{
			lesProduits = lesProduitsNR;
		}
		else
		{
			lesProduits = new HashMap();

			if (idCycleDeVie != -1) //Modification du cycle de vie
			{
				//Récupération des Produits suivis par le cycle de vie
				ArrayList lesIdProduitsSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesProduits();
				//Construction de la hashmap <id_Produit><Produit>
				for (Iterator it = lesIdProduitsSuivies.iterator(); it.hasNext();)
				{
					String unIdProduit = (String) it.next();

					if (utiliseCourant)
					{
						//Récupération de la liste des Produits
						E_Produit unProduit = C_Produit.getProduit(idProcessus, unIdProduit);

						if (unProduit != null)
							lesProduits.put(unIdProduit, unProduit);
					}
					else
					{
						//Récupération de la liste des Produits
						if (C_ProduitTemporaire.getProduits().get(unIdProduit) != null)
							lesProduits.put(unIdProduit, C_ProduitTemporaire.getProduits().get(unIdProduit));
					}
				}

				//Exclusions des Produits
				if (utiliseCourant)
				{
					lesProduits = doExclusion(lesProduits, C_Produit.getProduitsDuProcessus(idProcessus));
				}
				else
				{
					lesProduits = doExclusion(lesProduits, C_ProduitTemporaire.getProduits());
				}
			}
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

	private static HashMap CreerArborescenceRoleNonUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesRoles = null;
		//Les Roles
		HashMap lesEntites = null;

		//Récupération des composants et des définitions
		if (utiliseCourant)
		{
			//Récupération de la liste des composants
			lesComposants = C_Composant.getComposantsDuProcessus(idProcessus);
		}
		else
		{
			//Récupération de la liste des composants
			lesComposants = C_ComposantTemporaire.getComposants();
		}

		if (lesRolesNR != null)
		{
			lesRoles = lesRolesNR;
		}
		else
		{
			lesRoles = new HashMap();

			if (idCycleDeVie != -1) //Modification du cycle de vie
			{
				//Récupération des Roles suivis par le cycle de vie
				ArrayList lesIdRolesSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesRoles();
				//Construction de la hashmap <id_role><role>
				for (Iterator it = lesIdRolesSuivies.iterator(); it.hasNext();)
				{
					String unIdRole = (String) it.next();

					if (utiliseCourant)
					{
						//Récupération de la liste des Roles
						E_Role unRole = C_Role.getRole(idProcessus, unIdRole);

						if (unRole != null)
							lesRoles.put(unIdRole, unRole);
					}
					else
					{
						//Récupération de la liste des Roles
						if (C_RoleTemporaire.getRoles().get(unIdRole) != null)
							lesRoles.put(unIdRole, C_RoleTemporaire.getRoles().get(unIdRole));
					}
				}

				//Exclusions des Roles
				if (utiliseCourant)
				{
					lesRoles = doExclusion(lesRoles, C_Role.getRolesDuProcessus(idProcessus));
				}
				else
				{
					lesRoles = doExclusion(lesRoles, C_RoleTemporaire.getRoles());
				}
			}
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

	private static HashMap CreerArborescenceActiviteNonUtilise(String idProcessus, boolean utiliseCourant, int idCycleDeVie)
	{
		HashMap retour = new HashMap();
		HashMap lesComposants = null;
		HashMap lesDefinitions = null;
		HashMap lesActivites = null;
		//Les Roles
		HashMap lesEntites = null;
		HashMap lesEntites2 = null;

		//Récupération des composants et des définitions
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

		if (lesActivitesNR != null)
		{
			lesActivites = lesActivitesNR;
		}
		else
		{
			lesActivites = new HashMap();

			if (idCycleDeVie != -1) //Modification du cycle de vie
			{
				//Récupération des Activites suivi par le cycle de vie
				ArrayList lesIdActivitesSuivies = ((E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie))).getLesActivites();
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

				//Exclusions des activités
				if (utiliseCourant)
				{
					lesActivites = doExclusion(lesActivites, C_Activite.getActivitesDuProcessus(idProcessus));
				}
				else
				{
					lesActivites = doExclusion(lesActivites, C_ActiviteTemporaire.getActivites());
				}
			}
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

	public static void supprimerCycleDeVie(int id_cycleDeVie)
	{
		lesCyclesDeVie.remove(new Integer(id_cycleDeVie));
	}

	public static void supprimerCycleEnBase(int id_cycleDeVie, C_AccesBaseDonnees cBase) throws SQLException
	{

		int idStatement = 0;
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(new Integer(id_cycleDeVie));

		try
		{
			idStatement = cBase.creerPreparedStatement("delCycleDeVie");
			cBase.executerRequeteStockee(idStatement, "delCycleDeVie", lesParametres);
			cBase.fermerStatement(idStatement);
			idStatement = cBase.creerPreparedStatement("delCycleProduit");
			cBase.executerRequeteStockee(idStatement, "delCycleProduit", lesParametres);
			cBase.fermerStatement(idStatement);
			idStatement = cBase.creerPreparedStatement("delCycleRole");
			cBase.executerRequeteStockee(idStatement, "delCycleRole", lesParametres);
			cBase.fermerStatement(idStatement);
			idStatement = cBase.creerPreparedStatement("delCycleActivite");
			cBase.executerRequeteStockee(idStatement, "delCycleActivite", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
	}

	public static void ajouterUnCycleDeVie(int id_CycleDeVie, E_CycleDeVie leCycle)
	{
		lesCyclesDeVie.put(new Integer(id_CycleDeVie), leCycle);
	}

	/**
	 * Enregistre en base de données les listes réalisées
	 * 
	 * @param idCycleDeVie Identifiant du cycle de vie, -1 si nouveau
	 * @param idProjet Identifiant du projet
	 * @param idProcessus Identifiant du processus
	 * @param utiliseCourant ndique s'il faut utiliser le processus courant ou
	 *            le temporaire (false = < temporaire)
	 * @throws SQLException Exception lors de l'enregistrement
	 * @throws ChampsVideException
	 */
	public static void saveListes(int idCycleDeVie, String idProjet, String idProcessus, boolean utiliseCourant) throws SQLException, ChampsVideException
	{
		//Avant tout, s'assurer que les listes sont pleines
		if (lesProduitsR == null)
		{
			lesProduitsR = getFenetreProduits(idProcessus, utiliseCourant, idCycleDeVie).getListeUtilise();
		}

		if (lesRolesR == null)
		{
			lesRolesR = getFenetreRoles(idProcessus, utiliseCourant, idCycleDeVie).getListeUtilise();
		}

		if (lesActivitesR == null)
		{
			lesActivitesR = getFenetreActivites(idProcessus, utiliseCourant, idCycleDeVie).getListeUtilise();
		}
		
		if (lesActivitesR.size() == 0)
		{
			throw new ChampsVideException(0);
		}
		if (lesProduitsR.size() == 0)
		{
			throw new ChampsVideException(1);
		}
		if (lesRolesR.size() == 0)
		{
			throw new ChampsVideException(2);
		}


		int idStatement = 0;
		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			if (idCycleDeVie == -1)
			{
				lesParametres.add(idProjet);
				lesParametres.add(C_ExecutionProcessus.getExecutionProcessus(idProcessus, idProjet).getVersionProcessus());
				idStatement = cBase.creerPreparedStatement("setCycleDeVieParProjet");
				cBase.executerRequeteStockee(idStatement, "setCycleDeVieParProjet", lesParametres);
				cBase.fermerStatement(idStatement);
				//Recherche de l'id d'insertion
				idStatement = cBase.creerPreparedStatement("getMaxCycleDeVie");
				ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getMaxCycleDeVie");
				leResultat.next();
				Integer nouvelIdCycleDeVie = new Integer(leResultat.getInt(1));
				cBase.fermerStatement(idStatement);
				//Insertion dans chaque tables
				//Activités
				for (Iterator it = lesActivitesR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(nouvelIdCycleDeVie);
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleActivite");
					cBase.executerRequeteStockee(idStatement, "setCycleActivite", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Roles
				for (Iterator it = lesRolesR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(nouvelIdCycleDeVie);
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleRole");
					cBase.executerRequeteStockee(idStatement, "setCycleRole", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Produits
				for (Iterator it = lesProduitsR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(nouvelIdCycleDeVie);
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleProduit");
					cBase.executerRequeteStockee(idStatement, "setCycleProduit", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Ajout d'un cycle de vie
				E_CycleDeVie unCycleDeVie = new E_CycleDeVie(new ArrayList(lesActivitesR.keySet()), new ArrayList(lesRolesR.keySet()), new ArrayList(lesProduitsR.keySet()));
				ajouterUnCycleDeVie(nouvelIdCycleDeVie.intValue(), unCycleDeVie);
				C_ExecutionProcessus.getExecutionProcessus(idProcessus,idProjet).setIdCycleDeVie(nouvelIdCycleDeVie.intValue());
			}
			else
			{
				lesParametres.add(new Integer(idCycleDeVie));
				//Vidange des tables
				try
				{
					idStatement = cBase.creerPreparedStatement("delCycleProduit");
					cBase.executerRequeteStockee(idStatement, "delCycleProduit", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				catch (NoRowInsertedException e)
				{}
				try
				{
					idStatement = cBase.creerPreparedStatement("delCycleRole");
					cBase.executerRequeteStockee(idStatement, "delCycleRole", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				catch (NoRowInsertedException e)
				{}
				try
				{
					idStatement = cBase.creerPreparedStatement("delCycleActivite");
					cBase.executerRequeteStockee(idStatement, "delCycleActivite", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				catch (NoRowInsertedException e)
				{}
				//Insertion dans chaque tables
				//Activités
				for (Iterator it = lesActivitesR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(new Integer(idCycleDeVie));
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleActivite");
					cBase.executerRequeteStockee(idStatement, "setCycleActivite", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Roles
				for (Iterator it = lesRolesR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(new Integer(idCycleDeVie));
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleRole");
					cBase.executerRequeteStockee(idStatement, "setCycleRole", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Produits
				for (Iterator it = lesProduitsR.keySet().iterator(); it.hasNext();)
				{
					lesParametres.clear();
					lesParametres.add(new Integer(idCycleDeVie));
					lesParametres.add((String) it.next());
					idStatement = cBase.creerPreparedStatement("setCycleProduit");
					cBase.executerRequeteStockee(idStatement, "setCycleProduit", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				//Modification du cycle de vie
				E_CycleDeVie unCycleDeVie = (E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycleDeVie));
				unCycleDeVie.setLesActivites(new ArrayList(lesActivitesR.keySet()));
				unCycleDeVie.setLesRoles(new ArrayList(lesRolesR.keySet()));
				unCycleDeVie.setLesProduits(new ArrayList(lesProduitsR.keySet()));
			}
			cBase.commit();
		}
		catch (SQLException e)
		{
			cBase.rollback();
			throw e;
		}
		finally
		{
			cBase.fermerConnexion();
		}
	}

	/**
	 * @return Renvoie lesCyclesDeVie.
	 */
	public static HashMap getLesCyclesDeVie()
	{
		return lesCyclesDeVie;
	}
	
	/**
	 * Retourne un cycle de vie
	 * @param idCycle Identifiant du cycle de vie
	 * @return le Cycle de vie
	 */
	public static E_CycleDeVie getUnCycleDeVie(int idCycle)
	{
		return (E_CycleDeVie) lesCyclesDeVie.get(new Integer(idCycle));
	}
}