/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;

public class PreferencesDialog extends ModelessDialogWithClose
{
	public PreferencesDialog(MainWindow mainWindowToUse, DisposablePanel disposablePanelToUse, String headingText)
	{
		super(mainWindowToUse, disposablePanelToUse, headingText);
		disposablePanel = disposablePanelToUse;
	}
	
	public void dispose()
	{
		super.dispose();
		disposablePanel = null;
	}
	
	DisposablePanel disposablePanel;
}
