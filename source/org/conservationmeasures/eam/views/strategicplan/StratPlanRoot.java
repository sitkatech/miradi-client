/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanRoot extends TreeTableNode
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
		return goalNodes.length;
	}

	public TreeTableNode getChild(int index)
	{
		return goalNodes[index];
	}
	
	public String toString()
	{
		return project.getFilename();
	}
	
	public ObjectReference getObjectReference()
	{
		return null;
	}

	public int getType()
	{
		return -1;
	}
	
	public void rebuild()
	{
		Vector goalVector = new Vector();
		goalVector.addAll(getAllGoals(project.getGoalPool()));
		goalNodes = (StratPlanGoal[])goalVector.toArray(new StratPlanGoal[0]);
	}

	private Vector getAllGoals(GoalPool pool)
	{
		BaseId[] goalIds = pool.getIds();
		Vector goals = new Vector();
		for(int i = 0; i < goalIds.length; ++i)
		{
			Goal goal = (Goal)pool.findDesire(goalIds[i]);
			goals.add(new StratPlanGoal(project, goal));
		}
		return goals;
	}
	
	Project project;
	StratPlanGoal[] goalNodes;
}

