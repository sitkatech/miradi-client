/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;

public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}
	
	// FIXME: Write tests for this!!! (Richard, with Kevin)
	public Factor getDirectOrIndirectOwningFactor(ORef ref) throws Exception
	{
		BaseObject owner = project.findObject(ref);
		int AVOID_INFINITE_LOOP = 10000;
		for(int i = 0; i < AVOID_INFINITE_LOOP; ++i)
		{
			if(owner.getType() == ObjectType.FACTOR)
				return (Factor)owner;
			owner = getOwner(owner.getRef());
			if(owner == null)
				break;
		}
		return null;
	}
 
	public Vector getRefferedInObject(ORef ref)
	{
		switch (ref.getObjectType())
		{
			case ObjectType.PROJECT_RESOURCE:
				return getAssignmentsWithResource(ref);
				
			default:
				return new Vector();
		}
	}
	
	private Vector getAssignmentsWithResource(ORef ref)
	{
		Vector vector = new Vector();
		IdList allAssignments = getProject().getAssignmentPool().getIdList();
		for (int i = 0; i < allAssignments.size(); i++)
		{
			Assignment assignment = (Assignment) getProject().findObject(ObjectType.ASSIGNMENT, allAssignments.get(i));
			BaseId resourceId = assignment.getResourceId();
			if (ref.getObjectId().equals(resourceId))
				vector.add(assignment);
		}
		
		return vector;
	}

	// FIXME: Write tests for this!!! (Richard, with Kevin)
	public BaseObject getOwner(ORef ref) throws Exception
	{
		switch(ref.getObjectType())
		{
			case ObjectType.ASSIGNMENT:
				return assignmentOwner(ref);
			case ObjectType.GOAL:
				return goalOwner(ref);
			case ObjectType.INDICATOR:
				return indicatorOwner(ref);
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return keyEcologicalAttributeOwner(ref);
			case ObjectType.OBJECTIVE:
				return getObjectiveOwner(ref);
			case ObjectType.TASK:
				return getTaskOwner(ref);
			default:
				throw new RuntimeException("getOwner not implemented for object " + ref);
		}
	}
	
	private BaseObject assignmentOwner(ORef ref) throws Exception
	{
		return findOwner(ref, getProject().getTaskPool().getAllTasks(), Task.TAG_ASSIGNMENT_IDS);
	}

	private BaseObject goalOwner(ORef ref) throws Exception
	{
		BaseObject factorOwner = findOwner(ref, getFactorPool().getAllFactors(), Factor.TAG_GOAL_IDS);
		if(factorOwner != null)
			return factorOwner;
		return findOwner(ref, getProject().getIndicatorPool().getAllIndicators(), Indicator.TAG_GOAL_IDS);		
	}
	
	private BaseObject indicatorOwner(ORef ref) throws Exception
	{
		BaseObject factorOwner = findOwner(ref, getFactorPool().getAllFactors(), Factor.TAG_INDICATOR_IDS);
		if(factorOwner != null)
			return factorOwner;
		return findOwner(ref, getProject().getKeyEcologicalAttributePool().getAllKeyEcologicalAttribute(), KeyEcologicalAttribute.TAG_INDICATOR_IDS);		
	}
	
	private BaseObject keyEcologicalAttributeOwner(ORef ref) throws Exception
	{
		return findOwner(ref, getFactorPool().getTargets(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}

	private BaseObject getObjectiveOwner(ORef ref) throws Exception
	{
		return findOwner(ref, getFactorPool().getAllFactors(), Factor.TAG_OBJECTIVE_IDS);
	}
	
	private BaseObject getTaskOwner(ORef ref) throws Exception
	{
		BaseObject strategyOwner = findOwner(ref, getFactorPool().getDraftAndNonDraftStrategies(), Strategy.TAG_ACTIVITY_IDS);
		if(strategyOwner != null)
			return strategyOwner;
		BaseObject IndicatorOwner = findOwner(ref, getProject().getIndicatorPool().getAllIndicators(), Indicator.TAG_TASK_IDS);
		if(IndicatorOwner != null)
			return IndicatorOwner;
		return findOwner(ref, getProject().getTaskPool().getAllTasks(), Task.TAG_SUBTASK_IDS);		
	}

	private BaseObject findOwner(ORef refToFind, BaseObject[] objectsToSearchIn, String tagOfIdListToSearch) throws ParseException
	{
		for(int i = 0; i < objectsToSearchIn.length; ++i)
		{
			IdList candidates = new IdList(objectsToSearchIn[i].getData(tagOfIdListToSearch));
			if(candidates.contains(refToFind.getObjectId()))
				return objectsToSearchIn[i];
		}
		return null;
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
	
	public IdList getDirectOrIndirectGoals(Factor factor)
	{
		IdList goalIds = new IdList();
		if (!factor.isTarget())
			return goalIds;

		Target target = (Target)factor;
		if(!target.isViabilityModeTNC())
			return target.getGoals();
		
		IdList indicatorIds = getDirectOrIndirectIndicators(factor);
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			Indicator indicator = (Indicator)project.findObject(ObjectType.INDICATOR, indicatorIds.get(i));
			goalIds.addAll(indicator.getGoalIds());
		}
		
		return goalIds;
	}
	
	public FactorSet findAllFactorsRelatedToThisIndicator(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.INDICATOR, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisObjective(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.OBJECTIVE, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisGoal(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.GOAL, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisObject(ORef ref) throws Exception
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor(ref);
		FactorSet relatedFactors = new FactorSet();
		
		if(owningFactor != null)
		{
			FactorSet nodesInChain = getDiagramModel().getAllUpstreamDownstreamNodes(owningFactor);
			relatedFactors.attemptToAddAll(nodesInChain);
		}
		
		return relatedFactors;
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
	
	static final String HTML_ERROR = "<html>(Error)</html>";
	
	Project project;
}
