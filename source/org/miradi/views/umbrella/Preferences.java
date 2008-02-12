/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.PreferencesDialog;
import org.miradi.dialogs.base.PreferencesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.MainWindowDoer;

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
		PreferencesDialog dlg = new PreferencesDialog(getMainWindow(), preferencesPanel, "Miradi Preferences");
		dlg.pack();
		Utilities.centerDlg(dlg);
		dlg.setVisible(true);	
	}
}
