/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class TeamRemoveMember extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getObjects().length == 0)
			return false;

		if(getProject().getPool(ObjectType.PROJECT_RESOURCE).getIdList().contains(getSelectedId()))
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		BaseId selectedId = getSelectedId();
		clearSelection();
		try
		{
			removeFromTeam(selectedId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Text|Unknown error prevented removing this person from the team"));
		}
	}

	private void removeFromTeam(BaseId selectedId) throws ParseException, CommandFailedException
	{
		int type = ObjectType.PROJECT_RESOURCE;
		String tag = ProjectResource.TAG_ROLE_CODES;
		CodeList roles = new CodeList(getProject().getObjectData(type, selectedId, tag));
		roles.removeCode(ResourceRoleQuestion.TeamMemberRoleCode);
		CommandSetObjectData command = new CommandSetObjectData(type, selectedId, tag, roles.toString());
		getProject().executeCommand(command);
	}

}
