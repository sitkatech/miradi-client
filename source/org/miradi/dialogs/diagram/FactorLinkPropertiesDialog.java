/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.diagram;

import javax.swing.Box;

import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.main.MainWindow;

public class FactorLinkPropertiesDialog extends ModelessDialogWithClose
{

	public FactorLinkPropertiesDialog(MainWindow parent, ModelessDialogPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		factorPanel = panel;
	}
	
	public void addAdditionalButtons(Box buttonBar)
	{
		createDirectionsButton(buttonBar);
	}

	protected Class getJumpAction()
	{
		if(factorPanel == null)
			return null;
		
		return factorPanel.getJumpActionClass();
	}
	
	private ModelessDialogPanel factorPanel;
}
