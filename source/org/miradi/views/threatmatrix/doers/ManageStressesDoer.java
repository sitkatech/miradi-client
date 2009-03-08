/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.threatmatrix.doers;

import org.martus.swing.Utilities;
import org.miradi.dialogs.stress.ManageStressesDialog;
import org.miradi.dialogs.stress.StressListManagementPanel;
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
			// NOTE: Should create Stress panel without visibility panel
			StressListManagementPanel stressListManagementPanel = new StressListManagementPanel(getMainWindow(), selectedTarget.getRef(), getMainWindow().getActions());
			ManageStressesDialog manageStressesDialog = new ManageStressesDialog(getMainWindow(), (Target)selectedTarget, stressListManagementPanel);
			stressListManagementPanel.becomeActive();
			Utilities.centerDlg(manageStressesDialog);
			manageStressesDialog.setVisible(true);
			stressListManagementPanel.becomeInactive();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
