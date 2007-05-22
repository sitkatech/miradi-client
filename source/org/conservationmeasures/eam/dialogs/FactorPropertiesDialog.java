package org.conservationmeasures.eam.dialogs;

import javax.swing.Box;

import org.conservationmeasures.eam.main.MainWindow;

public class FactorPropertiesDialog extends ModelessDialogWithClose
{

	public FactorPropertiesDialog(MainWindow parent, FactorPropertiesPanel panel, String headingText)
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
		return ((ModelessDialogPanel)factorPanel.tabs.getSelectedComponent()).getJumpActionClass();
	}
	
	FactorPropertiesPanel factorPanel;
}
