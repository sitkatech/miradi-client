/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMBaseObject;

public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}

	public ConceptualModelNodeSet findNodesThatUseThisObjective(BaseId objectiveId)
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		ModelNodeId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = pool.find(allNodeIds[i]);
			if(node.getObjectives().contains(objectiveId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}

	public ConceptualModelNodeSet findNodesThatUseThisIndicator(BaseId indicatorId)
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		ModelNodeId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = pool.find(allNodeIds[i]);
			if(node.getIndicatorId().equals(indicatorId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisIndicator(BaseId indicatorId)
	{
		ConceptualModelNode[] nodesThatUseThisIndicator = findNodesThatUseThisIndicator(indicatorId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisIndicator.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisIndicator[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisObjective(BaseId objectiveId)
	{
		ConceptualModelNode[] nodesThatUseThisObjective = findNodesThatUseThisObjective(objectiveId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisObjective.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisObjective[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	NodePool getNodePool()
	{
		return getProject().getNodePool();
	}
	
	DiagramModel getDiagramModel()
	{
		return getProject().getDiagramModel();
	}

	Project getProject()
	{
		return project;
	}
	
	public String getRelatedTargetsAsHtml(BaseId indicatorId)
	{
		ConceptualModelNodeSet modelNodes = findAllNodesRelatedToThisIndicator(indicatorId);
		TargetSet targets = new TargetSet(modelNodes);
		
		return EAMBaseObject.toHtml(targets.toNodeArray());
	}

	public String getRelatedDirectThreatsAsHtml(BaseId indicatorId)
	{
		ConceptualModelNodeSet modelNodes =  findAllNodesRelatedToThisIndicator(indicatorId);
		DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
		
		return EAMBaseObject.toHtml(directThreats.toNodeArray());
	}

	Project project;
}
