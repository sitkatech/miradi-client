/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.main.MainWindow;

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
