/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.doers;

import org.conservationmeasures.eam.dialogs.stress.StressListManagementPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.properties.ManageStressesDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.martus.swing.Utilities;

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
