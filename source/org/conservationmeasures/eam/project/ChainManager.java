/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;

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
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllNodesInChain(nodesThatUseThisIndicator[i]);
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
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllNodesInChain(nodesThatUseThisObjective[i]);
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
	
	public String getRelatedTargetsAsString(BaseId indicatorId)
	{
		ConceptualModelNodeSet modelNodes = findAllNodesRelatedToThisIndicator(indicatorId);
		TargetSet targets = new TargetSet(modelNodes);
		
		return ConceptualModelNode.getNodeLabelsAsString(targets.toNodeArray());
	}

	Project project;
}
