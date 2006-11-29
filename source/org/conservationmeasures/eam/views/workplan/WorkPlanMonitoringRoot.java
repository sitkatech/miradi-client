/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
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
		FactorSet factorsWithIndicators = new FactorSet();
		
		Factor[] targets = project.getFactorPool().getTargets();
		Factor[] directThreats = project.getFactorPool().getDirectThreats();
		Factor[] interventions = project.getFactorPool().getInterventions();
		
		factorsWithIndicators.attemptToAddAll(getPossibleIndicators(targets));
		factorsWithIndicators.attemptToAddAll(getPossibleIndicators(directThreats));
		factorsWithIndicators.attemptToAddAll(getPossibleIndicators(interventions));
		
		allFactorsWithIndicators = new WorkPlanMonitoringFactor[factorsWithIndicators.size()];
		Object[] nodeArray = factorsWithIndicators.toArray();
		
		for(int i = 0; i < allFactorsWithIndicators.length; i++)
			allFactorsWithIndicators[i] = new WorkPlanMonitoringFactor(project, (Factor)nodeArray[i]);
		
		Arrays.sort(allFactorsWithIndicators, new IgnoreCaseStringComparator());
	}

	private FactorSet getPossibleIndicators(Factor[] factors)
	{
		FactorSet possibleIndicators = new FactorSet(); 
		for (int i = 0; i < factors.length; i++)
			if (factors[i].getIndicators().size() > 0)
				possibleIndicators.attemptToAdd(factors[i]);
		
		return possibleIndicators;
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
