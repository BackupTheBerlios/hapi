/*
 * Created on 25 févr. 2005
 *
 */
package hapi.application.modele;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;

/**
 * @author boursier
 *
 */
public class DateModel extends InternationalFormatter
{
    private static final long serialVersionUID = 8368736031555517573L;
    private DocumentFilter dateFilter = new DateFilter();

	public DateModel(SimpleDateFormat df)
	{
		super(df);
		setValueClass(Date.class);
	}

	protected DocumentFilter getDocumentFilter()
	{
		return dateFilter;
	}

	class DateFilter extends DocumentFilter
	{
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
		{
			for (int i = 0; i < text.length(); i++)
			{
				if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '/')
					return;
			}
			super.replace(fb, offset, length, text, attrs);
		}
	}
}
