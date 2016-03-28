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

package org.miradi.objecthelpers;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.*;

import java.util.HashMap;

public class RelevantActivitiesCache implements CommandExecutedListener
{
	public RelevantActivitiesCache(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
	
	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (commandInvalidatesCache(event))
			clear();
	}

	private boolean commandInvalidatesCache(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(TaskSchema.getObjectType(), Factor.TAG_INDICATOR_IDS))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(TaskSchema.getObjectType(), Task.TAG_RELEVANT_INDICATOR_SET))
			return true;

		if (event.isCreateCommandForThisType(TaskSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(TaskSchema.getObjectType()))
			return true;

		if (event.isCreateCommandForThisType(IndicatorSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(IndicatorSchema.getObjectType()))
			return true;

		return false;
	}

	private void clearAllCachedData()
	{
		relevantActivitiesByBaseObject = new HashMap<ORef, ORefList>();
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	private Project getProject()
	{
		return project;
	}

	public ORefList getRelevantActivitiesForIndicator(ORef indicatorRef) throws Exception
	{
		ORefList result = relevantActivitiesByBaseObject.get(indicatorRef);
		if(result == null)
		{
			result = getRelevantActivityRefListForIndicator(indicatorRef);
			relevantActivitiesByBaseObject.put(indicatorRef, result);
		}

		return result;
	}

	private ORefList getRelevantActivityRefListForIndicator(ORef indicatorRef) throws Exception
	{
		ORefSet taskRefs = getProject().getPool(ObjectType.TASK).getRefSet();
		ORefList result = new ORefList();
		for(ORef taskRef: taskRefs)
		{
			Task task = Task.find(getProject(), taskRef);
			if (task.getRelevantIndicatorRefList().contains(indicatorRef))
				result.add(task.getRef());
		}

		return result;
	}

	private Project project;
	private HashMap<ORef, ORefList> relevantActivitiesByBaseObject;
}
