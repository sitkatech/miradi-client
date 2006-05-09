/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

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
		strategies = new StratPlanStrategy[interventionObjects.length];
		for(int i = 0; i < strategies.length; ++i)
			strategies[i] = new StratPlanStrategy(project, (ConceptualModelIntervention)interventionObjects[i]);
	}
	
	Project project;
	StratPlanStrategy[] strategies;
}

