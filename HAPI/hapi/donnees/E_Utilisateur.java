package hapi.donnees;

import java.util.ArrayList;

/**
 * Entité de l'utilisateur connecté
 * @author marillaud
 */
public class E_Utilisateur
{
	static private String login = null;
	static private String nom = null;
	static private String prenom = null;
	static private int identifiant = 0;
	static private int role = -1;
	static private ArrayList serveursDPE = new ArrayList();
	static private ArrayList serveursDOM = new ArrayList();
	static private ArrayList serveursMES = new ArrayList();
	static private ArrayList serveursHTML = new ArrayList();
	static private ArrayList serveursCSS = new ArrayList();

	public static int getIdentifiant()
	{
		return identifiant;
	}

	public static String getLogin()
	{
		return login;
	}

	public static String getNom()
	{
		return nom;
	}

	public static String getPrenom()
	{
		return prenom;
	}

	public static int getRole()
	{
		return role;
	}

	public static void setIdentifiant(int i)
	{
		identifiant = i;
	}

	public static void setLogin(String string)
	{
		login = string;
	}

	public static void setNom(String string)
	{
		nom = string;
	}

	public static void setPrenom(String string)
	{
		prenom = string;
	}

	public static void setRole(int i)
	{
		role = i;
	}

	public static ArrayList getServeursDPE()
	{
		return serveursDPE;
	}

	public static ArrayList getServeursDOM()
	{
		return serveursDOM;
	}

	public static ArrayList getServeursMES()
	{
		return serveursMES;
	}
	
	public static ArrayList getServeursHTML()
	{
		return serveursHTML;
	}
		
	public static ArrayList getServeursCSS()
	{
		return serveursCSS;
	}		

	public static void addServeursDPE(E_Serveur unServeur)
	{
		serveursDPE.add(unServeur);
	}

	public static void addServeursDOM(E_Serveur unServeur)
	{
		serveursDOM.add(unServeur);
	}
	public static void addServeursMES(E_Serveur unServeur)
	{
		serveursMES.add(unServeur);
	}
	public static void addServeursHTML(E_Serveur unServeur)
	{
		serveursHTML.add(unServeur);
	}
	public static void addServeursCSS(E_Serveur unServeur)
	{
		serveursCSS.add(unServeur);
	}
}
