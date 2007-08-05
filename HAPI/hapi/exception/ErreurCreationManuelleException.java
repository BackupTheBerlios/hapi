/*
 * Fichier ErreurCreationManuelleException.java
 * Auteur Cédric
 *
 */
package hapi.exception;

/**
 * Exception contenant un message d'erreur lors de la création manuelle d'une évaluation
 */
public class ErreurCreationManuelleException extends Exception
{
    private static final long serialVersionUID = 5430362798796741347L;

    public ErreurCreationManuelleException(String leMessage)
	{
		super(leMessage);
	}

}
