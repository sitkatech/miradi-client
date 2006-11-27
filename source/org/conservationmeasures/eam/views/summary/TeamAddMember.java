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
import org.conservationmeasures.eam.ids.IdList;
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
		
		ProjectMetadata metadata = getMetaData();
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
			ProjectMetadata metadata = getMetaData();
			IdList selectedIds = getSelectedIdsNotInTeam();
			Command cmd = CommandSetObjectData.createAppendIdsCommand(metadata, ProjectMetadata.TAG_TEAM_RESOURCE_IDS, selectedIds);
			getProject().executeCommand(cmd);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Text|Unknown error prevented adding this person to the team"));
		}
	}
	
	private IdList getSelectedIdsNotInTeam()
	{
		BaseId[] selectedIds = getSelectedIds();
		IdList teamResourceIdList = getMetaData().getTeamResourceIdList();
		IdList newList = new IdList();
		for (int i = 0; i < selectedIds.length; i++)
			if (!teamResourceIdList.contains(selectedIds[i]))
				newList.add(selectedIds[i]);

		return newList;		
	}

	private ProjectMetadata getMetaData()
	{
		return getProject().getMetadata();
	}
}
