/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.planning.ColumnManager;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.planning.RowManager;

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
		String visibleRowsAsString = RowManager.getVisibleRowCodes(viewData).toString();
		String visibleColsAsString = ColumnManager.getVisibleColumnCodes(viewData).toString();
		
		CommandCreateObject createConfiguration = new CommandCreateObject(PlanningViewConfiguration.getObjectType());
		getProject().executeCommand(createConfiguration);
		
		ORef newConfigurationRef = createConfiguration.getObjectRef();
		CommandSetObjectData setVisibleRowsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_ROW_CONFIGURATION, visibleRowsAsString);
		getProject().executeCommand(setVisibleRowsCommand);
		
		CommandSetObjectData setVisibleColsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_COL_CONFIGURATION, visibleColsAsString);
		getProject().executeCommand(setVisibleColsCommand);
		
		CommandSetObjectData selectCurrentConfiguration = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, newConfigurationRef);
		getProject().executeCommand(selectCurrentConfiguration);
		
		CommandSetObjectData setConfigurationLabel = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_LABEL, getConfigurationDefaultLabel(getProject()));
		getProject().executeCommand(setConfigurationLabel);
		
		CommandSetObjectData setStyleChoice = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_STYLE_CHOICE, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
		getProject().executeCommand(setStyleChoice);
	}

	public static String getConfigurationDefaultLabel(Project project)
	{
		return "[Custom " + project.getPlanningViewConfigurationPool().size() + "]"; 
	}
}
