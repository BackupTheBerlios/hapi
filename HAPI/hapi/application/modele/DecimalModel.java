/*
 * Created on 2 févr. 2005
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
public class DecimalModel extends InternationalFormatter
{
    private static final long serialVersionUID = 2307762214394081704L;
    private DocumentFilter decimalFilter = new DecimalFilter();

	public DecimalModel(DecimalFormat df)
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
				if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '.')
				{
					Toolkit.getDefaultToolkit().beep();
					return;
				}
			}

			super.replace(fb, offset, length, text, attrs);
		}
	}

}
