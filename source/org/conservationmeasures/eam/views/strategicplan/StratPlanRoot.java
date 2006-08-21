/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
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
		return objectives.length;
	}

	public Object getChild(int index)
	{
		return objectives[index];
	}
	
	public String toString()
	{
		return project.getName();
	}

	public int getType()
	{
		return -1;
	}
	
	public BaseId getId()
	{
		return BaseId.INVALID;
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public void rebuild()
	{
		BaseId[] objectiveIds = project.getObjectivePool().getIds();
		Vector objectiveVector = new Vector();
		for(int i = 0; i < objectiveIds.length; ++i)
		{
			Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, objectiveIds[i]);
			objectiveVector.add(new StratPlanObjective(project, objective));
		}
		objectives = (StratPlanObjective[])objectiveVector.toArray(new StratPlanObjective[0]);
	}
	
	Project project;
	StratPlanObjective[] objectives;
}

