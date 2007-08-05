package hapi.application.importation;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_FTP;
import hapi.application.C_Local;
import hapi.application.C_NiveauMaturite;
import hapi.application.C_TransfertFichier;
import hapi.application.C_Utilisateur;
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
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.metier.temporaire.C_ElementPresentationTemporaire;
import hapi.application.metier.temporaire.C_EtatTemporaire;
import hapi.application.metier.temporaire.C_GuideTemporaire;
import hapi.application.metier.temporaire.C_InterfaceTemporaire;
import hapi.application.metier.temporaire.C_PaquetagePresentationTemporaire;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.metier.temporaire.C_ProduitTemporaire;
import hapi.application.metier.temporaire.C_RoleTemporaire;
import hapi.application.metier.temporaire.C_TypeGuideTemporaire;
import hapi.application.metier.temporaire.C_TypeProduitTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureRepresentation;
import hapi.donnees.E_NiveauMaturite;
import hapi.donnees.E_Utilisateur;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_ElementPresentation;
import hapi.donnees.metier.E_Etat;
import hapi.donnees.metier.E_Guide;
import hapi.donnees.metier.E_Interface;
import hapi.donnees.metier.E_PaquetagePresentation;
import hapi.donnees.metier.E_Processus;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.donnees.metier.E_TypeGuide;
import hapi.donnees.metier.E_TypeProduit;
import hapi.exception.E_ProcessusMisAJourException;
import hapi.exception.NoRowInsertedException;
import hapi.exception.ProcessusExistantException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

/**
 * Contrôleur de l'inporation des processus
 * 
 * @author Robin EYSSERIC
 */
public class C_ImportationProcessus extends C_TransfertFichier
{
	public static final String CHEMIN_SAUVEGARDE = "." + File.separator + "save" + File.separator;
	public static final String SUFFIXE_SAUVEGARDE = "SAVdpe";
	public static final String EXTENSION_SAUVEGARDE = ".sav";

	/**
	 * Constructeur préparant la configuration et les modèles
	 */
	public C_ImportationProcessus()
	{
		super();
		leModeleChemin.initialise(C_Utilisateur.getServeursDPE());
	}

	/**
	 * Récupération du login du serveur DPE à partir de son adresse
	 */
	public String getLogin(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurDPE(adresse).getLogin();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Récupération du mot de passe du serveur DPE à partir de son adresse
	 */
	public String getPWD(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurDPE(adresse).getMotDePasse();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Téléchargement du fichier sur le serveur
	 * 
	 * @return l'id du processus importé si le processus à été mis à jour, false
	 *         si le processus est nouveau.
	 */
	public ArrayList chargerFichier(String fichier, String chemin, String login, String passwd) throws Exception
	{
		ArrayList retour = new ArrayList();
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_serveur"));
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = ftp.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		ProcessusHandler handler = new ProcessusHandler();
		new C_Parser(fic, handler);
		fic = ftp.recupererFichier(fichier);
		SaveFichier(fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		try
		{
			transfereTemporaireDefinitif();
			//Ici, le processus est nouveau
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_enregistre_BD"));
			enregistrerProcessusBD(false, true);
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			retour.add(new Boolean(false));
			retour.add(C_ProcessusTemporaire.get().getIdentifiant());
		}
		catch (ProcessusExistantException e)
		{
			//Le processus existe déjà, on ne fait rien
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			retour.add(new Boolean(false));
			retour.add(null);
		}
		catch (E_ProcessusMisAJourException e)
		{
			//Le processus a été mis à jour, il faut mettre à jour la base de
			// données
			//Effacement de la base
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_mise_a_jour"));
			try
			{
				//Enregistrement du nouveau
				leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_enregistre_BD"));
				enregistrerProcessusBD(true, false);
				leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
				String idProcImporte = C_ProcessusTemporaire.get().getIdentifiant();
				//Effacement des entites temporaires
				effacerEntitesTemporaires();
				retour.add(new Boolean(true));
				retour.add(idProcImporte);
				return retour;
			}
			catch (Exception e1)
			{
				leModeleTelechargement.addDonnees(Bundle.getText("Erreur"));
			}
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
		}
		catch (Exception e)
		{
			leModeleTelechargement.addDonnees(Bundle.getText("Erreur"));
			C_Processus.supprimerProcessus(C_ProcessusTemporaire.get().getIdentifiant());
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			effacerEntitesTemporaires();
			throw e;
		}
		//Effacement des entites temporaires
		effacerEntitesTemporaires();
		return retour;
	}

	/**
	 * Récupération du fichier en local
	 * 
	 * @return l'id du processus importé si le processus à été mis à jour, false
	 *         si le processus est nouveau.
	 */
	public ArrayList chargerFichier(String fichier, String chemin) throws Exception
	{
		ArrayList retour = new ArrayList();
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_debut_transfert"));
		C_Local local = new C_Local();

		local.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = local.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier_recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fin_transfert"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		ProcessusHandler handler = new ProcessusHandler();
		new C_Parser(fic, handler);
		fic = local.recupererFichier(fichier);
		SaveFichier(fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		try
		{
			transfereTemporaireDefinitif();
			//Ici, le processus est nouveau
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_enregistre_BD"));
			enregistrerProcessusBD(false, true);
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			retour.add(new Boolean(false));
			retour.add(C_ProcessusTemporaire.get().getIdentifiant());
		}
		catch (ProcessusExistantException e)
		{
			//Le processus existe déjà, on ne fait rien
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			retour.add(new Boolean(false));
			retour.add(null);
		}
		catch (E_ProcessusMisAJourException e)
		{
			//Le processus a été mis à jour, il faut mettre à jour la base de données
			//Effacement de la base
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_mise_a_jour"));
			try
			{
				//Enregistrement du nouveau
				leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_processus_enregistre_BD"));
				enregistrerProcessusBD(true, false);
				leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
				String idProcImporte = C_ProcessusTemporaire.get().getIdentifiant();
				//Effacement des entites temporaires
				effacerEntitesTemporaires();
				retour.add(new Boolean(true));
				retour.add(idProcImporte);
				return retour;
			}
			catch (Exception e1)
			{
				leModeleTelechargement.addDonnees(Bundle.getText("Erreur"));
			}
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
		}
		catch (Exception e)
		{
			leModeleTelechargement.addDonnees(Bundle.getText("Erreur"));
			C_Processus.supprimerProcessus(C_ProcessusTemporaire.get().getIdentifiant());
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
			effacerEntitesTemporaires();
			throw e;
		}
		//Effacement des entites temporaires
		effacerEntitesTemporaires();
		return retour;
	}

	/**
	 * Enregistrement du processus en base de données
	 */
	public void enregistrerProcessusBD(boolean effaceBase, boolean ajouteNiveaux) throws Exception
	{
		//Enregistrement en base
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);

			if (effaceBase)
				effacerEntitesEnBase(cBase);

			ArrayList lesParametres = new ArrayList();

			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Processus"));
			// Enregistrement du processus
			E_Processus processus = C_ProcessusTemporaire.get();

			// récupération de l'identifiant du processus
			String id_processus = processus.getIdentifiant();
			// On récupère le numéro de l'utilisateur
			int idUtilisateur = E_Utilisateur.getIdentifiant();
			lesParametres.add(processus.getIdentifiant());
			lesParametres.add(processus.getNomSansVersion());
			lesParametres.add(processus.getNomAuteur());
			lesParametres.add(processus.getEmailAuteur());
			lesParametres.add(processus.getDescription());
			lesParametres.add(processus.getCheminGeneration());
			lesParametres.add(new Integer(processus.getNiveauMaturite()));
			lesParametres.add(new Integer(idUtilisateur));

			idStatement = cBase.creerPreparedStatement("setProcessus");
			cBase.executerRequeteStockee(idStatement, "setProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
			lesParametres.clear();

			//Enregistrement des versions du processus et
			//Creation d'une mesure de représentation arbitraire
			ArrayList lesVersions = processus.getListeDesVersion();
			for (int i = 0; i < lesVersions.size(); i++)
			{
				lesParametres.add(processus.getIdentifiant());
				lesParametres.add(((ArrayList) lesVersions.get(i)).get(0));
				lesParametres.add(((ArrayList) lesVersions.get(i)).get(1));
				try
				{
					idStatement = cBase.creerPreparedStatement("setVersionProcessus");
					cBase.executerRequeteStockee(idStatement, "setVersionProcessus", lesParametres);
					lesParametres.clear();
					//ATTENTION, en cas de modification des valeurs ci-dessous, penser à modifier le constructeur par défaut de	E_MesureRepresentation
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(((ArrayList) lesVersions.get(i)).get(0));
					ArrayList lesInterfaces = getInterfaces();
					lesParametres.add(lesInterfaces.get(0));
					lesParametres.add(lesInterfaces.get(1));
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Double(0));
					lesParametres.add("");
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Integer(C_DefinitionTemporaire.size()));
					lesParametres.add(new Integer(C_ActiviteTemporaire.size()));
					int nombreProduitsFournis = C_ProduitTemporaire.size() - (((Integer)lesInterfaces.get(0))).intValue(); 
					lesParametres.add(new Integer(nombreProduitsFournis));
					//lesParametres.add(new Integer(C_ProduitTemporaire.size()));
					lesParametres.add(new Integer(C_RoleTemporaire.size()));
					lesParametres.add(new Double(0));
					lesParametres.add("");
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Double(0));
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Double(0));
					lesParametres.add(new Integer(0));
					lesParametres.add("");
					lesParametres.add(new Double(0));
					lesParametres.add(new Double(0));
					lesParametres.add("");
					idStatement = cBase.creerPreparedStatement("setNouveauMesuresRepresentation");
					cBase.executerRequeteStockee(idStatement, "setNouveauMesuresRepresentation", lesParametres);					
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
				lesParametres.clear();
			}

			// Enregistrement des composants du processus
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Composant"));
			Collection lesComposants = C_ComposantTemporaire.getComposants().values();
			Iterator itComp = lesComposants.iterator();
			E_Composant composant;
			while (itComp.hasNext())
			{
				composant = (E_Composant) itComp.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(composant.getIdentifiant());
					lesParametres.add(composant.getNom());
					lesParametres.add(composant.getVersion());
					lesParametres.add(composant.getNomAuteur());
					lesParametres.add(composant.getEmailAuteur());
					lesParametres.add(composant.getDatePlacement());
					lesParametres.add(composant.getDescription());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(composant.getInterfaceFournie());
					lesParametres.add(composant.getInterfaceRequise());
					lesParametres.add(composant.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setComposants");
					cBase.executerRequeteStockee(idStatement, "setComposants", lesParametres);
					lesParametres.clear();
					//}
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des interfaces
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Interface"));
			Collection lesInterfaces = C_InterfaceTemporaire.getInterfaces().values();
			Iterator itInterf = lesInterfaces.iterator();
			E_Interface interf;
			while (itInterf.hasNext())
			{
				interf = (E_Interface) itInterf.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(interf.getIdentifiant());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(interf.getInterfaceFournieComposant());
					lesParametres.add(interf.getInterfaceRequiseComposant());

					idStatement = cBase.creerPreparedStatement("setInterfaces");
					cBase.executerRequeteStockee(idStatement, "setInterfaces", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des produits
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Produit"));
			Collection lesProduits = C_ProduitTemporaire.getProduits().values();
			Iterator itProd = lesProduits.iterator();
			E_Produit produit;
			while (itProd.hasNext())
			{
				produit = (E_Produit) itProd.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(produit.getIdentifiant());
					lesParametres.add(produit.getNom());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(produit.getAgregatComposant());
					lesParametres.add(produit.getIdResponsabilite());
					lesParametres.add(produit.getTypeProduitId());
					lesParametres.add(produit.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setProduits");
					cBase.executerRequeteStockee(idStatement, "setProduits", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des types de produits
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_TypeProduit"));
			Collection lesTypesProduit = C_TypeProduitTemporaire.getTypesProduit().values();
			Iterator itTypeProd = lesTypesProduit.iterator();
			E_TypeProduit typeProduit;
			while (itTypeProd.hasNext())
			{
				typeProduit = (E_TypeProduit) itTypeProd.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(typeProduit.getIdentifiant());
					lesParametres.add(typeProduit.getNom());
					lesParametres.add(processus.getIdentifiant());

					idStatement = cBase.creerPreparedStatement("setTypesProduit");
					cBase.executerRequeteStockee(idStatement, "setTypesProduit", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des liens entre produits et interfaces
			itProd = lesProduits.iterator();
			ArrayList lesInterfacesDuProduit;
			while (itProd.hasNext())
			{
				produit = (E_Produit) itProd.next();
				lesInterfacesDuProduit = produit.getInterfaces();
				itInterf = lesInterfacesDuProduit.iterator();
				while (itInterf.hasNext())
				{
					lesParametres.clear();
					lesParametres.add(produit.getIdentifiant());
					lesParametres.add((String) itInterf.next());
					lesParametres.add(processus.getIdentifiant());
					try
					{
						idStatement = cBase.creerPreparedStatement("setLiensProduitInterface");
						cBase.executerRequeteStockee(idStatement, "setLiensProduitInterface", lesParametres);
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
					lesParametres.clear();
				}
			}
			itInterf = lesInterfaces.iterator();
			ArrayList lesProduitsDeLInterface;
			while (itInterf.hasNext())
			{
				interf = (E_Interface) itInterf.next();
				lesProduitsDeLInterface = interf.getInterfaceProduit();
				itProd = lesProduitsDeLInterface.iterator();
				while (itProd.hasNext())
				{
					lesParametres.clear();
					lesParametres.add((String) itProd.next());
					lesParametres.add(interf.getIdentifiant());
					lesParametres.add(processus.getIdentifiant());
					try
					{
						idStatement = cBase.creerPreparedStatement("setLiensProduitInterface");
						cBase.executerRequeteStockee(idStatement, "setLiensProduitInterface", lesParametres);
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
					lesParametres.clear();
				}
			}

			// Enregistrement des roles
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Role"));
			Collection lesRoles = C_RoleTemporaire.getRoles().values();
			Iterator itRoles = lesRoles.iterator();
			E_Role role;
			while (itRoles.hasNext())
			{
				role = (E_Role) itRoles.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(role.getIdentifiant());
					lesParametres.add(role.getNom());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(role.getAgregatComposant());
					lesParametres.add(role.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setRoles");
					cBase.executerRequeteStockee(idStatement, "setRoles", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des definitions
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Definition"));
			Collection lesDefinitions = C_DefinitionTemporaire.getDefinitions().values();
			Iterator itDef = lesDefinitions.iterator();
			E_Definition definition;
			while (itDef.hasNext())
			{
				definition = (E_Definition) itDef.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(definition.getIdentifiant());
					lesParametres.add(definition.getNom());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(definition.getAgregatComposant());
					lesParametres.add(definition.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setDefinitions");
					cBase.executerRequeteStockee(idStatement, "setDefinitions", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des activités
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Activite"));
			Collection lesActivites = C_ActiviteTemporaire.getActivites().values();
			Iterator itAct = lesActivites.iterator();
			E_Activite activite;
			while (itAct.hasNext())
			{
				activite = (E_Activite) itAct.next();

				lesParametres.clear();
				lesParametres.add(activite.getIdentifiant());
				lesParametres.add(activite.getNom());
				lesParametres.add(processus.getIdentifiant());
				lesParametres.add(activite.getAgregatDefinitionTravail());
				lesParametres.add(activite.getParticipationRole());
				lesParametres.add(activite.getElementPresentationId());

				idStatement = cBase.creerPreparedStatement("setActivites");
				cBase.executerRequeteStockee(idStatement, "setActivites", lesParametres);
				cBase.fermerStatement(idStatement);
				lesParametres.clear();

				//enregistrement des liens entre l'activité et ses produits
				// en entrée
				Iterator itProdEntree = activite.getProduitsEntree().iterator();
				while (itProdEntree.hasNext())
				{
					lesParametres.clear();
					lesParametres.add((String) itProdEntree.next());
					lesParametres.add(activite.getIdentifiant());
					lesParametres.add(processus.getIdentifiant());
					try
					{
						idStatement = cBase.creerPreparedStatement("setLiensProdActEntree");
						cBase.executerRequeteStockee(idStatement, "setLiensProdActEntree", lesParametres);
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
					lesParametres.clear();
				}
				lesParametres.clear();
				//enregistrement des liens entre l'activité et ses produits
				// en entrée
				Iterator itProdSortie = activite.getProduitsSortie().iterator();
				while (itProdSortie.hasNext())
				{
					lesParametres.clear();
					lesParametres.add((String) itProdSortie.next());
					lesParametres.add(activite.getIdentifiant());
					lesParametres.add(processus.getIdentifiant());
					try
					{
						idStatement = cBase.creerPreparedStatement("setLiensProdActSortie");
						cBase.executerRequeteStockee(idStatement, "setLiensProdActSortie", lesParametres);
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
					lesParametres.clear();
				}
			}

			// Enregistrement des etats
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Etat"));
			Collection lesEtats = C_EtatTemporaire.getEtats().values();
			Iterator itEtat = lesEtats.iterator();
			E_Etat etat;
			while (itEtat.hasNext())
			{
				etat = (E_Etat) itEtat.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(etat.getIdentifiant());
					lesParametres.add(etat.getNom());
					lesParametres.add(processus.getIdentifiant());
					idStatement = cBase.creerPreparedStatement("setEtats");
					cBase.executerRequeteStockee(idStatement, "setEtats", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des liens entre produits et etats
			itProd = lesProduits.iterator();
			ArrayList lesEtatsDuProduit;
			while (itProd.hasNext())
			{
				produit = (E_Produit) itProd.next();
				lesEtatsDuProduit = produit.getEtats();
				itEtat = lesEtatsDuProduit.iterator();
				while (itEtat.hasNext())
				{
					lesParametres.clear();
					lesParametres.add(produit.getIdentifiant());
					lesParametres.add((String) itEtat.next());
					lesParametres.add(processus.getIdentifiant());
					try
					{
						idStatement = cBase.creerPreparedStatement("setLiensProduitEtat");
						cBase.executerRequeteStockee(idStatement, "setLiensProduitEtat", lesParametres);
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
					lesParametres.clear();
				}
			}

			// Enregistrement des types de guide
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_TypeGuide"));
			Collection lesTypesGuide = C_TypeGuideTemporaire.getTypesGuide().values();
			Iterator itTypeGuide = lesTypesGuide.iterator();
			E_TypeGuide typeGuide;
			while (itTypeGuide.hasNext())
			{
				typeGuide = (E_TypeGuide) itTypeGuide.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(typeGuide.getIdentifiant());
					lesParametres.add(typeGuide.getNom());
					lesParametres.add(processus.getIdentifiant());

					idStatement = cBase.creerPreparedStatement("setTypesGuide");
					cBase.executerRequeteStockee(idStatement, "setTypesGuide", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des guides
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Guide"));
			Collection lesGuides = C_GuideTemporaire.getGuides().values();
			Iterator itGuide = lesGuides.iterator();
			E_Guide guide;
			while (itGuide.hasNext())
			{
				guide = (E_Guide) itGuide.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(guide.getIdentifiant());
					lesParametres.add(guide.getNom());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(guide.getTypeGuideId());
					lesParametres.add(guide.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setGuide");
					cBase.executerRequeteStockee(idStatement, "setGuide", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			// Enregistrement des elements de presentation
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Element"));
			Collection lesElemDePresent = C_ElementPresentationTemporaire.getElementsPresentation().values();
			Iterator itElemDePresent = lesElemDePresent.iterator();
			E_ElementPresentation elemDePresent;
			while (itElemDePresent.hasNext())
			{
				elemDePresent = (E_ElementPresentation) itElemDePresent.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(elemDePresent.getIdentifiant());
					lesParametres.add(elemDePresent.getNom());
					lesParametres.add(elemDePresent.getCheminIcone());
					lesParametres.add(elemDePresent.getCheminContenu());
					lesParametres.add(elemDePresent.getDescription());
					lesParametres.add(elemDePresent.getCheminPage());
					lesParametres.add(processus.getIdentifiant());

					idStatement = cBase.creerPreparedStatement("setElementsPresentation");
					cBase.executerRequeteStockee(idStatement, "setElementsPresentation", lesParametres);
					lesParametres.clear();
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}
			// Enregistrement des paquetages de presentation
			leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_Paquetage"));
			Collection lesPaquetages = C_PaquetagePresentationTemporaire.getPaquetagesPresentation().values();
			Iterator itPaq = lesPaquetages.iterator();
			E_PaquetagePresentation paquetage;
			ArrayList lesElementsDuPaquetage = new ArrayList();
			while (itPaq.hasNext())
			{
				paquetage = (E_PaquetagePresentation) itPaq.next();
				try
				{
					lesParametres.clear();
					lesParametres.add(paquetage.getIdentifiant());
					lesParametres.add(paquetage.getNom());
					lesParametres.add(paquetage.getDossierIcone());
					lesParametres.add(paquetage.getDossierContenu());
					lesParametres.add(processus.getIdentifiant());
					lesParametres.add(paquetage.getElementPresentationId());

					idStatement = cBase.creerPreparedStatement("setPaquetagesPresentation");
					cBase.executerRequeteStockee(idStatement, "setPaquetagesPresentation", lesParametres);
					cBase.fermerStatement(idStatement);
					lesParametres.clear();

					// On met à jour chaque element du paquetage
					lesElementsDuPaquetage = paquetage.getAgregeElementPresentation();
					itElemDePresent = lesElementsDuPaquetage.iterator();
					while (itElemDePresent.hasNext())
					{
						try
						{
							lesParametres.add(paquetage.getIdentifiant());
							lesParametres.add((String) itElemDePresent.next());
							lesParametres.add(processus.getIdentifiant());
							idStatement = cBase.creerPreparedStatement("setElementIdPaquetage");
							cBase.executerRequeteStockee(idStatement, "setElementIdPaquetage", lesParametres);
							cBase.fermerStatement(idStatement);
							lesParametres.clear();
						}
						catch (NoRowInsertedException e)
						{}

					}
				}
				finally
				{
					cBase.fermerStatement(idStatement);
				}
			}

			if (ajouteNiveaux)
			{
				//Tout nouveau processus doit avoir sa liste de niveaux de
				// maturité prête
				ArrayList lesNiveauxProcessus = new ArrayList();
				for (int i = 1; i < 6; i++)
				{
					try
					{
						lesParametres.clear();
						lesParametres.add(id_processus);
						lesParametres.add(new Integer(i));
						lesParametres.add("");
						Calendar date = Calendar.getInstance();
						if (i == 1)
							lesParametres.add(new Date(date.getTimeInMillis()));
						else
							lesParametres.add(null);

						idStatement = cBase.creerPreparedStatement("setNiveauMaturiteVide");
						cBase.executerRequeteStockee(idStatement, "setNiveauMaturiteVide", lesParametres);
						cBase.fermerStatement(idStatement);

						lesNiveauxProcessus.add(i-1, new E_NiveauMaturite(i, "", (i == 1) ? new java.sql.Date(date.getTimeInMillis()) : null, null));
					}
					finally
					{
						cBase.fermerStatement(idStatement);
					}
				}
				C_NiveauMaturite.ajouterNiveauMaturite(id_processus, lesNiveauxProcessus);
			}

			cBase.commit();
		}
		catch (Exception e)
		{
			// On annule la transaction
			cBase.rollback();
			throw e;
		}
		finally
		{
			cBase.fermerStatement(idStatement);
			cBase.setAutoCommit(true);
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}

	/**
	 * Ajout d'un serveur de fichier DPE
	 */
	public void addChemin(String chemin)
	{
		C_Utilisateur.addServeurDPE(chemin, "", "");
		leModeleChemin.addElement(chemin);
	}

	/**
	 * Sauvegarde du serveur sélectionné
	 */
	public void sauvegarderSelectionChemin(String lAdresse)
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add("DPE");
			lesParametres.add(lAdresse);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

			idStatement = cBase.creerPreparedStatement("setServeurSelection");
			cBase.executerRequeteStockee(idStatement, "setServeurSelection", lesParametres);
			cBase.fermerStatement(idStatement);

			idStatement = cBase.creerPreparedStatement("setServeurDeselection");
			cBase.executerRequeteStockee(idStatement, "setServeurDeselection", lesParametres);
			
			C_Utilisateur.setSelectedDPE(lAdresse);
		}
		catch (Exception e)
		{
			//Rien d'interresant à faire ici
		}
		finally
		{
			try
			{
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{
				// Rien à faire
			}

		}
	}

	/**
	 * Procédure comparant les processus existant au temporaire Lève
	 * ProcessusExistantException si le processus existe avec la même version
	 * Lève E_ProcessusMisAJourException si le processus existe avec une version
	 * différente Rien ne se passe si c'est un nouveau processus
	 */
	private void transfereTemporaireDefinitif() throws ProcessusExistantException, E_ProcessusMisAJourException
	{
		E_Processus processusTemporaire = C_ProcessusTemporaire.get();
		String id_processusTemporaire = processusTemporaire.getIdentifiant();
		
		//Si le processus est nouveau
		if (C_Processus.getProcessus(id_processusTemporaire) == null)
		{
			C_Processus.addProcessus(C_ProcessusTemporaire.get());
			ajouterNouveauProcessus();
		}
		else
		{
			//Si la version n'existe pas
			if (!C_Processus.getProcessus(id_processusTemporaire).isExportExiste(processusTemporaire.getDateExport()))
			{
				//Mémorisation du niveau de maturité
				processusTemporaire.setNiveauMaturite(C_Processus.getProcessus(id_processusTemporaire).getNiveauMaturite());				
				// Mise à jour du processus
				C_Processus.getProcessus(id_processusTemporaire).update(processusTemporaire);
				//Effacement du contenu des contrôleurs associés aux processus
				effacerEntites();
				ajouterNouveauProcessus();
				throw new E_ProcessusMisAJourException();
			} //sinon, le processus existe déjà pour la même version
			else
				throw new ProcessusExistantException();

		}
	}

	private void ajouterNouveauProcessus()
	{
		//Récupération de l'identifiant
		String id_processus = C_ProcessusTemporaire.get().getIdentifiant();
		//Copie de chaque entités
		C_Activite.ajouterHashMap(id_processus, C_ActiviteTemporaire.getActivites());
		C_Composant.ajouterHashMap(id_processus, C_ComposantTemporaire.getComposants());
		C_Definition.ajouterHashMap(id_processus, C_DefinitionTemporaire.getDefinitions());
		C_ElementPresentation.ajouterHashMap(id_processus, C_ElementPresentationTemporaire.getElementsPresentation());
		C_Etat.ajouterHashMap(id_processus, C_EtatTemporaire.getEtats());
		C_Guide.ajouterHashMap(id_processus, C_GuideTemporaire.getGuides());
		C_Interface.ajouterHashMap(id_processus, C_InterfaceTemporaire.getInterfaces());
		C_PaquetagePresentation.ajouterHashMap(id_processus, C_PaquetagePresentationTemporaire.getPaquetagesPresentation());
		C_Produit.ajouterHashMap(id_processus, C_ProduitTemporaire.getProduits());
		C_Role.ajouterHashMap(id_processus, C_RoleTemporaire.getRoles());
		C_TypeGuide.ajouterHashMap(id_processus, C_TypeGuideTemporaire.getTypesGuide());
		C_TypeProduit.ajouterHashMap(id_processus, C_TypeProduitTemporaire.getTypesProduit());
		//Création de la mesure de représentation
		ArrayList lesInterfaces = getInterfaces();
		E_MesureRepresentation uneMesure = new E_MesureRepresentation(id_processus,C_Processus.getProcessus(id_processus).getDateExport(),((Integer)lesInterfaces.get(0)).intValue(),((Integer)lesInterfaces.get(1)).intValue(),C_DefinitionTemporaire.size(), C_ActiviteTemporaire.size(), C_ProduitTemporaire.size() - (((Integer)lesInterfaces.get(0))).intValue(), C_RoleTemporaire.size());
		C_Processus.getProcessus(id_processus).ajouterMesureRepresentation(uneMesure);
	}

	private void effacerEntites()
	{
		String id_processusTemporaire = C_ProcessusTemporaire.get().getIdentifiant();

		C_Activite.supprimerActivites(id_processusTemporaire);
		C_Composant.supprimerComposants(id_processusTemporaire);
		C_Definition.supprimerDefinitions(id_processusTemporaire);
		C_ElementPresentation.supprimerElementsPresentation(id_processusTemporaire);
		C_Etat.supprimerEtats(id_processusTemporaire);
		C_Guide.supprimerGuides(id_processusTemporaire);
		C_Interface.supprimerInterfaces(id_processusTemporaire);
		C_PaquetagePresentation.supprimerPaquetagesPresentation(id_processusTemporaire);
		C_Produit.supprimerProduits(id_processusTemporaire);
		C_Role.supprimerRoles(id_processusTemporaire);
		C_TypeGuide.supprimerTypesGuide(id_processusTemporaire);
		C_TypeProduit.supprimerTypesProduits(id_processusTemporaire);
	}

	public static void effacerEntitesTemporaires()
	{
		//Effacement de chaque entites temporaires
		C_ProcessusTemporaire.setProcessus(null);
		C_ActiviteTemporaire.effacerListe();
		C_ComposantTemporaire.effacerListe();
		C_DefinitionTemporaire.effacerListe();
		C_ElementPresentationTemporaire.effacerListe();
		C_EtatTemporaire.effacerListe();
		C_GuideTemporaire.effacerListe();
		C_InterfaceTemporaire.effacerListe();
		C_PaquetagePresentationTemporaire.effacerListe();
		C_ProduitTemporaire.effacerListe();
		C_RoleTemporaire.effacerListe();
		C_TypeGuideTemporaire.effacerListe();
		C_TypeProduitTemporaire.effacerListe();
	}

	private void effacerEntitesEnBase(C_AccesBaseDonnees cBase) throws Exception
	{
		int idStatement = 0;
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(C_ProcessusTemporaire.get().getIdentifiant());

		try
		{
			idStatement = cBase.creerPreparedStatement("delProcessus");
			cBase.executerRequeteStockee(idStatement, "delProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delComposants");
			cBase.executerRequeteStockee(idStatement, "delComposants", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delEtats");
			cBase.executerRequeteStockee(idStatement, "delEtats", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delPaquetagesPresentation");
			cBase.executerRequeteStockee(idStatement, "delPaquetagesPresentation", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delElementsPresentation");
			cBase.executerRequeteStockee(idStatement, "delElementsPresentation", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delGuides");
			cBase.executerRequeteStockee(idStatement, "delGuides", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delProduits");
			cBase.executerRequeteStockee(idStatement, "delProduits", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delRoles");
			cBase.executerRequeteStockee(idStatement, "delRoles", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delDefinitions");
			cBase.executerRequeteStockee(idStatement, "delDefinitions", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delActivites");
			cBase.executerRequeteStockee(idStatement, "delActivites", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delInterfaces");
			cBase.executerRequeteStockee(idStatement, "delInterfaces", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delTypesProduit");
			cBase.executerRequeteStockee(idStatement, "delTypesProduit", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delTypesGuide");
			cBase.executerRequeteStockee(idStatement, "delTypesGuide", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProdActEntree");
			cBase.executerRequeteStockee(idStatement, "delLiensProdActEntree", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProdActSortie");
			cBase.executerRequeteStockee(idStatement, "delLiensProdActSortie", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProduitEtat");
			cBase.executerRequeteStockee(idStatement, "delLiensProduitEtat", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}

		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProduitInterface");
			cBase.executerRequeteStockee(idStatement, "delLiensProduitInterface", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
	}

	/**
	 * Téléchargement du fichier sur le serveur et chargement en entité
	 * temporaires
	 * 
	 * @return l'id du processus importé si le processus à été mis à jour, false
	 *         si le processus est nouveau.
	 */
	public void memoriserFichier(String fichier, String chemin, String login, String passwd) throws Exception
	{
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_serveur"));
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = ftp.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		ProcessusHandler handler = new ProcessusHandler();
		new C_Parser(fic, handler);
		fic = ftp.recupererFichier(fichier);
		SaveFichier(fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
	}

	/**
	 * Récupération du fichier en local et chargement en entité temporaires
	 * 
	 * @return l'id du processus importé si le processus à été mis à jour, false
	 *         si le processus est nouveau.
	 */
	public void memoriserFichier(String fichier, String chemin) throws Exception
	{
		// effacement des fichiers temporaires
		effacerEntitesTemporaires();
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_debut_transfert"));
		C_Local local = new C_Local();

		local.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_recuperation_fichier"));
		InputStream fic = local.recupererFichier(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fichier_recupere"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_fin_transfert"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier"));
		ProcessusHandler handler = new ProcessusHandler();
		new C_Parser(fic, handler);
		fic = local.recupererFichier(fichier);
		SaveFichier(fic);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_parsage_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Importation_deconnexion"));
	}

	public void SaveFichier(InputStream source)
	{
		String idProcessus = C_ProcessusTemporaire.get().getIdentifiant();
		String dateExport = C_ProcessusTemporaire.get().getDateExport();
		String nomFichier = idProcessus + dateExport + SUFFIXE_SAUVEGARDE + EXTENSION_SAUVEGARDE;

		copier(source, CHEMIN_SAUVEGARDE, nomFichier);
	}

	private static boolean copier(InputStream source, String cheminDestination, String destination)
	{
		boolean resultat = false;

		// Declaration des flux
		File dest = new File(cheminDestination);

		FileOutputStream destinationFile = null;

		try
		{
			//Création du répertoire
			dest.mkdirs();
			//Création du fichier
			dest = new File(cheminDestination + destination);
			dest.createNewFile();

			// Ouverture des flux
			destinationFile = new FileOutputStream(dest);

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			//source.reset();
			int nbLecture;

			while ((nbLecture = source.read(buffer)) != -1)
			{
				destinationFile.write(buffer, 0, nbLecture);
			}

			// Copie réussie
			resultat = true;
			source.reset();
		}
		catch (java.io.FileNotFoundException f)
		{}
		catch (java.io.IOException e)
		{}
		finally
		{
			// Quoi qu'il arrive, on ferme les flux
			try
			{
				destinationFile.close();
			}
			catch (Exception e)
			{}
		}
		return (resultat);
	}

	private static ArrayList getInterfaces()
	{
		ArrayList retour = new ArrayList();
		//Calcul du nombre d'interfaces
		Collection lesInterfaces = null;
		//Récupération des interfaces
		lesInterfaces = C_InterfaceTemporaire.getInterfaces().values();
		//Recherche des interfaces pour chaque composant
		int valReq = 0;
		int valFor = 0;
		for (Iterator it = lesInterfaces.iterator(); it.hasNext();)
		{
			E_Interface uneInterface = (E_Interface) it.next();
				
			if (!uneInterface.getInterfaceFournieComposant().equals(""))
				valFor+=uneInterface.getInterfaceProduit().size();
			if (!uneInterface.getInterfaceRequiseComposant().equals(""))
				valReq+=uneInterface.getInterfaceProduit().size();
		}

		retour.add(new Integer(valReq));
		retour.add(new Integer(valFor));

		return retour;
	}
}
