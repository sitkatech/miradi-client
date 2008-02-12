/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewCustomLabelPropertiesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;

public class RenamePlanningViewConfigurationDoer extends AbstractPlanningViewConfigurationDoer
{
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{
			renameConfiguration();
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void renameConfiguration()
	{
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			String oRefAsString = viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			ORef configurationRef = ORef.createFromString(oRefAsString);

			PlanningViewCustomLabelPropertiesPanel labelPropertiesPanel = new PlanningViewCustomLabelPropertiesPanel(getProject(), configurationRef);
			ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), labelPropertiesPanel, labelPropertiesPanel.getPanelDescription()); 
			getView().showFloatingPropertiesDialog(dlg);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
}
