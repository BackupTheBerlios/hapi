/*
 * Auteur Cédric
 *
 */
package hapi.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controleur de la fenêtre de changement des infos utilisateurs
 */
public class C_ChangerInformations
{
	//Pour cette classe, l'ouverture de la connexion n'est effectuée qu'une seule fois
	private C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();

	/**
	 * Constructeur ouvrant la connexion
	 */
	public C_ChangerInformations()
	{
		try
		{
			cBase.ouvrirConnexion();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Fermeture de la connexion
	 */
	protected void finalize()
	{
		try
		{
			cBase.fermerConnexion();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Modification du nom et du prénom
	 */
	public void setNomPrenom(String nom, String prenom)
	{
		//On ne modifie la base que s'il y a eu vraiment changement
		if ((!nom.equals(C_Utilisateur.getNom())) || (!prenom.equals(C_Utilisateur.getPrenom())))
		{
			C_Utilisateur.setNomPrenom(nom, prenom);
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(nom);
			lesParametres.add(prenom);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
			int idStatement = 0;

			try
			{
				idStatement = cBase.creerPreparedStatement("setNomPrenomUtilisateurs");
				cBase.executerRequeteStockee(idStatement, "setNomPrenomUtilisateurs", lesParametres);
			}
			catch (SQLException e)
			{}
			finally
			{
				cBase.fermerStatement(idStatement);
			}
		}
	}

	/**
	 * Vérification de l'ancien mot de passe
	 */
	public boolean verifieAncien(char[] lancien)
	{
		String ancien = convertMotDePasse(lancien);
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
		int idStatement = 0;

		try
		{
			idStatement = cBase.creerPreparedStatement("getMotDePasseUtilisateurs");
			ResultSet resultat = cBase.executerRequeteStockee(idStatement, "getMotDePasseUtilisateurs", lesParametres);
			while (resultat.next())
			{
				if (resultat.getString(1).equals(ancien))
					return true;
			}
		}
		catch (SQLException e)
		{}
		finally
		{
			cBase.fermerStatement(idStatement);
		}
		return false;
	}

	/**
	 * Vérifie si la deuxième saisie du mot de passe est conforme à la première
	 */
	public boolean verifieRedonne(char[] Nouveau, char[] Redonne)
	{
		return (convertMotDePasse(Nouveau).equals(convertMotDePasse(Redonne)));
	}

	/**
	 * Modifie le mot de passe de l'utilisateur
	 */
	public void modifieMotDePasse(char[] MotDePasse) throws SQLException
	{
		ArrayList lesParametres = new ArrayList();
		int idStatement = 0;

		String pwd = convertMotDePasse(MotDePasse);

		lesParametres.add(pwd);
		lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));

		idStatement = cBase.creerPreparedStatement("setMotDePasseUtilisateurs");
		cBase.executerRequeteStockee(idStatement, "setMotDePasseUtilisateurs", lesParametres);
		cBase.fermerStatement(idStatement);
	}

	/**
	 * Transforme le mot de passe de char[] à String
	 */
	private String convertMotDePasse(char[] MotDePasse)
	{
		StringBuffer PWD = new StringBuffer("");
		for (int i = 0; i < MotDePasse.length; i++)
			PWD.append(MotDePasse[i]);

		return PWD.toString();
	}
}
