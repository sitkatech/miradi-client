/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodetypes.FactorType;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class Factor extends EAMBaseObject
{
	protected Factor(BaseId idToUse, FactorType nodeType)
	{
		super(idToUse);
		type = nodeType;
	}
	
	protected Factor(ModelNodeId idToUse, FactorType nodeType, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
		type = nodeType;
	}
	
	public ModelNodeId getModelNodeId()
	{
		return new ModelNodeId(getId().asInt());
	}
	
	public int getType()
	{
		return ObjectType.MODEL_NODE;
	}
	
	public FactorType getNodeType()
	{
		if(isDirectThreat() || isIndirectFactor())
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

	public void setGoals(IdList goalsToUse)
	{
		goals.set(goalsToUse);
	}

	public boolean isIntervention()
	{
		return false;
	}
	
	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isIndirectFactor()
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
	
	public boolean isCluster()
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

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateModelNodeParameter(getNodeType());
	}

	public static Factor createFrom(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		String typeString = json.getString(TAG_NODE_TYPE);
		if(typeString.equals(FactorTypeStrategy.STRATEGY_TYPE))
			return new Strategy(new ModelNodeId(idAsInt), json);
		if(typeString.equals(FactorTypeCause.CAUSE_TYPE))
			return new Cause(new ModelNodeId(idAsInt), json);
		if(typeString.equals(FactorTypeTarget.TARGET_TYPE))
			return new Target(new ModelNodeId(idAsInt), json);
		if(typeString.equals(FactorTypeCluster.CLUSTER_TYPE))
			return new FactorCluster(new ModelNodeId(idAsInt), json);
		
		throw new RuntimeException("Read unknown node type: " + typeString);
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
	
	public static Factor createConceptualModelObject(ModelNodeId idToCreate, CreateModelNodeParameter parameter)
	{
		FactorType nodeType = parameter.getNodeType();
		if(nodeType.isStrategy())
			return new Strategy(idToCreate);
		else if(nodeType.isCause())
			return new Cause(idToCreate);
		else if(nodeType.isTarget())
			return new Target(idToCreate);
		else if(nodeType.isFactorCluster())
			return new FactorCluster(idToCreate);
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	void clear()
	{
		super.clear();
		comment = new StringData();
	    indicators = new IdListData();
		objectives = new IdListData();
		goals = new IdListData();
		
		addField(TAG_COMMENT, comment);
		addField(TAG_INDICATOR_IDS, indicators);
		addField(TAG_OBJECTIVE_IDS, objectives);
		addField(TAG_GOAL_IDS, goals);
	}

	public static final FactorType TYPE_INVALID = null;
	public static final FactorType TYPE_TARGET = new FactorTypeTarget();
	public static final FactorType TYPE_CAUSE = new FactorTypeCause();
	public static final FactorType TYPE_INTERVENTION = new FactorTypeStrategy();
	public static final FactorType TYPE_CLUSTER = new FactorTypeCluster();
	
	public static final String TAG_NODE_TYPE = "Type";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	public static final String TAG_GOAL_IDS = "GoalIds";
	
	private FactorType type;
	private StringData comment;

	private IdListData indicators;
	private IdListData objectives;
	private IdListData goals;
}
