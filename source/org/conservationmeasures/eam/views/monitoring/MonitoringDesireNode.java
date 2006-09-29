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
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class MonitoringDesireNode extends MonitoringNode
{
	public MonitoringDesireNode(Project projectToUse, Desire desireToUse)
	{
		project = projectToUse;
		desire = desireToUse;
		children = new Vector();
		
		HashSet indicatorIds = new HashSet();
		indicatorIds.addAll(getAlUpstreamIndicators(getNodesWithThisDesire(desire.getId())));
		
		Iterator iter = indicatorIds.iterator();
		while(iter.hasNext())
		{
			IndicatorId id = (IndicatorId)iter.next();
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, id);
			if(indicator == null)
				throw new RuntimeException("Missing Indicator " + id);
			children.add(new MonitoringIndicatorNode(project, indicator));
		}
	}

	private HashSet getAlUpstreamIndicators(ConceptualModelNodeSet nodes)
	{
		HashSet indicatorIds = new HashSet(); 
		Iterator desireNodesIterator = nodes.iterator();
		while(desireNodesIterator.hasNext())
		{
			ConceptualModelNode nodeWithDesire = (ConceptualModelNode)desireNodesIterator.next();
			indicatorIds.add(nodeWithDesire.getIndicatorId());
			ConceptualModelNodeSet nodesInChain = project.getDiagramModel().getAllUpstreamNodes(nodeWithDesire);
			Iterator chainNodesIterator = nodesInChain.iterator();
			while(chainNodesIterator.hasNext())
			{
				ConceptualModelNode nodeInChain = (ConceptualModelNode)chainNodesIterator.next();
				indicatorIds.add(nodeInChain.getIndicatorId());
			}
		}
		indicatorIds.remove(new IndicatorId(BaseId.INVALID.asInt()));
		return indicatorIds;
	}

	public int getType()
	{
		return desire.getType();
	}
	
	public String toString()
	{
		return desire.getLabel() + " (" + desire.getShortLabel() + ")";
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
			return desire.getLabel();
		return "";
	}
	
	private ConceptualModelNodeSet getNodesWithThisDesire(BaseId desireId)
	{
		ConceptualModelNodeSet result = new ConceptualModelNodeSet();
		ModelNodeId[] allNodeIds = project.getNodePool().getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = project.findNode(allNodeIds[i]);
			if(node.getObjectives().contains(desireId))
				result.attemptToAdd(node);
			if(node.getGoals().contains(desireId))
				result.attemptToAdd(node);
		}
		return result;
	}

	Project project;
	Desire desire;
	Vector children;
}
