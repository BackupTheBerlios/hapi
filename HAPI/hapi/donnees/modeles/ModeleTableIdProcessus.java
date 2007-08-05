/*
 * Créé le 23 sept. 2005
 */
package hapi.donnees.modeles;

import java.util.ArrayList;

/**
 * @author Cédric
 *
 */
public class ModeleTableIdProcessus extends ModeleTable
{
    private static final long serialVersionUID = 7254056165744585543L;
    private ArrayList id = null;

	public ModeleTableIdProcessus()
	{
		id = new ArrayList();
	}

	public ArrayList getId()
	{
		return id;
	}

}