/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Factor;

public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public FactorSet findFactorsThatUseThisAnnotation(int type, BaseId id) throws Exception
	{
		if(type == ObjectType.OBJECTIVE)
			return findFactorsThatHaveThisObject(id, Factor.TAG_OBJECTIVE_IDS);
		if(type == ObjectType.GOAL)
			return findFactorsThatHaveThisObject(id, Factor.TAG_GOAL_IDS);
		if(type == ObjectType.INDICATOR)
			return findFactorsThatHaveThisObject(id, Factor.TAG_INDICATOR_IDS);
		
		throw new RuntimeException("Not an annotation type? " + type);
	}

	public FactorSet findFactorsThatUseThisObjective(BaseId objectiveId) throws Exception
	{
		return findFactorsThatUseThisAnnotation(ObjectType.OBJECTIVE, objectiveId);
	}

	public FactorSet findFactorsThatUseThisIndicator(BaseId indicatorId) throws Exception
	{
		return findFactorsThatUseThisAnnotation(ObjectType.INDICATOR, indicatorId);
	}
	
	public FactorSet findFactorsThatUseThisGoal(BaseId goalId) throws Exception
	{
		return findFactorsThatUseThisAnnotation(ObjectType.GOAL, goalId);
	}
	
	public FactorSet findFactorsThatHaveThisObject(BaseId objectId, String tag) throws ParseException
	{
		return findFactorsThatHaveThisObject(null, objectId, tag);
	}

	public FactorSet findFactorsThatHaveThisObject(FactorType type, BaseId objectId, String tag) throws ParseException
	{
		FactorSet foundFactors = new FactorSet();
		FactorPool pool = getFactorPool();
		FactorId[] allNodeIds = pool.getModelNodeIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			Factor node = pool.find(allNodeIds[i]);
			if(type == null || type.equals(node.getNodeType()))
			{
				IdList candidateIds = new IdList(node.getData(tag));
				if(candidateIds.contains(objectId))
					foundFactors.attemptToAdd(node);
			}
		}
		
		return foundFactors;
	}

	public FactorSet findAllFactorsRelatedToThisIndicator(BaseId indicatorId) throws Exception
	{
		Factor[] nodesThatUseThisIndicator = findFactorsThatUseThisIndicator(indicatorId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisIndicator.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisIndicator[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public FactorSet findAllFactorsRelatedToThisObjective(BaseId objectiveId) throws Exception
	{
		Factor[] nodesThatUseThisObjective = findFactorsThatUseThisObjective(objectiveId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisObjective.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisObjective[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	public FactorSet findAllFactorsRelatedToThisGoal(BaseId goalId) throws Exception
	{
		Factor[] nodesThatUseThisGoal = findFactorsThatUseThisGoal(goalId).toNodeArray();
		FactorSet relatedNodes = new FactorSet();
		
		for(int i = 0; i < nodesThatUseThisGoal.length; ++i)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(nodesThatUseThisGoal[i]);
			relatedNodes.attemptToAddAll(nodesInChain);
		}
		
		return relatedNodes;
	}
	
	FactorPool getFactorPool()
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
			FactorSet modelNodes = findAllFactorsRelatedToThisIndicator(indicatorId);
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
			FactorSet modelNodes =  findAllFactorsRelatedToThisIndicator(indicatorId);
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
