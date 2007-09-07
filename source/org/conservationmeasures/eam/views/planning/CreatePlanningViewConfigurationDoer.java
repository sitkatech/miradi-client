/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewCustomLabelPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreatePlanningViewConfigurationDoer extends ViewDoer
{
	public boolean isAvailable()
	{	
		if (! isPlanningView())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			createPlanningViewConfiguration();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void createPlanningViewConfiguration() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		String hiddenRowsAsString = viewData.getData(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES);
		String hiddenColsAsString = viewData.getData(ViewData.TAG_PLANNING_HIDDEN_COL_TYPES);
		
		CommandCreateObject createConfiguration = new CommandCreateObject(PlanningViewConfiguration.getObjectType());
		getProject().executeCommand(createConfiguration);
		
		ORef newConfigurationRef = createConfiguration.getObjectRef();
		CommandSetObjectData setHiddenRowsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_ROW_CONFIGURATION, hiddenRowsAsString);
		getProject().executeCommand(setHiddenRowsCommand);
		
		CommandSetObjectData setHiddenColsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_COL_CONFIGURATION, hiddenColsAsString);
		getProject().executeCommand(setHiddenColsCommand);
		
		CommandSetObjectData selectCurrentConfiguration = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, newConfigurationRef);
		getProject().executeCommand(selectCurrentConfiguration);
		
		CommandSetObjectData setConfigurationLabel = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_LABEL, getConfigurationDefaultLabel());
		getProject().executeCommand(setConfigurationLabel);
		
		PlanningViewCustomLabelPropertiesPanel labelPropertiesPanel = new PlanningViewCustomLabelPropertiesPanel(getProject(), createConfiguration.getObjectRef());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), labelPropertiesPanel, labelPropertiesPanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}

	private String getConfigurationDefaultLabel()
	{
		return "Customization " + getProject().getPlanningViewConfigurationPool().size(); 
	}
}
