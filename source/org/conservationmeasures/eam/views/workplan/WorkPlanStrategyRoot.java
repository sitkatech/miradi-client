/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategyRoot extends WorkPlanTreeTableNode
{
	public WorkPlanStrategyRoot(Project projectToUse)
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
		return strategies[index];
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public ORef getObjectReference()
	{
		return EAM.WORKPLAN_STRATEGY_ROOT;
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
		return STRATEGIC_LABEL;
	}
	
	public void rebuild()
	{
		Factor[] interventionObjects = project.getFactorPool().getInterventions();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			Strategy intervention = (Strategy)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
	
			WorkPlanStrategy workPlanStrategy = new WorkPlanStrategy(project, intervention);
			strategyVector.add(workPlanStrategy);
		}
		strategies = (WorkPlanStrategy[])strategyVector.toArray(new WorkPlanStrategy[0]);
		Arrays.sort(strategies, new IgnoreCaseStringComparator());
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
	WorkPlanStrategy[] strategies;
	private static final String STRATEGIC_LABEL = "Strategies";
}
