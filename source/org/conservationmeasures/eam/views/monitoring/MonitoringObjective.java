/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class MonitoringObjective extends MonitoringNode
{
	public MonitoringObjective(Project projectToUse, Objective objectiveToUse)
	{
		project = projectToUse;
		objective = objectiveToUse;
		children = new Vector();
		
		HashSet indicatorIds = new HashSet(); 
		ConceptualModelNodeSet nodes = getNodesWithThisObjective();
		Iterator objectiveNodesIterator = nodes.iterator();
		while(objectiveNodesIterator.hasNext())
		{
			ConceptualModelNode nodeWithObjective = (ConceptualModelNode)objectiveNodesIterator.next();
			ConceptualModelNodeSet nodesInChain = project.getDiagramModel().getAllNodesInChain(nodeWithObjective);
			Iterator chainNodesIterator = nodesInChain.iterator();
			while(chainNodesIterator.hasNext())
			{
				ConceptualModelNode nodeInChain = (ConceptualModelNode)chainNodesIterator.next();
				IndicatorId indicatorId = nodeInChain.getIndicatorId();
				if(!indicatorId.isInvalid())
					indicatorIds.add(indicatorId);
			}
		}
		
		Iterator iter = indicatorIds.iterator();
		while(iter.hasNext())
		{
			IndicatorId id = (IndicatorId)iter.next();
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, id);
			children.add(new MonitoringIndicator(indicator));
		}
	}

	public int getType()
	{
		return objective.getType();
	}
	
	public String toString()
	{
		return objective.getLabel();
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public MonitoringNode getChild(int index)
	{
		return (MonitoringNode)children.get(index);
	}

	public Object getValueAt(int column)
	{
		if(column == COLUMN_ITEM_LABEL)
			return objective.getLabel();
		return "";
	}
	
	private ConceptualModelNodeSet getNodesWithThisObjective()
	{
		BaseId objectiveId = objective.getId();
		ConceptualModelNodeSet result = new ConceptualModelNodeSet();
		ModelNodeId[] allNodeIds = project.getNodePool().getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = project.findNode(allNodeIds[i]);
			if(node.getObjectives().contains(objectiveId))
				result.attemptToAdd(node);
		}
		return result;
	}

	Project project;
	Objective objective;
	Vector children;
}
