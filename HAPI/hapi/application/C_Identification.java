package hapi.application;

import hapi.application.ressources.Bundle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

/**
 * Contr�leur de l'indentification des utilisateurs
 * @author Vincent
 */
public class C_Identification
{
	private DefaultComboBoxModel modeleListeDeroulante = null;

	/**
	 * Constructeur, pr�pare le mod�le des r�les
	 */
	public C_Identification() throws SQLException
	{
		int idStatement = 0;
		modeleListeDeroulante = new DefaultComboBoxModel();

		//Cr�ation d'une instance de la base
		C_AccesBaseDonnees base = new C_AccesBaseDonnees();
		base.ouvrirConnexion();
		//R�cup�ration des roles
		idStatement = base.creerPreparedStatement("getRolesUtilisateur");
		ResultSet lesRoles = base.executerRequeteStockee(idStatement, "getRolesUtilisateur");

		//Pour chaque r�le remplissage du mod�le
		while (lesRoles.next())
			modeleListeDeroulante.addElement(Bundle.getText(lesRoles.getString(1)));
		//Fermeture de la conception
		base.fermerStatement(idStatement);
		base.fermerConnexion();
	}

	/**
	 * V�rifie si l'identification est correcte
	 */
	public boolean verifierIdentification(String login, String motDePasse, int role) throws SQLException
	{
		int idStatement = 0;
		//Cr�ation d'une instance de la base
		C_AccesBaseDonnees base = new C_AccesBaseDonnees();
		base.ouvrirConnexion();
		//Cr�ation de la liste des param�tres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(login);

		lesParametres.add(motDePasse);
		lesParametres.add(new Integer(role));
		//R�cup�ration de l'identification
		idStatement = base.creerPreparedStatement("getIdentification");
		ResultSet lId = base.executerRequeteStockee(idStatement, "getIdentification", lesParametres);
		//s'il y a un utilisateur
		try
		{
			if (lId.next())
			{
				C_Utilisateur.indentifie(login, lId.getString(2), lId.getString(3), lId.getInt(1), role, base);
				return true;
			}
			else
				return false;
		}
		finally
		{
			//Fermeture de la conception
			base.fermerStatement(idStatement);
			base.fermerConnexion();
		}
	}

	/**
	 * R�cup�ration du mod�le des r�les
	 */
	public DefaultComboBoxModel getModeleRoles()
	{
		return modeleListeDeroulante;
	}

	/**
	 * Chargement de la configuration personnelle � l'utilisateur
	 */
	public void chargerConfiguration()
	{
		C_AccesBaseDonnees cAccesBaseDonnees = new C_AccesBaseDonnees();
		int idStatement = 0;

		try
		{
			cAccesBaseDonnees.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
			idStatement = cAccesBaseDonnees.creerPreparedStatement("getConfiguration");
			ResultSet retour = cAccesBaseDonnees.executerRequeteStockee(idStatement, "getConfiguration", lesParametres);
			retour.next();
			C_Configuration cConfiguration = new C_Configuration(true);
			cConfiguration.setLangueLaf(retour.getString(1), retour.getString(2));
		}
		catch (Exception e)
		{
			//A ce niveau, on utilise la configuration par d�faut
		}
		finally
		{
			//Fermeture de la conception
			try
			{
				cAccesBaseDonnees.fermerConnexion();
			}
			catch (SQLException e1)
			{}

		}
	}
}
