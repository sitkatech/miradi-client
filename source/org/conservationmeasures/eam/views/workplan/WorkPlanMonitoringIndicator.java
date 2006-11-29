/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringIndicator extends WorkPlanTreeTableNode
{
	public WorkPlanMonitoringIndicator(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
	}

	public ORef getObjectReference()
	{
		return indicator.getObjectReference();
	}
	
	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.getLabel();
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		if (column == 0)
			return toString();
		
		//TODO should this be HTML?
		if (column == 1)
			return toString();
		return "";
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public boolean canInsertTaskHere()
	{
		return false;
	}

	public BaseId getId()
	{
		return indicator.getId();
	}

	public void rebuild()
	{
	}
	
	Project project;
	Indicator indicator;
}
