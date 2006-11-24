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
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMBaseObject;

public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public ConceptualModelNodeSet findNodesThatUseThisAnnotation(int type, BaseId id) throws Exception
	{
		if(type == ObjectType.OBJECTIVE)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_OBJECTIVE_IDS);
		if(type == ObjectType.GOAL)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_GOAL_IDS);
		if(type == ObjectType.INDICATOR)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_INDICATOR_IDS);
		
		throw new RuntimeException("Not an annotation type? " + type);
	}

	public ConceptualModelNodeSet findNodesThatUseThisObjective(BaseId objectiveId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.OBJECTIVE, objectiveId);
	}

	public ConceptualModelNodeSet findNodesThatUseThisIndicator(BaseId indicatorId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.INDICATOR, indicatorId);
	}
	
	public ConceptualModelNodeSet findNodesThatUseThisGoal(BaseId goalId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.GOAL, goalId);
	}
	
	private ConceptualModelNodeSet findNodesThatHaveThisAnnotation(BaseId objectiveId, String tag) throws ParseException
	{
		ConceptualModelNodeSet foundNodes = new ConceptualModelNodeSet();
		NodePool pool = getNodePool();
		ModelNodeId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			Factor node = pool.find(allNodeIds[i]);
			IdList annotationIds = new IdList(node.getData(tag));
			if(annotationIds.contains(objectiveId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}

	public ConceptualModelNodeSet findAllNodesRelatedToThisIndicator(BaseId indicatorId) throws Exception
	{
		Factor[] nodesThatUseThisIndicator = findNodesThatUseThisIndicator(indicatorId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisIndicator.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisIndicator[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisObjective(BaseId objectiveId) throws Exception
	{
		Factor[] nodesThatUseThisObjective = findNodesThatUseThisObjective(objectiveId).toNodeArray();
		ConceptualModelNodeSet relatedNodes = new ConceptualModelNodeSet();
		
		for(int i = 0; i < nodesThatUseThisObjective.length; ++i)
		{
			ConceptualModelNodeSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisObjective[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public ConceptualModelNodeSet findAllNodesRelatedToThisGoal(BaseId goalId) throws Exception
	{
		Factor[] nodesThatUseThisGoal = findNodesThatUseThisGoal(goalId).toNodeArray();
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
		catch(Exception e)
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
		catch(Exception e)
		{
			EAM.logException(e);
			return HTML_ERROR;
		}
	}

	static final String HTML_ERROR = "<html>(Error)</html>";
	
	Project project;
}
