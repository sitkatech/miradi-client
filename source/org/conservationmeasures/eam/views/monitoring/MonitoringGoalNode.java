/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ObjectiveIds;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringGoalNode extends MonitoringNode
{
	public MonitoringGoalNode(Project projectToUse, Goal goalToUse)
	{
		project = projectToUse;
		goal = goalToUse;
		rebuild();
	}

	public ObjectReference getObjectReference()
	{
		return new ObjectReference(goal.getType(), goal.getId());
	}
	
	public int getType()
	{
		return goal.getType();
	}
	
	public String toString()
	{
		return goal.toString();
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public TreeTableNode getChild(int index)
	{
		return (MonitoringNode)children.get(index);
	}

	public Object getValueAt(int column)
	{
		if(column == COLUMN_ITEM_LABEL)
			return goal.getLabel();
		return "";
	}
	
	public void rebuild()
	{
		children = new Vector();
		ConceptualModelNodeSet relatedNodes = new ChainManager(project).findAllNodesRelatedToThisGoal(goal.getId());
		children.addAll(createIndicatorNodes(relatedNodes));
		children.addAll(createObjectiveNodes(relatedNodes));
	}
	
	private Vector createObjectiveNodes(ConceptualModelNodeSet relatedNodesToUse)
	{
		Vector result = new Vector();
		Iterator iter = relatedNodesToUse.iterator();
		while(iter.hasNext())
		{
			ConceptualModelNode node = (ConceptualModelNode)iter.next();
			ObjectiveIds objectiveIds = node.getObjectives();
			for(int i = 0; i < objectiveIds.size(); ++i)
			{
				if(objectiveIds.get(i).isInvalid())
					continue;
				Objective objective = (Objective)project.findObject(ObjectType.OBJECTIVE, objectiveIds.get(i));
				result.add(new MonitoringObjectiveNode(project, objective));
			}
		}
		return result;
	}

	private Vector createIndicatorNodes(ConceptualModelNodeSet relatedNodesToUse)
	{
		Iterator iter = relatedNodesToUse.iterator();
		Vector result = new Vector();
		while (iter.hasNext())
		{
			ConceptualModelNode node = (ConceptualModelNode)iter.next();
			if(!node.isTarget())
				continue;
			
			IdList indicators = node.getIndicators();
			for(int i = 0; i < indicators.size(); ++i)
			{
				Indicator indicator = project.getIndicatorPool().find(indicators.get(i));
				result.add(new MonitoringIndicatorNode(project, indicator));
			}
		}
		return result;
	}

	Goal goal;
	Project project;
	Vector children;
}
