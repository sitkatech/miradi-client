/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeIntermediateResult;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTextBox;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeThreatReductionResult;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.ProjectChainObject;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class Factor extends BaseObject
{
	protected Factor(ObjectManager objectManager, BaseId idToUse, FactorType nodeType)
	{
		super(objectManager, idToUse);
		type = nodeType;
	}
	
	protected Factor(BaseId idToUse, FactorType nodeType)
	{
		super(idToUse);
		type = nodeType;
	}
	
	protected Factor(ObjectManager objectManager, FactorId idToUse, FactorType nodeType, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
		type = nodeType;
	}
	
	protected Factor(FactorId idToUse, FactorType nodeType, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
		type = nodeType;
	}
	
	public FactorId getFactorId()
	{
		return new FactorId(getId().asInt());
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.INDICATOR: 
				return true;
			default:
				return false;
		}
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.INDICATOR: 
				list.addAll(new ORefList(objectType, getIndicators()));
				break;
		}
		return list;
	}
	
	
	public IdList getDirectOrIndirectIndicators()
	{
		return getIndicators();
	}
	
	public FactorType getNodeType()
	{
		if(isDirectThreat() || isContributingFactor())
			return new FactorTypeCause();
		return type;
	}
	
	public void setNodeType(FactorType typeToUse)
	{
		type = typeToUse;
	}
	
	public String getComment()
	{
		return comment.get();
	}
	
	public void setComment(String newComment) throws Exception
	{
		comment.set(newComment);
	}
	
	public boolean isStatusDraft()
	{
		return false;
	}

	public IdList getIndicators()
	{
		return indicators.getIdList();
	}
	
	public void setIndicators(IdList indicatorsToUse)
	{
		indicators.set(indicatorsToUse);
	}

	public IdList getObjectives()
	{
		return objectives.getIdList();
	}

	public void setObjectives(IdList objectivesToUse)
	{
		objectives.set(objectivesToUse);
	}
	
	public IdList getGoals()
	{
		return goals.getIdList();
	}
	
	public IdList getKeyEcologicalAttributes()
	{
		return keyEcologicalAttributes.getIdList();
	}


	public void setGoals(IdList goalsToUse)
	{
		goals.set(goalsToUse);
	}
	
	public static boolean isFactor(int typeToUse)
	{
		if (typeToUse == ObjectType.CAUSE)
			return true;
		
		if (typeToUse == ObjectType.TARGET)
			return true;
		
		if (typeToUse == ObjectType.STRATEGY)
			return true;
		
		if (typeToUse == ObjectType.INTERMEDIATE_RESULT)
			return true;
		
		if (typeToUse == ObjectType.THREAT_REDUCTION_RESULT)
			return true;
		
		if (typeToUse == ObjectType.FACTOR)
			return true;
	
		if (typeToUse == ObjectType.TEXT_BOX)
			return true;
		
		return false;
	}

	public boolean isTextBox()
	{
		return false;
	}
	
	public boolean isThreatReductionResult()
	{
		return false;
	}
	
	public boolean isIntermediateResult()
	{
		return false;
	}
	
	public boolean isStrategy()
	{
		return false;
	}
	
	public boolean isCause()
	{
		return false;
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isContributingFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isStress()
	{
		return false;
	}
	
	public boolean isFactorCluster()
	{
		return false;
	}
	
	public boolean canHaveIndicators()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return false;
	}

	public boolean canHaveGoal()
	{
		return false;
	}

	public boolean canHaveKeyEcologicalAttribures()
	{
		return false;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject superJson = super.toJson();
		superJson.put(TAG_NODE_TYPE , type.toString());
		
		return superJson;
	}

	public String toString()
	{
		return getLabel();
	}
	
	
	public static Factor createConceptualModelObject(FactorId idToCreate, int objectType)
	{
		 return createConceptualModelObject(null, idToCreate, objectType);
	}
	
	public static Factor createConceptualModelObject(ObjectManager objectManager, FactorId idToCreate, int objectType)
	{
		return createFactor(objectManager, idToCreate, objectType);
	}

	public static Factor createFactor(ObjectManager objectManager, FactorId idToCreate, int objectType)
	{
		if(objectType == ObjectType.STRATEGY)
			return new Strategy(objectManager, idToCreate);

		else if(objectType == ObjectType.CAUSE)
			return new Cause(objectManager, idToCreate);
		
		else if(objectType == ObjectType.TARGET)
			return new Target(objectManager, idToCreate);
	
		else if (objectType == ObjectType.INTERMEDIATE_RESULT)
			return new IntermediateResult(objectManager, idToCreate);
		
		else if (objectType == ObjectType.THREAT_REDUCTION_RESULT)
			return new ThreatReductionResult(objectManager, idToCreate);
		
		else if (objectType == ObjectType.TEXT_BOX)
			return new TextBox(objectManager, idToCreate);
		
		throw new RuntimeException("Tried to create unknown node type: " + objectType);
	}
	
	public Command[] fixupAllRefs(HashMap oldToNewRefMap) throws Exception
	{
		Vector commandsToFixRefs = new Vector();
		
		Command commandToFixIndicatorRefs = fixUpRefs(oldToNewRefMap, Indicator.getObjectType(), TAG_INDICATOR_IDS);
		commandsToFixRefs.add(commandToFixIndicatorRefs);
		
		Command commandToFixObjectiveRefs = fixUpRefs(oldToNewRefMap, Objective.getObjectType(), TAG_OBJECTIVE_IDS);
		commandsToFixRefs.add(commandToFixObjectiveRefs);
		
		Command commandToFixGoalRefs = fixUpRefs(oldToNewRefMap, Goal.getObjectType(), TAG_GOAL_IDS);
		commandsToFixRefs.add(commandToFixGoalRefs);
		
		Command commandToFixKEARefs = fixUpRefs(oldToNewRefMap, KeyEcologicalAttribute.getObjectType(), TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		commandsToFixRefs.add(commandToFixKEARefs);
		
		return (Command[]) commandsToFixRefs.toArray(new Command[0]);
	}

	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_GOALS))
			return getFactorGoals();
		
		if(fieldTag.equals(PSEUDO_TAG_OBJECTIVES))
			return getFactorObjectives();
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getFactorRelatedDirectThreats();
		
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getFactorRelatedTargets();
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getFactorRelatedDirectThreats()
	{
		ProjectChainObject chain = new ProjectChainObject();
		chain.buildNormalChain(this);
		DirectThreatSet directThreats = new DirectThreatSet(chain.getFactors());
		
		return getLabelsAsMultiline(directThreats);
	}

	private String getFactorRelatedTargets()
	{
		ProjectChainObject chain = new ProjectChainObject();
		chain.buildNormalChain(this);
		TargetSet directThreats = new TargetSet(chain.getFactors());
		
		return getLabelsAsMultiline(directThreats);
	}
	
	private String getFactorGoals()
	{
		return getFactorDesires(ObjectType.GOAL, Factor.TAG_GOAL_IDS);
	}

	private String getFactorObjectives()
	{
		return getFactorDesires(ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
	}
	
	private String getFactorDesires(int desireType, String desireIdsTag)
	{
		ProjectChainObject chain = new ProjectChainObject();
		chain.buildDownstreamChain(this);
		
		IdList allDesireIds = new IdList();
		Factor[] factors = chain.getFactorsArray();
		for(int i = 0; i < factors.length; ++i)
		{
			Factor factor = factors[i];
			IdList theseDesireIds = getIdList(desireIdsTag, factor);
			addMissingIds(allDesireIds, theseDesireIds);
		}
		
		return getDesiresAsMultiline(desireType, allDesireIds);
	}

	private IdList getIdList(String desireIdsTag, Factor factor)
	{
		try
		{
			return new IdList(factor.getData(desireIdsTag));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new IdList();
		}
	}
	
	private void addMissingIds(IdList destination, IdList source)
	{
		for(int i = 0; i < source.size(); ++i)
			if(!destination.contains(source.get(i)))
				destination.add(source.get(i));
	}
	
	private String getDesiresAsMultiline(int desireType, IdList desireIds)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < desireIds.size(); ++i)
		{
			if(result.length() > 0)
				result.append("\n");
			
			result.append(objectManager.getObjectData(desireType, desireIds.get(i), Desire.TAG_LABEL));
		}
		
		return result.toString();
	}

	void clear()
	{
		super.clear();
		comment = new StringData();
	    indicators = new IdListData();
		objectives = new IdListData();
		goals = new IdListData();
		keyEcologicalAttributes = new IdListData();
		multiLineGoals = new PseudoStringData(PSEUDO_TAG_GOALS);
		multiLineObjectives = new PseudoStringData(PSEUDO_TAG_OBJECTIVES);
		multiLineDeirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		
		addField(TAG_COMMENT, comment);
		addField(TAG_INDICATOR_IDS, indicators);
		addField(TAG_OBJECTIVE_IDS, objectives);
		addField(TAG_GOAL_IDS, goals);
		addField(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keyEcologicalAttributes);
		addField(PSEUDO_TAG_GOALS, multiLineGoals);
		addField(PSEUDO_TAG_OBJECTIVES, multiLineObjectives);
		addField(PSEUDO_TAG_DIRECT_THREATS, multiLineDeirectThreats);
		addField(PSEUDO_TAG_TARGETS, multiLineTargets);
	}

	public static final FactorType TYPE_TEXT_BOX = new FactorTypeTextBox();
	public static final FactorType TYPE_THREAT_REDUCTION_RESULT = new FactorTypeThreatReductionResult();
	public static final FactorType TYPE_INTERMEDIATE_RESULT = new FactorTypeIntermediateResult();
	public static final FactorType TYPE_TARGET = new FactorTypeTarget();
	public static final FactorType TYPE_CAUSE = new FactorTypeCause();
	public static final FactorType TYPE_STRATEGY = new FactorTypeStrategy();
	
	public static final String TAG_NODE_TYPE = "Type";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	public static final String TAG_GOAL_IDS = "GoalIds"; 
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS = "KeyEcologicalAttributeIds";
	public static final String PSEUDO_TAG_GOALS = "PseudoTagGoals";
	public static final String PSEUDO_TAG_OBJECTIVES = "PseudoTagObjectives";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public static final String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	
	private FactorType type;
	private StringData comment;

	private IdListData indicators;
	private IdListData objectives;
	private IdListData goals;
	private IdListData keyEcologicalAttributes;
	
	PseudoStringData multiLineGoals;
	PseudoStringData multiLineObjectives;
	PseudoStringData multiLineDeirectThreats;
	PseudoStringData multiLineTargets;
}
