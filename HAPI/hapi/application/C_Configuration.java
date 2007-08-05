/*
 * Auteur C�dric
 *
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_Configuration;
import hapi.donnees.modeles.ModeleTable;
import hapi.exception.ChampsVideException;
import hapi.exception.FichierConfInexistantException;
import hapi.exception.NoRowInsertedException;
import hapi.exception.UtilisateurNonIdentifieException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;

/**
 * Contr�leur de la configuration de HAPI
 */
public class C_Configuration
{
	/**
	 * Constructeur de la configuration
	 * @param initialise si true, le fichier de configuration est charg�
	 * @return
	 * @throws FichierConfInexistantException Si le fichier de configuration n'existe pas
	 */
	public C_Configuration(boolean initialise) throws FichierConfInexistantException
	{
		//S'il y a besoin
		if (initialise && !E_Configuration.isFichierCharge())
			//Demande d'initialisation de l'entit�
			try
			{
				E_Configuration.chargerCheminBase();
				//Modification du bundle
				Bundle.setLocaleCourante(new Locale(E_Configuration.getLangue()));
				//Modification du LookAndFeel
				try
				{
					UIManager.setLookAndFeel(getLookAndFeel());
				}
				catch (Exception e1)
				{
					try
					{
						E_Configuration.setLangue("Metal");
						UIManager.setLookAndFeel("javax.swing.plaf.metal");
					}
					catch (Exception e)
					{}
				}
			}
			catch (IOException e)
			{}
	}

	/**
	 * Affectation des informations de configuration pour la base
	 */
	public void setConfigurationBase(String login, String pwd, String serveur, String nomBase) throws ChampsVideException, FichierConfInexistantException, IOException
	{
		if (login.equals("") || serveur.equals("") || nomBase.equals(""))
			throw new ChampsVideException();

		//Cr�ation du fichier local
		E_Configuration.creerFichierLocal();
		//Affectation des valeurs
		E_Configuration.setLoginBaseDeDonnees(login);
		E_Configuration.setPwdBaseDeDonnees(pwd);
		E_Configuration.setNomBase(nomBase);
		E_Configuration.setServeurBaseDeDonnees(serveur);
		//Mise � jour du fichier
		E_Configuration.mettreAJourFichier();
	}

	/**
	 * Modifie le fichier local
	 */
	public void saveConfiguration() throws IOException
	{
		try
		{
			E_Configuration.mettreAJourFichier();
		}
		catch (FichierConfInexistantException e)
		{
			//Ne rien faire en cas d'abscence du fichier, il sera cr�� au prochain d�marrage

		}
	}

	/**
	 * R�cup�ration de la langue
	 */
	public String getLangue()
	{
		return E_Configuration.getLangue();
	}

	/**
	 * Modification de la langue
	 */
	public void setLangue(String langue)
	{
		//Langue
		Locale laLocale = new Locale(langue);
		E_Configuration.setLangue(laLocale.getLanguage().substring(0, 2));
		Bundle.setLocaleCourante(new Locale(laLocale.getLanguage().substring(0, 2)));

		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		//Affectation de la langue � la base
		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(E_Configuration.getLangue());
			lesParametres.add(E_Configuration.getLookAndFeel());
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

			idStatement = cBase.creerPreparedStatement("setConfiguration");
			cBase.executerRequeteStockee(idStatement, "setConfiguration", lesParametres);
			lesParametres.clear();
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Modification du laf
	 */
	public void setLaf(String LookAndFeel)
	{
		//Look And Feel
		E_Configuration.setLookAndFeel(LookAndFeel);
		int idStatement = 0;

		try
		{
			UIManager.setLookAndFeel(getLookAndFeel());
		}
		catch (Exception e)
		{
			try
			{
				E_Configuration.setLookAndFeel("Metal");
				UIManager.setLookAndFeel("javax.swing.plaf.metal");
			}
			catch (Exception e1)
			{}
		}

		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Affectation du look and feel � la base
		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(E_Configuration.getLangue());
			lesParametres.add(E_Configuration.getLookAndFeel());
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

			idStatement = cBase.creerPreparedStatement("setConfiguration");
			cBase.executerRequeteStockee(idStatement, "setConfiguration", lesParametres);
			lesParametres.clear();
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Modification de la langue et du laf
	 */
	public void setLangueLaf(String langue, String LookAndFeel)
	{
		//Langue
		Locale laLocale = new Locale(langue);
		E_Configuration.setLangue(laLocale.getLanguage().substring(0, 2));
		Bundle.setLocaleCourante(new Locale(laLocale.getLanguage().substring(0, 2)));

		//Look And Feel
		E_Configuration.setLookAndFeel(LookAndFeel);
		try
		{
			UIManager.setLookAndFeel(getLookAndFeel());
		}
		catch (Exception e)
		{
			try
			{
				E_Configuration.setLookAndFeel("Metal");
				UIManager.setLookAndFeel("javax.swing.plaf.metal");
			}
			catch (Exception e1)
			{}
		}

		int idStatement = 0;
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

		//Affectation de la langue et du look and feel � la base
		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(E_Configuration.getLangue());
			lesParametres.add(E_Configuration.getLookAndFeel());
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

			idStatement = cBase.creerPreparedStatement("setConfiguration");
			cBase.executerRequeteStockee(idStatement, "setConfiguration", lesParametres);
			lesParametres.clear();
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * R�cup�ration du LookAndFeel
	 */
	public String getLookAndFeel()
	{
		for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++)
		{
			if (UIManager.getInstalledLookAndFeels()[i].getName().equals(E_Configuration.getLookAndFeel()))
				return UIManager.getInstalledLookAndFeels()[i].getClassName();
		}

		return UIManager.getInstalledLookAndFeels()[0].getClassName();

	}

	/**
	 * R�cup�ration d'un mod�le contenant la liste des utilisateurs id�ntifi�s
	 */
	public static ModeleTable getModeleUtilisateurs()
	{
		ModeleTable lesUtilisateurs = new ModeleTable();
		int idStatement = 0;
		C_AccesBaseDonnees accesBase = new C_AccesBaseDonnees();

		try
		{
			//Chargement du mod�le
			lesUtilisateurs.addColumn(Bundle.getText("OO_GestionUtilisateur_login"));
			lesUtilisateurs.addColumn(Bundle.getText("OO_GestionUtilisateur_nom"));
			lesUtilisateurs.addColumn(Bundle.getText("OO_GestionUtilisateur_prenom"));
			lesUtilisateurs.addColumn(Bundle.getText("OO_GestionUtilisateur_role"));
			accesBase.ouvrirConnexion();

			idStatement = accesBase.creerPreparedStatement("getUtilisateurs");
			ResultSet resultat = accesBase.executerRequeteStockee(idStatement, "getUtilisateurs");
			while (resultat.next())
			{
				Vector uneLigne = new Vector();
				uneLigne.add(resultat.getString(1));
				uneLigne.add(resultat.getString(2));
				uneLigne.add(resultat.getString(3));
				uneLigne.add(Bundle.getText(resultat.getString(4)));
				lesUtilisateurs.addRow(uneLigne);
			}
			accesBase.fermerStatement(idStatement);
			accesBase.fermerConnexion();
		}
		catch (SQLException e)
		{}

		return lesUtilisateurs;
	}

	/**
	 * R�cup�ration des r�les connus
	 */
	public static DefaultComboBoxModel getModeleRoles()
	{
		DefaultComboBoxModel modeleListeDeroulante = new DefaultComboBoxModel();
		int idStatement = 0;

		//	Cr�ation d'une instance de la base
		C_AccesBaseDonnees base = new C_AccesBaseDonnees();

		try
		{
			base.ouvrirConnexion();
			//R�cup�ration des roles
			idStatement = base.creerPreparedStatement("getRolesUtilisateur");
			ResultSet lesRoles = base.executerRequeteStockee(idStatement, "getRolesUtilisateur");
			//Pour chaque r�le remplissage du mod�le
			while (lesRoles.next())
				modeleListeDeroulante.addElement(Bundle.getText(lesRoles.getString(1)));
			//Fermeture de la connexion
			base.fermerStatement(idStatement);
			base.fermerConnexion();
		}
		catch (SQLException e)
		{}

		return modeleListeDeroulante;
	}

	/**
	 * Ajout d'un utilisateur dans la base
	 */
	public static boolean addUtilisateur(String login, String nom, String prenom, int role) throws ChampsVideException
	{
		//S'il y a un champ vide
		if (login.equals("") || nom.equals("") || prenom.equals(""))
			throw new ChampsVideException();

		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(login);
			lesParametres.add(nom);
			lesParametres.add(prenom);
			lesParametres.add(new Integer(role + 1));

			idStatement = cBase.creerPreparedStatement("setUtilisateurs");
			cBase.executerRequeteStockee(idStatement, "setUtilisateurs", lesParametres);
			cBase.fermerStatement(idStatement);
			cBase.fermerConnexion();
		}
		catch (NoRowInsertedException e)
		{
			//Impossible d'ins�rer l'utilisateur
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{
				//Rien � faire ici
			}
			return false;
		}
		catch (SQLException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * Suppression de l'utilisateur � partir de son login et de son r�le
	 */
	public static void supprimeUtilisateur(String login, int leRole) throws Exception
	{
		if (leRole != -1)
		{
			int idStatement = 0;
			C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
			cBase.ouvrirConnexion();

			try
			{
				cBase.setAutoCommit(false);
				ArrayList lesParametres = new ArrayList();
				ResultSet resultat;

				int idUtilisateur;

				// R�cup�ration de l'identifiant du responsable
				lesParametres.add(login);
				lesParametres.add(new Integer(leRole));
				idStatement = cBase.creerPreparedStatement("getIdUtilisateur");
				resultat = cBase.executerRequeteStockee(idStatement, "getIdUtilisateur", lesParametres);
				resultat.next();
				idUtilisateur = resultat.getInt(1);
				resultat.close();
				cBase.fermerStatement(idStatement);

				// Si l'utilisateur supprim� est une responsable processus, alors on supprime les processus, executions processus et evaluations dont il est responsable 
				if (leRole == 1)
				{
					// Suppression des entit�s processus li�es � l'utilisateur supprim�
					lesParametres.clear();
					lesParametres.add(new Integer(idUtilisateur));
					idStatement = cBase.creerPreparedStatement("getProcessus");
					resultat = cBase.executerRequeteStockee(idStatement, "getProcessus", lesParametres);

					while (resultat.next())
					{
						String idProcessus = resultat.getString(1);

						// Suppression du processus et de tous les �l�ments li�s dans la base de donn�es
						C_Processus.supprimerProcessusEnBase(idProcessus, cBase);

						// Suppression de l'entit� processus
						C_Processus.supprimerProcessus(idProcessus);
					}
					resultat.close();
					cBase.fermerStatement(idStatement);
				}

				try
				{
					// Suppressions des adresses des serveurs de fichier de l'utilisateur
					lesParametres.clear();
					lesParametres.add(new Integer(idUtilisateur));
					idStatement = cBase.creerPreparedStatement("delServeursUtilisateur");
					cBase.executerRequeteStockee(idStatement, "delServeursUtilisateur", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				catch (NoRowInsertedException e)
				{}

				try
				{
					// Suppression de l'utilisateur
					lesParametres.clear();
					lesParametres.add(login);
					lesParametres.add(new Integer(leRole));
					idStatement = cBase.creerPreparedStatement("delUtilisateurs");
					cBase.executerRequeteStockee(idStatement, "delUtilisateurs", lesParametres);
					cBase.fermerStatement(idStatement);
				}
				catch (NoRowInsertedException e)
				{}

				// Si l'utilisateur supprim� est une responsable processus, alors on supprime aussi les entit�s processus dont il est responsable 
				if (leRole == 1)
				{
					// Suppression des entit�s processus li�es � l'utilisateur supprim�
					lesParametres.clear();
					lesParametres.add(new Integer(idUtilisateur));
					idStatement = cBase.creerPreparedStatement("getProcessus");
					resultat = cBase.executerRequeteStockee(idStatement, "getProcessus", lesParametres);

					while (resultat.next())
					{
						String idProcessus = resultat.getString(1);

						// Suppression de l'entit� processus
						C_Processus.supprimerProcessus(idProcessus);
					}
					resultat.close();
					cBase.fermerStatement(idStatement);
				}

				cBase.commit();
			}
			catch (Exception e)
			{
				cBase.rollback();
				throw e;
			}
			finally
			{
				cBase.fermerConnexion();
			}
		}
	}

	/**
	 * A partir d'un r�le de combo (= traduit en langue) r�cup�ration du code correspondant
	 */
	public static int convertRole(String role)
	{
		DefaultComboBoxModel modeleListeDeroulante = getModeleRoles();
		int leRole = -1;

		for (int i = 0; i < modeleListeDeroulante.getSize(); i++)
			if (modeleListeDeroulante.getElementAt(i).toString().equals(role))
				leRole = i;

		return leRole + 1;
	}

	/**
	 * V�rifie si l'utilisateur ne tente pas de se supprimer lui m�me
	 */
	public static boolean verifieSuppression(String login, String nom, String prenom, int role)
	{
		try
		{
			if ((C_Utilisateur.getRole() == role) && (C_Utilisateur.getNom().equals(nom)) && (C_Utilisateur.getPrenom().equals(prenom)) && (C_Utilisateur.getLogin().equals(login)))
				return false;
		}
		catch (UtilisateurNonIdentifieException e)
		{
			System.err.println("-----ERREUR GRAVE N�6-----");
		}

		return true;
	}

	/**
	 * R�cup�ration du chemin d'acc�s � la base de donn�es
	 */
	public String getCheminBase()
	{
		return E_Configuration.getServeurBaseDeDonnees();
	}

	/**
	 * R�cup�ration du nom de  la base de donn�es
	 */
	public String getNomBase()
	{
		return E_Configuration.getNomBase();
	}

	/**
	 * R�cup�ration du login de la base de donn�es
	 */
	public String getLoginBase()
	{
		return E_Configuration.getLoginBaseDeDonnees();
	}

	/**
	 * R�cup�ration du mot de passe de la base de donn�es
	 */
	public String getPWDBase()
	{
		return E_Configuration.getPwdBaseDeDonnees();
	}
}