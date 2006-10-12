/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

abstract public class EAMBaseObject implements EAMObject
{
	public EAMBaseObject(BaseId idToUse)
	{
		id = idToUse;
		label = "";
	}
	
	EAMBaseObject(BaseId idToUse, JSONObject json)
	{
		id = idToUse;
		label = json.optString(TAG_LABEL, "");
	}
	
	public static EAMObject createFromJson(int type, JSONObject json) throws Exception
	{
		int idAsInt = json.getInt(TAG_ID);
		switch(type)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				return new ThreatRatingCriterion(idAsInt, json);
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				return new ThreatRatingValueOption(idAsInt, json);
				
			case ObjectType.TASK:
				return new Task(idAsInt, json);
			
			case ObjectType.MODEL_NODE:
				return ConceptualModelNode.createFrom(idAsInt, json);

			case ObjectType.VIEW_DATA:
				return new ViewData(idAsInt, json);
				
			case ObjectType.MODEL_LINKAGE:
				return new ConceptualModelLinkage(idAsInt, json);
				
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResource(idAsInt, json);
				
			case ObjectType.INDICATOR:
				return new Indicator(idAsInt, json);
				
			case ObjectType.OBJECTIVE:
				return new Objective(idAsInt, json);
				
			case ObjectType.GOAL:
				return new Goal(idAsInt, json);
				
			case ObjectType.PROJECT_METADATA:
				return new ProjectMetadata(idAsInt, json);
				
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}
	
	abstract public int getType();

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof EAMObject))
			return false;
		
		EAMObject other = (EAMObject)rawOther;
		return other.getId().equals(getId());
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String newLabel)
	{
		label = newLabel;
	}
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_LABEL.equals(fieldTag))
			setLabel(dataValue);
		else
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_LABEL.equals(fieldTag))
			return getLabel();
		
		throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);
	}
	

	public BaseId getId()
	{
		return id;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_ID, getId().asInt());
		json.put(TAG_LABEL, getLabel());
		
		return json;
	}
	
	protected static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";

	private BaseId id;
	String label;


}
