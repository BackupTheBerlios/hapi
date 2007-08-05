/*
 * Créé le 1 oct. 2005
 */
package hapi.exception;

/**
 * @author Cédric
 */
public class NiveauInsuffisantException extends Exception
{
    private static final long serialVersionUID = 4162681275897429450L;

    public NiveauInsuffisantException(String message)
	{
		super(message);
	}
}
