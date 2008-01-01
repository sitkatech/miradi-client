/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.doers;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;
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
			getProject().executeCommand(new CommandBeginTransaction());
			ProjectResource resource = createBlankResource();
			addTeamMemberRole(resource);
			getView().showResourcePropertiesDialog(resource);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void addTeamMemberRole(ProjectResource resource) throws Exception
	{
		String codes = resource.getData(ProjectResource.TAG_ROLE_CODES);
		CodeList listData = new CodeList(codes);
		if (!listData.contains(ResourceRoleQuestion.TeamMemberRoleCode))
		{
			listData.add(ResourceRoleQuestion.TeamMemberRoleCode);
			Command cmd = new CommandSetObjectData(resource.getType(), resource.getId(), resource.TAG_ROLE_CODES, listData.toString());
			getProject().executeCommand(cmd);
		}
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
