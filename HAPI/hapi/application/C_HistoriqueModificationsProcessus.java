package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_Modification;
import hapi.donnees.metier.E_Processus;
import hapi.donnees.modeles.ModeleTable;
import hapi.donnees.modeles.ModeleTableDemandesModification;
import hapi.exception.ChampsVideException;
import hapi.exception.IdentifiantInconnuException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_HistoriqueModificationsProcessus
{
	// Modèle de la table contenant les demandes de modification
	// concernant un processus
	private static ModeleTableDemandesModification modele = new ModeleTableDemandesModification();

	public static void reinitModele()
	{
		modele = new ModeleTableDemandesModification();
	}
	
	/**
	 * Crée un modèle de table contenant l'ensemble des demandes de modification
	 * pour un processus donné.
	 * 
	 * @param idProcessus : identifiant du processus pour lequel on veut 
	 *  récupérer les demandes de modification.
	 * @return le modèle de table contenant toutes les demandes de modification
	 *  concernant le processus. 
	 */
	public static ModeleTable creerModeleHistoriqueModification(String idProcessus)
	{				
		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_versionModification"));
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_composant"));
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_description"));
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_date"));
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_type"));
		modele.addColumn(Bundle.getText("OO_HistoriqueModificationProcessus_champ_severite"));

		ArrayList modifications = C_GestionModifications.getModificationsProcessus(idProcessus);

		if (modifications != null)
		{
			for (int i = 0; i < modifications.size(); i++)
			{
				modele.getIdDemandes().add(new Integer(((E_Modification) modifications.get(i)).getIdModification()));
				Vector uneLigne = new Vector();
				uneLigne.add(((E_Modification) modifications.get(i)).getVersionModification());
				uneLigne.add(((E_Modification) modifications.get(i)).getNomComposant());
				uneLigne.add(((E_Modification) modifications.get(i)).getDescription());
				uneLigne.add(((E_Modification) modifications.get(i)).getDateDemande());
				uneLigne.add(((E_Modification) modifications.get(i)).getType());
				uneLigne.add(((E_Modification) modifications.get(i)).getSeverite()==0?"":new Integer(((E_Modification) modifications.get(i)).getSeverite()).toString());
				modele.addRow(uneLigne);
			}
		}
		return modele;
	}	

	public static ModeleTable getModeleHistoriqueModification(String idProcessus)
	{
		return modele;
	}
	
	/**
	 * Ajoute une modification en base de données.
	 * 
	 * @param idProc : identifiant du processus concerné par la modification.
	 * @param versionAModifier : version du processus concernée par la modification.
	 * @param composant : composant du processus concerné par la modification.
	 * @param description : description de la modification.
	 * @param versionModifiee : version du processus implémentant la modification.
	 * 
	 * @throws ChampsVideException : si la description est vide.
	 * @throws SQLException : si un probleme survient lors de l'ajout en base.
	 */
	public static void ajouterModification(String idProc, String date, String composant, String description, String type, int severite) throws Exception
	{
		E_Modification modification = new E_Modification();

		modification.setIdProcessus(idProc);
		modification.setDateDemande(date);
		modification.setVersionModification(((E_Processus) C_Processus.getProcessus(idProc)).getnumeroVersion(((E_Processus) C_Processus.getProcessus(idProc)).getDateExport()));
		modification.setNomComposant(composant);
		modification.setDescription(description);
		modification.setType(type);
		modification.setSeverite(severite);

		// Enregistrement en mémoire
		C_GestionModifications.ajouterModification(idProc, modification);

		// Enregistrement en base
		C_GestionModifications.enregistrerModificationsEnBase(idProc);		
		
		//ArrayList modifications = C_GestionModifications.getModificationsProcessus(idProc);
		
		// Ajout d'une nouvelle ligne dans la table
		//modele.getIdDemandes().add(new Integer(((E_Modification) modifications.get(modifications.size()-1)).getIdModification()));
		modele.getIdDemandes().add(new Integer(modification.getIdModification()));
		Vector uneLigne = new Vector();		
		uneLigne.add(((E_Processus) C_Processus.getProcessus(idProc)).getnumeroVersion(((E_Processus) C_Processus.getProcessus(idProc)).getDateExport()));
		uneLigne.add(composant);
		uneLigne.add(description);
		uneLigne.add(date);
		uneLigne.add(type);
		uneLigne.add(severite==0?"":new Integer(severite).toString());
		modele.addRow(uneLigne);

		modele.newDataAvailable(new TableModelEvent(modele));
	}

	/**
	 * Supprime dans la base de données la modification spécifiée.
	 * 
	 * @param ligne : indice de la modification dans le modèle de table.
	 * @throws SQLException : si un problème survient lors de la tentative 
	 * de suppression en base de données d'une modification concernant un processus. 
	 */
	public static void supprimerModification(int ligne, String idProcessus) throws SQLException
	{
		int idModif = ((Integer) modele.getIdDemandes().get(ligne)).intValue();

		try
		{
			C_GestionModifications.supprimerModificationEnBase(idModif);
			C_GestionModifications.supprimerModification(idModif, idProcessus);
			
			// Suppression de la ligne dans le modèle
			//modele.removeRow(ligne);					
		}
		catch (SQLException e)
		{
			throw new SQLException();
		}
		catch (IdentifiantInconnuException e2)
		{
			// Normalement on doit pas pouvoir passer ici
		}
	}

}