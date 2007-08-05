/*
 * Créé le 7 sept. 2005
 */
package hapi.application;

import java.util.HashMap;

/**
 * @author Cédric
 */
public class C_DescriptionMaturite
{
	private static HashMap lesDescriptions = new HashMap();
	
	public static HashMap getLesDescriptions()
	{
		return lesDescriptions;
	}
	
	public static void setLesDescriptions(HashMap les_Descriptions)
	{
		lesDescriptions = les_Descriptions;
	}

}
