/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.Task;

public class StratPlanActivity extends StratPlanObject
{
	public StratPlanActivity(Task activityToUse)
	{
		activity = activityToUse;
	}
	
	public Object getValueAt(int column)
	{
		return toString();
	}

	public int getChildCount()
	{
		return 0;
	}

	public Object getChild(int index)
	{
		return null;
	}
	
	public String toString()
	{
		return activity.getLabel();
	}

	public int getType()
	{
		return activity.getType();
	}
	
	public int getId()
	{
		return activity.getId();
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public void rebuild()
	{
		
	}
	
	Task activity;
}
