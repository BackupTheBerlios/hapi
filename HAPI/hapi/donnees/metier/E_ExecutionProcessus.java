/*
 * Created on 24 janv. 2005
 */
package hapi.donnees.metier;

import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.Date;

import javax.swing.ImageIcon;


/**
 * @author Vincent
 */
public class E_ExecutionProcessus implements InterfaceMetier
{
	// Identifiant défini par Hapi
	private int notreId;
	// Identifiant défini par les outils de worflow
	private int id;
	private String versionProcessus = null;
	private String nom = null;
	private String description = null;
	private String commentaire = new String();
	private Date dateDebut = null;
	private Date dateFin = null;
	private int id_CycleDeVie = -1;
	private ImageIcon icone;

	public E_ExecutionProcessus(String laVersionProcessus, ImageIcon lIcone, int cycleDeVie)
	{
		versionProcessus = laVersionProcessus;
		icone = lIcone;
		id_CycleDeVie = cycleDeVie;
	}

	//Redéfinition de la méthode equals : égalité de 2 exécutions de processus si leur id est identique
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof E_ExecutionProcessus)
		{
			E_ExecutionProcessus exec = (E_ExecutionProcessus) obj;
			if (this.id == exec.id)
				return true;
		}
		return false;
	}

	//getters
	public String getIdentifiant()
	{
		return String.valueOf(this.notreId);
	}

	public String getNom()
	{
		
		return this.nom + " (" + convertirVersion() /*versionProcessus*/
		+")";
	}

	public String getNomSansVersion()
	{
		return this.nom;
	}

	public String getDescription()
	{
		return this.description;
	}

	public Date getDateDebut()
	{
		return this.dateDebut;
	}

	public Date getDateFin()
	{
		return this.dateFin;
	}

	public String getCommentaire()
	{
		return this.commentaire;
	}

	/**
	 * Renvoie la version du processus sans mise en forme
	 */
	public String getVersionProcessus()
	{
		return versionProcessus;
	}

	/**
	 * Renvoie la version du processus avec une mise en forme
	 */
	public String getVersion()
	{
		return convertirVersion();
	}

	//setters
	public void setId(int i)
	{
		this.id = i;
	}

	public void setNom(String n)
	{
		this.nom = n;
	}

	public void setDescription(String desc)
	{
		this.description = desc;
	}

	public void setDateDebut(Date dateDeb)
	{
		this.dateDebut = dateDeb;
	}

	public void setDateFin(Date dateF)
	{
		this.dateFin = dateF;
	}

	public void setCommentaire(String leCommentaire)
	{
		this.commentaire = leCommentaire;
	}

	public void setVersionProcessus(String string)
	{
		versionProcessus = string;
	}

	private String convertirVersion()
	{
		return versionProcessus.substring(6, 8) + "/" + versionProcessus.substring(4, 6) + "/" + versionProcessus.substring(0, 4) + " " + versionProcessus.substring(8, 10) + ":" + versionProcessus.substring(10, 12) + ":" + versionProcessus.substring(12, 14);
	}

	/**
	 * Se servir de cette méthode uniquement pour les comparaisons à l'import 
	 * et l'export du comentaire.
	 * Cette méthode renvoie l'identifiant du projet défini par les outils de workflow.
	 * Pour récupérer l'identifiant défini par Hapi, utiliser getIdentifiant
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	public void setNotreId(int notreId)
	{
		this.notreId = notreId;
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
	
	/**
	 * @return Renvoie id_CycleDeVie
	 */
	public int getIdCycleDeVie()
	{
		return id_CycleDeVie;
	}
	/**
	 * @param cycleDeVie Défini l'identifiant du cycle de vie du projet
	 */
	public void setIdCycleDeVie(int cycleDeVie)
	{
		id_CycleDeVie = cycleDeVie;
	}	
}
