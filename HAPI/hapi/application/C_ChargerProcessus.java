/*
 * Fichier C_ChargerProcessus.java
 * Auteur Cédric
 *
 */
package hapi.application;

import hapi.application.metier.C_Activite;
import hapi.application.metier.C_Composant;
import hapi.application.metier.C_Definition;
import hapi.application.metier.C_ElementPresentation;
import hapi.application.metier.C_Etat;
import hapi.application.metier.C_Guide;
import hapi.application.metier.C_Interface;
import hapi.application.metier.C_PaquetagePresentation;
import hapi.application.metier.C_Processus;
import hapi.application.metier.C_Produit;
import hapi.application.metier.C_Role;
import hapi.application.metier.C_TypeGuide;
import hapi.application.metier.C_TypeProduit;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_ElementPresentation;
import hapi.donnees.metier.E_Etat;
import hapi.donnees.metier.E_Guide;
import hapi.donnees.metier.E_Interface;
import hapi.donnees.metier.E_PaquetagePresentation;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.donnees.metier.E_TypeGuide;
import hapi.donnees.metier.E_TypeProduit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Cette classe lancée par un thread charge les composants des processus en mémoire
 */
public class C_ChargerProcessus implements Runnable
{
	private C_AccesBaseDonnees cBase = null;

	public C_ChargerProcessus(C_AccesBaseDonnees laBase)
	{
		cBase = laBase;
	}

	public void run()
	{
		int idStatement = 0;

		try
		{
			//C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
			//cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			ResultSet leResultat = null;
			//Pour chaque processus, recherche des composants associés
			for (int j = 0; j < C_Processus.size(); j++)
			{
				String id_processus = C_Processus.get(j).getIdentifiant();

				/**
				 * Récupération des composants
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getComposants");
				leResultat = cBase.executerRequeteStockee(idStatement, "getComposants", lesParametres);

				//Pour chaque composant
				while (leResultat.next())
				{
					//Ajout de l'id dans le processus
					C_Processus.get(j).ajouterIdComposant(leResultat.getString(1));
					//Création du composant
					E_Composant unComposant = new E_Composant(C_Hapi.COMPOSANT_ICONE);
					unComposant.setId(leResultat.getString(1));
					unComposant.setNom(leResultat.getString(2));
					unComposant.setVersion(leResultat.getString(3));
					unComposant.setNomAuteur(leResultat.getString(4));
					unComposant.setEmailAuteur(leResultat.getString(5));
					unComposant.setDatePlacement(leResultat.getString(6));
					unComposant.setDescription(leResultat.getString(7));
					unComposant.setInterfaceFournie(leResultat.getString(9));
					unComposant.setInterfaceRequise(leResultat.getString(10));
					unComposant.setElementPresentationId(leResultat.getString(11));

					C_Composant.ajouterComposant(id_processus, leResultat.getString(1), unComposant);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Paquetages de présentation
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getPaquetagesPresentation");
				leResultat = cBase.executerRequeteStockee(idStatement, "getPaquetagesPresentation", lesParametres);

				//Pour chaque composant
				while (leResultat.next())
				{
					//Ajout de l'id dans le processus
					C_Processus.get(j).ajouterPaquetagePresentation(leResultat.getString(1));
					//Création du paquetage de presentation
					E_PaquetagePresentation unPaquetagePresentation = new E_PaquetagePresentation(new ImageIcon());
					unPaquetagePresentation.setIdentifiant(leResultat.getString(1));
					unPaquetagePresentation.setNom(leResultat.getString(2));
					unPaquetagePresentation.setDossierIcone(leResultat.getString(3));
					unPaquetagePresentation.setDossierContenu(leResultat.getString(4));
					unPaquetagePresentation.setElementPresentationId(leResultat.getString(6));

					C_PaquetagePresentation.ajouterPaquetagePresentation(id_processus, leResultat.getString(1), unPaquetagePresentation);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Eléments de présentation
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getElementsPresentation");
				leResultat = cBase.executerRequeteStockee(idStatement, "getElementsPresentation", lesParametres);

				//Pour chaque paquetage
				while (leResultat.next())
				{
					//Création de l'element de presentation
					E_ElementPresentation unElementPresentation = new E_ElementPresentation(new ImageIcon());
					unElementPresentation.setIdentifiant(leResultat.getString(1));
					unElementPresentation.setNom(leResultat.getString(2));
					unElementPresentation.setCheminIcone(leResultat.getString(3));
					unElementPresentation.setCheminContenu(leResultat.getString(4));
					unElementPresentation.setDescription(leResultat.getString(5));
					unElementPresentation.setCheminPage(leResultat.getString(6));

					C_ElementPresentation.ajouterElementPresentation(id_processus, leResultat.getString(1), unElementPresentation);
					// Un paquetage de présentation est associé à cet élément de présentation
					// On ajoute à ce paquetage l'id de l'élément
					if (C_PaquetagePresentation.getPaquetagePresentation(id_processus, leResultat.getString(8)) != null)
						C_PaquetagePresentation.getPaquetagePresentation(id_processus, leResultat.getString(8)).ajouterAgregeElementPresentation(leResultat.getString(1));
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Etats
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getEtats");
				leResultat = cBase.executerRequeteStockee(idStatement, "getEtats", lesParametres);
				//Pour chaque etat
				while (leResultat.next())
				{
					//Création de l'état
					E_Etat unEtat = new E_Etat(new ImageIcon());
					unEtat.setIdentifiant(leResultat.getString(1));
					unEtat.setNom(leResultat.getString(2));

					C_Etat.ajouterEtat(id_processus, leResultat.getString(1), unEtat);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Types Produit
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getTypesProduit");
				leResultat = cBase.executerRequeteStockee(idStatement, "getTypesProduit", lesParametres);

				while (leResultat.next())
				{
					//Création du type de produit
					E_TypeProduit unTypeProduit = new E_TypeProduit(new ImageIcon());
					unTypeProduit.setIdentifiant(leResultat.getString(1));
					unTypeProduit.setNom(leResultat.getString(2));

					C_TypeProduit.ajouterTypeProduit(id_processus, leResultat.getString(1), unTypeProduit);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Types Guide
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getTypesGuide");
				leResultat = cBase.executerRequeteStockee(idStatement, "getTypesGuide", lesParametres);

				while (leResultat.next())
				{
					//Création du type de produit
					E_TypeGuide unTypeGuide = new E_TypeGuide(new ImageIcon());
					unTypeGuide.setIdentifiant(leResultat.getString(1));
					unTypeGuide.setNom(leResultat.getString(2));

					C_TypeGuide.ajouterTypeGuide(id_processus, leResultat.getString(1), unTypeGuide);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Roles
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getRoles");
				leResultat = cBase.executerRequeteStockee(idStatement, "getRoles", lesParametres);

				//Pour chaque rôle
				while (leResultat.next())
				{
					//Création du role
					E_Role unRole = new E_Role(C_Hapi.ROLE_ICONE);
					unRole.setIdentifiant(leResultat.getString(1));
					unRole.setNom(leResultat.getString(2));
					unRole.setAgregatComposant(leResultat.getString(4));
					unRole.setElementPresentationId(leResultat.getString(5));

					C_Role.ajouterRole(id_processus, leResultat.getString(1), unRole);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Produits
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getProduits");
				leResultat = cBase.executerRequeteStockee(idStatement, "getProduits", lesParametres);

				//Pour chaque composant
				while (leResultat.next())
				{
					//Création du produit
					E_Produit unProduit = new E_Produit(C_Hapi.PRODUIT_ICONE);
					unProduit.setIdentifiant(leResultat.getString(1));
					unProduit.setNom(leResultat.getString(2));
					unProduit.setAgregatComposant(leResultat.getString(4));
					unProduit.setIdResponsabilite(leResultat.getString(5));
					unProduit.setTypeProduitId(leResultat.getString(6));
					unProduit.setElementPresentationId(leResultat.getString(7));

					C_Produit.ajouterProduit(id_processus, leResultat.getString(1), unProduit);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Définitions de travail
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getDefinitions");
				leResultat = cBase.executerRequeteStockee(idStatement, "getDefinitions", lesParametres);

				//Pour chaque composant
				while (leResultat.next())
				{
					// Création de la definition
					E_Definition uneDefinition = new E_Definition(C_Hapi.DEFINITION_ICONE);
					uneDefinition.setIdentifiant(leResultat.getString(1));
					uneDefinition.setNom(leResultat.getString(2));
					uneDefinition.setAgregatComposant(leResultat.getString(4));
					uneDefinition.setElementPresentationId(leResultat.getString(5));

					C_Definition.ajouterDefinition(id_processus, leResultat.getString(1), uneDefinition);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Activités
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getActivites");
				leResultat = cBase.executerRequeteStockee(idStatement, "getActivites", lesParametres);

				//Pour chaque definition
				while (leResultat.next())
				{
					//Création de l'activite
					E_Activite uneActivite = new E_Activite(C_Hapi.ACTIVITE_ICONE);
					uneActivite.setIdentifiant(leResultat.getString(1));
					uneActivite.setNom(leResultat.getString(2));
					uneActivite.setAgregatDefinitionTravail(leResultat.getString(4));
					uneActivite.setParticipationRole(leResultat.getString(5));
					uneActivite.setElementPresentationId(leResultat.getString(6));

					C_Activite.ajouterActivite(id_processus, leResultat.getString(1), uneActivite);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Interfaces
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getInterfaces");
				leResultat = cBase.executerRequeteStockee(idStatement, "getInterfaces", lesParametres);

				//Pour chaque interface
				while (leResultat.next())
				{
					//Création de l'interface
					E_Interface uneInterface = new E_Interface(new ImageIcon());
					uneInterface.setIdentifiant(leResultat.getString(1));
					uneInterface.setInterfaceFournieComposant(leResultat.getString(3));
					uneInterface.setInterfaceRequiseComposant(leResultat.getString(4));

					C_Interface.ajouterInterface(id_processus, leResultat.getString(1), uneInterface);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Guides
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getGuides");
				leResultat = cBase.executerRequeteStockee(idStatement, "getGuides", lesParametres);

				//Pour chaque element de presentation
				while (leResultat.next())
				{
					//Création du guide
					E_Guide unGuide = new E_Guide(new ImageIcon());
					unGuide.setIdentifiant(leResultat.getString(1));
					unGuide.setNom(leResultat.getString(2));
					unGuide.setTypeGuideId(leResultat.getString(4));
					unGuide.setElementPresentationId(leResultat.getString(5));

					C_Guide.ajouterGuide(id_processus, leResultat.getString(1), unGuide);
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Produits en entrée
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getLiensProdActEntree");
				leResultat = cBase.executerRequeteStockee(idStatement, "getLiensProdActEntree", lesParametres);

				//Pour chaque element de presentation
				while (leResultat.next())
				{
					try
					{
						C_Produit.getProduit(id_processus, leResultat.getString(1)).ajouterIdEntreeActivite(leResultat.getString(2));
					}
					catch (Exception e)
					{
						//Ici, le produit n'est pas en entrée de l'activite	
					}
					try
					{
						C_Activite.getActivite(id_processus, leResultat.getString(2)).ajouterProduitEntree(leResultat.getString(1));
					}
					catch (Exception e)
					{
						//Ici, l'activité n'a pas de produit en entrée	
					}
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Produits en sortie
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getLiensProdActSortie");
				leResultat = cBase.executerRequeteStockee(idStatement, "getLiensProdActSortie", lesParametres);

				//Pour chaque element de presentation
				while (leResultat.next())
				{
					try
					{
						C_Produit.getProduit(id_processus, leResultat.getString(1)).ajouterIdSortieActivite(leResultat.getString(2));
					}
					catch (Exception e)
					{
						//Ici, le produit n'est pas en sortie de l'activité				
					}
					try
					{
						C_Activite.getActivite(id_processus, leResultat.getString(2)).ajouterProduitSortie(leResultat.getString(1));
					}
					catch (Exception e)
					{
						//Ici, l'activité n'a pas de produit en sortie				
					}
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Etats des produits
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getLiensProduitEtat");
				leResultat = cBase.executerRequeteStockee(idStatement, "getLiensProduitEtat", lesParametres);

				//Pour chaque element de presentation
				while (leResultat.next())
				{
					try
					{
						C_Produit.getProduit(id_processus, leResultat.getString(1)).ajouterIdEtat(leResultat.getString(2));
					}
					catch (Exception e)
					{
						//Ici, le produit n'a pas d'état				
					}
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Interface des produits
				 */
				lesParametres.clear();
				lesParametres.add(id_processus);

				idStatement = cBase.creerPreparedStatement("getLiensProduitInterface");
				leResultat = cBase.executerRequeteStockee(idStatement, "getLiensProduitInterface", lesParametres);

				//Pour chaque element de presentation
				while (leResultat.next())
				{
					try
					{
						C_Produit.getProduit(id_processus, leResultat.getString(1)).ajouterIdInterface(leResultat.getString(2));
					}
					catch (Exception e)
					{
						//Ici, le produit n'a pas d'interface				
					}
					try
					{
						C_Interface.getInterface(id_processus, leResultat.getString(2)).ajouterInterfaceProduit(leResultat.getString(1));
					}
					catch (Exception e)
					{
						//Ici, l'interface n'a pas de produit				
					}
				}
				cBase.fermerStatement(idStatement);

				/**
				 * Remplissage des listes qui ont besoin des roles
				 */
				for (int i = 0; i < C_Role.size(id_processus); i++)
				{
					E_Role unRole = C_Role.getRole(id_processus, C_Role.getClef(id_processus, i));
					try
					{
						C_Composant.getComposant(id_processus, unRole.getAgregatComposant()).ajouterIdRole(unRole.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, le composant n'a pas de rôle				
					}
				}

				/**
				 * Remplissage des listes qui ont besoin des activités
				 */
				for (int i = 0; i < C_Activite.size(id_processus); i++)
				{
					E_Activite uneActivite = C_Activite.getActivite(id_processus, C_Activite.getClef(id_processus, i));
					try
					{
						C_Role.getRole(id_processus, uneActivite.getParticipationRole()).ajouterIdParticipationActivite(uneActivite.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, le rôle ne participe pas à l'activité				
					}
					try
					{
						C_Definition.getDefinition(id_processus, uneActivite.getAgregatDefinitionTravail()).ajouterIdActivite(uneActivite.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, la définition de travail n'a pas d'activité				
					}
				}

				/**
				 * Remplissage des listes qui ont besoin des produits
				 */
				for (int i = 0; i < C_Produit.size(id_processus); i++)
				{
					E_Produit unProduit = C_Produit.getProduit(id_processus, C_Produit.getClef(id_processus, i));
					try
					{
						C_Composant.getComposant(id_processus, unProduit.getAgregatComposant()).ajouterIdProduit(unProduit.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, le composant n'a pas de produit				
					}
					try
					{
						C_Role.getRole(id_processus, unProduit.getIdResponsabilite()).ajouterIdResponsabiliteProduit(unProduit.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, le produit n'a pas de responsable				
					}
				}

				/**
				 * Remplissage des listes qui ont besoin des définitions de travail
				 */
				for (int i = 0; i < C_Definition.size(id_processus); i++)
				{
					E_Definition uneDefinition = C_Definition.getDefinition(id_processus, C_Definition.getClef(id_processus, i));
					try
					{
						C_Composant.getComposant(id_processus, uneDefinition.getAgregatComposant()).ajouterIdDefinitionTravail(uneDefinition.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, le composant n'a pas de définition				
					}
				}

				/**
				 * Remplissage des listes qui ont besoin des guides
				 */
				for (int i = 0; i < C_Guide.size(id_processus); i++)
				{
					E_Guide unGuide = C_Guide.getGuide(id_processus, C_Guide.getClef(id_processus, i));
					try
					{
						C_ElementPresentation.getElementPresentation(id_processus, unGuide.getElementPresentationId()).ajouterIdGuide(unGuide.getIdentifiant());
					}
					catch (Exception e)
					{
						//Ici, l'élément n' pas de guide				
					}
				}
			}

			//cBase.fermerConnexion();
		}
		catch (SQLException e)
		{}
	}
}