/*
 * Fichier C_Commentaire.java
 * Auteur Cédric
 *
 */
package hapi.application;

import hapi.application.metier.C_ExecutionProcessus;

import java.sql.SQLException;

/**
 * Controleur permettant la gestion du commentaire d'un projet
 */
public class C_Commentaire
{
	public void modifierCommentaire(String idProcessus, String idProjet, String commentaire) throws SQLException
	{
		C_ExecutionProcessus.modifierCommentaireProjet(idProcessus, idProjet, commentaire);
	}
}
