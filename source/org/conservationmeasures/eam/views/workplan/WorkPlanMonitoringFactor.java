/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringFactor extends WorkPlanTreeTableNode
{
	public WorkPlanMonitoringFactor(Project projectToUse, Factor factorToUse)
	{
		project = projectToUse;
		factor = factorToUse;
		rebuild();
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public BaseId getId()
	{
		return getObjectReference().getObjectId();
	}

	public void rebuild()
	{
		IdList indicators = factor.getIndicators();
		allIndicators = new WorkPlanMonitoringIndicator[indicators.size()];
		for(int i = 0; i < indicators.size(); i++)
		{
			Indicator indicator = project.getIndicatorPool().find(indicators.get(i));
			allIndicators[i] = new WorkPlanMonitoringIndicator(project, indicator);
		}
		Arrays.sort(allIndicators, new IgnoreCaseStringComparator());
	}

	public TreeTableNode getChild(int index)
	{
		return allIndicators[index];
	}

	public int getChildCount()
	{
		return allIndicators.length;
	}

	public ORef getObjectReference()
	{
		return factor.getRef();
	}

	public int getType()
	{
		return getObjectReference().getObjectType();
	}

	public Object getValueAt(int column)
	{
		if (column == 0)
			return toString();
		return "";
	}

	public String toString()
	{
		return factor.getLabel();
	}
	
	WorkPlanMonitoringIndicator allIndicators[];
	Project project;
	Factor factor;
}
