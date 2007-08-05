/*
 * Auteur Cédric
 *
 */
package hapi.exception;

import java.sql.SQLException;

/**
 * Exception lorsqu'aucune ligne n'a été insérée
 */
public class NoRowInsertedException extends SQLException
{
    private static final long serialVersionUID = 8462557596823485169L;
}
