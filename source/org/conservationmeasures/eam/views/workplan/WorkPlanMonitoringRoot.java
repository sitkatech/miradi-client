/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
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
	
	public BaseObject getObject()
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
		indicatorsNodes = getWorkPlanIndicators(project);
	}

	static public WorkPlanMonitoringIndicator[] getWorkPlanIndicators(Project project)
	{
		Indicator[] indicators = project.getIndicatorPool().getAllIndicators();
		WorkPlanMonitoringIndicator[] indicatorsNodes = new WorkPlanMonitoringIndicator[indicators.length];
		for(int i = 0; i < indicators.length; i++)
			indicatorsNodes[i] = new WorkPlanMonitoringIndicator(project, indicators[i]);
		
		Arrays.sort(indicatorsNodes, new IgnoreCaseStringComparator());
		return indicatorsNodes;
	}
	
	public BaseId getId()
	{
		return null;
	}

	WorkPlanMonitoringIndicator indicatorsNodes[];
	Project project;
	private static final String MONITORING_LABEL = "Monitoring";
}
