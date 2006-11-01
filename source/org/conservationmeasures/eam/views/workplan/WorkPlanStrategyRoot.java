/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;

public class WorkPlanStrategyRoot extends WorkPlanNode
{
	
	public WorkPlanStrategyRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuildAll();
	}
	
	public Object getChild(int index)
	{
		return strategies[index];
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public int getType()
	{
		return strategies[0].getType();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public String toString()
	{
		return STRATEGIC_LABEL;
	}
	
	private void rebuildAll()
	{
		ConceptualModelNode[] interventionObjects = project.getNodePool().getInterventions();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			ConceptualModelIntervention intervention = (ConceptualModelIntervention)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
	
			WorkPlanStrategy workPlanStrategy = new WorkPlanStrategy(project, intervention);
			strategyVector.add(workPlanStrategy);
		}
		strategies = (WorkPlanStrategy[])strategyVector.toArray(new WorkPlanStrategy[0]);
	}
	
	Project project;
	WorkPlanStrategy[] strategies;
	private static final String STRATEGIC_LABEL = "Strategic";
}
