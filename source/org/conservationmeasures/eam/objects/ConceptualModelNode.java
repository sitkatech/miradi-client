/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class ConceptualModelNode extends EAMBaseObject
{
	protected ConceptualModelNode(BaseId idToUse, NodeType nodeType)
	{
		super(idToUse);
		type = nodeType;
		
		indicators = new IdList();
		objectives = new IdList();
		goals = new IdList();
	}
	
	protected ConceptualModelNode(ModelNodeId idToUse, NodeType nodeType, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
		type = nodeType;

		indicators = new IdList(json.optString(TAG_INDICATOR_IDS));
		goals = new IdList(json.optString(TAG_GOAL_IDS));
		objectives = new IdList(json.optString(TAG_OBJECTIVE_IDS));
	}
	
	public ModelNodeId getModelNodeId()
	{
		return new ModelNodeId(getId().asInt());
	}
	
	public int getType()
	{
		return ObjectType.MODEL_NODE;
	}
	
	public NodeType getNodeType()
	{
		if(isDirectThreat() || isIndirectFactor())
			return new NodeTypeFactor();
		return type;
	}
	
	public void setNodeType(NodeType typeToUse)
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
		return indicators;
	}
	
	public void setIndicators(IdList indicatorsToUse)
	{
		indicators = indicatorsToUse;
	}

	public IdList getObjectives()
	{
		return objectives;
	}

	public void setObjectives(IdList objectivesToUse)
	{
		objectives = objectivesToUse;
	}
	
	public IdList getGoals()
	{
		return goals;
	}

	public void setGoals(IdList goalsToUse)
	{
		goals = goalsToUse;
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

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_NODE_TYPE))
			throw new RuntimeException("Cannot use getData to obtain node type");

		if(fieldTag.equals(TAG_INDICATOR_IDS))
			return indicators.toString();
		if(fieldTag.equals(TAG_GOAL_IDS))
			return goals.toString();
		if(fieldTag.equals(TAG_OBJECTIVE_IDS))
			return objectives.toString();
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_NODE_TYPE))
			throw new RuntimeException("Cannot use setData to change node type");
		
		else if(fieldTag.equals(TAG_INDICATOR_IDS))
			setIndicators(new IdList(dataValue));
		else if(fieldTag.equals(TAG_GOAL_IDS))
			setGoals(new IdList(dataValue));
		else if(fieldTag.equals(TAG_OBJECTIVE_IDS))
			setObjectives(new IdList(dataValue));
		else
			super.setData(fieldTag, dataValue);
	}

	public static ConceptualModelNode createFrom(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		String typeString = json.getString(TAG_NODE_TYPE);
		if(typeString.equals(NodeTypeIntervention.INTERVENTION_TYPE))
			return new ConceptualModelIntervention(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeFactor.FACTOR_TYPE))
			return new ConceptualModelFactor(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeTarget.TARGET_TYPE))
			return new ConceptualModelTarget(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeCluster.CLUSTER_TYPE))
			return new ConceptualModelCluster(new ModelNodeId(idAsInt), json);
		
		throw new RuntimeException("Read unknown node type: " + typeString);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_NODE_TYPE, type.toString());
		json.put(TAG_INDICATOR_IDS, getIndicators().toString());
		json.put(TAG_GOAL_IDS, goals.toString());
		json.put(TAG_OBJECTIVE_IDS, objectives.toString());
		
		return json;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public static ConceptualModelNode createConceptualModelObject(ModelNodeId idToCreate, CreateModelNodeParameter parameter)
	{
		NodeType nodeType = parameter.getNodeType();
		if(nodeType.isIntervention())
			return new ConceptualModelIntervention(idToCreate);
		else if(nodeType.isFactor())
			return new ConceptualModelFactor(idToCreate);
		else if(nodeType.isTarget())
			return new ConceptualModelTarget(idToCreate);
		else if(nodeType.isCluster())
			return new ConceptualModelCluster(idToCreate);
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	void clear()
	{
		super.clear();
		comment = new StringData();
		
		addField(TAG_COMMENT, comment);
	}

	public static final String TAG_NODE_TYPE = "Type";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_GOAL_IDS = "GoalIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	
	private NodeType type;
	private StringData comment;

	private IdList indicators;
	private IdList objectives;
	private IdList goals;
}
