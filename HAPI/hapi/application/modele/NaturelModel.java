/*
 * Created on 25 févr. 2005
 *
 */
package hapi.application.modele;

import java.text.NumberFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;

/**
 * @author boursier
 *
 */
public class NaturelModel extends InternationalFormatter
{
    private static final long serialVersionUID = 4623354848225759247L;
    private DocumentFilter naturelFilter = new NaturelFilter();

	public NaturelModel(NumberFormat df)
	{
		super(df);
		setValueClass(Integer.class);
	}

	protected DocumentFilter getDocumentFilter()
	{
		return naturelFilter;
	}

	class NaturelFilter extends DocumentFilter
	{
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
		{
			for (int i = 0; i < text.length(); i++)
			{
				if (!Character.isDigit(text.charAt(i)))
				{
					return;
				}
			}
			super.replace(fb, offset, length, text, attrs);
		}
	}
}
