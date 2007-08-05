/*
 * Auteur Cédric
 *
 */
package hapi.application.metier;

import hapi.application.C_AccesBaseDonnees;
import hapi.application.C_GestionDemandes;
import hapi.application.C_GestionModifications;
import hapi.application.C_NiveauMaturite;
import hapi.donnees.E_MesureAcces;
import hapi.donnees.E_MesureAmelioration;
import hapi.donnees.E_MesureFormation;
import hapi.donnees.E_MesureRepresentation;
import hapi.donnees.E_MesureUtilisation;
import hapi.donnees.E_NiveauMaturite;
import hapi.donnees.metier.E_Processus;
import hapi.exception.NoRowInsertedException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Commentaire sur la classe/la méthode
 */
public class C_Processus
{
	// liste des processus chargés
	static private HashMap lesProcessus = new HashMap();

	/**
	 * Ajoute un processus à la liste
	 * @param unProcessus : de type E_Processus
	 */
	synchronized static public void addProcessus(E_Processus unProcessus)
	{
		lesProcessus.put(unProcessus.getIdentifiant(), unProcessus);
	}

	/**
	 * récupère le processus d'indice index dans la liste
	 * @param index : indice dans la liste
	 * @return
	 */
	synchronized static public E_Processus get(int index)
	{
		TreeSet lesClefs = new TreeSet(lesProcessus.keySet());
		int i = -1;
		Iterator it = lesClefs.iterator();
		while (++i < index)
		{
			it.next();
		}

		return (E_Processus) lesProcessus.get(it.next());
	}

	/**
	 * récupère le processus d'identifiant passé en paramètre
	 * @param identifiant
	 * @return
	 */
	synchronized static public E_Processus getProcessus(String identifiant)
	{
		return (E_Processus) lesProcessus.get(identifiant);
	}

	/**
	 * récupère les informations de description d'un processus
	 * @param identifiant
	 * @return
	 */
	synchronized static public ArrayList getDescriptifProcessus(String identifiant)
	{
		ArrayList laDescription = new ArrayList();
		E_Processus leProcessus = (E_Processus) lesProcessus.get(identifiant);

		laDescription.add(leProcessus.getNomAuteur());
		laDescription.add(leProcessus.getIdentifiant());
		laDescription.add(leProcessus.getPrenomResponsable() + " " + leProcessus.getNomResponsable());
		laDescription.add(leProcessus.getNomSansVersion());
		laDescription.add(leProcessus.getEmailAuteur());
		laDescription.add(leProcessus.getDescription());
		laDescription.add(leProcessus.getCheminGeneration());

		return laDescription;
	}

	synchronized static public int size()
	{
		return lesProcessus.size();
	}

	/**
	 * supprime le processus passé en paramètre
	 * @param id_processus
	 */
	synchronized static public void supprimerProcessus(String id_processus)
	{
		// suppression du contenu du processus
		C_Activite.supprimerActivites(id_processus);
		C_Composant.supprimerComposants(id_processus);
		C_Definition.supprimerDefinitions(id_processus);
		C_ElementPresentation.supprimerElementsPresentation(id_processus);
		C_Etat.supprimerEtats(id_processus);
		C_Guide.supprimerGuides(id_processus);
		C_Interface.supprimerInterfaces(id_processus);
		C_PaquetagePresentation.supprimerPaquetagesPresentation(id_processus);
		C_Produit.supprimerProduits(id_processus);
		C_Role.supprimerRoles(id_processus);
		C_TypeGuide.supprimerTypesGuide(id_processus);
		C_TypeProduit.supprimerTypesProduits(id_processus);
		C_ExecutionProcessus.supprimerExecutionProcessus(id_processus);
		C_GestionDemandes.supprimerDemandesProcessus(id_processus);
		C_GestionModifications.supprimerModificationsProcessus(id_processus);
		C_NiveauMaturite.supprimerNiveauMaturite(id_processus);

		// suppression du processus dans la liste
		lesProcessus.remove(id_processus);
	}

	public static void supprimerProcessusEnBase(String idProcessus, C_AccesBaseDonnees cBase) throws Exception
	{
		int idStatement = 0;
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(idProcessus);

		try
		{
			idStatement = cBase.creerPreparedStatement("delProcessus");
			cBase.executerRequeteStockee(idStatement, "delProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delVersionsProcessus");
			cBase.executerRequeteStockee(idStatement, "delVersionsProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delComposants");
			cBase.executerRequeteStockee(idStatement, "delComposants", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delRoles");
			cBase.executerRequeteStockee(idStatement, "delRoles", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delProduits");
			cBase.executerRequeteStockee(idStatement, "delProduits", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delDefinitions");
			cBase.executerRequeteStockee(idStatement, "delDefinitions", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delActivites");
			cBase.executerRequeteStockee(idStatement, "delActivites", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delInterfaces");
			cBase.executerRequeteStockee(idStatement, "delInterfaces", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delTypesProduit");
			cBase.executerRequeteStockee(idStatement, "delTypesProduit", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delEtats");
			cBase.executerRequeteStockee(idStatement, "delEtats", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delPaquetagesPresentation");
			cBase.executerRequeteStockee(idStatement, "delPaquetagesPresentation", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delElementsPresentation");
			cBase.executerRequeteStockee(idStatement, "delElementsPresentation", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delGuides");
			cBase.executerRequeteStockee(idStatement, "delGuides", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delTypesGuide");
			cBase.executerRequeteStockee(idStatement, "delTypesGuide", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProduitInterface");
			cBase.executerRequeteStockee(idStatement, "delLiensProduitInterface", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProduitEtat");
			cBase.executerRequeteStockee(idStatement, "delLiensProduitEtat", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProdActEntree");
			cBase.executerRequeteStockee(idStatement, "delLiensProdActEntree", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delLiensProdActSortie");
			cBase.executerRequeteStockee(idStatement, "delLiensProdActSortie", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delDemandesModificationsProcessus");
			cBase.executerRequeteStockee(idStatement, "delDemandesModificationsProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delHistoriqueModificationsProcessus");
			cBase.executerRequeteStockee(idStatement, "delHistoriqueModificationsProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delSeuilsProcessus");
			cBase.executerRequeteStockee(idStatement, "delSeuilsProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delMesuresRepresentation");
			cBase.executerRequeteStockee(idStatement, "delMesuresRepresentation", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delMesuresAmelioration");
			cBase.executerRequeteStockee(idStatement, "delMesuresAmelioration", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}		
		try
		{
			idStatement = cBase.creerPreparedStatement("delRevuesProcessusIdProcessus");
			cBase.executerRequeteStockee(idStatement, "delRevuesProcessusIdProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delMesureAccesIdProcessus");
			cBase.executerRequeteStockee(idStatement, "delMesureAccesIdProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
		try
		{
			idStatement = cBase.creerPreparedStatement("delMesureFormationIdProcessus");
			cBase.executerRequeteStockee(idStatement, "delMesureFormationIdProcessus", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}	
		C_NiveauMaturite.supprimerNiveauMaturiteEnBase(idProcessus,cBase);
		
		try
		{
			idStatement = cBase.creerPreparedStatement("getExecutionsProcessus");
			ResultSet res = cBase.executerRequeteStockee(idStatement, "getExecutionsProcessus", lesParametres);

			while (res.next())
			{
				// Suppression de l'execution processus
				C_ExecutionProcessus.supprimerExecutionProcessusEnBase(idProcessus, new Integer(res.getInt(1)).toString(), cBase);
			}
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}
	}

	public synchronized static void enregistrerVersionProcessus(String leProcessus, String dateExport, String laVersion) throws SQLException
	{
		//Création d'une instance de la base
		C_AccesBaseDonnees accesBD = new C_AccesBaseDonnees();
		accesBD.ouvrirConnexion();
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(laVersion);
		lesParametres.add(leProcessus);
		lesParametres.add(dateExport);
		int idStatement = 0;
		
		try
		{
			idStatement = accesBD.creerPreparedStatement("setVersionProcessusMAJ");
			accesBD.executerRequeteStockee(idStatement, "setVersionProcessusMAJ", lesParametres);
		}
		finally
		{
			//Fermeture de la conception
			accesBD.fermerStatement(idStatement);
			accesBD.fermerConnexion();
		}
	}	
	
	public synchronized static void modifierMaturite(String leProcessus, int niveauMaturite, String commentaireMaturite, Date datePassage,Date dateObjectif) throws SQLException
	{
		//Création d'une instance de la base
		C_AccesBaseDonnees accesBD = new C_AccesBaseDonnees();
		accesBD.ouvrirConnexion();
		accesBD.setAutoCommit(false);
		//Création de la liste des paramètres
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(new Integer(niveauMaturite));
		lesParametres.add(leProcessus);
		int idStatement = 0;

		try
		{
			idStatement = accesBD.creerPreparedStatement("setMaturiteProcessus");
			accesBD.executerRequeteStockee(idStatement, "setMaturiteProcessus", lesParametres);
			
			lesParametres.clear();
			lesParametres.add(commentaireMaturite);
			lesParametres.add(datePassage);
			lesParametres.add(dateObjectif);
			lesParametres.add(leProcessus);
			lesParametres.add(new Integer(niveauMaturite));
			idStatement = accesBD.creerPreparedStatement("setNiveauMaturite");
			accesBD.executerRequeteStockee(idStatement, "setNiveauMaturite", lesParametres);			
			
			E_NiveauMaturite unNiveau = new E_NiveauMaturite(niveauMaturite,commentaireMaturite,datePassage,dateObjectif);

			C_NiveauMaturite.modifierNiveauMaturite(leProcessus,unNiveau);
			setNiveauMaturiteProcessus(leProcessus, niveauMaturite);
			
			accesBD.commit();
		}
		catch (Exception e)
		{
			accesBD.rollback();
		}
		finally
		{
			//Fermeture de la conception
			accesBD.fermerStatement(idStatement);
			accesBD.fermerConnexion();
		}
	}

	public synchronized static int getNiveauMaturiteProcessus(String leProcessus)
	{
		return getProcessus(leProcessus).getNiveauMaturite();
	}

	public synchronized static void setNiveauMaturiteProcessus(String leProcessus, int niveauMaturite)
	{
		getProcessus(leProcessus).setNiveauMaturite(niveauMaturite);
	}

	public synchronized static String getDateExportFormatee(String leProcessus)
	{
		return getProcessus(leProcessus).getDateExportFormatee();
	}

	public static ArrayList getListeDateExportFormatee(String leProcessus)
	{
		return getProcessus(leProcessus).getListeDateExportFormatee();
	}
	
	public static String getNumeroVersion(String leProcessus,String dateExport)
	{
		return getProcessus(leProcessus).getnumeroVersion(dateExport);
	}
	
	public static void setNumeroVersion(String leProcessus, String dateExport, String numeroVersion)
	{
		getProcessus(leProcessus).setnumeroVersion(dateExport,numeroVersion);
	}
	
	synchronized static public HashMap getProcessus()
	{
		return lesProcessus;
	}
	
	synchronized static public E_MesureRepresentation getMesureRepresentation(String leProcessus, String dateExport)
	{
		return getProcessus(leProcessus).getMesureRepresentation(dateExport);
	}
	
	synchronized static public void ajouterMesureRepresentation(String leProcessus, E_MesureRepresentation laMesure)
	{
		getProcessus(leProcessus).ajouterMesureRepresentation(laMesure);
	}
	
	synchronized static public E_MesureAmelioration getMesureAmelioration(String leProcessus, String dateExport)
	{
		return getProcessus(leProcessus).getMesureAmelioration(dateExport);
	}
	
	synchronized static public void ajouterMesureAmelioration(String leProcessus, E_MesureAmelioration laMesure)
	{
		getProcessus(leProcessus).ajouterMesureAmelioration(laMesure);
	}	
	
	synchronized static public E_MesureAcces getMesureAcces(String leProcessus,int indice)
	{
		return getProcessus(leProcessus).getMesureAcces(indice);
	}
	
	synchronized static public ArrayList getMesuresAcces(String leProcessus)
	{
		return getProcessus(leProcessus).getListeMesuresAcces();
	}	
	
	synchronized static public void ajouterMesureAcces(String leProcessus, E_MesureAcces laMesure)
	{
		getProcessus(leProcessus).ajouterMesureAcces(laMesure);
	}
	
	synchronized static public E_MesureFormation getMesureFormation(String leProcessus,int indice)
	{
		return getProcessus(leProcessus).getMesureFormation(indice);
	}
	
	synchronized static public ArrayList getMesuresFormation(String leProcessus)
	{
		return getProcessus(leProcessus).getListeMesuresFormation();
	}	
	
	synchronized static public void ajouterMesureFormation(String leProcessus, E_MesureFormation laMesure)
	{
		getProcessus(leProcessus).ajouterMesureFormation(laMesure);
	}
	
	synchronized static public E_MesureUtilisation getMesureUtilisation(String leProcessus,int indice)
	{
		return getProcessus(leProcessus).getMesureUtilisation(indice);
	}
	
	synchronized static public ArrayList getMesuresUtilisation(String leProcessus)
	{
		return getProcessus(leProcessus).getListeMesuresUtilisation();
	}	
	
	synchronized static public void ajouterMesureUtilisation(String leProcessus, E_MesureUtilisation laMesure)
	{
		getProcessus(leProcessus).ajouterMesureUtilisation(laMesure);
	}
}