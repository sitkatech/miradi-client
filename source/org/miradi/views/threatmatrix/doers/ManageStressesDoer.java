/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix.doers;

import org.martus.swing.Utilities;
import org.miradi.dialogs.stress.StressListManagementPanel;
import org.miradi.dialogs.threatstressrating.properties.ManageStressesDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Target;
import org.miradi.views.ObjectsDoer;

public class ManageStressesDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getSingleSelected(Target.getObjectType()) == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			BaseObject selectedTarget = getSingleSelected(Target.getObjectType());
			StressListManagementPanel stressListManagementPanel = new StressListManagementPanel(getProject(), getMainWindow(), selectedTarget.getRef(), getMainWindow().getActions());
			ManageStressesDialog manageStressesDialog = new ManageStressesDialog(getMainWindow(), (Target)selectedTarget, stressListManagementPanel);
			stressListManagementPanel.updateSplitterLocation();
			Utilities.centerDlg(manageStressesDialog);
			manageStressesDialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
