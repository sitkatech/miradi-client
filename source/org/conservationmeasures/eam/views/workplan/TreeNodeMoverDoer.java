/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;

public abstract class TreeNodeMoverDoer extends ObjectsDoer
{
	WorkPlanPanel getPanel()
	{
		WorkPlanPanel panel = ((WorkPlanView)getView()).getWorkPlanPanel();
		return panel;
	}

	protected IdList getTaskIds(ORef parentRef)
	{
		try
		{
			return new IdList(getProject().getObjectData(parentRef, getTaskIdsTag(parentRef)));
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return new IdList();
		}
	}

	public String getTaskIdsTag(ORef container)
	{
		int type = container.getObjectType();
		switch(type)
		{
			case ObjectType.TASK:
				return Task.TAG_SUBTASK_IDS;
			case ObjectType.FACTOR:
				return Strategy.TAG_ACTIVITY_IDS;
			case ObjectType.INDICATOR:
				return Indicator.TAG_TASK_IDS;
		}
		
		throw new RuntimeException("getTaskIdsTag called for non-task container type " + container.getObjectType());
	}

}
