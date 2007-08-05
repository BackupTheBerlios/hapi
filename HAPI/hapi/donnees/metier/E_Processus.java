package hapi.donnees.metier;

import hapi.donnees.E_MesureAcces;
import hapi.donnees.E_MesureAmelioration;
import hapi.donnees.E_MesureFormation;
import hapi.donnees.E_MesureRepresentation;
import hapi.donnees.E_MesureUtilisation;
import hapi.donnees.metier.interfaces.InterfaceMetier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

/**
 * @author boursier
 *
 */
public class E_Processus implements InterfaceMetier
{
	// identifiant du processus
	private String id = null;
	// nom du processus
	private String nom = null;
	// nom de l'auteur du processus
	private String nomAuteur = null;
	// e-mail de l'auteur du processus
	private String emailAuteur = null;
	// description du processus
	private String descriptionProcessus = null;
	// chemein de génération du site décrivant le processus
	private String cheminGeneration = null;
	// niveau de maturité du processus
	private int niveauMaturite = 1;
	// nom du responsable
	private String nomResponsable = null;
	// prenom du responsable
	private String prenomResponsable = null;

	// liste des identifiants de composants du processus
	private ArrayList idComposants = null;
	// liste des paquetages de présentation du processus
	private ArrayList idPaquetagePresentation = null;
	// liste des versions connus du processus
	// contient des ArrayList de 2 cases, id0=version id1=numeroverion
	private ArrayList listeDatesExport = null;
	//liste des mesures de représentation
	//<DateExport><E_MesureRepresentation>
	private HashMap listeMesuresRepresentation = new HashMap();
	private ArrayList listeMesuresAcces = new ArrayList();
	private ArrayList listeMesuresFormation = new ArrayList();
	private ArrayList listeMesuresUtilisation = new ArrayList();
	//<DateExport><E_MesureAmelioration>
	private HashMap listeMesuresAmelioration = new HashMap();
	
	private ImageIcon icone;

	public E_Processus(ImageIcon lIcone)
	{
		// Initialisation des listes
		idComposants = new ArrayList();
		idPaquetagePresentation = new ArrayList();
		listeDatesExport = new ArrayList();
		// Initialisation des String
		id = new String();
		nom = new String();
		nomAuteur = new String();
		emailAuteur = new String();
		descriptionProcessus = new String();
		cheminGeneration = new String();

		icone = lIcone;
	}

	/**
	 * ajoute un identifiant de composant à la liste
	 * @param idcomp
	 */
	public void ajouterIdComposant(String idcomp)
	{
		idComposants.add(idcomp);
	}
	
	public void replaceIdComposant(String ancien,String idcomp)
	{
		idComposants.remove(ancien);
		idComposants.add(idcomp);
	}	

	public void ajouterPaquetagePresentation(String idpaq)
	{
		idPaquetagePresentation.add(idpaq);
	}
	
	public void replacePaquetagePresentation(String ancien,String idpaq)
	{
		idPaquetagePresentation.remove(ancien);
		idPaquetagePresentation.add(idpaq);
	}	

	/**
	 * Si le processus est déjà connu, l'entité est modifié
	 * Les listes sont réinitialisées et une version supplémentaire
	 * est ajoutée à la liste
	 */
	public void update(E_Processus laCopie)
	{
		idComposants.clear();
		idComposants.addAll(laCopie.getIdComposants());
		idPaquetagePresentation.clear();
		idPaquetagePresentation.addAll(laCopie.getIdPaquetagePresentation());
		listeDatesExport.add(laCopie.getDatesExportEtNumero());
		nom = laCopie.getNomSansVersion();
		nomAuteur = laCopie.getNomAuteur();
		emailAuteur = laCopie.getEmailAuteur();
		descriptionProcessus = laCopie.getDescription();
		cheminGeneration = laCopie.getCheminGeneration();
	}

	// getters et setters
	public String getNomSansVersion()
	{
		return nom;
	}
	
	public String getNom()
	{
		//if (getnumeroVersion(getDateExport()).equals(""))
			return nom;
		//else
		//	return nom + " ("+ getnumeroVersion(getDateExport()) +")";
	}

	public void setNom(String n)
	{
		nom = n;
	}

	public String getIdentifiant()
	{
		return id;
	}
	public void setId(String identifiant)
	{
		id = identifiant;
	}
	/**
	 * @return
	 */
	public String getCheminGeneration()
	{
		return cheminGeneration;
	}

	/**
	 * @return
	 */
	public String getEmailAuteur()
	{
		return emailAuteur;
	}

	/**
	 * @return
	 */
	public String getNomAuteur()
	{
		return nomAuteur;
	}

	/**
	 * @param string
	 */
	public void setCheminGeneration(String string)
	{
		cheminGeneration = string;
	}

	/**
	 * @param string
	 */
	public void setEmailAuteur(String string)
	{
		emailAuteur = string;
	}

	/**
	 * @param string
	 */
	public void setNomAuteur(String string)
	{
		nomAuteur = string;
	}

	/**
	 * @return
	 */
	public String getDescription()
	{
		if (descriptionProcessus == null)
			return " ";
		return descriptionProcessus;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		descriptionProcessus = string;
	}

	/**
	 * @return
	 */
	public ArrayList getIdComposants()
	{
		return idComposants;
	}

	public int getNiveauMaturite()
	{
		return this.niveauMaturite;
	}

	public void setNiveauMaturite(int nivMaturite)
	{
		this.niveauMaturite = nivMaturite;
	}

	public ArrayList getIdPaquetagePresentation()
	{
		return idPaquetagePresentation;
	}

	public void addDateExport(String nouvelleVersion,String numeroVersion)
	{
		ArrayList unElement = new ArrayList();
		unElement.add(nouvelleVersion);
		unElement.add(numeroVersion);
		listeDatesExport.add(unElement);
	}
	
	public void replaceDateExport(String ancien,String nouvelleVersion,String numeroVersion)
	{
		boolean trouve = false;
		int i = -1;
		while (++i < listeDatesExport.size() && !trouve)
		{
			if (((ArrayList)listeDatesExport.get(i)).get(0).equals(ancien))
				trouve = true;
		}
		if (trouve)
			listeDatesExport.remove(--i);		
		
		ArrayList unElement = new ArrayList();
		unElement.add(nouvelleVersion);
		unElement.add(numeroVersion);
		listeDatesExport.add(unElement);		
	}

	public String getDateExportFormatee()
	{
		return formaterDateExport(getDateExport());
	}

	public ArrayList getDatesExportEtNumero()
	{
		return (ArrayList) listeDatesExport.get(listeDatesExport.size() - 1);
	}
	
	public String getDateExport()
	{
		return (String) ((ArrayList) listeDatesExport.get(listeDatesExport.size() - 1)).get(0);
	}

	public boolean isExportExiste(String laVersion)
	{
		boolean trouve = false;
		for (Iterator it=listeDatesExport.iterator();it.hasNext() && !trouve;)
		{
			if (((ArrayList) it.next()).get(0).equals(laVersion))
					trouve = true;
		}
		return trouve;
	}

	//Renvoie un ArrayList d'ArrayList <DateExport,verionProcessus>
	public ArrayList getListeDesVersion()
	{
		return listeDatesExport;
	}
	
	//Renvoie un ArrayList de versions
	public ArrayList getListeNumeroVersion()
	{
		ArrayList lesNumeros = new ArrayList();
		for (Iterator it = listeDatesExport.iterator();it.hasNext();)
			lesNumeros.add(((ArrayList) it.next()).get(1));
		return lesNumeros;
	}	
	
	//Renvoie la liste des dates d'exports
	public ArrayList getListeDateExport()
	{
		ArrayList lesDates = new ArrayList();
		for (Iterator it = listeDatesExport.iterator();it.hasNext();)
			lesDates.add(((ArrayList) it.next()).get(0));
		return lesDates;
	}

	public ArrayList getListeDateExportFormatee()
	{
		ArrayList listeDate = getListeDateExport();
		ArrayList listDateExpFormatee = new ArrayList();
		for (int i = 0; i < listeDate.size(); i++)
			listDateExpFormatee.add(this.formaterDateExport((String) listeDate.get(i)));
		return listDateExpFormatee;
	}
	
	public void setnumeroVersion(String dateExport,String numeroVersion)
	{
		boolean trouve = false;
		for (int i=0;i<listeDatesExport.size() && !trouve;i++)
			if (((ArrayList)(listeDatesExport.get(i))).get(0).equals(dateExport))
			{
				ArrayList nouvelElement = new ArrayList();
				nouvelElement.add(dateExport);
				nouvelElement.add(numeroVersion);
				listeDatesExport.set(i,nouvelElement);
				trouve = true;
			}
	}	
	
	public String getnumeroVersion(String dateExport)
	{
		for (int i=0;i<listeDatesExport.size();i++)
			if (((ArrayList)(listeDatesExport.get(i))).get(0).equals(dateExport))
			{
				return (String) ((ArrayList)(listeDatesExport.get(i))).get(1);
			}
			
		return null;
	}	

	public String getNomResponsable()
	{
		return nomResponsable;
	}

	public String getPrenomResponsable()
	{
		return prenomResponsable;
	}

	public void setNomResponsable(String string)
	{
		nomResponsable = string;
	}

	public void setPrenomResponsable(String string)
	{
		prenomResponsable = string;
	}

	public String formaterDateExport(String dExport)
	{
		return (dExport.substring(6, 8) + "/" + dExport.substring(4, 6) + "/" + dExport.substring(0, 4) + " " + dExport.substring(8, 10) + ":" + dExport.substring(10, 12) + ":" + dExport.substring(12, 14));
	}

	public ImageIcon getIcone()
	{
		return icone;
	}
	
	public void ajouterMesureRepresentation(E_MesureRepresentation uneMesure)
	{
		listeMesuresRepresentation.put(uneMesure.getDateExport(),uneMesure);
	}
	
	public E_MesureRepresentation getMesureRepresentation(String dateExport)
	{
		return (E_MesureRepresentation) listeMesuresRepresentation.get(dateExport);
	}
	
	public void ajouterMesureAmelioration(E_MesureAmelioration uneMesure)
	{
		listeMesuresAmelioration.remove(uneMesure.getDateExport());
		listeMesuresAmelioration.put(uneMesure.getDateExport(),uneMesure);
	}
	
	public void supprimerMesureAmelioration(String dateExport)
	{
		listeMesuresAmelioration.remove(dateExport);
	}	
	
	public E_MesureAmelioration getMesureAmelioration(String dateExport)
	{
		return (E_MesureAmelioration) listeMesuresAmelioration.get(dateExport);
	}
	
	public HashMap getListeMesureAmelioration()
	{
		return listeMesuresAmelioration;
	}
		
	
	public void ajouterMesureAcces(E_MesureAcces uneMesure)
	{
		listeMesuresAcces.add(uneMesure);
	}
	
	public E_MesureAcces getMesureAcces(int indice)
	{
		return (E_MesureAcces) listeMesuresAcces.get(indice);
	}
	
	public void ajouterMesureFormation(E_MesureFormation uneMesure)
	{
		listeMesuresFormation.add(uneMesure);
	}
	
	public E_MesureFormation getMesureFormation(int indice)
	{
		return (E_MesureFormation) listeMesuresFormation.get(indice);
	}
	
	public void ajouterMesureUtilisation(E_MesureUtilisation uneMesure)
	{
		listeMesuresUtilisation.add(uneMesure);
	}
	
	public E_MesureUtilisation getMesureUtilisation(int indice)
	{
		return (E_MesureUtilisation) listeMesuresUtilisation.get(indice);
	}
	
	public ArrayList getListeMesuresAcces()
	{
		return listeMesuresAcces;
	}

	public ArrayList getListeMesuresFormation()
	{
		return listeMesuresFormation;
	}

	public ArrayList getListeMesuresUtilisation()
	{
		return listeMesuresUtilisation;
	}
	
	public void supprimerMesureUtilisation(String idProjet)
	{
		boolean trouve = false;
		int i = -1;
		while (++i < listeMesuresUtilisation.size() && !trouve)
			if (((E_MesureUtilisation)listeMesuresUtilisation.get(i)).getIdExec().equals(idProjet))
			{
				listeMesuresUtilisation.remove(i);
				trouve = true;
			}
	}	
}