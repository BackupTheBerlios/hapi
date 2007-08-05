/*
 * Created on 10 mars 2005
 *
 */
package hapi.application.modele;

import java.awt.Toolkit;
import java.text.DecimalFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;

/**
 * @author Natalia
 *
 */
public class NegatifDecimalModel extends InternationalFormatter
{
    private static final long serialVersionUID = -4071714003148456818L;
    private DocumentFilter decimalFilter = new DecimalFilter();

	public NegatifDecimalModel(DecimalFormat df)
	{
		super(df);
		setValueClass(Double.class);
	}

	protected DocumentFilter getDocumentFilter()
	{
		return decimalFilter;
	}

	class DecimalFilter extends DocumentFilter
	{
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
		{
			for (int i = 0; i < text.length(); i++)
			{
				if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '.' && text.charAt(i) != '-')
				{
					Toolkit.getDefaultToolkit().beep();
					return;
				}
			}
			super.replace(fb, offset, length, text, attrs);
		}
	}
}
