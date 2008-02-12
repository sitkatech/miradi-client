/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary.doers;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.ObjectsDoer;

public class TeamCreateMemberDoer extends ObjectsDoer
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
			
			if (getPicker() != null)
				getPicker().ensureObjectVisible(resource.getRef());
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
