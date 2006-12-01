package org.conservationmeasures.eam.views.summary;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.ViewDoer;

public class TeamCreateMemberDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ProjectResource resource = createBlankResource();
			
			addMemberToList(resource);
			
			getView().showPropertiesDialog(resource);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private void addMemberToList(ProjectResource resource) throws ParseException, CommandFailedException
	{
		ProjectMetadata metadata = getProject().getMetadata();
		Command cmd = CommandSetObjectData.createAppendIdCommand(metadata, ProjectMetadata.TAG_TEAM_RESOURCE_IDS, resource.getId());
		getProject().executeCommand(cmd);
	}

	private ProjectResource createBlankResource() throws CommandFailedException
	{
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.PROJECT_RESOURCE);
		getProject().executeCommand(cmd);
		BaseId baseId = cmd.getCreatedId();
		ProjectResource resource = (ProjectResource)getProject().findObject(ObjectType.PROJECT_RESOURCE, baseId);
		return resource;
	}
}
