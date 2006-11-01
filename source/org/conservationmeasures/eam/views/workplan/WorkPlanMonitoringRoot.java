/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringRoot extends TreeTableNode
{
	public WorkPlanMonitoringRoot(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuildAll();
	}

	public TreeTableNode getChild(int index)
	{
		return allIndicators[index];
	}

	public int getChildCount()
	{
		return allIndicators.length;
	}

	public int getType()
	{
		return allIndicators[0].getType();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public String toString()
	{
		return MONITORING_LABEL;
	}

	private void rebuildAll() throws Exception
	{
		Indicator[] indicators = project.getIndicatorPool().getAllIndicators();
		if (indicators.length <= 0)
			throw new Exception();
			
		allIndicators = new WorkPlanMonitoringIndicatorNode[indicators.length];
		for(int i = 0; i < indicators.length; i++)
			allIndicators[i] = new WorkPlanMonitoringIndicatorNode(project, indicators[i]);
	}

	Project project;
	WorkPlanMonitoringIndicatorNode[] allIndicators;
	private static final String MONITORING_LABEL = "Monitoring";
}
