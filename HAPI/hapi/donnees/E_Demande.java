package hapi.donnees;

import java.sql.Date;

/**
 * @author Robin EYSSERIC
 */
public class E_Demande
{
	// Identifiant de la demande
	private int idDemande = -1;

	// Identifiant du processus li�
	private String idProcessus = null;

	// Date de la demande
	private Date dateDemande = null;

	// Nom du composant concern�
	private String nomComposant = null;

	// Description de la demande
	private String description = null;
	
	//Type
	private String type = null;
	
	//Severite
	private int severite = 0;

	public E_Demande()
	{}

	public Date getDateDemande()
	{
		return dateDemande;
	}

	public String getDescription()
	{
		return description;
	}

	public int getIdDemande()
	{
		return idDemande;
	}

	public String getIdProcessus()
	{
		return idProcessus;
	}

	public String getNomComposant()
	{
		return nomComposant;
	}

	public void setDateDemande(Date date)
	{
		dateDemande = date;
	}

	public void setDescription(String string)
	{
		description = string;
	}

	public void setIdDemande(int i)
	{
		idDemande = i;
	}

	public void setIdProcessus(String string)
	{
		idProcessus = string;
	}

	public void setNomComposant(String string)
	{
		nomComposant = string;
	}

	/**
	 * @return Renvoie severite.
	 */
	public int getSeverite()
	{
		return severite;
	}
	
	/**
	 * @param severite severite � d�finir.
	 */
	public void setSeverite(int severite)
	{
		this.severite = severite;
	}
	
	/**
	 * @return Renvoie type.
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * @param type type � d�finir.
	 */
	
	public void setType(String type)
	{
		this.type = type;
	}
}
