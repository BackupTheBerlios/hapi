/*
 * Auteur Cédric
 *
 */
package hapi.donnees;

/**
 * Classe représentant un serveur
 */
public class E_Serveur
{
	private String chemin = "";
	private String motDePasse = "";
	private String login = "";
	private boolean selected = false;

	public E_Serveur(String chemin, String login, String motDePasse)
	{
		this.chemin = chemin;
		this.login = login;
		this.motDePasse = motDePasse;
	}

	public String getChemin()
	{
		return chemin;
	}

	public String getLogin()
	{
		return login;
	}

	public String getMotDePasse()
	{
		return motDePasse;
	}

	public void setLogin(String string)
	{
		login = string;
	}

	public void setMotDePasse(String string)
	{
		motDePasse = string;
	}

	public boolean equals(Object arg0)
	{
		if (arg0 instanceof E_Serveur)
			return ((E_Serveur) arg0).getChemin().equals(chemin);
		return false;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean b)
	{
		selected = b;
	}

}
