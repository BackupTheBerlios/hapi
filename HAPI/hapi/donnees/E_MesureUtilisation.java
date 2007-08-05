/*
 * Créé le 29 sept. 2005
 */
package hapi.donnees;

/**
 * @author Cédric
 */
public class E_MesureUtilisation
{
	private int id = -1;
	private String idExec;
	private int nombrePlanType;
	private double pourPlanType;
	private int nombreActivites;
	private double pourActivite;
	
	public E_MesureUtilisation(int lId,String lIdExec,int leNombrePlanType,double lePourPlanType,int leNombreActivites,double lePourActivite)
	{
		id = lId;
		idExec = lIdExec;
		nombrePlanType = leNombrePlanType;
		pourPlanType = lePourPlanType;
		nombreActivites = leNombreActivites;
		pourActivite = lePourActivite;		
	}
	
	public E_MesureUtilisation(String lIdExec,int leNombrePlanType,double lePourPlanType,int leNombreActivites,double lePourActivite)
	{
		idExec = lIdExec;
		nombrePlanType = leNombrePlanType;
		pourPlanType = lePourPlanType;
		nombreActivites = leNombreActivites;
		pourActivite = lePourActivite;		
	}	
	

	/**
	 * @return Renvoie id.
	 */
	public int getId()
	{
		return id;
	}	
	/**
	 * @return Renvoie idExec.
	 */
	public String getIdExec()
	{
		return idExec;
	}
	/**
	 * @return Renvoie nombreActivites.
	 */
	public int getNombreActivites()
	{
		return nombreActivites;
	}
	/**
	 * @return Renvoie nombrePlanType.
	 */
	public int getNombrePlanType()
	{
		return nombrePlanType;
	}
	/**
	 * @return Renvoie pourActivite.
	 */
	public double getPourActivite()
	{
		return pourActivite;
	}
	/**
	 * @return Renvoie pourPlanType.
	 */
	public double getPourPlanType()
	{
		return pourPlanType;
	}
	
	/**
	 * @param id id à définir.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
}
