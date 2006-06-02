/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.project.IdAssigner;
import org.json.JSONArray;
import org.json.JSONObject;

abstract public class ConceptualModelNode extends EAMObject
{
	protected ConceptualModelNode(int idToUse, NodeType nodeType)
	{
		super(idToUse);
		type = nodeType;
		
		comment = "";
		indicator = IdAssigner.INVALID_ID;
		objectives = new ObjectiveIds();
		goals = new GoalIds();
	}
	
	protected ConceptualModelNode(NodeType nodeType, JSONObject json)
	{
		super(json);
		type = nodeType;

		comment = json.optString(TAG_COMMENT);
		setIndicatorId(json.optInt(TAG_INDICATOR_ID, IdAssigner.INVALID_ID));
		
		goals = new GoalIds();
		JSONArray goalIds = json.getJSONArray(TAG_GOAL_IDS);
		for(int i = 0; i < goalIds.length(); ++i)
			goals.addId(goalIds.getInt(i));
		
		objectives = new ObjectiveIds();
		JSONArray objectiveIds = json.getJSONArray(TAG_OBJECTIVE_IDS);
		for(int i = 0; i < objectiveIds.length(); ++i)
			objectives.addId(objectiveIds.getInt(i));
	}
	
	public abstract JSONObject toJson();
	
	public int getType()
	{
		return ObjectType.MODEL_NODE;
	}
	
	public NodeType getNodeType()
	{
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

	public int getIndicatorId()
	{
		return indicator;
	}
	
	public void setIndicatorId(int indicatorToUse)
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
	
	public static ConceptualModelNode createFrom(JSONObject json) throws ParseException
	{
		String typeString = json.getString(TAG_NODE_TYPE);
		if(typeString.equals(INTERVENTION_TYPE))
			return new ConceptualModelIntervention(json);
		if(typeString.equals(FACTOR_TYPE))
			return new ConceptualModelFactor(json);
		if(typeString.equals(TARGET_TYPE))
			return new ConceptualModelTarget(json);
		
		throw new RuntimeException("Read unknown node type: " + typeString);
	}
	
	JSONObject createBaseJsonObject(String typeString)
	{
		JSONObject json = super.toJson();
		json.put(TAG_NODE_TYPE, typeString);
		json.put(TAG_COMMENT, getComment());
		json.put(TAG_INDICATOR_ID, getIndicatorId());
		
		JSONArray goalIds = new JSONArray();
		for(int i = 0; i < goals.size(); ++i)
			goalIds.appendInt(goals.getId(i));
		json.put(TAG_GOAL_IDS, goalIds);
		
		JSONArray objectiveIds = new JSONArray();
		for(int i = 0; i < objectives.size(); ++i)
			objectiveIds.appendInt(objectives.getId(i));
		json.put(TAG_OBJECTIVE_IDS, objectiveIds);
		
		return json;
	}
	
	public static ConceptualModelNode createConceptualModelObject(int idToCreate, NodeType nodeType)
	{
		if(nodeType.isIntervention())
			return new ConceptualModelIntervention(idToCreate);
		else if(nodeType.isFactor())
			return new ConceptualModelFactor(idToCreate, nodeType);
		else if(nodeType.isTarget())
			return new ConceptualModelTarget(idToCreate);
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	private static final String TAG_NODE_TYPE = "Type";
	private static final String TAG_COMMENT = "Comment";
	private static final String TAG_INDICATOR_ID = "IndicatorId";
	private static final String TAG_GOAL_IDS = "GoalIds";
	private static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	
	static final String INTERVENTION_TYPE = "Intervention";
	static final String FACTOR_TYPE = "Factor";
	static final String TARGET_TYPE = "Target";
	
	private NodeType type;
	private String comment;

	private int indicator;
	private ObjectiveIds objectives;
	private GoalIds goals;
}
