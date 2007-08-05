/*
 * Créé le 7 sept. 2005
 */
package hapi.application;

import hapi.application.ressources.Bundle;
import hapi.donnees.modeles.ModeleTableClefValeur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Cédric
 */
public class C_AssocierDescriptionMaturite
{
	public final static int NOMBRE_NIVEAUX = 5;
	private ModeleTableClefValeur leModele = null;
	
	public C_AssocierDescriptionMaturite()
	{
		leModele = new ModeleTableClefValeur();
		
		leModele.addColumn(Bundle.getText("BD_AssocierDescriptionMaturite_Niveau"));
		leModele.addColumn(Bundle.getText("BD_AssocierDescriptionMaturite_Description"));
		
		HashMap lesNiveaux = C_DescriptionMaturite.getLesDescriptions(); 
	
		for (int i=0;i<NOMBRE_NIVEAUX;i++)
		{
			ArrayList uneLigne = new ArrayList();
			if (lesNiveaux.containsKey(new Integer(i)))
			{
				uneLigne.add(new Integer(i+1));
				uneLigne.add(lesNiveaux.get(new Integer(i)));
			}
			else
			{
				uneLigne.add(new Integer(i+1));
				uneLigne.add("");				
			}
			
			leModele.addRow(uneLigne.toArray());
		}
	}
	
	public ModeleTableClefValeur getModele()
	{
		return leModele;
	}
	
	public void SauvegarderAssociations() throws SQLException
	{
		C_AccesBaseDonnees cBase = new C_AccesBaseDonnees();
		int idStatement=0;
		
		try
		{
			cBase.ouvrirConnexion();
			cBase.setAutoCommit(false);
			ArrayList lesParametres = new ArrayList();
			ArrayList lesValeurs = leModele.getValeurs();
			HashMap laListeDeDescription = new HashMap();
			
			if (C_DescriptionMaturite.getLesDescriptions().size() == 0)
			{
				for (int i=0;i<lesValeurs.size();i++)
				{
					//Insertion
					lesParametres.clear();
					laListeDeDescription.put(new Integer(i),lesValeurs.get(i));
					lesParametres.add(new Integer(i+1));
					lesParametres.add(lesValeurs.get(i));
					cBase.fermerStatement(idStatement);
					idStatement = cBase.creerPreparedStatement("setListeDescriptionMaturite");
					cBase.executerRequeteStockee(idStatement, "setListeDescriptionMaturite", lesParametres);
					cBase.fermerStatement(idStatement);
				}
			}
			else
			{
				for (int i=0;i<lesValeurs.size();i++)
				{
					//Insertion
					lesParametres.clear();
					laListeDeDescription.put(new Integer(i),lesValeurs.get(i));
					lesParametres.add(lesValeurs.get(i));
					lesParametres.add(new Integer(i+1));
					cBase.fermerStatement(idStatement);
					idStatement = cBase.creerPreparedStatement("setUpdateListeDescriptionMaturite");
					cBase.executerRequeteStockee(idStatement, "setUpdateListeDescriptionMaturite", lesParametres);
					cBase.fermerStatement(idStatement);
				}
			}
			
			cBase.commit();
			
			C_DescriptionMaturite.setLesDescriptions(laListeDeDescription);
		}
		catch (SQLException e)
		{
			cBase.rollback();
			throw e;
		}
		finally
		{
			cBase.fermerStatement(idStatement);
			try
			{
				cBase.fermerConnexion();
			}
			catch (SQLException e1)
			{}
		}
		
	}
}
