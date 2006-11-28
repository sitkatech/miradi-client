/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.dialogs.PreferencesDialog;
import org.conservationmeasures.eam.dialogs.PreferencesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class Preferences extends MainWindowDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{		
		showPreferencesDialog();
	}
	
	void showPreferencesDialog()
	{
		PreferencesPanel preferencesPanel = new PreferencesPanel(getMainWindow());
		//TODO change heading
		PreferencesDialog dlg = new PreferencesDialog(getMainWindow(), preferencesPanel, "Preferences");
		dlg.setVisible(true);	
	}
}
