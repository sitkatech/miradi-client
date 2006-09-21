/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalIds;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.ids.ObjectiveIds;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONArray;
import org.json.JSONObject;

abstract public class ConceptualModelNode extends EAMBaseObject
{
	protected ConceptualModelNode(BaseId idToUse, NodeType nodeType)
	{
		super(idToUse);
		type = nodeType;
		
		comment = "";
		indicator = new IndicatorId(BaseId.INVALID.asInt());
		objectives = new ObjectiveIds();
		goals = new GoalIds();
	}
	
	protected ConceptualModelNode(NodeType nodeType, JSONObject json)
	{
		super(json);
		type = nodeType;

		comment = json.optString(TAG_COMMENT);
		setIndicatorId(new IndicatorId(json.optInt(TAG_INDICATOR_ID, IdAssigner.INVALID_ID)));
		
		goals = new GoalIds();
		JSONArray goalIds = json.getJSONArray(TAG_GOAL_IDS);
		for(int i = 0; i < goalIds.length(); ++i)
			goals.addId(new BaseId(goalIds.getInt(i)));
		
		objectives = new ObjectiveIds();
		JSONArray objectiveIds = json.getJSONArray(TAG_OBJECTIVE_IDS);
		for(int i = 0; i < objectiveIds.length(); ++i)
			objectives.addId(new BaseId(objectiveIds.getInt(i)));
	}
	
	public abstract JSONObject toJson();
	
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
		return comment;
	}
	
	public void setComment(String newComment)
	{
		comment = newComment;
	}
	
	public boolean isStatusDraft()
	{
		return false;
	}

	public IndicatorId getIndicatorId()
	{
		return indicator;
	}
	
	public void setIndicatorId(IndicatorId indicatorToUse)
	{
		indicator = indicatorToUse;
	}

	public ThreatRatingValue getThreatPriority()
	{
		return null;
	}
	
	public ObjectiveIds getObjectives()
	{
		return objectives;
	}

	public void setObjectives(ObjectiveIds objectivesToUse)
	{
		objectives = objectivesToUse;
	}
	
	public GoalIds getGoals()
	{
		return goals;
	}

	public void setGoals(GoalIds goalsToUse)
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

	public boolean canHavePriority()
	{
		return false;
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_NODE_TYPE))
			throw new RuntimeException("Cannot use getData to obtain node type");
		
		if(fieldTag.equals(TAG_COMMENT))
			return getComment();
		if(fieldTag.equals(TAG_INDICATOR_ID))
			return Integer.toString(getIndicatorId().asInt());
		if(fieldTag.equals(TAG_GOAL_IDS))
			return goals.toString();
		if(fieldTag.equals(TAG_OBJECTIVE_IDS))
			return objectives.toString();
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_NODE_TYPE))
			throw new RuntimeException("Cannot use setData to change node type");
		
		if(fieldTag.equals(TAG_COMMENT))
			setComment((String)dataValue);
		else if(fieldTag.equals(TAG_INDICATOR_ID))
			setIndicatorId(new IndicatorId(Integer.parseInt((String)dataValue)));
		else if(fieldTag.equals(TAG_GOAL_IDS))
			setGoals(new GoalIds(new IdList((String)dataValue)));
		else if(fieldTag.equals(TAG_OBJECTIVE_IDS))
			setObjectives(new ObjectiveIds(new IdList((String)dataValue)));
		else
			super.setData(fieldTag, dataValue);
	}

	public static ConceptualModelNode createFrom(JSONObject json) throws ParseException
	{
		String typeString = json.getString(TAG_NODE_TYPE);
		if(typeString.equals(NodeTypeIntervention.INTERVENTION_TYPE))
			return new ConceptualModelIntervention(json);
		if(typeString.equals(NodeTypeFactor.FACTOR_TYPE))
			return new ConceptualModelFactor(json);
		if(typeString.equals(NodeTypeTarget.TARGET_TYPE))
			return new ConceptualModelTarget(json);
		if(typeString.equals(NodeTypeCluster.CLUSTER_TYPE))
			return new ConceptualModelCluster(json);
		
		throw new RuntimeException("Read unknown node type: " + typeString);
	}
	
	JSONObject createBaseJsonObject(String typeString)
	{
		JSONObject json = super.toJson();
		json.put(TAG_NODE_TYPE, typeString);
		json.put(TAG_COMMENT, getComment());
		json.put(TAG_INDICATOR_ID, getIndicatorId().asInt());
		
		JSONArray goalIds = new JSONArray();
		for(int i = 0; i < goals.size(); ++i)
			goalIds.appendInt(goals.getId(i).asInt());
		json.put(TAG_GOAL_IDS, goalIds);
		
		JSONArray objectiveIds = new JSONArray();
		for(int i = 0; i < objectives.size(); ++i)
			objectiveIds.appendInt(objectives.getId(i).asInt());
		json.put(TAG_OBJECTIVE_IDS, objectiveIds);
		
		return json;
	}
	
	public static ConceptualModelNode createConceptualModelObject(BaseId idToCreate, CreateModelNodeParameter parameter)
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

	public static String getNodeLabelsAsString(ConceptualModelNode[] modelNodes)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < modelNodes.length; ++i)
		{
			if(i > 0)
				result.append(", ");
			result.append(modelNodes[i].getLabel());
		}
		
		return result.toString();
	}

	public static final String TAG_NODE_TYPE = "Type";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_INDICATOR_ID = "IndicatorId";
	public static final String TAG_GOAL_IDS = "GoalIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	
	private NodeType type;
	private String comment;

	private IndicatorId indicator;
	private ObjectiveIds objectives;
	private GoalIds goals;
}
