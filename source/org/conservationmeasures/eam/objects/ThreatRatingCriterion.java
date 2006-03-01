/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ThreatRatingCriterion
{
	public ThreatRatingCriterion(int idToUse)
	{
		id = idToUse;
		label = "Unknown";
	}
	
	public ThreatRatingCriterion(JSONObject json)
	{
		id = json.getInt(TAG_ID);
		label = json.getString(TAG_LABEL);
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setData(String fieldTag, Object dataValue)
	{
		if(TAG_LABEL.equals(fieldTag))
			label = (String)dataValue;
		else
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_LABEL.equals(fieldTag))
			return label;
		
		throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof ThreatRatingCriterion))
			return false;
		
		ThreatRatingCriterion other = (ThreatRatingCriterion)rawOther;
		return other.getId() == getId();
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_ID, getId());
		json.put(TAG_LABEL, getLabel());
		
		return json;
	}
	
	private static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	int id;
	String label;
}
