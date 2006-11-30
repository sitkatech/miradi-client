/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
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
		return allFactorsWithIndicators[index];
	}

	public int getChildCount()
	{
		return allFactorsWithIndicators.length;
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
		FactorId[] factorNodeIds = project.getFactorPool().getModelNodeIds();
		Vector workPlanMonFactors = new Vector();
		
		for (int i  =0; i < factorNodeIds.length; i++)
		{
			Factor factor = project.getFactorPool().find(factorNodeIds[i]);
			if (factor.getIndicators().size() > 0)
				workPlanMonFactors.add(new WorkPlanMonitoringFactor(project, factor));
		}
		
		allFactorsWithIndicators = (WorkPlanMonitoringFactor[])workPlanMonFactors.toArray(new WorkPlanMonitoringFactor[0]);
		Arrays.sort(allFactorsWithIndicators, new IgnoreCaseStringComparator());
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
	WorkPlanMonitoringFactor[] allFactorsWithIndicators;
	private static final String MONITORING_LABEL = "Monitoring";

}
