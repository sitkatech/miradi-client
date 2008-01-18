/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.dialogs.base.PreferencesDialog;
import org.conservationmeasures.eam.dialogs.base.PreferencesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.martus.swing.Utilities;

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
