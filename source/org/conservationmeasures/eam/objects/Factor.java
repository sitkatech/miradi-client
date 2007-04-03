/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.project.ObjectManager;
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
	
	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.FACTOR;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.INDICATOR: 
				return true;
			case ObjectType.GOAL: 
				return true;
			case ObjectType.OBJECTIVE: 
				return true;
			case ObjectType.TASK: 
				return true;
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE: 
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.INDICATOR: 
				list.addAll(new ORefList(objectType, getIndicators()));
			case ObjectType.GOAL: 
				list.addAll(new ORefList(objectType, getGoals()));
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectives()));
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE: 
				list.addAll(new ORefList(objectType, getKeyEcologicalAttributes()));
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
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateFactorParameter(getNodeType());
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
	
	
	public static Factor createConceptualModelObject(FactorId idToCreate, CreateFactorParameter parameter)
	{
		 return createConceptualModelObject(null, idToCreate, parameter);
	}
	
	public static Factor createConceptualModelObject(ObjectManager objectManager, FactorId idToCreate, CreateFactorParameter parameter)
	{
		if (parameter==null)
			throw new RuntimeException("Tried to create factor without factor type");
		FactorType nodeType = parameter.getNodeType();
		if(nodeType.isStrategy())
			return new Strategy(objectManager, idToCreate);
		else if(nodeType.isCause())
			return new Cause(objectManager, idToCreate);
		else if(nodeType.isTarget())
			return new Target(objectManager, idToCreate);
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_GOALS))
			return getFactorGoals();
		
		if(fieldTag.equals(PSEUDO_TAG_OBJECTIVES))
			return getFactorObjectives();
		
		if(fieldTag.equals(Factor.PSEUDO_TAG_DIRECT_THREATS))
			return getFactorRelatedDirectThreats();
		
		if(fieldTag.equals(Factor.PSEUDO_TAG_TARGETS))
			return getFactorRelatedTargets();
		
		return super.getData(fieldTag);
	}
	
	private String getFactorRelatedDirectThreats()
	{
		ChainObject chain = new ChainObject();
		chain.buildNormalChain(objectManager.getDiagramModel(), this);
		DirectThreatSet directThreats = new DirectThreatSet(chain.getFactors());
		
		return getLabelsAsMultiline(directThreats);
	}

	private String getFactorRelatedTargets()
	{
		ChainObject chain = new ChainObject();
		chain.buildNormalChain(objectManager.getDiagramModel(), this);
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
		ChainObject chain = new ChainObject();
		chain.buildDownstreamChain(objectManager.getDiagramModel(), this);
		
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
		
		addField(TAG_COMMENT, comment);
		addField(TAG_INDICATOR_IDS, indicators);
		addField(TAG_OBJECTIVE_IDS, objectives);
		addField(TAG_GOAL_IDS, goals);
		addField(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keyEcologicalAttributes);
	}

	public static final FactorType TYPE_INVALID = null;
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

	public static final String OBJECT_NAME = "Factor";
	public static final String OBJECT_NAME_THREAT = "Direct Threat";
	public static final String OBJECT_NAME_CONTRIBUTING_FACTOR = "Contributing Factor";
	
	private FactorType type;
	private StringData comment;

	private IdListData indicators;
	private IdListData objectives;
	private IdListData goals;
	private IdListData keyEcologicalAttributes;
}
