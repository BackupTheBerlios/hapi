package hapi.application.importation;

import hapi.application.C_Hapi;
import hapi.application.C_Mesures;
import hapi.application.metier.C_Evaluation;
import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.temporaire.C_ProcessusTemporaire;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_Artefact;
import hapi.donnees.E_Membre;
import hapi.donnees.E_Mesures;
import hapi.donnees.E_Tache;
import hapi.donnees.metier.E_ExecutionProcessus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Vincent et Stéphane
 */
public class MesuresHandler extends DefaultHandler
{
	//format des dates qui sont parsées dans le fichier de mesures
	private static final String FORMAT_DATE = "dd/MM/yyyy";

	private String baliseCourante = null;
	//l'exécution de processus importée
	private E_ExecutionProcessus eExecProcess = null;
	//identifiant temporatire utilisé pour le remplissage des map
	private String idTemporaire = null;
	//nom temporatire utilisé pour le remplissage des map
	private String nomTemporaire = null;
	// numero d'IT temporatire utilisé pour le remplissage des map
	private String numITTemporaire = null;
	// date de début d'IT temporatire utilisé pour le remplissage des map
	private String dateDebutITTemporaire = null;
	// Temps passé pour une tache temporaire utilisé pour le remplissage des map
	private String tempsPasseTacheTemporaire = null;
	//listes temporaires utilisées pour le parsage
	//tous les membres
	private HashMap listeMembres = new HashMap();
	//tous les artefacts
	private HashMap listeArtefacts = new HashMap();
	// toutes les taches 
	private HashMap listeTaches = new HashMap();
	//toutes les it
	private ArrayList listeIterations = new ArrayList();
	//Tous les liens taches-iterations
	private HashMap liensTachesIterations = new HashMap();
	//Tous les liens responsables-artefacts
	private HashMap liensResponsablesArtefacts = new HashMap();
	//Tous les liens membres-taches
	private HashMap liensMembresTaches = new HashMap();
	//Tous les liens taches-artefacts en entrée 
	private HashMap liensArtefactsEntreeTaches = new HashMap();
	//Tous les liens taches-artefacts en sortie
	private HashMap liensArtefactsSortieTaches = new HashMap();
	//Tous les liens produits-artefacts
	private HashMap liensProduitsArtefacts = new HashMap();
	//Tous les liens activités-taches
	private HashMap liensActivitesTaches = new HashMap();
	//Tous les liens roles-membres
	private HashMap liensMembresRoles = new HashMap();

	// variables permettant de connaitre la section courante durant le parsage
	//attributs du projet
	//private boolean isProcessus = false;
	private boolean isProjet = false;
	//tous les membres du projet
	private boolean isListeMembres = false;
	//toutes les it du projet
	private boolean isListeIterations = false;
	//toutes les taches du projet
	private boolean isListeTaches = false;
	//tous les artefacts du projet
	private boolean isListeArtefacts = false;
	//Lien tache-iteration
	private boolean isListeIterationTache = false;
	private boolean isIterationTache_ListeIdTache = false;
	//Lien responsable-artefact
	private boolean isListeMembreArtefact = false;
	private boolean isMembreArtefact_ListeArtefact = false;
	//Lien membre-tache 
	private boolean isListeMembreTache = false;
	private boolean isMembreTache_ListeTache = false;
	//Lien tache-artefact en entree
	private boolean isListeTacheArtefactEntree = false;
	private boolean isTacheArtefactEntree_ListeArtefact = false;
	//Lien tache-artefact en sortie
	private boolean isListeTacheArtefactSortie = false;
	private boolean isTacheArtefactSortie_ListeArtefact = false;
	//Lien produit-artefact
	private boolean isListeProduitArtefact = false;
	private boolean isProduitArtefact_ListeIdArtefact = false;
	//Lien activité-tache
	private boolean isListeActiviteTache = false;
	private boolean isActiviteTache_ListeIdTache = false;
	//Lien role-membre
	private boolean isMembreRole = false;
	private boolean isMembreRole_ListeRole = false;

	public MesuresHandler()
	{
		super();
		//création d'une nouvelle exécution de processus utilisée lors du parsage du fichier XML
		this.eExecProcess = new E_ExecutionProcessus(C_ProcessusTemporaire.get().getDateExport(), C_Hapi.PROJET_ICONE, -1);
	}
	// On récupère l'évènement "je rentre sur une nouvelle balise"
	public void startElement(String uri, String localName, String baliseName, Attributes attributes)
	{
		this.baliseCourante = baliseName;
		/*if(this.baliseCourante.equals("processus"))
			this.isProcessus = true;*/
		if (this.baliseCourante.equals("projet"))
			this.isProjet = true;
		else if (this.baliseCourante.equals("listeMembres"))
			this.isListeMembres = true;
		else if (this.baliseCourante.equals("listeIterations"))
			this.isListeIterations = true;
		else if (this.baliseCourante.equals("listeTaches"))
			this.isListeTaches = true;
		else if (this.baliseCourante.equals("listeArtefacts"))
			this.isListeArtefacts = true;
		else if (this.baliseCourante.equals("listeIterationTache"))
			this.isListeIterationTache = true;
		else if (this.isListeIterationTache && this.baliseCourante.equals("listeIdTache"))
			this.isIterationTache_ListeIdTache = true;
		else if (this.baliseCourante.equals("listeMembreArtefact"))
			this.isListeMembreArtefact = true;
		else if (this.isListeMembreArtefact && this.baliseCourante.equals("listeArtefact"))
			this.isMembreArtefact_ListeArtefact = true;
		else if (this.baliseCourante.equals("listeMembreTache"))
			this.isListeMembreTache = true;
		else if (this.isListeMembreTache && this.baliseCourante.equals("listeTache"))
			this.isMembreTache_ListeTache = true;
		else if (this.baliseCourante.equals("listeTacheArtefact_Entree"))
			this.isListeTacheArtefactEntree = true;
		else if (this.isListeTacheArtefactEntree && this.baliseCourante.equals("listeArtefact"))
			this.isTacheArtefactEntree_ListeArtefact = true;
		else if (this.baliseCourante.equals("listeTacheArtefact_Sortie"))
			this.isListeTacheArtefactSortie = true;
		else if (this.isListeTacheArtefactSortie && this.baliseCourante.equals("listeArtefact"))
			this.isTacheArtefactSortie_ListeArtefact = true;
		else if (this.baliseCourante.equals("listeProduitArtefact"))
			this.isListeProduitArtefact = true;
		else if (this.isListeProduitArtefact && this.baliseCourante.equals("listeIdArtefact"))
			this.isProduitArtefact_ListeIdArtefact = true;
		else if (this.baliseCourante.equals("listeActiviteTache"))
			this.isListeActiviteTache = true;
		else if (this.isListeActiviteTache && this.baliseCourante.equals("listeIdTache"))
			this.isActiviteTache_ListeIdTache = true;
		else if (this.baliseCourante.equals("MembreRole"))
			this.isMembreRole = true;
		else if (this.isMembreRole && this.baliseCourante.equals("listeRole"))
			this.isMembreRole_ListeRole = true;
	}

	// On récupère l'évènement "je sors d'une balise"
	public void endElement(String namespace, String name, String raw)
	{
		/*if(raw.equals("processus"))
			this.isProcessus = false;*/
	    if (raw.equals("projet"))
			this.isProjet = false;
		else if (raw.equals("listeMembres"))
			this.isListeMembres = false;
		else if (raw.equals("listeIterations"))
			this.isListeIterations = false;
		else if (raw.equals("listeTaches"))
			this.isListeTaches = false;
		else if (raw.equals("listeArtefacts"))
			this.isListeArtefacts = false;
		else if (raw.equals("listeIterationTache"))
			this.isListeIterationTache = false;
		else if (this.isListeIterationTache && raw.equals("listeIdTache"))
			this.isIterationTache_ListeIdTache = false;
		else if (raw.equals("listeMembreArtefact"))
			this.isListeMembreArtefact = false;
		else if (this.isListeMembreArtefact && raw.equals("listeArtefact"))
			this.isMembreArtefact_ListeArtefact = false;
		else if (raw.equals("listeMembreTache"))
			this.isListeMembreTache = false;
		else if (this.isListeMembreTache && raw.equals("listeTache"))
			this.isMembreTache_ListeTache = false;
		else if (raw.equals("listeTacheArtefact_Entree"))
			this.isListeTacheArtefactEntree = false;
		else if (this.isListeTacheArtefactEntree && raw.equals("listeArtefact"))
			this.isTacheArtefactEntree_ListeArtefact = false;
		else if (raw.equals("listeTacheArtefact_Sortie"))
			this.isListeTacheArtefactSortie = false;
		else if (this.isListeTacheArtefactSortie && raw.equals("listeArtefact"))
			this.isTacheArtefactSortie_ListeArtefact = false;
		else if (raw.equals("listeProduitArtefact"))
			this.isListeProduitArtefact = false;
		else if (this.isListeProduitArtefact && raw.equals("listeIdArtefact"))
			this.isProduitArtefact_ListeIdArtefact = false;
		else if (raw.equals("listeActiviteTache"))
			this.isListeActiviteTache = false;
		else if (this.isListeActiviteTache && raw.equals("listeIdTache"))
			this.isActiviteTache_ListeIdTache = false;
		else if (raw.equals("MembreRole"))
			this.isMembreRole = false;
		else if (this.isMembreRole && raw.equals("listeRole"))
			this.isMembreRole_ListeRole = false;
	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		// récupération des valeurs des balises
		String valeur = new String(buf, offset, len);

		if (!valeur.trim().equals(""))
		{
			//System.out.println(valeur);
			try
			{
				/*if(this.isProcessus)
				{
					if (baliseCourante.equals("id"))
						this.idProcessus = valeur;
					else if (baliseCourante.equals("dateExport"))
					//création d'une nouvelle exécution de processus utilisée lors du parsage du fichier XML
						this.eExecProcess = new E_ExecutionProcessus(valeur);
				}
				else */
				if (this.isProjet)
				{
					if (this.baliseCourante.equals("id"))
						this.eExecProcess.setId(Integer.parseInt(valeur));
					else if (this.baliseCourante.equals("nom"))
						this.eExecProcess.setNom(valeur);
					else if (this.baliseCourante.equals("description"))
						this.eExecProcess.setDescription(valeur);
					else if (this.baliseCourante.equals("dateDebut"))
					{
						SimpleDateFormat dfDateDeb = new SimpleDateFormat(FORMAT_DATE);
						this.eExecProcess.setDateDebut(dfDateDeb.parse(valeur));
					}
					else if (this.baliseCourante.equals("dateFin"))
					{
						SimpleDateFormat dfDateFin = new SimpleDateFormat(FORMAT_DATE);
						this.eExecProcess.setDateFin(dfDateFin.parse(valeur));
					}
				}
				else if (this.isListeMembres)
				{
					if (this.baliseCourante.equals("id"))
						this.idTemporaire = valeur;
					else if (this.baliseCourante.equals("nom"))
						this.listeMembres.put(idTemporaire, new E_Membre(idTemporaire, valeur, new ImageIcon()));
				}
				else if (this.isListeIterations)
				{
					if (this.baliseCourante.equals("id"))
						this.idTemporaire = valeur;
					else if (this.baliseCourante.equals("numero"))
						this.numITTemporaire = valeur;
					else if (this.baliseCourante.equals("dateDebutReelle"))
						this.dateDebutITTemporaire = valeur;
					else if (this.baliseCourante.equals("dateFinReelle"))
					{
						try
						{
							SimpleDateFormat dfDateDebutReelle = new SimpleDateFormat(FORMAT_DATE);
							SimpleDateFormat dfDateFinReelle = new SimpleDateFormat(FORMAT_DATE);
							this.listeIterations.add(new Iteration(idTemporaire, Integer.parseInt(this.numITTemporaire), dfDateDebutReelle.parse(this.dateDebutITTemporaire), dfDateFinReelle.parse(valeur)));
						} /* on ne fait rien si erreur de parsage de la date de fin réelle d'IT : 
																		cela signifie que l'iteration n'est pas terminée, donc on ne l'ajoute pas à la liste */
						catch (ParseException pe)
						{}
					}
				}
				else if (this.isListeTaches)
				{
					if (this.baliseCourante.equals("id"))
						this.idTemporaire = valeur;
					else if (this.baliseCourante.equals("nom"))
						this.nomTemporaire = valeur;
					else if (this.baliseCourante.equals("tempsPasse"))
						this.tempsPasseTacheTemporaire = valeur;
					else if (this.baliseCourante.equals("dateFinReelle"))
					{
						try
						{
							SimpleDateFormat dfDateFinReelle = new SimpleDateFormat(FORMAT_DATE);
							this.listeTaches.put(idTemporaire, new E_Tache(idTemporaire, nomTemporaire, Double.parseDouble(tempsPasseTacheTemporaire), dfDateFinReelle.parse(valeur), new ImageIcon()));
						} /* on ne fait rien si erreur de parsage de la date de fin réelle de la tache : 
																		cela signifie que la tache n'est pas terminée, donc on ne l'ajoute pas à la liste */
						catch (ParseException pe)
						{}
					}
				}
				else if (this.isListeArtefacts)
				{
					if (this.baliseCourante.equals("id"))
						this.idTemporaire = valeur;
					else if (this.baliseCourante.equals("nom"))
						this.listeArtefacts.put(idTemporaire, new E_Artefact(idTemporaire, valeur, new ImageIcon()));
				}
				else if (this.isListeIterationTache)
				{
					if (this.isIterationTache_ListeIdTache && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensTachesIterations.get(idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idIteration"))
					{
						this.idTemporaire = valeur;
						if (!this.liensTachesIterations.containsKey(this.idTemporaire))
							this.liensTachesIterations.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeMembreArtefact)
				{
					if (this.isMembreArtefact_ListeArtefact && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensResponsablesArtefacts.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idMembre"))
					{
						this.idTemporaire = valeur;
						if (!this.liensResponsablesArtefacts.containsKey(this.idTemporaire))
							this.liensResponsablesArtefacts.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeMembreTache)
				{
					if (this.isMembreTache_ListeTache && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensMembresTaches.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idMembre"))
					{
						this.idTemporaire = valeur;
						if (!this.liensMembresTaches.containsKey(this.idTemporaire))
							this.liensMembresTaches.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeTacheArtefactEntree)
				{
					if (this.isTacheArtefactEntree_ListeArtefact && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensArtefactsEntreeTaches.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idTache"))
					{
						this.idTemporaire = valeur;
						if (!this.liensArtefactsEntreeTaches.containsKey(this.idTemporaire))
							this.liensArtefactsEntreeTaches.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeTacheArtefactSortie)
				{
					if (this.isTacheArtefactSortie_ListeArtefact && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensArtefactsSortieTaches.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idTache"))
					{
						this.idTemporaire = valeur;
						if (!this.liensArtefactsSortieTaches.containsKey(this.idTemporaire))
							this.liensArtefactsSortieTaches.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeProduitArtefact)
				{
					if (this.isProduitArtefact_ListeIdArtefact && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensProduitsArtefacts.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idProduit"))
					{
						this.idTemporaire = valeur;
						if (!this.liensProduitsArtefacts.containsKey(this.idTemporaire))
							this.liensProduitsArtefacts.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isListeActiviteTache)
				{
					if (this.isActiviteTache_ListeIdTache && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensActivitesTaches.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("idActivite"))
					{
						this.idTemporaire = valeur;
						if (!this.liensActivitesTaches.containsKey(this.idTemporaire))
							this.liensActivitesTaches.put(this.idTemporaire, new ArrayList());
					}
				}
				else if (this.isMembreRole)
				{
					if (this.isMembreRole_ListeRole && this.baliseCourante.equals("id"))
						 ((ArrayList) this.liensMembresRoles.get(this.idTemporaire)).add(valeur);
					else if (this.baliseCourante.equals("id"))
					{
						this.idTemporaire = valeur;
						if (!this.liensMembresRoles.containsKey(this.idTemporaire))
							this.liensMembresRoles.put(this.idTemporaire, new ArrayList());
					}
				}
			}
			catch (NullPointerException npe)
			{
				throw new SAXException(Bundle.getText("C_Importation_fichier_execution_processus_incorrect"));
			}
			catch (ParseException pe)
			{
				throw new SAXException(Bundle.getText("C_Importation_fichier_execution_processus_incorrect"));
			}
			catch (NumberFormatException nfe)
			{
				throw new SAXException(Bundle.getText("C_Importation_fichier_execution_processus_incorrect"));
			}
		}
	}

	public void endDocument() throws SAXException
	{
		this.recupererActivitesProduitsRoles();
	}

	public void recupererActivitesProduitsRoles()
	{
		// On réinitialise les mesures
		C_Mesures.reinitialiserLesMesures();

		// On récupère les IT déjà enregistrées
		HashMap lesITdejaEnregistrees = null;

		E_ExecutionProcessus eExec = C_ExecutionProcessus.getExecutionProcessusAPartirDeLeurId(C_ProcessusTemporaire.get().getIdentifiant(), String.valueOf(this.eExecProcess.getId()), eExecProcess.getDateDebut(), eExecProcess.getDateFin());
		if (eExec != null)
			lesITdejaEnregistrees = C_Evaluation.getEvaluationPourUneExecProc(C_ProcessusTemporaire.get().getIdentifiant(), Integer.parseInt(eExec.getIdentifiant()));

		// On fait le traitement pour chaque itération
		for (int indIT = 0; indIT < listeIterations.size(); indIT++)
		{
			Iteration iteration = ((Iteration) listeIterations.get(indIT));
			if ((lesITdejaEnregistrees == null) || (!lesITdejaEnregistrees.containsKey(new Integer(iteration.getNumIteration()))))
			{
				//Liste d'activités, de produits et de rôles utilisés lors de l'itération
				HashMap listeActivitesIT = new HashMap();
				HashMap listeProduitsIT = new HashMap();
				HashMap listeRolesIT = new HashMap();

				// on recupere les taches pour l'iteration
				ArrayList listeTachesIT = (ArrayList) liensTachesIterations.get(iteration.getIdIteration());

				// On recupere les activites correspondant aux taches
				Set lesActivites = liensActivitesTaches.keySet();
				ArrayList lesTachesAppartenantActivite = new ArrayList();
				Iterator it = lesActivites.iterator();
				while (it.hasNext())
				{
					String idActivite = (String) it.next();
					ArrayList lesIdTachesDelActivite = (ArrayList) (liensActivitesTaches.get(idActivite));
					ArrayList lesETachesDelActivite = new ArrayList();

					for (int i = 0; i < lesIdTachesDelActivite.size(); i++)
					{
						E_Tache laTache = (E_Tache) (this.listeTaches.get((String) lesIdTachesDelActivite.get(i)));
						if ((listeTachesIT.contains((String) lesIdTachesDelActivite.get(i))) && (laTache != null) && (laTache.getDateFinReelle() != null))
						{
							lesTachesAppartenantActivite.add(laTache);
							lesETachesDelActivite.add(laTache);
						}
					}
					if (!lesETachesDelActivite.isEmpty())
						listeActivitesIT.put(idActivite, lesETachesDelActivite);
				}

				// On récupère les taches qui ne sont pas associées à une activité du processus
				ArrayList lesETachesHorsProcessus = new ArrayList();
				Set lesIdTaches = listeTaches.keySet();
				it = lesIdTaches.iterator();
				while (it.hasNext())
				{
					String idTache = (String) it.next();
					if ((!lesTachesAppartenantActivite.contains((E_Tache) listeTaches.get(idTache))) && (listeTachesIT.contains(idTache)))
						lesETachesHorsProcessus.add((E_Tache) listeTaches.get(idTache));
				}
				listeActivitesIT.put(new String("-1"), lesETachesHorsProcessus);

				// on recupere les artefacts en entree des taches
				Set listeArtefactsIT = new HashSet();
				Set lesTaches = liensArtefactsEntreeTaches.keySet();
				it = lesTaches.iterator();
				while (it.hasNext())
				{
					String idTache = (String) it.next();
					E_Tache laTache = (E_Tache) (this.listeTaches.get(idTache));
					if ((listeTachesIT.contains(idTache)) && (laTache != null) && (laTache.getDateFinReelle() != null))
						listeArtefactsIT.addAll((Collection) (liensArtefactsEntreeTaches.get(idTache)));
				}

				// on recupere les artefacts en sortie des taches
				lesTaches = liensArtefactsSortieTaches.keySet();
				it = lesTaches.iterator();
				while (it.hasNext())
				{
					String idTache = (String) it.next();
					E_Tache laTache = (E_Tache) (this.listeTaches.get(idTache));
					if ((listeTachesIT.contains(idTache)) && (laTache != null) && (laTache.getDateFinReelle() != null))
						listeArtefactsIT.addAll((Collection) (liensArtefactsSortieTaches.get(idTache)));
				}

				// on recupere les produits correspondant aux artefacts
				Set lesProduits = liensProduitsArtefacts.keySet();
				ArrayList lesArtefactAppartenantProduit = new ArrayList();
				it = lesProduits.iterator();
				while (it.hasNext())
				{
					String idProduit = (String) it.next();
					ArrayList lesIdArtefactsDuProduit = (ArrayList) (liensProduitsArtefacts.get(idProduit));
					ArrayList lesEArtefactsDuProduit = new ArrayList();
					for (int i = 0; i < lesIdArtefactsDuProduit.size(); i++)
					{
						E_Artefact lArtefact = (E_Artefact) (this.listeArtefacts.get((String) lesIdArtefactsDuProduit.get(i)));
						if ((listeArtefactsIT.contains((String) lesIdArtefactsDuProduit.get(i))) && (lArtefact != null))
						{
							lesArtefactAppartenantProduit.add(lArtefact);
							lesEArtefactsDuProduit.add(lArtefact);
						}
					}
					if (!lesEArtefactsDuProduit.isEmpty())
						listeProduitsIT.put(idProduit, lesEArtefactsDuProduit);
				}

				// On récupère les artefacts qui ne sont pas associées à un produit du processus
				ArrayList lesEArtefactsHorsProcessus = new ArrayList();
				Set lesIdArtefacts = listeArtefacts.keySet();
				it = lesIdArtefacts.iterator();
				while (it.hasNext())
				{
					String idArtefact = (String) it.next();
					if ((!lesArtefactAppartenantProduit.contains((E_Artefact) listeArtefacts.get(idArtefact))) && (listeArtefactsIT.contains(idArtefact)))
						lesEArtefactsHorsProcessus.add((E_Artefact) listeArtefacts.get(idArtefact));
				}
				listeProduitsIT.put(new String("-1"), lesEArtefactsHorsProcessus);

				// on recupere les membres qui realisent des taches		
				Set listeMembresIT = new HashSet();
				Set lesMembres = liensMembresTaches.keySet();
				it = lesMembres.iterator();
				while (it.hasNext())
				{
					String idMembre = (String) it.next();
					ArrayList lesTachesDuMembre = (ArrayList) (liensMembresTaches.get(idMembre));
					for (int i = 0; i < lesTachesDuMembre.size(); i++)
					{
						E_Tache laTache = (E_Tache) (this.listeTaches.get((String) lesTachesDuMembre.get(i)));
						if ((listeTachesIT.contains((String) lesTachesDuMembre.get(i))) && (laTache != null) && (laTache.getDateFinReelle() != null))
							listeMembresIT.add(idMembre);
					}
				}

				// on recupere les roles correspondant aux membres
				ArrayList lesEMembresHorsProcessus = new ArrayList();
				it = listeMembresIT.iterator();
				while (it.hasNext())
				{
					String idMembre = (String) it.next();
					if (!liensMembresRoles.containsKey(idMembre))
					{
						lesEMembresHorsProcessus.add((E_Membre) listeMembres.get(idMembre));
					}
					else
					{
						ArrayList lesRolesDuMembre = (ArrayList) (liensMembresRoles.get(idMembre));
						for (int i = 0; i < lesRolesDuMembre.size(); i++)
						{
							String unIdRole = (String) lesRolesDuMembre.get(i);
							ArrayList lesMembresDuRoles = new ArrayList();
							if (listeRolesIT.containsKey(unIdRole))
							{
								lesMembresDuRoles = (ArrayList) listeRolesIT.get(unIdRole);
							}
							lesMembresDuRoles.add((E_Membre) listeMembres.get(idMembre));
							listeRolesIT.put(unIdRole, lesMembresDuRoles);
						}
					}
				}
				listeRolesIT.put("-1", lesEMembresHorsProcessus);
				//on ajoute ces mesures au controleur de mesures

				E_Mesures mesure = new E_Mesures(iteration.getNumIteration(), iteration.getDateDebutReelle(), iteration.getDateFinReelle(), listeActivitesIT, listeProduitsIT, listeRolesIT /*, chargeTachesLieesActivites, chargeTotale*/
				);
				C_Mesures.setIdExecutionProcessus(String.valueOf(eExecProcess.getId()));
				C_Mesures.setDateDebut(eExecProcess.getDateDebut());
				C_Mesures.setDateFin(eExecProcess.getDateFin());
				C_Mesures.ajouterMesure(mesure);
			}
		}
	}

	public E_ExecutionProcessus getExecutionProcessusImportee()
	{
		return this.eExecProcess;
	}

	/* Classes temporaire facilitant le parsage */
	private class Iteration
	{
		private String idIteration;
		private int numIteration;
		private Date dateDebutReelle = null;
		private Date dateFinReelle = null;
		public Iteration(String id, int num, Date dateDebut, Date dateFin)
		{
			idIteration = id;
			numIteration = num;
			dateDebutReelle = dateDebut;
			dateFinReelle = dateFin;
		}
		public String getIdIteration()
		{
			return idIteration;
		}
		public int getNumIteration()
		{
			return numIteration;
		}
		public Date getDateDebutReelle()
		{
			return dateDebutReelle;
		}
		public Date getDateFinReelle()
		{
			return dateFinReelle;
		}
	}

}
