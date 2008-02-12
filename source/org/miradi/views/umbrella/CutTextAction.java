/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class CutTextAction extends AbstractAction
{
	public CutTextAction(JTextComponent fieldToUse)
	{
		field = fieldToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		field.cut();
	}
	
	JTextComponent field;

}
