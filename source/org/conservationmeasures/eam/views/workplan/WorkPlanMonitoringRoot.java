/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringRoot extends WorkPlanTreeTableNode
{
	public WorkPlanMonitoringRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}

	public TreeTableNode getChild(int index)
	{
		return allIndicators[index];
	}

	public int getChildCount()
	{
		return allIndicators.length;
	}
	
	public ObjectReference getObjectReference()
	{
		return null;
	}

	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public String toString()
	{
		return MONITORING_LABEL;
	}

	public void rebuild()
	{
		Indicator[] indicators = project.getIndicatorPool().getAllIndicators();
		allIndicators = new WorkPlanMonitoringIndicatorNode[indicators.length];
		for(int i = 0; i < indicators.length; i++)
			allIndicators[i] = new WorkPlanMonitoringIndicatorNode(project, indicators[i]);
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}

	public BaseId getId()
	{
		return null;
	}

	Project project;
	WorkPlanMonitoringIndicatorNode[] allIndicators;
	private static final String MONITORING_LABEL = "Monitoring";

}
