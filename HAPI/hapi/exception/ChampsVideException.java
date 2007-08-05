/*
 * Auteur Cédric
 *
 */
package hapi.exception;

public class ChampsVideException extends Exception
{
    private static final long serialVersionUID = -8631738858655121854L;
    private int Numero = -1;
	
	public ChampsVideException()
	{
		super();
	}
	
	public ChampsVideException(int Numero)
	{
		super();
		this.Numero = Numero;
	}
	
	public ChampsVideException(String message)
	{
		super(message);
	}	
	
	
	/**
	 * @return Renvoie numero.
	 */
	public int getNumero()
	{
		return Numero;
	}
}
