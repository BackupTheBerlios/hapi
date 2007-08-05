/*
 * Créé le 19 sept. 2005
 */
package hapi.donnees;

/**
 * @author Cédric
 */
public class E_MesureRepresentation
{
	private String idProcessus;
	private String dateExport;
	private int interfaceE;
	private int interfaceS;
	private int scenario;
	private String comScenario;
	private int explicite;
	private String comExplicite;
	private int estime;
	private String comEstime;
	private double evaluationIT;
	private String comEvalIT;
	private int coherence;
	private String comCoherence;
	private int completude;
	private String comCompletude;
	private int nbDef;
	private int nbAct;
	private int nbProd;
	private int nbRole;
	private double evaluationMo;
	private String comEvalMo;
	private int nbProdType;
	private String comNbProdType;
	private double pourPlan;
	private int nbGuide;
	private String comGuide;
	private double pourGuide;
	private int nbDocu;
	private String comDocu;
	private double pourDocu;
	private double evaluationDocu;
	private String comEvalDocu;

	public E_MesureRepresentation(String idProcessus, String dateExport, int interfaceE, int interfaceS, int scenario, String comScenario, int explicite, String comExplicite, int estime, String comEstime, double evaluationIT, String comEvalIT, int coherence, String comCoherence, int completude, String comCompletude, int nbDef, int nbAct, int nbProd, int nbRole, double evaluationMo, String comEvalMo, int nbProdType, String comNbProdType, double pourPlan, int nbGuide, String comGuide, double pourGuide, int nbDocu,
			String comDocu, double pourDocu, double evaluationDocu, String comEvalDocu)
	{
		this.idProcessus = idProcessus;
		this.dateExport = dateExport;
		this.interfaceE = interfaceE;
		this.interfaceS = interfaceS;
		this.scenario = scenario;
		this.comScenario = comScenario;
		this.explicite = explicite;
		this.comExplicite = comExplicite;
		this.estime = estime;
		this.comEstime = comEstime;
		this.evaluationIT = evaluationIT;
		this.comEvalIT = comEvalIT;
		this.coherence = coherence;
		this.comCoherence = comCoherence;
		this.completude = completude;
		this.comCompletude = comCompletude;
		this.nbDef = nbDef;
		this.nbAct = nbAct;
		this.nbProd = nbProd;
		this.nbRole = nbRole;
		this.evaluationMo = evaluationMo;
		this.comEvalMo = comEvalMo;
		this.nbProdType = nbProdType;
		this.comNbProdType = comNbProdType;
		this.pourPlan = pourPlan;
		this.nbGuide = nbGuide;
		this.comGuide = comGuide;
		this.pourGuide = pourGuide;
		this.nbDocu = nbDocu;
		this.comDocu = comDocu;
		this.pourDocu = pourDocu;
		this.evaluationDocu = evaluationDocu;
		this.comEvalDocu = comEvalDocu;
	}
	
	public E_MesureRepresentation(String idProcessus, String dateExport, int interfaceE, int interfaceS, int nbDef, int nbAct, int nbProd, int nbRole)
	{
		this.idProcessus = idProcessus;
		this.dateExport = dateExport;
		this.interfaceE = interfaceE;
		this.interfaceS = interfaceS;
		this.scenario = 0;
		this.comScenario = "";
		this.explicite = 0;
		this.comExplicite = "";
		this.estime = 0;
		this.comEstime = "";
		this.evaluationIT = 0;
		this.comEvalIT = "";
		this.coherence = 0;
		this.comCoherence = "";
		this.completude = 0;
		this.comCompletude = "";
		this.nbDef = nbDef;
		this.nbAct = nbAct;
		this.nbProd = nbProd;
		this.nbRole = nbRole;
		this.evaluationMo = 0;
		this.comEvalMo = "";
		this.nbProdType = 0;
		this.comNbProdType = "";
		this.pourPlan = 0;
		this.nbGuide = 0;
		this.comGuide = "";
		this.pourGuide = 0;
		this.nbDocu = 0;
		this.comDocu = "";
		this.pourDocu = 0;
		this.evaluationDocu = 0;
		this.comEvalDocu = "";
	}	

	/**
	 * @return Renvoie dateExport.
	 */
	public String getDateExport()
	{
		return dateExport;
	}

	/**
	 * @return Renvoie coherence.
	 */
	public int getCoherence()
	{
		return coherence;
	}

	/**
	 * @return Renvoie comCoherence.
	 */
	public String getComCoherence()
	{
		return comCoherence;
	}

	/**
	 * @return Renvoie comCompletude.
	 */
	public String getComCompletude()
	{
		return comCompletude;
	}

	/**
	 * @return Renvoie comDocu.
	 */
	public String getComDocu()
	{
		return comDocu;
	}

	/**
	 * @return Renvoie comEstime.
	 */
	public String getComEstime()
	{
		return comEstime;
	}

	/**
	 * @return Renvoie comEvalDocu.
	 */
	public String getComEvalDocu()
	{
		return comEvalDocu;
	}

	/**
	 * @return Renvoie comEvalIT.
	 */
	public String getComEvalIT()
	{
		return comEvalIT;
	}

	/**
	 * @return Renvoie comEvalMo.
	 */
	public String getComEvalMo()
	{
		return comEvalMo;
	}

	/**
	 * @return Renvoie comExplicite.
	 */
	public String getComExplicite()
	{
		return comExplicite;
	}

	/**
	 * @return Renvoie comGuide.
	 */
	public String getComGuide()
	{
		return comGuide;
	}

	/**
	 * @return Renvoie comNbProdType.
	 */
	public String getComNbProdType()
	{
		return comNbProdType;
	}

	/**
	 * @return Renvoie competude.
	 */
	public int getCompletude()
	{
		return completude;
	}

	/**
	 * @return Renvoie comScenario.
	 */
	public String getComScenario()
	{
		return comScenario;
	}

	/**
	 * @return Renvoie estime.
	 */
	public int getEstime()
	{
		return estime;
	}

	/**
	 * @return Renvoie evaluationDocu.
	 */
	public double getEvaluationDocu()
	{
		return evaluationDocu;
	}

	/**
	 * @return Renvoie evaluationIT.
	 */
	public double getEvaluationIT()
	{
		return evaluationIT;
	}

	/**
	 * @return Renvoie evaluationMo.
	 */
	public double getEvaluationMo()
	{
		return evaluationMo;
	}

	/**
	 * @return Renvoie explicite.
	 */
	public int getExplicite()
	{
		return explicite;
	}

	/**
	 * @return Renvoie idProcessus.
	 */
	public String getIdProcessus()
	{
		return idProcessus;
	}

	/**
	 * @return Renvoie interfaceE.
	 */
	public int getInterfaceE()
	{
		return interfaceE;
	}

	/**
	 * @return Renvoie interfaceS.
	 */
	public int getInterfaceS()
	{
		return interfaceS;
	}

	/**
	 * @return Renvoie nbAct.
	 */
	public int getNbAct()
	{
		return nbAct;
	}

	/**
	 * @return Renvoie nbDef.
	 */
	public int getNbDef()
	{
		return nbDef;
	}

	/**
	 * @return Renvoie nbDocu.
	 */
	public int getNbDocu()
	{
		return nbDocu;
	}

	/**
	 * @return Renvoie nbGuide.
	 */
	public int getNbGuide()
	{
		return nbGuide;
	}

	/**
	 * @return Renvoie nbProd.
	 */
	public int getNbProd()
	{
		return nbProd;
	}

	/**
	 * @return Renvoie nbProdType.
	 */
	public int getNbProdType()
	{
		return nbProdType;
	}

	/**
	 * @return Renvoie nbRole.
	 */
	public int getNbRole()
	{
		return nbRole;
	}

	/**
	 * @return Renvoie pourDocu.
	 */
	public double getPourDocu()
	{
		return pourDocu;
	}

	/**
	 * @return Renvoie pourGuide.
	 */
	public double getPourGuide()
	{
		return pourGuide;
	}

	/**
	 * @return Renvoie pourPlan.
	 */
	public double getPourPlan()
	{
		return pourPlan;
	}

	/**
	 * @return Renvoie scenario.
	 */
	public int getScenario()
	{
		return scenario;
	}
}
