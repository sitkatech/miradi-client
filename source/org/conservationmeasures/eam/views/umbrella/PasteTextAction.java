/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class PasteTextAction extends AbstractAction
{
	public PasteTextAction(JTextComponent fieldToUse)
	{
		field = fieldToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		field.paste();
	}
	
	JTextComponent field;

}
