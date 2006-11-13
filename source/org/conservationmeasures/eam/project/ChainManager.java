/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
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

	public ConceptualModelNodeSet findNodesThatUseThisObjective(BaseId objectiveId) throws ParseException
	{
		return findNodesThatHaveThisAnnotation(objectiveId, ConceptualModelNode.TAG_OBJECTIVE_IDS);
	}

	public ConceptualModelNodeSet findNodesThatUseThisIndicator(BaseId indicatorId) throws ParseException
	{
		return findNodesThatHaveThisAnnotation(indicatorId, ConceptualModelNode.TAG_INDICATOR_IDS);
	}
	
	public ConceptualModelNodeSet findNodesThatUseThisGoal(BaseId goalId) throws ParseException
	{
		return findNodesThatHaveThisAnnotation(goalId, ConceptualModelNode.TAG_GOAL_IDS);
	}
	
	private ConceptualModelNodeSet findNodesThatHaveThisAnnotation(BaseId objectiveId, String tag) throws ParseException
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		ModelNodeId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			ConceptualModelNode node = pool.find(allNodeIds[i]);
			IdList annotationIds = new IdList(node.getData(tag));
			if(annotationIds.contains(objectiveId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}

	public ConceptualModelNodeSet findAllNodesRelatedToThisIndicator(BaseId indicatorId) throws ParseException
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
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisObjective(BaseId objectiveId) throws ParseException
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
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisGoal(BaseId goalId) throws ParseException
	{
		ConceptualModelNode[] nodesThatUseThisGoal = findNodesThatUseThisGoal(goalId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisGoal.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisGoal[i]);
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
		try
		{
			ConceptualModelNodeSet modelNodes = findAllNodesRelatedToThisIndicator(indicatorId);
			TargetSet targets = new TargetSet(modelNodes);
			
			return EAMBaseObject.toHtml(targets.toNodeArray());
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return HTML_ERROR;
		}
	}

	public String getRelatedDirectThreatsAsHtml(BaseId indicatorId)
	{
		try
		{
			ConceptualModelNodeSet modelNodes =  findAllNodesRelatedToThisIndicator(indicatorId);
			DirectThreatSet directThreats = new DirectThreatSet(modelNodes);
			
			return EAMBaseObject.toHtml(directThreats.toNodeArray());
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return HTML_ERROR;
		}
	}

	static final String HTML_ERROR = "<html>(Error)</html>";
	
	Project project;
}
