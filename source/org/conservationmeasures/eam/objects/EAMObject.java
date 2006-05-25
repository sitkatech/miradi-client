/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

abstract public class EAMObject
{
	public EAMObject(int idToUse)
	{
		id = idToUse;
		label = DEFAULT_LABEL;
	}
	
	EAMObject(JSONObject json)
	{
		id = json.getInt(TAG_ID);
		label = json.optString(TAG_LABEL, DEFAULT_LABEL);
	}
	
	public static EAMObject createFromJson(int type, JSONObject json) throws Exception
	{
		switch(type)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				return new ThreatRatingCriterion(json);
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				return new ThreatRatingValueOption(json);
				
			case ObjectType.TASK:
				return new Task(json);
			
			case ObjectType.MODEL_NODE:
				return ConceptualModelNode.createFrom(json);

			case ObjectType.VIEW_DATA:
				return new ViewData(json);
				
			case ObjectType.MODEL_LINKAGE:
				return new ConceptualModelLinkage(json);
				
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResource(json);
				
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
		return other.getId() == getId();
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String newLabel)
	{
		label = newLabel;
	}
	
	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_LABEL.equals(fieldTag))
			setLabel((String)dataValue);
		else
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_LABEL.equals(fieldTag))
			return getLabel();
		
		throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);
	}
	

	public int getId()
	{
		return id;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_ID, getId());
		json.put(TAG_LABEL, getLabel());
		
		return json;
	}
	
	protected static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";
	private int id;
	String label;

}
