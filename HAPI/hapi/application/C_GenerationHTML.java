/*
 * Créé le 21 sept. 2005
 */
package hapi.application;

import hapi.application.metier.C_ExecutionProcessus;
import hapi.application.metier.C_Processus;
import hapi.application.ressources.Bundle;
import hapi.donnees.E_MesureAcces;
import hapi.donnees.E_MesureAmelioration;
import hapi.donnees.E_MesureFormation;
import hapi.donnees.E_MesureRepresentation;
import hapi.donnees.E_MesureUtilisation;
import hapi.donnees.metier.E_ExecutionProcessus;
import hapi.donnees.metier.E_Processus;
import hapi.exception.ConnexionException;
import hapi.exception.EnvoyerFichierException;
import hapi.exception.LoginFTPException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Cédric
 */
public class C_GenerationHTML extends C_TransfertFichier
{
	private String idProcessus = null;
	private String nomFichierCSS = null;

	/**
	 * Constructeur préparant la configuration et les modèles
	 */
	public C_GenerationHTML(String lIdProcessus, String nomFichierCSS)
	{
		super();
		idProcessus = lIdProcessus;
		this.nomFichierCSS = nomFichierCSS;
		leModeleChemin.initialise(C_Utilisateur.getServeursHTML());
	}

	/**
	 * Ajout d'un serveur de fichier HTML
	 */
	public void addChemin(String chemin)
	{
		C_Utilisateur.addServeurHTML(chemin, "", "");
		leModeleChemin.addElement(chemin);
	}

	/**
	 * Sauvegarde du serveur sélectionné
	 */
	public void sauvegarderSelectionChemin(String lAdresse)
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement = 0;
		int idStatement2 = 0;

		try
		{
			cBase.ouvrirConnexion();
			ArrayList lesParametres = new ArrayList();
			lesParametres.add("HTM");
			lesParametres.add(lAdresse);
			lesParametres.add(new Integer(C_Utilisateur.getIdentifiant()));
			idStatement = cBase.creerPreparedStatement("setServeurSelection");
			cBase.executerRequeteStockee(idStatement, "setServeurSelection", lesParametres);
			idStatement2 = cBase.creerPreparedStatement("setServeurDeselection");
			cBase.executerRequeteStockee(idStatement2, "setServeurDeselection", lesParametres);

			C_Utilisateur.setSelectedHTML(lAdresse);
		}
		catch (Exception e)
		{
			//Rien d'interresant à faire ici
		}
		finally
		{
			try
			{
				cBase.fermerStatement(idStatement);
				cBase.fermerStatement(idStatement2);
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}

		}
	}

	/**
	 * Téléchargement du fichier sur le serveur
	 */
	public void uploaderFichier(String fichier, String chemin, String login, String passwd) throws ConnexionException, LoginFTPException, EnvoyerFichierException, FileNotFoundException, IOException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_serveur"));
		C_FTP ftp = new C_FTP();

		ftp.seConnecter(chemin, login, passwd);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier"));

		creerFichierHTMLMesureRepresentation(fichier);

		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_transfert_fichier"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_debut_transfert"));
		ftp.envoyerFichier("temp" + File.separator + fichier);
		if (!nomFichierCSS.equals(""))
			ftp.envoyerFichier("temp" + File.separator + nomFichierCSS);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_fichier_transfere"));
		supprimerFichierHTMLMesureRepresentation(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_deconnexion"));
	}

	/**
	 * Récupération du fichier en local
	 */
	public void uploaderFichier(String fichier, String chemin) throws ConnexionException, FileNotFoundException, IOException, EnvoyerFichierException
	{
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_serveur"));
		C_Local ftp = new C_Local();

		ftp.seConnecter(chemin);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_connexion_etablie"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier"));

		creerFichierHTMLMesureRepresentation(fichier);

		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_creation_fichier_termine"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_transfert_fichier"));
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_debut_transfert"));
		ftp.envoyerFichier("temp" + File.separator + fichier);
		if (!nomFichierCSS.equals(""))
			ftp.envoyerFichier("temp" + File.separator + nomFichierCSS);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_fichier_transfere"));
		supprimerFichierHTMLMesureRepresentation(fichier);
		leModeleTelechargement.addDonnees(Bundle.getText("C_Exportation_deconnexion"));
	}

	/**
	 * Récupération du login du serveur HTML à partir de son adresse
	 */
	public String getLogin(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurHTML(adresse).getLogin();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Récupération du mot de passe du serveur HTML à partir de son adresse
	 */
	public String getPWD(String adresse)
	{
		try
		{
			return C_Utilisateur.findServeurHTML(adresse).getMotDePasse();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * Création du fichier
	 */
	private void creerFichierHTMLMesureRepresentation(String nomFichierExport) throws FileNotFoundException, IOException
	{
		//Création du répertoire temposaire
		File repTemp = new File("temp" + File.separator);
		if (!repTemp.isDirectory())
			repTemp.mkdir();

		File fichExp = new File(nomFichierExport);
		E_Processus leProcessus = C_Processus.getProcessus(idProcessus);
		FileWriter fichierExport = new FileWriter("temp" + File.separator + fichExp);
		SimpleDateFormat sfDate = new SimpleDateFormat(Bundle.DATE_MODEL);
		SimpleDateFormat sfMois = new SimpleDateFormat(Bundle.MOIS_MODEL);

		fichierExport.write("<html lang=\"fr\">\n<head>\n<meta content=\"text/html; charset=ISO-8859-1\" http-equiv=\"content-type\">\n<meta name=\"GENERATOR\" content=\"HAPI\">\n");
		fichierExport.write("<title>" + Bundle.getText("BD_GenererHTML_TitrePage") + leProcessus.getNomSansVersion() + "</title>\n</head>\n<body>\n");
		if (!nomFichierCSS.equals(""))
			fichierExport.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + nomFichierCSS + "\">\n");
		fichierExport.write("<h2>" + Bundle.getText("BD_GenererHTML_PremiereLigne") + leProcessus.getNomSansVersion() + "</h2>\n");
		fichierExport.write("<h4 class=PILOTEPROCESSUS>Pilote du processus : &nbsp;" + leProcessus.getNomAuteur() + "</h4>\n");

		//---------------------Niveaux de maturite

		fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("BD_AssocierDescriptionMaturite_NiveauMaturite") + "</h4>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 1 ? "class=NIVEAUENCOURS" : "class=PREMIERELIGNETABLE") + ">1</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 2 ? "class=NIVEAUENCOURS" : "class=PREMIERELIGNETABLE") + ">2</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 3 ? "class=NIVEAUENCOURS" : "class=PREMIERELIGNETABLE") + ">3</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 4 ? "class=NIVEAUENCOURS" : "class=PREMIERELIGNETABLE") + ">4</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 5 ? "class=NIVEAUENCOURS" : "class=PREMIERELIGNETABLE") + ">5</td>\n");
		fichierExport.write("    </tr>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 1 ? "class=NIVEAUENCOURS" : "") + ">" + C_NiveauMaturite.getNiveauMaturite(idProcessus, 0).getDatePassageFormateeHTML() + "</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 2 ? "class=NIVEAUENCOURS" : "") + ">" + (C_NiveauMaturite.getNiveauMaturite(idProcessus, 1).getDatePassageFormateeHTML().equals("&nbsp;") ? C_NiveauMaturite.getNiveauMaturite(idProcessus, 0).getDateObjectifFormateeHTML() : C_NiveauMaturite.getNiveauMaturite(idProcessus, 1).getDatePassageFormateeHTML()) + "</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 3 ? "class=NIVEAUENCOURS" : "") + ">" + (C_NiveauMaturite.getNiveauMaturite(idProcessus, 2).getDatePassageFormateeHTML().equals("&nbsp;") ? C_NiveauMaturite.getNiveauMaturite(idProcessus, 1).getDateObjectifFormateeHTML() : C_NiveauMaturite.getNiveauMaturite(idProcessus, 2).getDatePassageFormateeHTML()) + "</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 4 ? "class=NIVEAUENCOURS" : "") + ">" + (C_NiveauMaturite.getNiveauMaturite(idProcessus, 3).getDatePassageFormateeHTML().equals("&nbsp;") ? C_NiveauMaturite.getNiveauMaturite(idProcessus, 2).getDateObjectifFormateeHTML() : C_NiveauMaturite.getNiveauMaturite(idProcessus, 3).getDatePassageFormateeHTML()) + "</td>\n");
		fichierExport.write("      <td " + (leProcessus.getNiveauMaturite() == 5 ? "class=NIVEAUENCOURS" : "") + ">" + (C_NiveauMaturite.getNiveauMaturite(idProcessus, 4).getDatePassageFormateeHTML().equals("&nbsp;") ? C_NiveauMaturite.getNiveauMaturite(idProcessus, 3).getDateObjectifFormateeHTML() : C_NiveauMaturite.getNiveauMaturite(idProcessus, 4).getDatePassageFormateeHTML()) + "</td>\n");
		fichierExport.write("    </tr>\n");
		fichierExport.write("  </tbody>\n");
		fichierExport.write("</table>\n");
		fichierExport.write("<div class=SOUSTABLE>" + Bundle.getText("OO_MaturiteProcessus_infoHTML") + "</div>\n");

		//---------------------Historique

		fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("BD_ImporterProcessus_Historique") + "</h4>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Version") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_GenererHTML_Date") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_CommentaireProjet_caption") + "</td>\n");
		fichierExport.write("    </tr>\n");
		int nombreVersion = leProcessus.getListeDesVersion().size();
		for (Iterator it = leProcessus.getListeDesVersion().iterator(); it.hasNext(); --nombreVersion)
		{
			ArrayList unGroupe = (ArrayList) it.next();
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td>" + unGroupe.get(1) + "</td>\n");
			fichierExport.write("      <td>" + leProcessus.formaterDateExport((String) unGroupe.get(0)) + "</td>\n");
			//TODO : Commentaire du fichier HTML
			fichierExport.write("      <td>" + (nombreVersion == 1 ? leProcessus.getDescription() : "&nbsp;") + "</td>\n");
			fichierExport.write("    </tr>\n");
		}
		fichierExport.write("  </tbody>\n");
		fichierExport.write("</table>\n");
		fichierExport.write("<div class=SOUSTABLE>" + Bundle.getText("BD_GenererHTML_DateExport") + "</div>\n");

		//---------------------Revues processus

		fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("OO_RevueProcessus_caption") + "</h4>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_RevueProcessus_champ_date") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_RevueProcessus_champ_dateProchaine") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_RevueProcessus_champ_decision") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_RevueProcessus_champ_action") + "</td>\n");
		fichierExport.write("    </tr>\n");
		for (int i = 0; i < C_RevueProcessus.size(idProcessus); i++)
		{
			try
			{
				fichierExport.write("<tr>\n");
				fichierExport.write("  <td>" + sfDate.format(C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getDateRevue()) + "</td>\n");
				fichierExport.write("  <td>" + ((C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getDateProchaineRevue() == null)?"&nbsp;":sfDate.format(C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getDateRevue())) + "</td>\n");
				String uneDecision = C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getDecision();
				uneDecision = uneDecision.replaceAll("\n","<BR>");
				uneDecision = uneDecision.replaceAll(" ","&nbsp;");
				fichierExport.write("  <td>" +  uneDecision + "</td>\n");
				String uneAction = (C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getAction().equals("")?"&nbsp;":C_RevueProcessus.getRevueProcessusIndice(idProcessus, i).getAction());
				uneAction = uneAction.replaceAll("\n","<BR>");
				uneAction = uneAction.replaceAll(" ","&nbsp;");
				fichierExport.write("  <td>" + uneAction + "</td>\n");
				fichierExport.write("</tr>\n");
			}
			catch (Exception e)
			{
				fichierExport.write("</tr>\n");
			}
		}
		fichierExport.write("</tbody>\n");
		fichierExport.write("</table>\n");
		fichierExport.write("<br>\n");

		//---------------------Mesures de représentation

		fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("BD_MesureVersion_MesureRepresentation") + "</h4>\n");
		fichierExport.write("<ul>\n");
		fichierExport.write("  <li>" + Bundle.getText("BD_MesureVersion_RI") + "</li>\n");
		fichierExport.write("</ul>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Version") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_InterfacesE") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_InterfacesS") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Scenario") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Explicite") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Estime") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_EvaluationIT") + "</td>\n");
		fichierExport.write("    </tr>\n");
		for (Iterator it = leProcessus.getListeDesVersion().iterator(); it.hasNext();)
		{
			ArrayList uneVersion = (ArrayList) it.next();
			String dateExport = (String) uneVersion.get(0);
			E_MesureRepresentation uneMesure = leProcessus.getMesureRepresentation(dateExport);
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td>" + uneVersion.get(1) + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getInterfaceE() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getInterfaceS() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getScenario() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getExplicite() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getEstime() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getEvaluationIT() + "</td>\n");
			fichierExport.write("    </tr>\n");
		}
		fichierExport.write("  </tbody>\n");
		fichierExport.write("</table>\n");
		fichierExport.write("<ul>\n");
		fichierExport.write("  <li>" + Bundle.getText("BD_MesureVersion_RM") + "</li>\n");
		fichierExport.write("</ul>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Version") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Coherence") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Completude") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Definitions") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Activites") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Roles") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Produits") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_EvaluationMO") + "</td>\n");
		fichierExport.write("    </tr>\n");
		for (Iterator it = leProcessus.getListeDesVersion().iterator(); it.hasNext();)
		{
			ArrayList uneVersion = (ArrayList) it.next();
			String dateExport = (String) uneVersion.get(0);
			E_MesureRepresentation uneMesure = leProcessus.getMesureRepresentation(dateExport);
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td>" + uneVersion.get(1) + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getCoherence() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getCompletude() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbDef() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbAct() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbRole() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbProd() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getEvaluationMo() + "</td>\n");
			fichierExport.write("    </tr>\n");
		}
		fichierExport.write("  </tbody>\n");
		fichierExport.write("</table>\n");
		fichierExport.write("<ul>\n");
		fichierExport.write("  <li>" + Bundle.getText("BD_MesureVersion_RD") + "</li>\n");
		fichierExport.write("</ul>\n");
		fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
		fichierExport.write("  <tbody>\n");
		fichierExport.write("    <tr>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Version") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_ProduitsPlan") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_PourPlan") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Guide") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_PourGuide") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_Documents") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_PourDoc") + "</td>\n");
		fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureVersion_EvaluationDO") + "</td>\n");
		fichierExport.write("    </tr>\n");
		for (Iterator it = leProcessus.getListeDesVersion().iterator(); it.hasNext();)
		{
			ArrayList uneVersion = (ArrayList) it.next();
			String dateExport = (String) uneVersion.get(0);
			E_MesureRepresentation uneMesure = leProcessus.getMesureRepresentation(dateExport);
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td>" + uneVersion.get(1) + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbProdType() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getPourPlan() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbGuide() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getPourGuide() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getNbDocu() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getPourDocu() + "</td>\n");
			fichierExport.write("      <td>" + uneMesure.getEvaluationDocu() + "</td>\n");
			fichierExport.write("    </tr>\n");
		}
		fichierExport.write("  </tbody>\n");
		fichierExport.write("</table>\n");

		//---------------------Mesures d'adhésion
		if (C_Processus.getProcessus(idProcessus).getNiveauMaturite() > 1)
		{
			fichierExport.write("<br>\n");
			fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion") + "</h4>\n");
			fichierExport.write("<ul>\n");
			fichierExport.write("  <li>" + Bundle.getText("OO_MesureAcces_caption") + " : </li>\n");
			fichierExport.write("</ul>\n");
			fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
			fichierExport.write("  <tbody>\n");
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureAcces_champ_mois") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureAcces_champ_nombre") + "</td>\n");
			fichierExport.write("    </tr>\n");
			for (Iterator it = leProcessus.getListeMesuresAcces().iterator(); it.hasNext();)
			{
				E_MesureAcces uneMesure = (E_MesureAcces) it.next();
				fichierExport.write("    <tr>\n");
				fichierExport.write("      <td>" + sfMois.format(uneMesure.getMois()) + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getNombre() + "</td>\n");
				fichierExport.write("    </tr>\n");
			}
			fichierExport.write("  </tbody>\n");
			fichierExport.write("</table>\n");
			fichierExport.write("<div class=SOUSTABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_HAbas") + "</div>\n");
			fichierExport.write("<ul>\n");
			fichierExport.write("  <li>" + Bundle.getText("OO_MesureFormation_caption") + " : </li>\n");
			fichierExport.write("</ul>\n");
			fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
			fichierExport.write("  <tbody>\n");
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureFormation_champ_date") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureFormation_champ_effPro") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureFormation_champ_effMes") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureFormation_champ_note") + "</td>\n");
			fichierExport.write("    </tr>\n");
			for (Iterator it = leProcessus.getListeMesuresFormation().iterator(); it.hasNext();)
			{
				E_MesureFormation uneMesure = (E_MesureFormation) it.next();
				fichierExport.write("    <tr>\n");
				fichierExport.write("      <td>" + sfDate.format(uneMesure.getDateMesure()) + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getEffectifProcessus() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getEffectifMesure() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getNote() + "</td>\n");
				fichierExport.write("    </tr>\n");
			}
			fichierExport.write("  </tbody>\n");
			fichierExport.write("</table>\n");
			fichierExport.write("<div class=SOUSTABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_HFbas") + "</div>\n");
			fichierExport.write("<ul>\n");
			fichierExport.write("  <li>" + Bundle.getText("OO_MesureUtilisation_caption") + " : </li>\n");
			fichierExport.write("</ul>\n");
			fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
			fichierExport.write("  <tbody>\n");
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_NomProjet") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_DateDebut") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_DateFin") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureUtilisation_champ_pourPT") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("OO_MesureUtilisation_champ_pourAct") + "</td>\n");
			fichierExport.write("    </tr>\n");
			for (Iterator it = leProcessus.getListeMesuresUtilisation().iterator(); it.hasNext();)
			{
				E_MesureUtilisation uneMesure = (E_MesureUtilisation) it.next();
				fichierExport.write("    <tr>\n");
				fichierExport.write("      <td>" + ((E_ExecutionProcessus) (C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus).get(uneMesure.getIdExec()))).getNomSansVersion() + "</td>\n");
				fichierExport.write("      <td>" + sfDate.format(((E_ExecutionProcessus) (C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus).get(uneMesure.getIdExec()))).getDateDebut()) + "</td>\n");
				fichierExport.write("      <td>" + sfDate.format(((E_ExecutionProcessus) (C_ExecutionProcessus.getExecutionsProcessusDuProcessus(idProcessus).get(uneMesure.getIdExec()))).getDateFin()) + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getPourPlanType() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getPourActivite() + "</td>\n");
				fichierExport.write("    </tr>\n");
			}
			fichierExport.write("  </tbody>\n");
			fichierExport.write("</table>\n");
			fichierExport.write("<div class=SOUSTABLE>" + Bundle.getText("BD_GenererHTML_MesureAdhesion_HUbas") + "</div>\n");
		}

		//---------------------Mesures d'amélioration

		if (C_Processus.getProcessus(idProcessus).getNiveauMaturite() > 1)
		{		
			fichierExport.write("<h4 class=TITRETABLE>" + Bundle.getText("BD_MesureAmelioration_caption") + "</h4>\n");
			fichierExport.write("<table border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n");
			fichierExport.write("  <tbody>\n");
			fichierExport.write("    <tr>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_version") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_GenererHTML_Date") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_nombrePropose") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_nombrePris") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_presentation") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_modele") + "</td>\n");
			fichierExport.write("      <td class=PREMIERELIGNETABLE>" + Bundle.getText("BD_MesureAmelioration_documentation") + "</td>\n");
			fichierExport.write("    </tr>\n");
			for (Iterator it = leProcessus.getListeMesureAmelioration().keySet().iterator(); it.hasNext();)
			{
				String dateExport = (String) it.next();
				E_MesureAmelioration uneMesure = leProcessus.getMesureAmelioration(dateExport);
				fichierExport.write("    <tr>\n");
				fichierExport.write("      <td>" + C_Processus.getNumeroVersion(idProcessus,dateExport) + "</td>\n");
				fichierExport.write("      <td>" + sfDate.format(uneMesure.getDateChangement()) + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getNombrePropose() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getNombrePris() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getPresentation() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getModele() + "</td>\n");
				fichierExport.write("      <td>" + uneMesure.getDocumentation() + "</td>\n");
				fichierExport.write("    </tr>\n");
			}
			fichierExport.write("  </tbody>\n");
			fichierExport.write("</table>\n");
		}
		
		//------------------------

		fichierExport.write("</body>\n</html>");

		fichierExport.close();
	}

	/**
	 * Suppression su fichier temporaire
	 */
	private void supprimerFichierHTMLMesureRepresentation(String nomFichierExport)
	{
		File ficTemp = new File("temp" + File.separator + nomFichierExport);
		if (ficTemp.isFile())
			ficTemp.delete();
	}
}