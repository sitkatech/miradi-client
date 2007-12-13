/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		if (getSelectedHierarchies().length == 0)
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
			ManageStressesDialog manageStressesDialog = new ManageStressesDialog(getMainWindow(), stressListManagementPanel);

			stressListManagementPanel.updateSplitterLocation();
			manageStressesDialog.pack();
			Utilities.centerDlg(manageStressesDialog);
			manageStressesDialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
