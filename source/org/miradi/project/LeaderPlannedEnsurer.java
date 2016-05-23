/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.project;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.IdList;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Timeframe;
import org.miradi.schemas.TimeframeSchema;

public class LeaderPlannedEnsurer implements CommandExecutedListener
{
	public LeaderPlannedEnsurer(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(TimeframeSchema.getObjectType(), Timeframe.TAG_RESOURCE_ID))
				possiblyClearResourceLeaderDueToResourceTimeframeUpdate(event);
			
			if (event.isSetDataCommandWithThisTag(BaseObject.TAG_TIMEFRAME_IDS))
				possiblyClearResourceLeaderDueToTimeframeDeletion(event);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException(e);
		}
	}

	private void possiblyClearResourceLeaderDueToTimeframeDeletion(CommandExecutedEvent event) throws Exception
	{
		CommandSetObjectData setCommand = event.getSetCommand();
		ORefList currentList = new ORefList(new IdList(TimeframeSchema.getObjectType(), setCommand.getDataValue()));
		ORefList previousList = new ORefList(new IdList(TimeframeSchema.getObjectType(), setCommand.getPreviousDataValue()));
		if (previousList.size() <= currentList.size())
			return;
		
		ORefList changedRefList = ORefList.subtract(previousList, currentList);
		if (changedRefList.size() != 1)
			return;
		
		ensureLeaderIsLegal(setCommand.getObjectORef());
	}

	private void possiblyClearResourceLeaderDueToResourceTimeframeUpdate(CommandExecutedEvent event) throws Exception
	{
		CommandSetObjectData setCommand = event.getSetCommand();
		final String previousDataValue = setCommand.getPreviousDataValue();
		if (previousDataValue.length() == 0)
			return;

		Timeframe timeframe = Timeframe.find(getProject(), setCommand.getObjectORef());
		ORefList referrers = timeframe.findAllObjectsThatReferToUs();
		if (referrers.isEmpty())
			return;
		
		if (referrers.size() > 1)
			throw new Exception("Timeframes cannot be shared");
		
		ensureLeaderIsLegal(referrers.getFirstElement());
	}

	private void ensureLeaderIsLegal(ORef objectContainingLeaderRef) throws Exception
	{
		BaseObject objectContainingLeader = BaseObject.find(getProject(), objectContainingLeaderRef);
		ORef currentLeaderRef = objectContainingLeader.getPlannedLeaderResourceRef();
		ORefSet resourceRefs = objectContainingLeader.getTotalTimePeriodCostsMapForPlans().getAllProjectResourceRefs();
		if (!resourceRefs.contains(currentLeaderRef))
			clearLeaderResourceRef(objectContainingLeader);
	}

	private void clearLeaderResourceRef(BaseObject objectToClearLeaderFrom) throws Exception
	{
		CommandSetObjectData clearCommand = new CommandSetObjectData(objectToClearLeaderFrom, BaseObject.TAG_PLANNED_LEADER_RESOURCE, "");
		getProject().executeAsSideEffect(clearCommand);
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
