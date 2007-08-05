/*
 * Created on 25 févr. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package hapi.application.importation;

/**
 * @author stephane
 * */
public class ParamSelectionElemArbre
{
	private static String idProcessus;
	private static String idEvaluationQuantitative;

	public static String getIdEvaluationQuantitative()
	{
		return idEvaluationQuantitative;
	}

	public static String getIdProcessus()
	{
		return idProcessus;
	}

	public static void setIdEvaluationQuantitative(String idEval)
	{
		idEvaluationQuantitative = idEval;
	}

	public static void setIdProcessus(String idProc)
	{
		idProcessus = idProc;
	}

}
