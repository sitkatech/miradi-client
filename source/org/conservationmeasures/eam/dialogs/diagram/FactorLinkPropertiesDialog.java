/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.diagram;

import javax.swing.Box;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogPanel;
import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.main.MainWindow;

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
		return factorPanel.getJumpActionClass();
	}
	
	private ModelessDialogPanel factorPanel;
}
