/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class TeamAddMember extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getObjects().length == 0)
			return false;
		
		if (getSelectedIds().length > 1)
			return true;
		
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata.getTeamResourceIdList().contains(getSelectedId()))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			BaseId[] selectedIds = getSelectedIds();
			tryToAddSelectedIds(selectedIds);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Text|Unknown error prevented adding this person to the team"));
		}
	}

	private void tryToAddSelectedIds(BaseId[] selectedIds) throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		for (int i = 0; i < selectedIds.length; i++)
		{
			Command cmd = CommandSetObjectData.createAppendIdCommand(metadata, ProjectMetadata.TAG_TEAM_RESOURCE_IDS, selectedIds[i]);
			getProject().executeCommand(cmd);
		}
	}
}
