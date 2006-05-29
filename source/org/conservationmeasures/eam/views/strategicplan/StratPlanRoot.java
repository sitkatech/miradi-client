/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class StratPlanRoot extends StratPlanObject
{
	public StratPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	public Object getValueAt(int column)
	{
		return "";
	}

	public int getChildCount()
	{
		return strategies.length;
	}

	public Object getChild(int index)
	{
		return strategies[index];
	}
	
	public String toString()
	{
		return project.getName();
	}

	public int getType()
	{
		return -1;
	}
	
	public int getId()
	{
		return IdAssigner.INVALID_ID;
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public void rebuild()
	{
		ConceptualModelNode[] interventionObjects = project.getNodePool().getInterventions();
		Vector strategyVector = new Vector();
		for(int i = 0; i < interventionObjects.length; ++i)
		{
			ConceptualModelIntervention intervention = (ConceptualModelIntervention)interventionObjects[i];
			if(intervention.isStatusDraft())
				continue;
			
			strategyVector.add(new StratPlanStrategy(project, intervention));
		}
		strategies = (StratPlanStrategy[])strategyVector.toArray(new StratPlanStrategy[0]);
	}
	
	Project project;
	StratPlanStrategy[] strategies;
}

