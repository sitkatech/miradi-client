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
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringObjectiveNode extends MonitoringNode
{
	public MonitoringObjectiveNode(Project projectToUse, Objective objectiveToUse)
	{
		project = projectToUse;
		objective = objectiveToUse;
		children = new Vector();
		
		HashSet indicatorIds = new HashSet();
		indicatorIds.addAll(getAlUpstreamIndicators(getNodesWithThisObjective(objective.getId())));
		
		Iterator iter = indicatorIds.iterator();
		while(iter.hasNext())
		{
			BaseId id = (BaseId)iter.next();
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, id);
			if(indicator == null)
				throw new RuntimeException("Missing Indicator " + id);
			children.add(new MonitoringIndicatorNode(project, indicator));
		}
	}

	private HashSet getAlUpstreamIndicators(ConceptualModelNodeSet nodes)
	{
		HashSet indicatorIds = new HashSet(); 
		Iterator objectiveNodesIterator = nodes.iterator();
		while(objectiveNodesIterator.hasNext())
		{
			ConceptualModelNode nodeWithObjective = (ConceptualModelNode)objectiveNodesIterator.next();
			for(int i = 0; i < nodeWithObjective.getIndicators().size(); ++i)
				indicatorIds.add(nodeWithObjective.getIndicators().get(i));
			ConceptualModelNodeSet nodesInChain = project.getDiagramModel().getAllUpstreamNodes(nodeWithObjective);
			Iterator chainNodesIterator = nodesInChain.iterator();
			while(chainNodesIterator.hasNext())
			{
				ConceptualModelNode nodeInChain = (ConceptualModelNode)chainNodesIterator.next();
				for(int i = 0; i < nodeInChain.getIndicators().size(); ++i)
					indicatorIds.add(nodeInChain.getIndicators().get(i));
			}
		}
		indicatorIds.remove(new IndicatorId(BaseId.INVALID.asInt()));
		return indicatorIds;
	}

	public ObjectReference getObjectReference()
	{
		return new ObjectReference(objective.getType(), objective.getId());
	}
	
	public int getType()
	{
		return objective.getType();
	}
	
	public String toString()
	{
		return objective.toString();
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
			return objective.getLabel();
		return "";
	}
	
	private ConceptualModelNodeSet getNodesWithThisObjective(BaseId objectiveId)
	{
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
