/*
 * Auteur Cédric
 */
package hapi.application.importation;

import hapi.application.C_Hapi;
import hapi.application.metier.temporaire.C_ActiviteTemporaire;
import hapi.application.metier.temporaire.C_ComposantTemporaire;
import hapi.application.metier.temporaire.C_DefinitionTemporaire;
import hapi.application.metier.temporaire.C_ElementPresentationTemporaire;
import hapi.application.metier.temporaire.C_EtatTemporaire;
import hapi.application.metier.temporaire.C_GuideTemporaire;
import hapi.application.metier.temporaire.C_InterfaceTemporaire;
import hapi.application.metier.temporaire.C_PaquetagePresentationTemporaire;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.metier.temporaire.C_ProduitTemporaire;
import hapi.application.metier.temporaire.C_RoleTemporaire;
import hapi.application.metier.temporaire.C_TypeGuideTemporaire;
import hapi.application.metier.temporaire.C_TypeProduitTemporaire;
import hapi.donnees.E_Utilisateur;
import hapi.donnees.metier.E_Activite;
import hapi.donnees.metier.E_Composant;
import hapi.donnees.metier.E_Definition;
import hapi.donnees.metier.E_ElementPresentation;
import hapi.donnees.metier.E_Etat;
import hapi.donnees.metier.E_Guide;
import hapi.donnees.metier.E_Interface;
import hapi.donnees.metier.E_PaquetagePresentation;
import hapi.donnees.metier.E_Processus;
import hapi.donnees.metier.E_Produit;
import hapi.donnees.metier.E_Role;
import hapi.donnees.metier.E_TypeGuide;
import hapi.donnees.metier.E_TypeProduit;

import javax.swing.ImageIcon;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Classe permettant de récupérer les évènements survenus lors du parsing d'un
 * fichier xml Parsage du fichier .dpe contenant un définition de processus
 */
public class ProcessusHandler extends DefaultHandler
{
	//Correction du composant tion
	private String caracteresPrecedents = new String();
	private String morceauPrecedent = new String();
	private boolean nouvelleBalise = true;
	private boolean elementOuvert = false;

	//nom de la balise courante
	private String baliseCourante = null;

	// entité processus
	private E_Processus processus = null;

	// entités liées au processus
	private E_Composant composant = null;
	private E_Role role = null;
	private E_Produit produit = null;
	private E_Definition definition = null;
	private E_Activite activite = null;
	private E_Interface einterface = null;
	private E_TypeProduit typeProduit = null;
	private E_Etat etat = null;
	private E_PaquetagePresentation paquetagePresentation = null;
	private E_ElementPresentation elementPresentation = null;
	private E_Guide guide = null;
	private E_TypeGuide typeGuide = null;

	// variables permettant de connaitre la section courante durant le parsage
	private boolean isRole = false;
	private boolean isActivite = false;
	private boolean isProduit = false;
	private boolean isDefinition = false;
	private boolean isProcessus = false;
	private boolean isComposant = false;
	private boolean isInterface = false;
	private boolean isTypeProduit = false;
	private boolean isEtat = false;
	private boolean isPaquetagePresentation = false;
	private boolean isElementPresentation = false;
	private boolean isGuide = false;
	private boolean isTypeGuide = false;

	public ProcessusHandler()
	{
		super();
		// Création du nouveau processus
		processus = new E_Processus(C_Hapi.PROCESSUS_ICONE);
	}

	/**
	 * On récupère l'évènement "je rentre sur une nouvelle balise"
	 */
	public void startElement(String uri, String localName, String baliseName, Attributes attributes)
	{

		elementOuvert = true;
		nouvelleBalise = true;
		baliseCourante = baliseName;
		// agir selon le nom de la balise courante
		if (baliseCourante.equals("processus"))
		{
			isProcessus = true;
		}
		if (baliseCourante.equals("liste_composant"))
		{
			isComposant = true;
		}
		if (baliseCourante.equals("composant"))
		{
			composant = new E_Composant(C_Hapi.COMPOSANT_ICONE);
		}
		if (baliseCourante.equals("liste_role"))
		{
			isRole = true;
		}
		if (baliseCourante.equals("role"))
		{
			role = new E_Role(C_Hapi.ROLE_ICONE);
		}
		if (!isDefinition && baliseCourante.equals("liste_activite"))
		{
			isActivite = true;
		}
		if (baliseCourante.equals("activite"))
		{
			activite = new E_Activite(C_Hapi.ACTIVITE_ICONE);
		}
		if (baliseCourante.equals("liste_produit"))
		{
			isProduit = true;
		}
		if (baliseCourante.equals("produit"))
		{
			produit = new E_Produit(C_Hapi.PRODUIT_ICONE);
		}
		if (baliseCourante.equals("liste_definitionTravail"))
		{
			isDefinition = true;
		}
		if (baliseCourante.equals("definitionTravail"))
		{
			definition = new E_Definition(C_Hapi.DEFINITION_ICONE);
		}
		if (baliseCourante.equals("liste_guide"))
		{
			isGuide = true;
		}
		if (baliseCourante.equals("guide"))
		{
			guide = new E_Guide(new ImageIcon());
		}
		if (baliseCourante.equals("liste_typeGuide"))
		{
			isTypeGuide = true;
		}
		if (baliseCourante.equals("typeGuide"))
		{
			typeGuide = new E_TypeGuide(new ImageIcon());
		}
		if (baliseCourante.equals("liste_paquetagePresentation"))
		{
			isPaquetagePresentation = true;
		}
		if (baliseCourante.equals("paquetagePresentation"))
		{
			paquetagePresentation = new E_PaquetagePresentation(new ImageIcon());
		}
		if (baliseCourante.equals("liste_elementPresentation"))
		{
			isElementPresentation = true;
		}
		if (baliseCourante.equals("elementPresentation"))
		{
			elementPresentation = new E_ElementPresentation(new ImageIcon());
		}
		if (baliseCourante.equals("liste_interface"))
		{
			isInterface = true;
		}
		if (baliseCourante.equals("interface"))
		{
			einterface = new E_Interface(new ImageIcon());
		}
		if (baliseCourante.equals("liste_typeProduit"))
		{
			isTypeProduit = true;
		}
		if (baliseCourante.equals("typeProduit"))
		{
			typeProduit = new E_TypeProduit(new ImageIcon());
		}
		if (baliseCourante.equals("liste_etat"))
		{
			isEtat = true;
		}
		if (baliseCourante.equals("etat"))
		{
			etat = new E_Etat(new ImageIcon());
		}
	}

	public void endElement(String namespace, String name, String raw)
	{
		// sauvegarde des entités
		if (raw.equals("processus"))
		{
			C_ProcessusTemporaire.setProcessus(processus);
			isProcessus = false;
		}
		if (raw.equals("liste_role"))
			isRole = false;
		if (raw.equals("liste_activite"))
			isActivite = false;
		if (raw.equals("liste_produit"))
			isProduit = false;
		if (raw.equals("liste_definitionTravail"))
			isDefinition = false;
		if (raw.equals("liste_composant"))
			isComposant = false;
		if (raw.equals("liste_guide"))
			isGuide = false;
		if (raw.equals("liste_typeGuide"))
			isTypeGuide = false;
		if (raw.equals("liste_paquetagePresentation"))
			isPaquetagePresentation = false;
		if (raw.equals("liste_elementPresentation"))
			isElementPresentation = false;
		if (raw.equals("liste_interface"))
			isInterface = false;
		if (raw.equals("liste_typeProduit"))
			isTypeProduit = false;
		if (raw.equals("liste_etat"))
			isEtat = false;

		if (raw.equals("role"))
		{
			//C_Role.ajouterRole(id_processus, role.getIdentifiant(), role);
			C_RoleTemporaire.ajouterRole(role.getIdentifiant(), role);
		}
		if (raw.equals("produit"))
		{
			//C_Produit.ajouterProduit(id_processus, produit.getIdentifiant(),
			// produit);
			C_ProduitTemporaire.ajouterProduit(produit.getIdentifiant(), produit);
		}
		if (raw.equals("activite"))
		{
			//C_Activite.ajouterActivite(id_processus,
			// activite.getIdentifiant(), activite);
			C_ActiviteTemporaire.ajouterActivite(activite.getIdentifiant(), activite);
		}
		if (raw.equals("definitionTravail"))
		{
			//C_Definition.ajouterDefinition(id_processus,
			// definition.getIdentifiant(), definition);
			C_DefinitionTemporaire.ajouterDefinition(definition.getIdentifiant(), definition);
		}
		if (raw.equals("composant"))
		{
			//C_Composant.ajouterComposant(id_processus,
			// composant.getIdentifiant(), composant);
			C_ComposantTemporaire.ajouterComposant(composant.getIdentifiant(), composant);
		}
		if (raw.equals("guide"))
		{
			//C_Guide.ajouterGuide(id_processus, guide.getIdentifiant(),
			// guide);
			C_GuideTemporaire.ajouterGuide(guide.getIdentifiant(), guide);
		}
		if (raw.equals("typeGuide"))
		{
			//C_TypeGuide.ajouterTypeGuide(id_processus,
			// typeGuide.getIdentifiant(), typeGuide);
			C_TypeGuideTemporaire.ajouterTypeGuide(typeGuide.getIdentifiant(), typeGuide);
		}
		if (raw.equals("paquetagePresentation"))
		{
			//C_PaquetagePresentation.ajouterPaquetagePresentation(id_processus,
			// paquetagePresentation.getIdentifiant(), paquetagePresentation);
			C_PaquetagePresentationTemporaire.ajouterPaquetagePresentation(paquetagePresentation.getIdentifiant(), paquetagePresentation);
		}
		if (raw.equals("elementPresentation"))
		{
			//C_ElementPresentation.ajouterElementPresentation(id_processus,
			// elementPresentation.getIdentifiant(), elementPresentation);
			C_ElementPresentationTemporaire.ajouterElementPresentation(elementPresentation.getIdentifiant(), elementPresentation);
		}
		if (raw.equals("interface"))
		{
			//C_Interface.ajouterInterface(id_processus,
			// einterface.getIdentifiant(), einterface);
			C_InterfaceTemporaire.ajouterInterface(einterface.getIdentifiant(), einterface);
		}
		if (raw.equals("typeProduit"))
		{
			//C_TypeProduit.ajouterTypeProduit(id_processus,
			// typeProduit.getIdentifiant(), typeProduit);
			C_TypeProduitTemporaire.ajouterTypeProduit(typeProduit.getIdentifiant(), typeProduit);
		}
		if (raw.equals("etat"))
		{
			//C_Etat.ajouterEtat(id_processus, etat.getIdentifiant(), etat);
			C_EtatTemporaire.ajouterEtat(etat.getIdentifiant(), etat);
		}

		elementOuvert = false;
	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		if (elementOuvert)
		{
			String valeur = null;

			//Correction du composant tion
			if (!nouvelleBalise)
			{
				// récupération des valeurs des balises
				valeur = new String(buf, offset, len);
				valeur = caracteresPrecedents + valeur;
				caracteresPrecedents = valeur;
			}
			else
			{
				// récupération des valeurs des balises
				valeur = new String(buf, offset, len);
				caracteresPrecedents = valeur;
				morceauPrecedent = valeur;
			}

			nouvelleBalise = false;

			if (!valeur.trim().equals(""))
			{
				//if (valeur.charAt(valeur.length()-1) == '\n')
				//valeur = valeur.substring(0,valeur.length()-1);
				//System.out.println(valeur);
				if (isProcessus)
				{
					if (baliseCourante.equals("id"))
					{
						processus.setId(valeur);
						processus.setNomResponsable(E_Utilisateur.getNom());
						processus.setPrenomResponsable(E_Utilisateur.getPrenom());
					}
					if (baliseCourante.equals("nom"))
					{
						processus.setNom(valeur);
					}
					if (baliseCourante.equals("nomAuteur"))
					{
						processus.setNomAuteur(valeur);
					}
					if (baliseCourante.equals("emailAuteur"))
					{
						processus.setEmailAuteur(valeur);
					}
					if (baliseCourante.equals("dateExport"))
					{
						if (valeur.equals(morceauPrecedent))
							processus.addDateExport(valeur, "");
						else
							processus.replaceDateExport(morceauPrecedent, valeur, "");
					}
					if (baliseCourante.equals("cheminGeneration"))
					{
						processus.setCheminGeneration(valeur);
					}
					if (baliseCourante.equals("description"))
					{
						processus.setDescription(valeur);
					}
					if (baliseCourante.equals("composantId"))
					{
						if (valeur.equals(morceauPrecedent))
							processus.ajouterIdComposant(valeur);
						else
							processus.replaceIdComposant(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("paquetagePresentationId"))
					{
						if (valeur.equals(morceauPrecedent))
							processus.ajouterPaquetagePresentation(valeur);
						else
							processus.replacePaquetagePresentation(morceauPrecedent, valeur);
					}
				}
				else if (isComposant)
				{
					if (baliseCourante.equals("id"))
					{
						composant.setId(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						composant.setNom(valeur);
					}
					if (baliseCourante.equals("version"))
					{
						composant.setVersion(valeur);
					}
					if (baliseCourante.equals("nomAuteur"))
					{
						composant.setNomAuteur(valeur);
					}
					if (baliseCourante.equals("emailAuteur"))
					{
						composant.setEmailAuteur(valeur);
					}
					if (baliseCourante.equals("datePlacement"))
					{
						composant.setDatePlacement(valeur);
					}
					if (baliseCourante.equals("description"))
					{
						composant.setDescription(valeur);
					}
					if (baliseCourante.equals("roleId"))
					{
						if (valeur.equals(morceauPrecedent))
							composant.ajouterIdRole(valeur);
						else
							composant.replaceIdRole(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("produitId"))
					{
						if (valeur.equals(morceauPrecedent))
							composant.ajouterIdProduit(valeur);
						else
							composant.replaceIdProduit(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("definitionTravailId"))
					{
						if (valeur.equals(morceauPrecedent))
							composant.ajouterIdDefinitionTravail(valeur);
						else
							composant.replaceIdDefinitionTravail(morceauPrecedent, valeur);
					}

					if (baliseCourante.equals("interfaceRequise"))
					{
						composant.setInterfaceRequise(valeur);
					}
					if (baliseCourante.equals("interfaceFournie"))
					{
						composant.setInterfaceFournie(valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						composant.setElementPresentationId(valeur);
					}

				}
				else if (isRole)
				{
					if (baliseCourante.equals("id"))
					{
						role.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						role.setNom(valeur);
					}
					if (baliseCourante.equals("agregatComposant"))
					{
						role.setAgregatComposant(valeur);
					}
					if (baliseCourante.equals("responsabiliteProduit"))
					{
						if (valeur.equals(morceauPrecedent))
							role.ajouterIdResponsabiliteProduit(valeur);
						else
							role.replaceIdResponsabiliteProduit(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("participationActivite"))
					{
						if (valeur.equals(morceauPrecedent))
							role.ajouterIdParticipationActivite(valeur);
						else
							role.replaceIdParticipationActivite(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						role.setElementPresentationId(valeur);
					}
				}
				else if (isActivite)
				{
					if (baliseCourante.equals("id"))
					{
						activite.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						activite.setNom(valeur);
					}
					if (baliseCourante.equals("participationRole"))
					{
						activite.setParticipationRole(valeur);
					}
					if (baliseCourante.equals("agregatDefinitionTravail"))
					{
						activite.setAgregatDefinitionTravail(valeur);
					}
					if (baliseCourante.equals("entreeProduit"))
					{
						if (valeur.equals(morceauPrecedent))
							activite.ajouterProduitEntree(valeur);
						else
							activite.replaceProduitEntree(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("sortieProduit"))
					{
						if (valeur.equals(morceauPrecedent))
							activite.ajouterProduitSortie(valeur);
						else
							activite.replaceProduitSortie(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						activite.setElementPresentationId(valeur);
					}
				}
				else if (isProduit)
				{
					if (baliseCourante.equals("id"))
					{
						produit.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						produit.setNom(valeur);
					}
					if (baliseCourante.equals("agregatComposant"))
					{
						produit.setAgregatComposant(valeur);
					}
					if (baliseCourante.equals("responsabiliteRole"))
					{
						produit.setIdResponsabilite(valeur);
					}
					if (baliseCourante.equals("typeProduitId"))
					{
						produit.setTypeProduitId(valeur);
					}
					if (baliseCourante.equals("interfaceId"))
					{
						if (valeur.equals(morceauPrecedent))
							produit.ajouterIdInterface(valeur);
						else
							produit.replaceIdInterface(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("etatId"))
					{
						if (valeur.equals(morceauPrecedent))
							produit.ajouterIdEtat(valeur);
						else
							produit.replaceIdEtat(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						produit.setElementPresentationId(valeur);
					}
					if (baliseCourante.equals("entreeActivite"))
					{
						if (valeur.equals(morceauPrecedent))
							produit.ajouterIdEntreeActivite(valeur);
						else
							produit.replaceIdEntreeActivite(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("sortieActivite"))
					{
						if (valeur.equals(morceauPrecedent))
							produit.ajouterIdSortieActivite(valeur);
						else
							produit.replaceIdSortieActivite(morceauPrecedent, valeur);
					}
				}
				else if (isDefinition)
				{
					if (baliseCourante.equals("id"))
					{
						definition.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						definition.setNom(valeur);
					}
					if (baliseCourante.equals("agregatComposant"))
					{
						definition.setAgregatComposant(valeur);
					}
					if (baliseCourante.equals("activiteId"))
					{
						if (valeur.equals(morceauPrecedent))
							definition.ajouterIdActivite(valeur);
						else
							definition.replaceIdActivite(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						definition.setElementPresentationId(valeur);
					}
				}
				if (isGuide)
				{
					if (baliseCourante.equals("id"))
					{
						guide.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						guide.setNom(valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						guide.setElementPresentationId(valeur);
					}
					if (baliseCourante.equals("typeGuideId"))
					{
						guide.setTypeGuideId(valeur);
					}
				}
				if (isTypeGuide)
				{
					if (baliseCourante.equals("id"))
					{
						typeGuide.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						typeGuide.setNom(valeur);
					}
				}
				if (isPaquetagePresentation)
				{
					if (baliseCourante.equals("id"))
					{
						paquetagePresentation.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						paquetagePresentation.setNom(valeur);
					}
					if (baliseCourante.equals("dossierIcone"))
					{
						paquetagePresentation.setDossierIcone(valeur);
					}
					if (baliseCourante.equals("dossierContenu"))
					{
						paquetagePresentation.setDossierContenu(valeur);
					}
					if (baliseCourante.equals("agregeElementPresentation"))
					{
						if (valeur.equals(morceauPrecedent))
							paquetagePresentation.ajouterAgregeElementPresentation(valeur);
						else
							paquetagePresentation.replaceAgregeElementPresentation(morceauPrecedent, valeur);
					}
					if (baliseCourante.equals("elementPresentationId"))
					{
						paquetagePresentation.setElementPresentationId(valeur);
					}

				}
				if (isElementPresentation)
				{
					if (baliseCourante.equals("id"))
					{
						elementPresentation.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						elementPresentation.setNom(valeur);
					}
					if (baliseCourante.equals("cheminIcone"))
					{
						elementPresentation.setCheminIcone(valeur);
					}
					if (baliseCourante.equals("cheminContenu"))
					{
						elementPresentation.setCheminContenu(valeur);
					}
					if (baliseCourante.equals("cheminPage"))
					{
						elementPresentation.setCheminPage(valeur);
					}
					if (baliseCourante.equals("description"))
					{
						elementPresentation.setDescription(valeur);
					}
					if (baliseCourante.equals("guideId"))
					{
						if (valeur.equals(morceauPrecedent))
							elementPresentation.ajouterIdGuide(valeur);
						else
							elementPresentation.replaceIdGuide(morceauPrecedent, valeur);
					}
				}
				if (isInterface)
				{
					if (baliseCourante.equals("id"))
					{
						einterface.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("interfaceRequiseComposant"))
					{
						einterface.setInterfaceRequiseComposant(valeur);
					}
					if (baliseCourante.equals("interfaceFournieComposant"))
					{
						einterface.setInterfaceFournieComposant(valeur);
					}
					if (baliseCourante.equals("interfaceProduit"))
					{
						if (valeur.equals(morceauPrecedent))
							einterface.ajouterInterfaceProduit(valeur);
						else
							einterface.replaceInterfaceProduit(morceauPrecedent, valeur);
					}

				}
				if (isTypeProduit)
				{
					if (baliseCourante.equals("id"))
					{
						typeProduit.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						typeProduit.setNom(valeur);
					}

				}
				if (isEtat)
				{
					if (baliseCourante.equals("id"))
					{
						etat.setIdentifiant(valeur);
					}
					if (baliseCourante.equals("nom"))
					{
						etat.setNom(valeur);
					}
				}
			}

			if (!valeur.equals(morceauPrecedent))
				morceauPrecedent = valeur;
		}

	}

	public E_Processus getProcessus()
	{
		return processus;
	}
}