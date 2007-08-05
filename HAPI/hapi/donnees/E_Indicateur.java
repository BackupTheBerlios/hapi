/*
 * Created on 21 janv. 2005
 *
 */
package hapi.donnees;

/**
 * @author Natalia
 *
 * 
 */
public class E_Indicateur
{
	// identifiant de l'indicateur
	private int id = 0;
	// nom de l'indicateur
	private String nom = null;
	// valeur de l'indicateur
	private Double valeur = null;
	// formule textuelle permettant la calcul
	private String formule = null;
	// seuil minimum associé à l'indicateur 
	private Double min = null;
	// seuil maximum associé à l'indicateur
	private Double max = null;
	// identifiant du processus auquel il est lié
	private String idProcessus = null;
	// identifiant de l'exécution de processus auquel il est lié
	private String idExecutionProcessus = null;

	public E_Indicateur()
	{}

	// getters et setters
	public String getFormule()
	{
		return formule;
	}
	public void setFormule(String formule)
	{
		this.formule = formule;
	}
	public Double getMax()
	{
		return max;
	}
	public void setMax(Double max)
	{
		this.max = max;
	}
	public Double getMin()
	{
		return min;
	}
	public void setMin(Double min)
	{
		this.min = min;
	}
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	public Double getValeur()
	{
		return valeur;
	}
	public void setValeur(Double val)
	{
		this.valeur = val;
	}
	public String getIdProcessus()
	{
		return idProcessus;
	}
	public void setIdProcessus(String idExec)
	{
		this.idProcessus = idExec;
	}
	public String getIdExecutionProcessus()
	{
		return idExecutionProcessus;
	}
	public void setIdExecutionProcessus(String idExec)
	{
		this.idExecutionProcessus = idExec;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
}
