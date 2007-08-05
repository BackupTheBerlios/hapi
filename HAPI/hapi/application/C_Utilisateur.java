/*
 * Auteur Cédric
 *
 */
package hapi.application;

import hapi.application.indicateurs.C_ChargerEvaluation;
import hapi.application.metier.C_Processus;
import hapi.donnees.E_Indicateur;
import hapi.donnees.E_Serveur;
import hapi.donnees.E_Seuil;
import hapi.donnees.E_Utilisateur;
import hapi.donnees.metier.E_Processus;
import hapi.exception.UtilisateurNonIdentifieException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contrôleur de l'utilisateur connecté
 * Static car il n'y en a qu'un à la fois
 */
public class C_Utilisateur
{
	//Indicateur de l'état d'identification
	static private boolean utilisateurIdentifie = false;

	/**
	 * Récupération du rôle de l'utilisateur
	 * @return 1 : Responsable 2 : Directeur 3 : Ingénieur
	 * @throws UtilisateurNonIdentifieException
	 */
	public static int getRole() throws UtilisateurNonIdentifieException
	{
		if (!utilisateurIdentifie)
			throw new UtilisateurNonIdentifieException();
		return E_Utilisateur.getRole();
	}

	/**
	 * Récupération de l'identificant de l'utilisateur
	 */
	public static int getIdentifiant()
	{
		return E_Utilisateur.getIdentifiant();
	}

	/**
	 * Récupération du nom de l'utilisateur
	 */
	public static String getNom()
	{
		return E_Utilisateur.getNom();
	}

	/**
	 * Récupération du prénom de l'utilisateur
	 */
	public static String getPrenom()
	{
		return E_Utilisateur.getPrenom();
	}

	/**
	 * Récupération du login de l'utilisateur
	 */
	public static String getLogin()
	{
		return E_Utilisateur.getLogin();
	}

	/**
	 * Construction de l'utilisateur lors de son identification
	 */
	public static void indentifie(String login, String nom, String prenom, int identifiant, int role, final C_AccesBaseDonnees cBase)
	{
		//Préparation des entités
		E_Utilisateur.setLogin(login);
		E_Utilisateur.setNom(nom);
		E_Utilisateur.setPrenom(prenom);
		E_Utilisateur.setIdentifiant(identifiant);
		E_Utilisateur.setRole(role);
		utilisateurIdentifie = true;
		//Recherche des serveurs
		Thread lesServeurs = new Thread(new Runnable()
		{
			public void run()
			{
				int idStatement = 0;
				//C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

				try
				{
					//cBase.ouvrirConnexion();
					ArrayList lesParametres = new ArrayList();
					lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
					idStatement = cBase.creerPreparedStatement("getServeursUtilisateurs");
					ResultSet resultat = cBase.executerRequeteStockee(idStatement, "getServeursUtilisateurs", lesParametres);
					while (resultat.next())
					{
						E_Serveur unServeur = new E_Serveur(resultat.getString(1), resultat.getString(2), resultat.getString(3));
						unServeur.setSelected(resultat.getString(4).equals("1"));

						if (resultat.getString(5).equals("DPE"))
							E_Utilisateur.addServeursDPE(unServeur);
						else if (resultat.getString(5).equals("DOM"))
							E_Utilisateur.addServeursDOM(unServeur);
						else if (resultat.getString(5).equals("MES"))
							E_Utilisateur.addServeursMES(unServeur);
						else if (resultat.getString(5).equals("HTM"))
							E_Utilisateur.addServeursHTML(unServeur);
						else if (resultat.getString(5).equals("CSS"))
							E_Utilisateur.addServeursCSS(unServeur);						
					}
					cBase.fermerStatement(idStatement);

					// récupération des indicateurs
					idStatement = cBase.creerPreparedStatement("getIndicateurs");
					resultat = cBase.executerRequeteStockee(idStatement, "getIndicateurs");
					while (resultat.next())
					{
						E_Indicateur unIndicateur = new E_Indicateur();
						unIndicateur.setId(resultat.getInt(1));
						unIndicateur.setNom(resultat.getString(2));
						unIndicateur.setFormule(resultat.getString(3));
						C_Indicateur.ajouterIndicateur(unIndicateur);
					}
					cBase.fermerStatement(idStatement);

					idStatement = cBase.creerPreparedStatement("getSeuils");
					resultat = cBase.executerRequeteStockee(idStatement, "getSeuils", lesParametres);
					while (resultat.next())
					{
						E_Seuil unSeuil = new E_Seuil();
						unSeuil.setId(resultat.getInt(1));
						unSeuil.setId_processus(resultat.getString(2));
						unSeuil.setId_indicateur(resultat.getInt(3));
						unSeuil.setMin(new Double(resultat.getDouble(5)));
						unSeuil.setMax(new Double(resultat.getDouble(6)));

						C_GestionSeuils.ajouterSeuil(unSeuil.getId_processus(), unSeuil);
					}

					cBase.fermerStatement(idStatement);
					//cBase.fermerConnexion();
				}
				catch (SQLException e)
				{}
			}
		});
		lesServeurs.setName("ChargementServeurs");
		lesServeurs.start();

		//Recherche des processus dont il est responsable
		RechercherProcessus(cBase);

		try
		{
			lesServeurs.join();
		}
		catch (InterruptedException e)
		{}
	}

	/**
	 * Modification de son nom et prénom
	 */
	public static void setNomPrenom(String nom, String prenom)
	{
		E_Utilisateur.setNom(nom);
		E_Utilisateur.setPrenom(prenom);
	}

	/**
	 * Ajout d'un serveur de fichiers DPE
	 */
	public static void addServeurDPE(String adresse, String login, String motDePasse)
	{
		//Création d'un serveur
		E_Serveur leServeur = new E_Serveur(adresse, login, motDePasse);
		//Si le serveur n'existe pas déjà
		if (E_Utilisateur.getServeursDPE().indexOf(leServeur) < 0)
		{
			//Ajout du serveur
			E_Utilisateur.addServeursDPE(leServeur);
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

			try
			{
				//Ajour du serveur en base				
				cBase.ouvrirConnexion();
				ArrayList lesParametres = new ArrayList();
				lesParametres.add("DPE");
				lesParametres.add(adresse);
				lesParametres.add(login);
				lesParametres.add(motDePasse);
				lesParametres.add("0");
				lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
				idStatement = cBase.creerPreparedStatement("setServeurs");
				cBase.executerRequeteStockee(idStatement, "setServeurs", lesParametres);
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}

	/**
	 * Récupération des serveurs de fichiers DPE
	 */
	public static ArrayList getServeursDPE()
	{
		return E_Utilisateur.getServeursDPE();
	}

	/**
	 * Recherche de l'entité serveur à partir de son adresse
	 */
	public static E_Serveur findServeurDPE(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursDPE();
		boolean trouve = false;
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size() && !trouve)
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				trouve = true;
		}
		if (trouve)
			return (E_Serveur) lesServeurs.get(--i);
		else
			return null;
	}

	/**
	 * Modification du login et du mot de passe
	 */
	public static void setLoginMotDePasseDPE(String adresse, String login, String motDePasse) throws SQLException
	{
		E_Serveur unServeur = findServeurDPE(adresse);
		unServeur.setLogin(login);
		unServeur.setMotDePasse(motDePasse);
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Ajour du login et du mot de passe en base			
		cBase.ouvrirConnexion();
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);
		lesParametres.add(motDePasse);
		lesParametres.add("DPE");
		lesParametres.add(adresse);
		lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
		idStatement = cBase.creerPreparedStatement("setLoginPasswordServeur");
		cBase.executerRequeteStockee(idStatement, "setLoginPasswordServeur", lesParametres);
		cBase.fermerStatement(idStatement);
		cBase.fermerConnexion();
	}
	
	public static void setSelectedDPE(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursDPE();
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size())
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				((E_Serveur) lesServeurs.get(i)).setSelected(true);
			else
				((E_Serveur) lesServeurs.get(i)).setSelected(false);
		}		
	}
	
	/**
	 * Recherche des descriptions de processus
	 */
	private static void RechercherDescriptionMaturite(C_AccesBaseDonnees cBase)
	{
		int idStatement = 0;
		HashMap lesNiveaux = new HashMap();

		try
		{
			idStatement = cBase.creerPreparedStatement("getDescriptionMaturite");
			ResultSet leResultat = cBase.executerRequeteStockee(idStatement, "getDescriptionMaturite");
			
			while (leResultat.next())
			{
				lesNiveaux.put(new Integer(leResultat.getInt(1)-1),leResultat.getString(2));
			}
			
			cBase.fermerStatement(idStatement);
		}
		catch (SQLException e)
		{}		
		
		C_DescriptionMaturite.setLesDescriptions(lesNiveaux);
	}

	/**
	 * Recherche des processus dont l'utilisateur est responsable
	 */
	private static void RechercherProcessus(C_AccesBaseDonnees cBase)
	{
		int idStatement = 0;

		try
		{
			ArrayList lesParametres = new ArrayList();
			ResultSet leResultat = null;
			switch (E_Utilisateur.getRole())
			{
				case 1 : //Responsable processus
					lesParametres.clear();
					//Recherche des processus
					lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
					idStatement = cBase.creerPreparedStatement("getProcessus");
					leResultat = cBase.executerRequeteStockee(idStatement, "getProcessus", lesParametres);
					break;
				default : //Directeur et ingénieur processus
					//Recherche des processus
					idStatement = cBase.creerPreparedStatement("getAllProcessus");
					leResultat = cBase.executerRequeteStockee(idStatement, "getAllProcessus");
					break;
			}

			//Remplissage des processus
			while (leResultat.next())
			{
				E_Processus unProcessus = new E_Processus(C_Hapi.PROCESSUS_ICONE);
				unProcessus.setId(leResultat.getString(1));
				unProcessus.setNom(leResultat.getString(2));
				unProcessus.setNomAuteur(leResultat.getString(3));
				unProcessus.setEmailAuteur(leResultat.getString(4));
				unProcessus.setDescription(leResultat.getString(5));
				unProcessus.setCheminGeneration(leResultat.getString(6));
				unProcessus.setNiveauMaturite(leResultat.getInt(7));
				C_Processus.addProcessus(unProcessus);
			}
			cBase.fermerStatement(idStatement);

			//Remplissage des versions du processus
			for (int i = 0; i < C_Processus.size(); i++)
			{
				lesParametres.clear();
				lesParametres.add(C_Processus.get(i).getIdentifiant());
				idStatement = cBase.creerPreparedStatement("getVersionProcessus");
				leResultat = cBase.executerRequeteStockee(idStatement, "getVersionProcessus", lesParametres);
				while (leResultat.next())
				{
					C_Processus.get(i).addDateExport(leResultat.getString(1),leResultat.getString(2));
				}
				cBase.fermerStatement(idStatement);

				lesParametres.clear();
				lesParametres.add(C_Processus.get(i).getIdentifiant());
				idStatement = cBase.creerPreparedStatement("getNomResponsable");
				leResultat = cBase.executerRequeteStockee(idStatement, "getNomResponsable", lesParametres);
				while (leResultat.next())
				{
					C_Processus.get(i).setNomResponsable(leResultat.getString(1));
					C_Processus.get(i).setPrenomResponsable(leResultat.getString(2));
				}
				cBase.fermerStatement(idStatement);
			}
			
			//Recherche des descriptions des niveaux de maturité
			RechercherDescriptionMaturite(cBase);

			//cBase.fermerConnexion();
			Thread chargementMesuresRepresentation = new Thread(new C_ChargerMesuresRepresentation(cBase));
			chargementMesuresRepresentation.setName("chargementMesuresRepresentation");
			chargementMesuresRepresentation.start();
			Thread chargementNiveaux = new Thread(new C_ChargerNiveauxMaturite(cBase));
			chargementNiveaux.setName("chargementNiveauxDeMaturite");
			chargementNiveaux.start();
			Thread chargementCycle = new Thread(new C_ChargerCycleDeVie(cBase));
			chargementCycle.setName("chargementCycleDeVie");
			//Le start du chargement du cycle est fait dans le chargement de l'exec
			Thread chargementExecution = new Thread(new C_ChargerExecutionProcessus(cBase,chargementCycle));
			chargementExecution.setName("chargementExecutionProcessus");
			chargementExecution.start();
			Thread chargementProcessus = new Thread(new C_ChargerProcessus(cBase));
			chargementProcessus.setName("chargementProcessus");
			chargementProcessus.start();
			Thread chargementEvaluation = new Thread(new C_ChargerEvaluation(cBase));
			chargementEvaluation.setName("chargementEvaluations");
			chargementEvaluation.start();
			Thread chargementDemandes = new Thread(new C_ChargerDemandesModification(cBase));
			chargementDemandes.setName("chargementDemandes");
			chargementDemandes.start();
			Thread chargementModifications = new Thread(new C_ChargerModifications(cBase));
			chargementModifications.setName("chargementModifications");
			chargementModifications.start();
			Thread chargementRevues = new Thread(new C_ChargerRevuesProcessus(cBase));
			chargementRevues.setName("chargementRevues");
			chargementRevues.start();
			Thread chargementMesureAdhesion = new Thread(new C_ChargerMesuresAdhesion(cBase));
			chargementMesureAdhesion.setName("chargementMesureAdhesion");
			chargementMesureAdhesion.start();
			Thread chargementMesureAmelioration = new Thread(new C_ChargerMesuresAmelioration(cBase));
			chargementMesureAmelioration.setName("chargementMesureAmelioration");
			chargementMesureAmelioration.start();			

			try
			{
				chargementExecution.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementEvaluation.join();
			}
			catch (InterruptedException e2)
			{}
			try
			{
				chargementDemandes.join();
			}
			catch (InterruptedException e3)
			{}
			try
			{
				chargementModifications.join();
			}
			catch (InterruptedException e4)
			{}
			try
			{
				chargementProcessus.join();
			}
			catch (InterruptedException e5)
			{}
			try
			{
				chargementCycle.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementNiveaux.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementMesuresRepresentation.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementRevues.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementMesureAdhesion.join();
			}
			catch (InterruptedException e1)
			{}
			try
			{
				chargementMesureAmelioration.join();
			}
			catch (InterruptedException e1)
			{}			
			//cBase.fermerConnexion();

		}
		catch (SQLException e)
		{}
	}

	/**
	 * Ajout d'un serveur de fichiers DOM
	 */
	public static void addServeurDOM(String adresse, String login, String motDePasse)
	{
		//Création d'un serveur
		E_Serveur leServeur = new E_Serveur(adresse, login, motDePasse);
		//Si le serveur n'existe pas déjà
		if (E_Utilisateur.getServeursDOM().indexOf(leServeur) < 0)
		{
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

			//Ajout du serveur
			E_Utilisateur.addServeursDOM(leServeur);
			try
			{
				//Ajour du serveur en base				
				cBase.ouvrirConnexion();
				ArrayList lesParametres = new ArrayList();
				lesParametres.add("DOM");
				lesParametres.add(adresse);
				lesParametres.add(login);
				lesParametres.add(motDePasse);
				lesParametres.add("0");
				lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
				idStatement = cBase.creerPreparedStatement("setServeurs");
				cBase.executerRequeteStockee(idStatement, "setServeurs", lesParametres);
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}

	/**
	 * Récupération des serveurs de fichiers DOM
	 */
	public static ArrayList getServeursDOM()
	{
		return E_Utilisateur.getServeursDOM();
	}

	/**
	 * Recherche de l'entité serveur à partir de son adresse
	 */
	public static E_Serveur findServeurDOM(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursDOM();
		boolean trouve = false;
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size() && !trouve)
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				trouve = true;
		}
		if (trouve)
			return (E_Serveur) lesServeurs.get(--i);
		else
			return null;
	}
	
	public static void setSelectedDOM(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursDOM();
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size())
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				((E_Serveur) lesServeurs.get(i)).setSelected(true);
			else
				((E_Serveur) lesServeurs.get(i)).setSelected(false);
		}		
	}	

	/**
	 * Modification du login et du mot de passe
	 */
	public static void setLoginMotDePasseDOM(String adresse, String login, String motDePasse) throws SQLException
	{
		E_Serveur unServeur = findServeurDOM(adresse);
		unServeur.setLogin(login);
		unServeur.setMotDePasse(motDePasse);
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Ajour du login et du mot de passe en base			
		cBase.ouvrirConnexion();
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);
		lesParametres.add(motDePasse);
		lesParametres.add("DOM");
		lesParametres.add(adresse);
		lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
		idStatement = cBase.creerPreparedStatement("setLoginPasswordServeur");
		cBase.executerRequeteStockee(idStatement, "setLoginPasswordServeur", lesParametres);
		cBase.fermerStatement(idStatement);
		cBase.fermerConnexion();

	}

	/**
	 * Récupération des serveurs de fichiers de mesures (workflow)
	 */
	public static ArrayList getServeursMES()
	{
		return E_Utilisateur.getServeursMES();
	}

	/**
	 * Recherche de l'entité serveur à partir de son adresse
	 */
	public static E_Serveur findServeurMES(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursMES();
		boolean trouve = false;
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size() && !trouve)
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				trouve = true;
		}
		if (trouve)
			return (E_Serveur) lesServeurs.get(--i);
		else
			return null;
	}

	/**
	 * Ajout d'un serveur de fichiers workflow
	 */
	public static void addServeurMES(String adresse, String login, String motDePasse)
	{
		//Création d'un serveur
		E_Serveur leServeur = new E_Serveur(adresse, login, motDePasse);
		//Si le serveur n'existe pas déjà
		if (E_Utilisateur.getServeursMES().indexOf(leServeur) < 0)
		{
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

			//Ajout du serveur
			E_Utilisateur.addServeursMES(leServeur);
			try
			{
				//Ajour du serveur en base				
				cBase.ouvrirConnexion();
				ArrayList lesParametres = new ArrayList();
				lesParametres.add("MES");
				lesParametres.add(adresse);
				lesParametres.add(login);
				lesParametres.add(motDePasse);
				lesParametres.add("0");
				lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
				idStatement = cBase.creerPreparedStatement("setServeurs");
				cBase.executerRequeteStockee(idStatement, "setServeurs", lesParametres);
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}
	
	public static void setSelectedMES(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursMES();
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size())
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				((E_Serveur) lesServeurs.get(i)).setSelected(true);
			else
				((E_Serveur) lesServeurs.get(i)).setSelected(false);
		}		
	}	
	
	/**
	 * Modification du login et du mot de passe
	 */
	public static void setLoginMotDePasseMES(String adresse, String login, String motDePasse) throws SQLException
	{
		E_Serveur unServeur = findServeurMES(adresse);
		unServeur.setLogin(login);
		unServeur.setMotDePasse(motDePasse);
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Ajour du login et du mot de passe en base			
		cBase.ouvrirConnexion();
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);
		lesParametres.add(motDePasse);
		lesParametres.add("MES");
		lesParametres.add(adresse);
		lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
		idStatement = cBase.creerPreparedStatement("setLoginPasswordServeur");
		cBase.executerRequeteStockee(idStatement, "setLoginPasswordServeur", lesParametres);
		cBase.fermerStatement(idStatement);
		cBase.fermerConnexion();

	}		
	
	/**
	 * Ajout d'un serveur de fichiers HTML
	 */
	public static void addServeurHTML(String adresse, String login, String motDePasse)
	{
		//Création d'un serveur
		E_Serveur leServeur = new E_Serveur(adresse, login, motDePasse);
		//Si le serveur n'existe pas déjà
		if (E_Utilisateur.getServeursHTML().indexOf(leServeur) < 0)
		{
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

			//Ajout du serveur
			E_Utilisateur.addServeursHTML(leServeur);
			try
			{
				//Ajour du serveur en base				
				cBase.ouvrirConnexion();
				ArrayList lesParametres = new ArrayList();
				lesParametres.add("HTM");
				lesParametres.add(adresse);
				lesParametres.add(login);
				lesParametres.add(motDePasse);
				lesParametres.add("0");
				lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
				idStatement = cBase.creerPreparedStatement("setServeurs");
				cBase.executerRequeteStockee(idStatement, "setServeurs", lesParametres);
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}

	/**
	 * Récupération des serveurs de fichiers HTML
	 */
	public static ArrayList getServeursHTML()
	{
		return E_Utilisateur.getServeursHTML();
	}

	/**
	 * Recherche de l'entité serveur à partir de son adresse
	 */
	public static E_Serveur findServeurHTML(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursHTML();
		boolean trouve = false;
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size() && !trouve)
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				trouve = true;
		}
		if (trouve)
			return (E_Serveur) lesServeurs.get(--i);
		else
			return null;
	}

	/**
	 * Modification du login et du mot de passe
	 */
	public static void setLoginMotDePasseHTML(String adresse, String login, String motDePasse) throws SQLException
	{
		E_Serveur unServeur = findServeurHTML(adresse);
		unServeur.setLogin(login);
		unServeur.setMotDePasse(motDePasse);
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Ajour du login et du mot de passe en base			
		cBase.ouvrirConnexion();
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);
		lesParametres.add(motDePasse);
		lesParametres.add("HTM");
		lesParametres.add(adresse);
		lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
		idStatement = cBase.creerPreparedStatement("setLoginPasswordServeur");
		cBase.executerRequeteStockee(idStatement, "setLoginPasswordServeur", lesParametres);
		cBase.fermerStatement(idStatement);
		cBase.fermerConnexion();

	}
	
	public static void setSelectedHTML(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursHTML();
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size())
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				((E_Serveur) lesServeurs.get(i)).setSelected(true);
			else
				((E_Serveur) lesServeurs.get(i)).setSelected(false);
		}		
	}	
	
	/**
	 * Ajout d'un serveur de fichiers CSS
	 */
	public static void addServeurCSS(String adresse, String login, String motDePasse)
	{
		//Création d'un serveur
		E_Serveur leServeur = new E_Serveur(adresse, login, motDePasse);
		//Si le serveur n'existe pas déjà
		if (E_Utilisateur.getServeursCSS().indexOf(leServeur) < 0)
		{
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

			//Ajout du serveur
			E_Utilisateur.addServeursCSS(leServeur);
			try
			{
				//Ajour du serveur en base				
				cBase.ouvrirConnexion();
				ArrayList lesParametres = new ArrayList();
				lesParametres.add("CSS");
				lesParametres.add(adresse);
				lesParametres.add(login);
				lesParametres.add(motDePasse);
				lesParametres.add("0");
				lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
				idStatement = cBase.creerPreparedStatement("setServeurs");
				cBase.executerRequeteStockee(idStatement, "setServeurs", lesParametres);
				cBase.fermerStatement(idStatement);
				cBase.fermerConnexion();
			}
			catch (SQLException e)
			{}
		}
	}

	/**
	 * Récupération des serveurs de fichiers CSS
	 */
	public static ArrayList getServeursCSS()
	{
		return E_Utilisateur.getServeursCSS();
	}

	/**
	 * Recherche de l'entité serveur à partir de son adresse
	 */
	public static E_Serveur findServeurCSS(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursCSS();
		boolean trouve = false;
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size() && !trouve)
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				trouve = true;
		}
		if (trouve)
			return (E_Serveur) lesServeurs.get(--i);
		else
			return null;
	}

	/**
	 * Modification du login et du mot de passe
	 */
	public static void setLoginMotDePasseCSS(String adresse, String login, String motDePasse) throws SQLException
	{
		E_Serveur unServeur = findServeurCSS(adresse);
		unServeur.setLogin(login);
		unServeur.setMotDePasse(motDePasse);
		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Ajour du login et du mot de passe en base			
		cBase.ouvrirConnexion();
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);
		lesParametres.add(motDePasse);
		lesParametres.add("CSS");
		lesParametres.add(adresse);
		lesParametres.add(new Integer(E_Utilisateur.getIdentifiant()));
		idStatement = cBase.creerPreparedStatement("setLoginPasswordServeur");
		cBase.executerRequeteStockee(idStatement, "setLoginPasswordServeur", lesParametres);
		cBase.fermerStatement(idStatement);
		cBase.fermerConnexion();

	}

	public static void setSelectedCSS(String adresse)
	{
		//Serveur connus
		ArrayList lesServeurs = E_Utilisateur.getServeursCSS();
		int i = -1;
		//Pour chaque serveur comparaison de son adresse
		while (++i < lesServeurs.size())
		{
			if (((E_Serveur) lesServeurs.get(i)).getChemin().equals(adresse))
				((E_Serveur) lesServeurs.get(i)).setSelected(true);
			else
				((E_Serveur) lesServeurs.get(i)).setSelected(false);
		}		
	}	
}