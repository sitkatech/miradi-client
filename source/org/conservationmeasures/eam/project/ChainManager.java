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
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMBaseObject;

public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public FactorSet findNodesThatUseThisAnnotation(int type, BaseId id) throws Exception
	{
		if(type == ObjectType.OBJECTIVE)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_OBJECTIVE_IDS);
		if(type == ObjectType.GOAL)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_GOAL_IDS);
		if(type == ObjectType.INDICATOR)
			return findNodesThatHaveThisAnnotation(id, Factor.TAG_INDICATOR_IDS);
		
		throw new RuntimeException("Not an annotation type? " + type);
	}

	public FactorSet findNodesThatUseThisObjective(BaseId objectiveId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.OBJECTIVE, objectiveId);
	}

	public FactorSet findNodesThatUseThisIndicator(BaseId indicatorId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.INDICATOR, indicatorId);
	}
	
	public FactorSet findNodesThatUseThisGoal(BaseId goalId) throws Exception
	{
		return findNodesThatUseThisAnnotation(ObjectType.GOAL, goalId);
	}
	
	private FactorSet findNodesThatHaveThisAnnotation(BaseId objectiveId, String tag) throws ParseException
	{
		FactorSet foundNodes = new FactorSet();
		FactorPool pool = getNodePool();
		FactorId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			Factor node = pool.find(allNodeIds[i]);
			IdList annotationIds = new IdList(node.getData(tag));
			if(annotationIds.contains(objectiveId))
				foundNodes.attemptToAdd(node);
		}
		
		return foundNodes;
	}

	public FactorSet findAllNodesRelatedToThisIndicator(BaseId indicatorId) throws Exception
	{
		Factor[] nodesThatUseThisIndicator = findNodesThatUseThisIndicator(indicatorId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisIndicator.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisIndicator[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public FactorSet findAllNodesRelatedToThisObjective(BaseId objectiveId) throws Exception
	{
		Factor[] nodesThatUseThisObjective = findNodesThatUseThisObjective(objectiveId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisObjective.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisObjective[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public FactorSet findAllNodesRelatedToThisGoal(BaseId goalId) throws Exception
	{
		Factor[] nodesThatUseThisGoal = findNodesThatUseThisGoal(goalId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisGoal.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisGoal[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	FactorPool getNodePool()
	{
		return getProject().getFactorPool();
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
			FactorSet modelNodes = findAllNodesRelatedToThisIndicator(indicatorId);
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
			FactorSet modelNodes =  findAllNodesRelatedToThisIndicator(indicatorId);
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
