/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class TeamRemoveMember extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getObjects().length == 0)
			return false;
		
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata.getTeamResourceIdList().contains(getSelectedId()))
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProjectMetadata metadata = getProject().getMetadata();
		BaseId selectedId = getSelectedId();
		clearSelection();
		try
		{
			getProject().executeCommand(new CommandBeginTransaction());
			removeResourceFromList(metadata, selectedId);
			deleteResourceIfNotUsedElsewhere(selectedId);
			getProject().executeCommand(new CommandEndTransaction());
		}
		catch (ParseException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Text|Unknown error prevented removing this person from the team"));
		}
	}

	private void removeResourceFromList(ProjectMetadata metadata, BaseId selectedId) throws ParseException, CommandFailedException
	{
		Command cmd = CommandSetObjectData.createRemoveIdCommand(metadata, ProjectMetadata.TAG_TEAM_RESOURCE_IDS, selectedId);
		getProject().executeCommand(cmd);
	}

	private void deleteResourceIfNotUsedElsewhere(BaseId selectedId) throws CommandFailedException
	{
		Task[] tasksThatUseThisResource = getProject().findTasksThatUseThisResource(selectedId);
		if (tasksThatUseThisResource.length==0)
		{
			ProjectResource resource = (ProjectResource)getProject().findObject(ObjectType.PROJECT_RESOURCE, selectedId);
			getProject().executeCommands(resource.createCommandsToClear());
			getProject().executeCommand(new CommandDeleteObject(ObjectType.PROJECT_RESOURCE, selectedId));
		}
	}
}
