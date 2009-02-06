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
