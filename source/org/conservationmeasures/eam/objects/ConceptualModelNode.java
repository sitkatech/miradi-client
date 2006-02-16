/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.project.IdAssigner;
import org.json.JSONArray;
import org.json.JSONObject;

abstract public class ConceptualModelNode
{
	protected ConceptualModelNode(NodeType nodeType)
	{
		type = nodeType;
		
		id = IdAssigner.INVALID_ID;
		name = "";
		indicator = new IndicatorId();
		objectives = new ObjectiveIds();
		goals = new GoalIds();
	}
	
	protected ConceptualModelNode(NodeType nodeType, JSONObject json)
	{
		this(nodeType);
		id = json.getInt(TAG_ID);
		name = json.optString(TAG_NAME);
		setIndicatorId(new IndicatorId(json.getInt(TAG_INDICATOR_ID)));
		
		JSONArray goalIds = json.getJSONArray(TAG_GOAL_IDS);
		for(int i = 0; i < goalIds.length(); ++i)
			goals.addId(goalIds.getInt(i));
		
		JSONArray objectiveIds = json.getJSONArray(TAG_OBJECTIVE_IDS);
		for(int i = 0; i < objectiveIds.length(); ++i)
			objectives.addId(objectiveIds.getInt(i));
	}
	
	public abstract JSONObject toJson();
	
	public void setId(int idToUse)
	{
		id = idToUse;
	}

	public int getId()
	{
		return id;
	}
	
	public void setName(String nameToUse)
	{
		name = nameToUse;
	}
	
	public String getName()
	{
		return name;
	}

	public NodeType getType()
	{
		return type;
	}
	
	public void setType(NodeType typeToUse)
	{
		type = typeToUse;
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
	
	public static ConceptualModelNode createFrom(JSONObject json)
	{
		String typeString = json.getString(TAG_TYPE);
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
		JSONObject json = new JSONObject();
		json.put(TAG_TYPE, typeString);
		json.put(TAG_ID, getId());
		json.put(TAG_NAME, getName());
		json.put(TAG_INDICATOR_ID, getIndicatorId().getValue());
		
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
	
	public static ConceptualModelNode createConceptualModelObject(NodeType nodeType)
	{
		if(nodeType.isIntervention())
			return new ConceptualModelIntervention();
		else if(nodeType.isFactor())
			return new ConceptualModelFactor(nodeType);
		else if(nodeType.isTarget())
			return new ConceptualModelTarget();
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	private static final String TAG_TYPE = "Type";
	private static final String TAG_ID = "Id";
	private static final String TAG_NAME = "Name";
	private static final String TAG_INDICATOR_ID = "IndicatorId";
	private static final String TAG_GOAL_IDS = "GoalIds";
	private static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	
	static final String INTERVENTION_TYPE = "Intervention";
	static final String FACTOR_TYPE = "Factor";
	static final String TARGET_TYPE = "Target";
	
	private int id;
	private String name;
	private NodeType type;

	private IndicatorId indicator;
	private ObjectiveIds objectives;
	private GoalIds goals;
}
