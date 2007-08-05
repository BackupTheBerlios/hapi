/*
 * Created on 9 janv. 2005
 *
 */
package hapi.application;

import hapi.application.metier.C_Processus;
import hapi.donnees.E_NiveauMaturite;
import hapi.exception.NoRowInsertedException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Vincent,Cédric
 *
 */

public class C_NiveauMaturite
{
	//Contient les niveaux de maturité des processus
	//Structure : <id_Processus><ArrayList de E_NiveauMaturite>
	static private HashMap lesNiveauxMaturite = new HashMap();
	
	static public ArrayList getNiveauMaturite(String idProcessus)
	{
		return (ArrayList) lesNiveauxMaturite.get(idProcessus);
	}
	
	static public E_NiveauMaturite getNiveauMaturite(String idProcessus,int niveau)
	{
		return (E_NiveauMaturite) ((ArrayList) lesNiveauxMaturite.get(idProcessus)).get(niveau);
	}
	
	static public void ajouterNiveauMaturite(String idProcessus,ArrayList lesNiveaux)
	{
		lesNiveauxMaturite.remove(idProcessus);
		lesNiveauxMaturite.put(idProcessus,lesNiveaux);
	}
	
	static public void modifierNiveauMaturite(String idProcessus,E_NiveauMaturite leNiveau)
	{
		ArrayList lesNiveaux = (ArrayList) lesNiveauxMaturite.get(idProcessus);
		lesNiveaux.remove(leNiveau.getNiveau()-1);
		lesNiveaux.add(leNiveau.getNiveau()-1,leNiveau);
		
		lesNiveauxMaturite.remove(idProcessus);
		lesNiveauxMaturite.put(idProcessus,lesNiveaux);
	}
	
	static public void supprimerNiveauMaturite(String idProcessus)
	{
		lesNiveauxMaturite.remove(idProcessus);
	}
	
	static public void supprimerNiveauMaturiteEnBase(String idProcessus,C_AccesBaseDonnees cBase) throws SQLException
	{
		int idStatement = 0;
		ArrayList lesParametres = new ArrayList();
		lesParametres.add(idProcessus);

		try
		{
			idStatement = cBase.creerPreparedStatement("delNiveauMaturite");
			cBase.executerRequeteStockee(idStatement, "delNiveauMaturite", lesParametres);
			cBase.fermerStatement(idStatement);
		}
		catch (NoRowInsertedException e)
		{}		
	}	
	
	
	public void modifierMaturiteProcessus(String idProcessus, String niveau, String commentaire, Date datePassage, Date dateObjectif) throws NumberFormatException, SQLException
	{
		//tentative de conversion du niveau en int : lance une NumberFormatException en cas d'erreur
		int niv = Integer.parseInt(niveau);
		if (niv >= 1 && niv <= 5)
			C_Processus.modifierMaturite(idProcessus, niv, commentaire, datePassage, dateObjectif);
		else
			throw new NumberFormatException();
	}
}