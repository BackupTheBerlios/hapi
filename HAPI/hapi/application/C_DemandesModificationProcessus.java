package hapi.application;

import hapi.application.metier.C_Composant;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_Demande;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.modeles.ModeleTable;
import hapi.donnees.modeles.ModeleTableDemandesModification;
import hapi.exception.ChampsVideException;
import hapi.exception.IdentifiantInconnuException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * @author Robin EYSSERIC
 *
 */
public class C_DemandesModificationProcessus
{
	// Modèle de la table contenant les demandes de modification
	// concernant un processus
	private static ModeleTableDemandesModification modele = null;

	/**
	 * Crée un modèle de table contenant l'ensemble des demandes de modification
	 * pour un processus donné.
	 * 
	 * @param idProcessus : identifiant du processus pour lequel on veut 
	 *  récupérer les demandes de modification.
	 * @return le modèle de table contenant toutes les demandes de modification
	 *  concernant le processus.
	 */
	public static ModeleTable getModeleDemandesModification(String idProcessus)
	{
		modele = new ModeleTableDemandesModification();

		//Création des colonnes et des en-tête
		modele.addColumn(Bundle.getText("OO_DemandeModificationProcessus_champ_date"));
		modele.addColumn(Bundle.getText("OO_DemandeModificationProcessus_champ_composant"));
		modele.addColumn(Bundle.getText("OO_DemandeModificationProcessus_champ_description"));
		modele.addColumn(Bundle.getText("OO_DemandeModificationProcessus_champ_type"));
		modele.addColumn(Bundle.getText("OO_DemandeModificationProcessus_champ_severite"));

		ArrayList demandes = C_GestionDemandes.getDemandesProcessus(idProcessus);
		SimpleDateFormat fdDate = new SimpleDateFormat(Bundle.DATE_MODEL);

		if (demandes != null)
		{
			for (int i = 0; i < demandes.size(); i++)
			{
				modele.getIdDemandes().add(new Integer(((E_Demande) demandes.get(i)).getIdDemande()));
				Vector uneLigne = new Vector();
				uneLigne.add(fdDate.format(((E_Demande) demandes.get(i)).getDateDemande()));
				uneLigne.add(((E_Demande) demandes.get(i)).getNomComposant());
				uneLigne.add(((E_Demande) demandes.get(i)).getDescription());
				uneLigne.add(((E_Demande) demandes.get(i)).getType());
				uneLigne.add(((E_Demande) demandes.get(i)).getSeverite()==0?"":new Integer(((E_Demande) demandes.get(i)).getSeverite()).toString());
				modele.addRow(uneLigne);
			}
		}

		return modele;
	}

	/**
	 * Crée un modèle de liste contenant l'ensemble des composants
	 * pour un processus donné.
	 * 
	 * @param idProcessus : identifiant du processus pour lequel on veut 
	 *  récupérer les composants.
	 * @return le modèle de liste contenant tous les composants du processus.
	 */
	public static DefaultComboBoxModel getModeleComposants(String idProcessus)
	{
		DefaultComboBoxModel modeleListeDeroulante = new DefaultComboBoxModel();

		Collection composants = C_Composant.getComposantsDuProcessus(idProcessus).values();

		Iterator itComp = composants.iterator();
		E_Composant composant;
		while (itComp.hasNext())
		{
			composant = (E_Composant) itComp.next();
			modeleListeDeroulante.addElement(composant.getNom());
		}

		return modeleListeDeroulante;
	}
	
	public static DefaultComboBoxModel getModeleType()
	{
		DefaultComboBoxModel modeleListeDeroulante = new DefaultComboBoxModel();

		modeleListeDeroulante.addElement(Bundle.getText("OO_DemandeModificationProcessus_TypePresentation"));
		modeleListeDeroulante.addElement(Bundle.getText("OO_DemandeModificationProcessus_TypeModele"));
		modeleListeDeroulante.addElement(Bundle.getText("OO_DemandeModificationProcessus_TypeDocumentation"));
		
		return modeleListeDeroulante;
	}	

	/**
	 * Ajoute une demande de modification en base de données.
	 * 
	 * @param idProc : identifiant du processus concerné par la demande.
	 * @param versionAModifier : version du processus concernée par la demande.
	 * @param composant : composant du processus concerné par la demande.
	 * @param description : description de la demande de modification.
	 * @param versionModifiee : version du processus implémentant par la demande.
	 * 
	 * @throws ChampsVideException : si la description est vide.
	 * @throws SQLException : si un probleme survient lors de l'ajout en base.
	 */
	public static void ajouterDemande(String idProc, String composant, String description, String type, int severite) throws Exception
	{
		// Teste si une description a été saisie
		if (description.length() == 0)
		{
			throw new ChampsVideException();
		}

		E_Demande demande = new E_Demande();

		// Vérification de la taille de la demande et troncature de celle-ci 
		// si nécéssaire 
		demande.setDescription(description);

		demande.setIdProcessus(idProc);

		// Récupération de la date du jour
		Calendar date = Calendar.getInstance();
		Date dateJour = new java.sql.Date(date.getTimeInMillis());

		demande.setDateDemande(dateJour);
		demande.setNomComposant(composant);
		demande.setType(type);
		demande.setSeverite(severite);

		// Enregistrement en mémoire
		C_GestionDemandes.ajouterDemande(idProc, demande);

		// Enregistrement en base
		C_GestionDemandes.enregistrerDemandesEnBase(idProc);
	}

	public static void archiverDemande(String idProc, int ligne) throws Exception
	{
		String dateDemande = modele.getValueAt(ligne, 0).toString();
		String nomComposant = modele.getValueAt(ligne, 1).toString();
		String description = modele.getValueAt(ligne, 2).toString();
		String type = modele.getValueAt(ligne, 3).toString();
		int severite = modele.getValueAt(ligne, 4).toString().equals("")?0:new Integer(modele.getValueAt(ligne, 4).toString()).intValue();

		C_HistoriqueModificationsProcessus.ajouterModification(idProc, dateDemande, nomComposant, description, type, severite);
		C_DemandesModificationProcessus.supprimerDemande(ligne, idProc);
	}

	/**
	 * Supprime dans la base de données la demande spécifiée.
	 * 
	 * @param ligne : indice de la demande dans le modèle de table.
	 * @throws SQLException : si un problème survient lors de la tentative 
	 * de suppression en base de données d'une demande de modification concernant un processus. 
	 */
	public static void supprimerDemande(int ligne, String idProcessus) throws SQLException
	{
		int idDemande = ((Integer) modele.getIdDemandes().get(ligne)).intValue();

		try
		{
			C_GestionDemandes.supprimerDemandeEnBase(idDemande);
			C_GestionDemandes.supprimerDemande(idDemande, idProcessus);
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