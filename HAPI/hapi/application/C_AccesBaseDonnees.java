package hapi.application;

import hapi.donnees.E_Configuration;
import hapi.exception.NoRowInsertedException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.Driver;

/**
 * @author Robin EYSSERIC
 */
public class C_AccesBaseDonnees
{
	private Connection connexion = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	static private HashMap statements = null;
	static private int idStatementCourant = 0;

	/**
	 * Type de protocole utilisé.
	 * Dépend de la base exploitée.
	 */
	private String typeConnexion = "jdbc:mysql:";
	//private String typeConnexion = "jdbc:oracle:thin:@";

	/**
	 * Nom de la base de données à exploiter.
	 */
	//private String nomBase = "domino";
	//private String nomBase = ":1526:etu923";

	/**
	 * HashMap contenant les requêtes à effectuer.
	 * Une requête est identifiée par une clé unique.
	 */
	static private HashMap requetes = new HashMap();

	static {
		requetes = new HashMap();
		statements = new HashMap();

		//GET rolesutilisateurs
		requetes.put("getRolesUtilisateur", "SELECT nom FROM rolesutilisateurs");

		//GET utilisateurs
		requetes.put("getIdUtilisateur", "SELECT numero FROM utilisateurs WHERE login=? AND idrole=?");
		requetes.put("getIdentification", "SELECT numero,nom,prenom FROM utilisateurs WHERE login=? AND motdepasse=? AND idrole=?");
		requetes.put("getConfiguration", "SELECT langue,aspectGraphe FROM utilisateurs WHERE numero=?");
		requetes.put("getUtilisateurs", "SELECT login, utilisateurs.nom, prenom, rolesutilisateurs.nom FROM utilisateurs,rolesutilisateurs WHERE idrole=rolesutilisateurs.numero");
		requetes.put("getMotDePasseUtilisateurs", "SELECT motdepasse FROM utilisateurs WHERE numero=?");
		requetes.put("getNomResponsable", "SELECT u.nom,u.prenom FROM utilisateurs u, processus p WHERE p.idresponsable = u.numero and p.id =?");

		//GET serveurs
		requetes.put("getServeursUtilisateurs", "SELECT adresse,login,motdepasse,selection,type FROM serveurs WHERE idutil=?");

		//SET serveurs				
		requetes.put("setServeurs", "INSERT INTO serveurs VALUES (NULL,?,?,?,?,?,?)");
		requetes.put("setServeurSelection", "UPDATE serveurs SET selection = '1' WHERE type=? AND adresse=? AND idutil=?");
		requetes.put("setServeurDeselection", "UPDATE serveurs SET selection = '0' WHERE type=? AND adresse<>? AND idutil=?");
		requetes.put("setLoginPasswordServeur", "UPDATE serveurs SET login = ?,motdepasse = ? WHERE type=? AND adresse=? AND idutil=?");

		//GET métier		
		requetes.put("getAllProcessus", "SELECT * FROM processus ORDER BY nom");
		requetes.put("getProcessus", "SELECT * FROM processus WHERE idresponsable=? ORDER BY nom");
		requetes.put("getVersionProcessus", "SELECT dateExport,numeroVersion FROM versionprocessus WHERE idProcessus=? ORDER BY dateExport");
		requetes.put("getComposants", "SELECT * FROM composants WHERE idprocessus=?");
		requetes.put("getEtats", "SELECT * FROM etats WHERE idprocessus=?");
		requetes.put("getPaquetagesPresentation", "SELECT * FROM paquetagesPresentation WHERE idProcessus=?");
		requetes.put("getElementsPresentation", "SELECT * FROM elementsPresentation WHERE idProcessus=?");
		requetes.put("getGuides", "SELECT * FROM guides WHERE idprocessus=?");
		requetes.put("getProduits", "SELECT * FROM produits WHERE idprocessus=?");
		requetes.put("getRoles", "SELECT * FROM roles WHERE idProcessus=?");
		requetes.put("getDefinitions", "SELECT * FROM definitions WHERE idProcessus=?");
		requetes.put("getActivites", "SELECT * FROM activites WHERE idProcessus=?");
		requetes.put("getInterfaces", "SELECT * FROM interfaces WHERE idProcessus=?");
		requetes.put("getTypesProduit", "SELECT * FROM typesProduit WHERE idProcessus=?");
		requetes.put("getTypesGuide", "SELECT * FROM typesGuide WHERE idProcessus=?");
		requetes.put("getLiensProdActEntree", "SELECT * FROM liensProdActEntree WHERE idProcessus=?");
		requetes.put("getLiensProdActSortie", "SELECT * FROM liensProdActSortie WHERE idProcessus=?");
		requetes.put("getLiensProduitEtat", "SELECT * FROM liensProduitEtat WHERE idProcessus=?");
		requetes.put("getLiensProduitInterface", "SELECT * FROM liensProduitInterface WHERE idProcessus=?");

		requetes.put("getComposantsId", "SELECT * FROM composants WHERE id=? AND idProcessus=?");
		requetes.put("getInterfacesId", "SELECT * FROM interfaces WHERE id=? AND idProcessus=?");
		requetes.put("getProduitsId", "SELECT * FROM produits WHERE id=? AND idProcessus=?");
		requetes.put("getTypesProduitId", "SELECT * FROM typesProduit WHERE id=? AND idProcessus=?");
		requetes.put("getRolesId", "SELECT * FROM roles WHERE id=? AND idProcessus=?");
		requetes.put("getDefinitionsId", "SELECT * FROM definitions WHERE id=? AND idProcessus=?");
		requetes.put("getActivitesId", "SELECT * FROM activites WHERE id=? AND idProcessus=?");
		requetes.put("getEtatsId", "SELECT * FROM etats WHERE id=? AND idProcessus=?");
		requetes.put("getElementsPresentationId", "SELECT * FROM elementsPresentation WHERE id=? AND idProcessus=?");
		requetes.put("getPaquetagesPresentationId", "SELECT * FROM paquetagesPresentation WHERE id=? AND idProcessus=?");
		requetes.put("getGuidesId", "SELECT * FROM guides WHERE id=? AND idProcessus=?");
		requetes.put("getTypesGuideId", "SELECT * FROM typesGuide WHERE id=? AND idProcessus=?");
		requetes.put("getLiensProduitInterfaceAll", "SELECT * FROM liensProduitInterface WHERE idProduit=? AND idInterface=? AND idProcessus=?");
		requetes.put("getLiensProduitEtatAll", "SELECT * FROM liensProduitEtat WHERE idProduit=? AND idEtat=? AND idProcessus=?");
		requetes.put("getLiensDefProdTravailEntree", "SELECT * FROM liensDefProdTravailEntree WHERE idDefinition=? AND idProduitTravail=?");
		requetes.put("getLiensDefProdTravailSortie", "SELECT * FROM liensDefProdTravailSortie WHERE idDefinition=? AND idProduitTravail=?");
		requetes.put("getLiensActProdTravailEntree", "SELECT * FROM liensActProdTravailEntree WHERE idActivite=? AND idProduitTravail=?");
		requetes.put("getLiensActProdTravailSortie", "SELECT * FROM liensActProdTravailSortie WHERE idActivite=? AND idProduitTravail=?");
		requetes.put("getLiensProdActEntreeId", "SELECT * FROM liensProdActEntree WHERE idProduit=? AND idActivite=? AND idProcessus=?");
		requetes.put("getLiensProdActSortieId", "SELECT * FROM liensProdActSortie WHERE idProduit=? AND idActivite=? AND idProcessus=?");

		requetes.put("getEvaluationParProcessus", "SELECT * FROM evaluations WHERE idProcessus=?");
		requetes.put("getEvaluationParExecProcessus", "SELECT * FROM evaluations WHERE idExecutionProcessus=?");
		requetes.put("getOrdreActivites", "SELECT * FROM ordreactivite WHERE idevaluation = ? ");
		requetes.put("getRolesEvaluations", "SELECT * FROM rolesevaluations WHERE idevaluation = ? ");
		requetes.put("getChargesParComposant", "SELECT * FROM chargesParComposant WHERE idEvaluation = ?");

		requetes.put("getMaxEvaluations", "SELECT MAX(id) FROM evaluations");
		requetes.put("getIterationDefaut", "SELECT MAX(numIteration) FROM evaluations WHERE idProcessus=? AND idExecutionProcessus=?");

		requetes.put("getExecutionsProcessus", "SELECT * FROM executionProcessus WHERE id_processus=?");
		requetes.put("getMaxExecutionProcessus", "SELECT MAX(notreId) FROM executionProcessus");
		
		//GET cycle de vie
		requetes.put("getCycleDeVieParProjet", "SELECT id_cycle FROM cycleDeVie WHERE id_projet=?");
		requetes.put("getMaxCycleDeVie", "SELECT MAX(id_cycle) FROM cycleDeVie");
		requetes.put("getCycleProduit", "SELECT * FROM lienCycleProduit WHERE id_cycle=?");
		requetes.put("getCycleRole", "SELECT * FROM lienCycleRole WHERE id_cycle=?");
		requetes.put("getCycleActivite", "SELECT * FROM lienCycleActivite WHERE id_cycle=?");
		
		//GET Description niveau maturité
		requetes.put("getDescriptionMaturite", "SELECT niveauMaturite,Description FROM listeDescriptionMaturite ORDER BY niveauMaturite");
		requetes.put("getNiveauMaturite", "SELECT * FROM niveauMaturite WHERE idProcessus=? ORDER BY nivMaturite");
		
		//GET indicateurs - seuils
		requetes.put("getIndicateurs", "SELECT * FROM indicateurs");
		requetes.put("getSeuils", "SELECT * FROM seuils WHERE id_utilisateur=?");
		
		//GET Mesures de représentation
		requetes.put("getMesuresRepresentation", "SELECT * FROM mesureRepresentation WHERE idProcessus=?");
		
		//DEL Mesures de représentation
		requetes.put("delMesuresRepresentation", "DELETE FROM mesureRepresentation WHERE idProcessus=?");
		
		//SET Mesures de représentation
		requetes.put("setNouveauMesuresRepresentation", "INSERT INTO mesureRepresentation VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		requetes.put("setMesuresRepresentation1", "UPDATE mesureRepresentation SET interfaceE = ?, interfaceS = ?, scenario = ?, comScenario = ?, explicite = ?, comExplicite = ?, estime = ?, comEstime = ?, evaluationIT = ?, comEvalIT = ? WHERE idProcessus = ? AND dateExport = ?");   
		requetes.put("setMesuresRepresentation2", "UPDATE mesureRepresentation SET coherence = ?, comCoherence = ?, competude = ?, comCompletude = ?, nbDef = ?, nbAct = ?, nbProd = ?, nbRole = ?, evaluationMo = ?, comEvalMo = ? WHERE idProcessus = ? AND dateExport = ?");
		requetes.put("setMesuresRepresentation3", "UPDATE mesureRepresentation SET nbProdType = ?, comNbProdType = ?, pourPlan = ?, nbGuide = ?, comGuide = ?, pourGuide = ?, nbDocu = ?, comDocu = ?, pourDocu = ?, evaluationDocu = ?, comEvalDocu = ? WHERE idProcessus = ? AND dateExport = ?");

		//SET Cycle de vie
		requetes.put("setCycleDeVieParProjet", "INSERT INTO cycleDeVie VALUES(NULL,?,?)");
		requetes.put("setCycleProduit", "INSERT INTO lienCycleProduit VALUES(?,?)");
		requetes.put("setCycleRole", "INSERT INTO lienCycleRole VALUES(?,?)");
		requetes.put("setCycleActivite", "INSERT INTO lienCycleActivite VALUES(?,?)");

		// DEL seuils
		requetes.put("delSeuilsProcessus", "DELETE FROM seuils WHERE id_processus=?");

		//SET & UPDATE seuils
		requetes.put("setSeuil", "INSERT INTO seuils VALUES(NULL,?,?,?,?,?)");
		requetes.put("updateSeuil", "UPDATE seuils SET min = ?, max = ? WHERE id = ?");

		//SET utilisateurs		
		requetes.put("setConfiguration", "UPDATE utilisateurs SET langue=?,aspectGraphe=? WHERE numero=?");
		requetes.put("setUtilisateurs", "INSERT INTO utilisateurs VALUES (NULL,?,'',?,?,'fr','Metal',?)");
		requetes.put("setNomPrenomUtilisateurs", "UPDATE utilisateurs SET nom=?,prenom=? WHERE numero=?");
		requetes.put("setMotDePasseUtilisateurs", "UPDATE utilisateurs SET motdepasse = ? WHERE numero=?");

		//SET maturité
		requetes.put("setMaturiteProcessus", "UPDATE processus SET nivmaturite=? WHERE id=?");
		requetes.put("setNiveauMaturite", "UPDATE niveauMaturite SET comMaturite=?,datePassage=?,dateObjectif=? WHERE idProcessus=? AND nivMaturite=?");
		requetes.put("setNiveauMaturiteVide", "INSERT INTO niveauMaturite VALUES(?,?,?,?,NULL)");
		
		
		//DEL maturité
		requetes.put("delNiveauMaturite", "DELETE FROM niveauMaturite WHERE idProcessus=?");
		
		//SET Description niveau maturité
		requetes.put("setListeDescriptionMaturite", "INSERT INTO listeDescriptionMaturite VALUES(?,?)");
		requetes.put("setUpdateListeDescriptionMaturite", "UPDATE listeDescriptionMaturite SET Description=? WHERE niveauMaturite=?");

		//SET commentaire de projet
		requetes.put("setCommentaireProjet", "UPDATE executionProcessus SET commentaire=? WHERE notreId=?");

		//SET métier
		requetes.put("setProcessus", "INSERT INTO processus (id, nom, nomAuteur, emailAuteur, description , cheminGeneration, nivMaturite, idResponsable) VALUES (?,?,?,?,?,?,?,?)");
		requetes.put("setVersionProcessus", "INSERT INTO versionprocessus VALUES (?,?,?)");
		requetes.put("setVersionProcessusMAJ", "UPDATE versionprocessus SET numeroVersion=? WHERE idProcessus = ? AND dateExport = ?");
		requetes.put("setComposants", "INSERT INTO composants VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		requetes.put("setInterfaces", "INSERT INTO interfaces VALUES (?,?,?,?)");
		requetes.put("setProduits", "INSERT INTO produits VALUES (?,?,?,?,?,?,?)");
		requetes.put("setLiensProduitInterface", "INSERT INTO liensProduitInterface VALUES (?,?,?)");
		requetes.put("setTypesProduit", "INSERT INTO typesProduit VALUES (?,?,?)");
		requetes.put("setRoles", "INSERT INTO roles VALUES (?,?,?,?,?)");
		requetes.put("setDefinitions", "INSERT INTO definitions VALUES (?,?,?,?,?)");
		requetes.put("setActivites", "INSERT INTO activites VALUES (?,?,?,?,?,?)");
		requetes.put("setProduitsTravail", "INSERT INTO produitsTravail VALUES (?,?,?)");
		requetes.put("setEtats", "INSERT INTO etats VALUES (?,?,?)");
		requetes.put("setLiensProduitEtat", "INSERT INTO liensProduitEtat VALUES (?,?,?)");
		requetes.put("setLiensProdActEntree", "INSERT INTO liensProdActEntree VALUES (?,?,?)");
		requetes.put("setLiensProdActSortie", "INSERT INTO liensProdActSortie VALUES (?,?,?)");
		requetes.put("setLiensDefProdTravailEntree", "INSERT INTO liensDefProdTravailEntree VALUES (?,?)");
		requetes.put("setLiensDefProdTravailSortie", "INSERT INTO liensDefProdTravailSortie VALUES (?,?)");
		requetes.put("setLiensActProdTravailEntree", "INSERT INTO liensActProdTravailEntree VALUES (?,?)");
		requetes.put("setLiensActProdTravailSortie", "INSERT INTO liensActProdTravailSortie VALUES (?,?)");
		requetes.put("setTypesGuide", "INSERT INTO typesGuide VALUES (?,?,?)");
		requetes.put("setGuide", "INSERT INTO guides VALUES (?,?,?,?,?)");
		requetes.put("setPaquetagesPresentation", "INSERT INTO paquetagesPresentation VALUES (?,?,?,?,?,?)");
		requetes.put("setElementsPresentation", "INSERT INTO elementsPresentation (id, nom, cheminIcone, cheminContenu, description, cheminPage, idProcessus) VALUES (?,?,?,?,?,?,?)");
		requetes.put("setElementIdPaquetage", "UPDATE elementsPresentation SET idPaquetagePresentation=? WHERE id=? AND idProcessus=?");
		requetes.put("setExecutionProcessus", "INSERT INTO executionProcessus (notreId, id, nom, version_processus, description, datedebut, datefin, id_processus) VALUES (NULL,?,?,?,?,?,?,?)");

		requetes.put("setEvaluation", "INSERT INTO evaluations (id, idProcessus, idExecutionProcessus, numIteration, RUA, RCT, RUR, RUP, debut, fin) VALUES (NULL,?,?,?,?,?,?,?,?,?)");
		requetes.put("setEvaluationQualitative", "UPDATE evaluations SET evalQualitative=? WHERE id=?");
		requetes.put("setOrdreActivites", "INSERT INTO ordreactivite VALUES (?,?,?,?)");
		requetes.put("setRolesEvaluations", "INSERT INTO rolesevaluations VALUES (?,?,?,?)");
		requetes.put("setChargesParComposant", "INSERT INTO chargesParComposant VALUES (?,?,?)");

		//DEL serveurs		
		requetes.put("delServeursUtilisateur", "DELETE FROM serveurs WHERE idutil=?");

		//DEL utilisateurs		
		requetes.put("delUtilisateurs", "DELETE FROM utilisateurs WHERE login=? AND idrole = ?");

		// DEL evaluations
		requetes.put("delEvaluationsProcessus", "DELETE FROM evaluations WHERE idProcessus=?");
		requetes.put("delEvaluationsExecProc", "DELETE FROM evaluations WHERE idExecutionProcessus=?");
		requetes.put("delEvaluation", "DELETE FROM evaluations WHERE id = ?");
		requetes.put("delEvaluationUnique", "DELETE FROM evaluations WHERE idProcessus = ? AND idExecutionProcessus=? AND numIteration=?");
		requetes.put("delRolesEvaluations", "DELETE FROM rolesevaluations WHERE idevaluation=?");
		requetes.put("delOrdreActivites", "DELETE FROM ordreactivite WHERE idevaluation=?");
		requetes.put("delChargesDesComposants", "DELETE FROM chargesParComposant WHERE idevaluation = ?");

		// DEL executionProcessus
		requetes.put("delExecutionsProcessus", "DELETE FROM executionProcessus WHERE id_processus=?");
		requetes.put("delExecutionProcessus", "DELETE FROM executionProcessus WHERE notreId=?");
		
		//DEL cycle de vie
		requetes.put("delCycleDeVie", "DELETE FROM cycleDeVie WHERE id_cycle=?");
		requetes.put("delCycleProduit", "DELETE FROM lienCycleProduit WHERE id_cycle=?");
		requetes.put("delCycleRole", "DELETE FROM lienCycleRole WHERE id_cycle=?");
		requetes.put("delCycleActivite", "DELETE FROM lienCycleActivite WHERE id_cycle=?");

		// demandesModifications
		requetes.put("getDemandesModifications", "SELECT * FROM demandesModifications WHERE id_processus=?");
		requetes.put("setDemandesModifications", "INSERT INTO demandesModifications VALUES (NULL,?,?,?,?,?,?)");
		requetes.put("delDemandesModifications", "DELETE FROM demandesModifications WHERE id_demande=?");
		requetes.put("delDemandesModificationsProcessus", "DELETE FROM demandesModifications WHERE id_processus=?");
		requetes.put("getMaxDemandesModifications", "SELECT MAX(id_demande) FROM demandesModifications");

		// historiqueModifications
		requetes.put("getHistoriqueModifications", "SELECT * FROM historiqueModifications WHERE id_processus=?");
		requetes.put("setHistoriqueModifications", "INSERT INTO historiqueModifications VALUES (NULL,?,?,?,?,?,?,?)");
		requetes.put("delHistoriqueModifications", "DELETE FROM historiqueModifications WHERE id_modif=?");
		requetes.put("delHistoriqueModificationsProcessus", "DELETE FROM historiqueModifications WHERE id_processus=?");
		requetes.put("getMaxHistoriqueModifications", "SELECT MAX(id_modif) FROM historiqueModifications");

		//DEL métier
		requetes.put("delProcessus", "DELETE FROM processus WHERE id=?");
		requetes.put("delVersionsProcessus", "DELETE FROM versionprocessus WHERE idProcessus=?");
		requetes.put("delComposants", "DELETE FROM composants WHERE idprocessus=?");
		requetes.put("delEtats", "DELETE FROM etats WHERE idprocessus=?");
		requetes.put("delPaquetagesPresentation", "DELETE FROM paquetagesPresentation WHERE idProcessus=?");
		requetes.put("delElementsPresentation", "DELETE FROM elementsPresentation WHERE idProcessus=?");
		requetes.put("delGuides", "DELETE FROM guides WHERE idprocessus=?");
		requetes.put("delProduits", "DELETE FROM produits WHERE idprocessus=?");
		requetes.put("delRoles", "DELETE FROM roles WHERE idProcessus=?");
		requetes.put("delDefinitions", "DELETE FROM definitions WHERE idProcessus=?");
		requetes.put("delActivites", "DELETE FROM activites WHERE idProcessus=?");
		requetes.put("delInterfaces", "DELETE FROM interfaces WHERE idProcessus=?");
		requetes.put("delTypesProduit", "DELETE FROM typesProduit WHERE idProcessus=?");
		requetes.put("delTypesGuide", "DELETE FROM typesGuide WHERE idProcessus=?");
		requetes.put("delLiensProdActEntree", "DELETE FROM liensProdActEntree WHERE idProcessus=?");
		requetes.put("delLiensProdActSortie", "DELETE FROM liensProdActSortie WHERE idProcessus=?");
		requetes.put("delLiensProduitEtat", "DELETE FROM liensProduitEtat WHERE idProcessus=?");
		requetes.put("delLiensProduitInterface", "DELETE FROM liensProduitInterface WHERE idProcessus=?");
		
		// revuesProcessus
		requetes.put("getRevuesProcessus", "SELECT * FROM revuesProcessus WHERE idProcessus=? ORDER BY dateRevue");
		requetes.put("setRevuesProcessus", "INSERT INTO revuesProcessus VALUES (NULL,?,?,?,?,?)");
		requetes.put("setMAJRevuesProcessus", "UPDATE revuesProcessus SET dateRevue = ?, dateProchaineRevue = ?, decision = ?, action = ? WHERE idProcessus = ? AND id = ?");
		requetes.put("delRevuesProcessus", "DELETE FROM revuesProcessus WHERE id=?");
		requetes.put("delRevuesProcessusIdProcessus", "DELETE FROM revuesProcessus WHERE idProcessus=?");
		requetes.put("getMaxRevuesProcessus", "SELECT MAX(id) FROM revuesProcessus");
		
		//mesure d'adhésion
		requetes.put("getMesureAcces", "SELECT * FROM mesureAcces WHERE idProcessus=? ORDER BY mois");
		requetes.put("getMaxMesureAcces", "SELECT MAX(id) FROM mesureAcces");
		requetes.put("getMesureFormation", "SELECT * FROM mesureFormation WHERE idProcessus=? ORDER BY dateMesure");
		requetes.put("getMaxMesureFormation", "SELECT MAX(id) FROM mesureFormation");
		requetes.put("getMesureUtilisation", "SELECT * FROM mesureUtilisation WHERE idProcessus=?");
		requetes.put("getMaxMesureUtilisation", "SELECT MAX(id) FROM mesureUtilisation");
		requetes.put("delMesureAcces", "DELETE FROM mesureAcces WHERE id=?");
		requetes.put("delMesureAccesIdProcessus", "DELETE FROM mesureAcces WHERE idProcessus=?");
		requetes.put("delMesureFormation", "DELETE FROM mesureFormation WHERE id=?");
		requetes.put("delMesureFormationIdProcessus", "DELETE FROM mesureFormation WHERE idProcessus=?");
		requetes.put("delMesureUtilisation", "DELETE FROM mesureUtilisation WHERE id=?");
		requetes.put("delMesureUtilisationIdProcessus", "DELETE FROM mesureUtilisation WHERE idProcessus=?");
		requetes.put("delMesureInformationExecProc", "DELETE FROM mesureUtilisation WHERE idProcessus=? AND idProjet=?");
		requetes.put("setMesureAcces", "INSERT INTO mesureAcces VALUES (NULL,?,?,?)");
		requetes.put("setMesureFormation", "INSERT INTO mesureFormation VALUES (NULL,?,?,?,?,?)");
		requetes.put("setMesureUtilisation", "INSERT INTO mesureUtilisation VALUES (NULL,?,?,?,?,?,?)");
		
		//mesure d'amélioration
		requetes.put("getMesureAmelioration", "SELECT * FROM mesureAmelioration WHERE idProcessus=? ORDER BY dateExport");
		requetes.put("setMesureAmelioration", "INSERT INTO mesureAmelioration VALUES (?,?,?,?,?,?,?,?)");
		requetes.put("delMesuresAmelioration", "DELETE FROM mesureAmelioration WHERE idProcessus=?");
		requetes.put("delUneMesureAmelioration", "DELETE FROM mesureAmelioration WHERE idProcessus=? AND dateExport=?");
	}

	/**
	 * Permet d'établir la connexion une base de données en utilisant
	 * les attributs de l'objet.
	 *
	 * @return : true si la connection est établie, false sinon.
	 * @throws AccesBDException
	 */
	public synchronized boolean ouvrirConnexion() throws SQLException
	{
		String connecteur = typeConnexion + E_Configuration.getServeurBaseDeDonnees() + E_Configuration.getNomBase();
		DriverManager.registerDriver(new Driver());
		//Class.forName(pilote);
		connexion = DriverManager.getConnection(connecteur, E_Configuration.getLoginBaseDeDonnees(), E_Configuration.getPwdBaseDeDonnees());

		return true;
	}

	/**
	 * Ferme la connexion à la base de données.
	 *
	 * @return : true si la deconnexion a eu lieu, false sinon.
	 * @throws AccesBDException
	 */
	public synchronized boolean fermerConnexion() throws SQLException
	{
		if (!connexion.isClosed())
			connexion.close();
		return true;
	}

	public synchronized int creerStatement() throws SQLException
	{
		statement = connexion.createStatement();
		statements.put(new Integer(idStatementCourant), statement);

		return idStatementCourant++;
	}

	public synchronized int creerPreparedStatement(String cle) throws SQLException
	{
		preparedStatement = connexion.prepareStatement(requetes.get(cle).toString());
		statements.put(new Integer(idStatementCourant), preparedStatement);

		return idStatementCourant++;
	}

	public synchronized void fermerStatement(int idStatement)
	{
		try
		{
			// Récupération du statement
			statement = (Statement) statements.get(new Integer(idStatement));

			// Fermeture du statement
			statement.close();

			// Suppression du statement de la liste des statements en cours
			statements.remove(new Integer(idStatement));
		}
		catch (Exception e)
		{}
	}

	public synchronized ResultSet executerRequete(int idStatement, String requete) throws SQLException
	{
		// Récupération du statement
		statement = (Statement) statements.get(new Integer(idStatement));

		// Exécution de la requête
		resultSet = statement.executeQuery(requete);

		return resultSet;
	}

	public synchronized ResultSet executerRequeteStockee(int idStatement, String cle) throws SQLException
	{
		// Récupération du statement
		preparedStatement = (PreparedStatement) statements.get(new Integer(idStatement));

		// Exécution de la requête
		if (!cle.substring(0, 3).equals("get"))
		{
			try
			{
				if (preparedStatement.executeUpdate() == 0)
					throw new NoRowInsertedException();
			}
			catch (SQLException e)
			{
				throw new NoRowInsertedException();
			}
		}
		else
			resultSet = preparedStatement.executeQuery();

		return resultSet;
	}

	public synchronized ResultSet executerRequeteStockee(int idStatement, String cle, ArrayList parametres) throws SQLException, NoRowInsertedException
	{
		// Récupération du statement
		preparedStatement = (PreparedStatement) statements.get(new Integer(idStatement));

		// Affectation des paramètres
		for (int i = 0; i < parametres.size(); i++)
		{
			if (parametres.get(i) instanceof String)
				preparedStatement.setString(i + 1, parametres.get(i).toString());
			else if (parametres.get(i) instanceof Integer)
				preparedStatement.setInt(i + 1, ((Integer) parametres.get(i)).intValue());
			else if (parametres.get(i) instanceof Double)
				preparedStatement.setDouble(i + 1, ((Double) parametres.get(i)).doubleValue());
			else if (parametres.get(i) instanceof Date)
				preparedStatement.setDate(i + 1, (Date) parametres.get(i));
			else if (parametres.get(i) instanceof Float)
				preparedStatement.setFloat(i + 1, ((Float) parametres.get(i)).floatValue());
			else if (parametres.get(i) == null)
				preparedStatement.setNull(i + 1,Types.NULL);
		}
		// Exécution de la requête
		if (!cle.substring(0, 3).equals("get"))
		{
			try
			{
				if (preparedStatement.executeUpdate() == 0)
					throw new NoRowInsertedException();
			}
			catch (NoRowInsertedException e)
			{
				throw e;
			}
			catch (SQLException e)
			{
				//System.out.println("ERREUR:" + e.getMessage());
				throw new NoRowInsertedException();
			}
		}
		else
			resultSet = preparedStatement.executeQuery();
		// Fermeture du statement
		//statement.close();

		return resultSet;
	}

	/**
	 * Envoi la requête spécifiée en paramètre à la base de données.
	 *
	 * @param requete : contenu de la requête SQL.
	 * @return : les lignes demandées par la requête sous forme d'un ResultSet.
	 * @throws AccesBDException
	 */
	/*
	public ResultSet executerRequete(String requete) throws SQLException
	{
	
		// Création du statement
		statement = connexion.createStatement();
		// Exécution de la requête
		resultSet = statement.executeQuery(requete);
		// Fermeture du statement
		//statement.close();
	
		return resultSet;
	}
	*/

	/**
	 * Envoi la requête dont l'identifiant est spécifié en paramètre à la base de données.
	 *
	 * @param cle : l'identifiant de la requête SQL dans la HashMap.
	 * @return : les lignes demandées par la requête sous forme d'un ResultSet.
	 * @throws AccesBDException
	 */
	/*
	public ResultSet executerRequeteStockee(String cle) throws SQLException
	{
		// Création du statement
		PreparedStatement statementPrepare = connexion.prepareStatement(requetes.get(cle).toString());
		// Exécution de la requête
		if (!cle.substring(0, 3).equals("get"))
		{
			try
			{
				if (statementPrepare.executeUpdate() == 0)
					throw new NoRowInsertedException();
			}
			catch (SQLException e)
			{
				throw new NoRowInsertedException();
			}
		}
		else
			resultSet = statementPrepare.executeQuery();
		// Fermeture du statement
		//statement.close();
		return resultSet;
	}
	*/

	/**
	 * Envoi la requête dont l'identifiant est spécifié en paramètre à la base de données.
	 *
	 * @param cle : l'identifiant de la requête SQL dans la HashMap.
	 * @param parametres : la liste des paramètres à définir dans la requette
	 * @return : les lignes demandées par la requête sous forme d'un ResultSet.
	 * @throws AccesBDException
	 */
	/*
	public ResultSet executerRequeteStockee(String cle, ArrayList parametres) throws SQLException, NoRowInsertedException
	{
		// Création du statement
		PreparedStatement statementPrepare = connexion.prepareStatement(requetes.get(cle).toString());
		// Affecation des paramètres
		for (int i = 0; i < parametres.size(); i++)
		{
		    if (parametres.get(i) instanceof String)
				statementPrepare.setString(i + 1, parametres.get(i).toString());
			if (parametres.get(i) instanceof Integer)
				statementPrepare.setInt(i + 1, ((Integer) parametres.get(i)).intValue());
			if (parametres.get(i) instanceof Double)
				statementPrepare.setDouble(i + 1, ((Double) parametres.get(i)).doubleValue());
			if (parametres.get(i) instanceof Date)
				statementPrepare.setDate(i + 1, (Date) parametres.get(i));
		}
		// Exécution de la requête
		if (!cle.substring(0, 3).equals("get"))
		{
			try
			{
				if (statementPrepare.executeUpdate() == 0)
					throw new NoRowInsertedException();
			}
			catch (NoRowInsertedException e)
			{
				throw e;
			}
			catch (SQLException e)
			{
				System.out.println("ERREUR :"+e.getMessage());
			    throw new NoRowInsertedException();
				//e.printStackTrace();
			}
		}
		else
			resultSet = statementPrepare.executeQuery();
		// Fermeture du statement
		//statement.close();
	
		return resultSet;
	}
	*/

	/**
	 * Méthode permettant de mettre l'autocommit à vrai ou faux
	 */
	public synchronized void setAutoCommit(boolean autocommit)
	{
		try
		{
			connexion.setAutoCommit(autocommit);
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Méthode permettant de faire un commit
	 */
	public synchronized void commit() throws SQLException
	{
		connexion.commit();
	}

	/**
	 * Méthode permettant de faire un rollback
	 */
	public synchronized void rollback()
	{
		try
		{
			connexion.rollback();
		}
		catch (SQLException e)
		{}
	}

	/**
	 * Destructeur fermant systématiquement la connexion.
	 */
	protected void finalize()
	{
		try
		{
			fermerConnexion();
		}
		catch (SQLException e)
		{}
	}

}
