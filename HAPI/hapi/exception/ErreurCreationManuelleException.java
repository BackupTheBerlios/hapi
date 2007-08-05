/*
 * Fichier ErreurCreationManuelleException.java
 * Auteur C�dric
 *
 */
package hapi.exception;

/**
 * Exception contenant un message d'erreur lors de la cr�ation manuelle d'une �valuation
 */
public class ErreurCreationManuelleException extends Exception
{
    private static final long serialVersionUID = 5430362798796741347L;

    public ErreurCreationManuelleException(String leMessage)
	{
		super(leMessage);
	}

}
