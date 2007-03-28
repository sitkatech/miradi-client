/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanRoot extends TreeTableNode
{
	public StratPlanRoot(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return null;
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
	
	public ORef getObjectReference()
	{
		return null;
	}

	public int getType()
	{
		return -1;
	}
	
	public void rebuild() throws Exception
	{
		Vector goalVector = new Vector();
		goalVector.addAll(getAllGoals(project.getGoalPool()));
		goalNodes = (StratPlanGoal[])goalVector.toArray(new StratPlanGoal[0]);
		Arrays.sort(goalNodes, new IgnoreCaseStringComparator());
	}

	private Vector getAllGoals(GoalPool pool) throws Exception
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

