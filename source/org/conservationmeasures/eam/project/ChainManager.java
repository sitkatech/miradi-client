/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.text.ParseException;
import java.util.Vector;

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
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;

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
			return findFactorsThatHaveThisIndicator(id);
		if(type == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return findFactorsThatHaveThisObject(id, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		throw new RuntimeException("Not an annotation type? " + type);
	}

	public FactorSet findFactorsThatHaveThisIndicator(BaseId objectId) throws Exception
	{
		FactorSet factorSet =  findFactorsThatHaveThisObject(objectId, Factor.TAG_INDICATOR_IDS);
		FactorSet targetsFound = findTargetsWithKEAsThatHaveThisIndicator(objectId);
		factorSet.attemptToAddAll(targetsFound);
		return factorSet;
	}

	private FactorSet findTargetsWithKEAsThatHaveThisIndicator(BaseId objectId)
	{
		FactorSet targetsFound = new FactorSet();
		Factor[] targets = getFactorPool().getTargets();
		for (int i=0; i<targets.length; ++i)
		{
			if (doesTargetContainKEAWithIndicator(objectId,  (Target)targets[i]))
				targetsFound.attemptToAdd(targets[i]);
		}
		return targetsFound;
	}

	public KeyEcologicalAttribute[] findKEAsThatHaveThisIndicator(BaseId objectId)
	{
		Vector keasFound = new Vector();
		Factor[] targets = getFactorPool().getTargets();
		for (int i=0; i<targets.length; ++i)
		{
			KeyEcologicalAttribute kea = findKEAWithIndicator(objectId,  (Target)targets[i]);
			if (kea!=null)
				keasFound.add(kea);
		}
		return (KeyEcologicalAttribute[])keasFound.toArray(new KeyEcologicalAttribute[0]);
	}
	
	
	public KeyEcologicalAttribute findKEAWithIndicator(BaseId objectId,  Target target)
	{
		IdList keas = target.getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) project.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			if (doesKEAContainIndicator(objectId, kea))
				return kea;
		}
		return null;
	}
	
	public boolean doesTargetContainKEAWithIndicator(BaseId objectId,  Target target)
	{
		IdList keas = target.getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) project.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			if (doesKEAContainIndicator(objectId, kea))
				return true;
		}
		return false;
	}
	
	
	public IdList findAllKeaIndicatorsForTarget(Target target)
	{
		IdList list = new IdList();
		IdList keas = target.getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) project.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			list.addAll(kea.getIndicatorIds());
		}
		return list;
	}
	
	
	public IdList getDirectOrIndirectIndicators(Factor factor)
	{
		if (factor.isTarget())
		{
			String code = factor.getData(Target.TAG_VIABILITY_MODE);
			if (code.equals(ViabilityModeQuestion.TNC_STYLE_CODE))
			{
				return findAllKeaIndicatorsForTarget((Target)factor);
			}
		}

		return factor.getIndicators();

	}
	
	private boolean doesKEAContainIndicator(BaseId objectId,  KeyEcologicalAttribute kea)
	{
		return (kea.getIndicatorIds().find(objectId) != BaseId.INVALID.asInt());
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
