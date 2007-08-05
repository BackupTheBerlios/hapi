package hapi.donnees.modeles;

import java.util.ArrayList;

/**
 * @author Robin EYSSERIC
 */
public class ModeleTableDemandesModification extends ModeleTable
{
    private static final long serialVersionUID = -3910110371347931643L;
    /**
	 * Permet de conserver les clefs primaires des demandes
	 * dans le modele sans les afficher
	 */
	private ArrayList idDemandes = null;

	public ModeleTableDemandesModification()
	{
		idDemandes = new ArrayList();
	}

	public ArrayList getIdDemandes()
	{
		return idDemandes;
	}

}