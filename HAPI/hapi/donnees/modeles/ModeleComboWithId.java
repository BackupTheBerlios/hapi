/*
 * Created on 25 févr. 2005
 *
 */
package hapi.donnees.modeles;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

/**
 * @author boursier
 *
 */
public class ModeleComboWithId extends DefaultComboBoxModel
{
    private static final long serialVersionUID = 6487266996977971402L;
    private ArrayList lesIdentifiants = new ArrayList();

	// ajoute un element
	public void addElement(String Identifiant, Object anObject)
	{
		lesIdentifiants.add(Identifiant);
		super.addElement(anObject);
	}

	// supprimer un element
	public void removeElementAt(int index)
	{
		lesIdentifiants.remove(index);
		super.removeElementAt(index);
	}

	// retourne la clé 
	public String getCleAt(int index)
	{
		return (String) lesIdentifiants.get(index);
	}
	
	public String getSelectedCle()
	{
		return (String) lesIdentifiants.get(getIndexOf(getSelectedItem()));
	}
	
	public void setSelectedCle(String cle)
	{
		setSelectedItem(getElementAt(lesIdentifiants.indexOf(cle)));
	}	
	
	public void removeAllElements()
	{
		lesIdentifiants.clear();
		super.removeAllElements();
	}
}