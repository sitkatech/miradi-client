/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objects.Objective;

public class MonitoringObjective extends MonitoringNode
{
	public MonitoringObjective(Objective objectiveToUse)
	{
		objective = objectiveToUse;
	}

	public int getType()
	{
		return objective.getType();
	}
	
	public String toString()
	{
		return objective.getLabel();
	}
	
	public int getChildCount()
	{
		return 0;
	}

	public MonitoringNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		if(column == COLUMN_ITEM_LABEL)
			return objective.getLabel();
		return "";
	}

	Objective objective;
}
