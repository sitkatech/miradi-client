/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringRoot extends TreeTableNode
{
	public WorkPlanMonitoringRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public TreeTableNode getChild(int index)
	{
		return indicatorsNodes[index];
	}

	public int getChildCount()
	{
		return indicatorsNodes.length;
	}
	
	public ORef getObjectReference()
	{
		return EAM.WORKPLAN_MONITORING_ROOT;
	}

	public int getType()
	{
		return getObjectReference().getObjectType();
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
		indicatorsNodes = new WorkPlanMonitoringIndicator[indicators.length];
		for(int i = 0; i < indicators.length; i++)
			indicatorsNodes[i] = new WorkPlanMonitoringIndicator(project, indicators[i]);
		
		Arrays.sort(indicatorsNodes, new IgnoreCaseStringComparator());
	}
	
	public BaseId getId()
	{
		return null;
	}

	WorkPlanMonitoringIndicator indicatorsNodes[];
	Project project;
	private static final String MONITORING_LABEL = "Monitoring";
}
