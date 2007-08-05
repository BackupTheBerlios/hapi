package hapi.donnees;

/**
 * @author Robin EYSSERIC
 */
public class E_Modification
{
	// Identifiant de la modification
	private int idModification = -1;

	// Identifiant du processus lié
	private String idProcessus = null;

	// Date de la demande
	private String dateDemande = null;

	// Version du processus lors de la modification
	private String versionModification = null;

	// Nom du composant concerné
	private String nomComposant = null;

	// Description de la demande
	private String description = null;
	private String type = null;
	private int severite = 0;

	public E_Modification()
	{}

	public String getDateDemande()
	{
		return dateDemande;
	}

	public String getDescription()
	{
		return description;
	}

	public int getIdModification()
	{
		return idModification;
	}

	public String getIdProcessus()
	{
		return idProcessus;
	}

	public String getNomComposant()
	{
		return nomComposant;
	}

	public String getVersionModification()
	{
		return versionModification;
	}

	public void setDateDemande(String date)
	{
		dateDemande = date;
	}

	public void setDescription(String string)
	{
		description = string;
	}

	public void setIdModification(int i)
	{
		idModification = i;
	}

	public void setIdProcessus(String string)
	{
		idProcessus = string;
	}

	public void setNomComposant(String string)
	{
		nomComposant = string;
	}

	public void setVersionModification(String string)
	{
		versionModification = string;
	}

	/**
	 * @return Renvoie severite.
	 */
	public int getSeverite()
	{
		return severite;
	}
	/**
	 * @param severite severite à définir.
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
	 * @param type type à définir.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
