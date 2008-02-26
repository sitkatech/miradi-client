/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.diagram;

import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.main.MainWindow;

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
		if(factorPanel == null)
			return null;
		
		ModelessDialogPanel selectedComponent = (ModelessDialogPanel)factorPanel.tabs.getSelectedComponent();
		if(selectedComponent == null)
			return null;
		return selectedComponent.getJumpActionClass();
	}
	
	FactorPropertiesPanel factorPanel;
}
