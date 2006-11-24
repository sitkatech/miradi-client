/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StratPlanGoal extends TreeTableNode
{
	public StratPlanGoal(Project projectToUse, Goal goalToUse) throws Exception
	{
		project = projectToUse;
		if(goalToUse == null)
			EAM.logError("Attempted to create tree node for null goal");
		goal = goalToUse;
		rebuild();
		
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}

	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)objectiveVector.get(index);
	}

	public int getChildCount()
	{
		return objectiveVector.size();
	}
	
	public ORef getObjectReference()
	{
		return goal.getObjectReference();
	}

	public BaseId getId()
	{
		return goal.getId();
	}

	public int getType()
	{
		return goal.getType();
	}

	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return toString();
		return "";
	}

	public String toString()
	{
		return goal.toString();
	}

	public void rebuild() throws Exception
	{
		objectiveVector = new Vector();
		ConceptualModelNodeSet relatedNodes = new ChainManager(project).findAllNodesRelatedToThisGoal(goal.getId());
		Iterator iter = relatedNodes.iterator();
		while(iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			IdList objectiveIds = node.getObjectives();
			for(int i = 0; i < objectiveIds.size(); ++i)
			{
				if(objectiveIds.get(i).isInvalid())
					continue;
				Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, objectiveIds.get(i));
				objectiveVector.add(new StratPlanObjective(project, objective));
			}
		}
		Collections.sort(objectiveVector, new IgnoreCaseStringComparator());
	}

	Project project;
	Goal goal;
	Vector objectiveVector;
}
