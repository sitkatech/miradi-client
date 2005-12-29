/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.annotations.Goals;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.Objectives;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.project.IdAssigner;
import org.json.JSONObject;

abstract public class ConceptualModelNode
{
	public ConceptualModelNode(NodeType nodeType)
	{
		type = nodeType;
		
		id = IdAssigner.INVALID_ID;
		setNodePriority(ThreatPriority.createPriorityNotUsed());
		indicator = new IndicatorId();
		objectives = new Objectives();
		goals = new Goals();
	}
	
	public ConceptualModelNode(NodeType nodeType, JSONObject json)
	{
		this(nodeType);
		id = json.getInt(TAG_ID);
		setNodePriority(ThreatPriority.createFromInt(json.getInt(TAG_PRIORITY)));
		setIndicatorId(new IndicatorId(json.getInt(TAG_INDICATOR_ID)));
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

	public ThreatPriority getThreatPriority()
	{
		return threatPriority;
	}
	
	public void setNodePriority(ThreatPriority priorityToUse)
	{
		threatPriority = priorityToUse;
	}
	
	public Objectives getObjectives()
	{
		return objectives;
	}

	public void setObjectives(Objectives objectivesToUse)
	{
		objectives = objectivesToUse;
	}
	
	public Goals getGoals()
	{
		return goals;
	}

	public void setGoals(Goals goalsToUse)
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
		json.put(TAG_PRIORITY, getThreatPriority().getValue());
		json.put(TAG_INDICATOR_ID, getIndicatorId().getValue());
		
		return json;
	}
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_ID = "Id";
	private static final String TAG_PRIORITY = "Priority";
	private static final String TAG_INDICATOR_ID = "IndicatorId";
	
	static final String INTERVENTION_TYPE = "Intervention";
	static final String FACTOR_TYPE = "Factor";
	static final String TARGET_TYPE = "Target";
	
	private int id;
	private NodeType type;
	private ThreatPriority threatPriority;

	private IndicatorId indicator;
	private Objectives objectives;
	private Goals goals;
}
