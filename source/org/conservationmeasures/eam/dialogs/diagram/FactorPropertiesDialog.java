/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.diagram;

import javax.swing.Box;
import javax.swing.JComponent;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogPanel;
import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.main.MainWindow;

public class FactorPropertiesDialog extends ModelessDialogWithClose
{

	public FactorPropertiesDialog(MainWindow parent, FactorPropertiesPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		factorPanel = panel;
	}
	
	protected JComponent createMainPanel()
	{
		return getWrappedPanel();
	}

	public void addAdditionalButtons(Box buttonBar)
	{
		createDirectionsButton(buttonBar);
	}

	protected Class getJumpAction()
	{
		return ((ModelessDialogPanel)factorPanel.tabs.getSelectedComponent()).getJumpActionClass();
	}
	
	FactorPropertiesPanel factorPanel;
}
