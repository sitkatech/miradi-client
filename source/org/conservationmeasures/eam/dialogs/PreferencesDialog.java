/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
		disposablePanel.dispose();
		disposablePanel = null;
		super.dispose();
	}
	
	DisposablePanel disposablePanel;
}
