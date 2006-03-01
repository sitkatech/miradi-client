/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ThreatRatingCriterion extends EAMObject
{
	public ThreatRatingCriterion(int idToUse)
	{
		super(idToUse);
		label = "Unknown";
	}
	
	public ThreatRatingCriterion(JSONObject json)
	{
		super(json);
		label = json.getString(TAG_LABEL);
	}
	
	public int getType()
	{
		return ObjectType.THREAT_RATING_CRITERION;
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
		JSONObject json = super.toJson();
		json.put(TAG_LABEL, getLabel());

		return json;
	}
	
	public static final String TAG_LABEL = "Label";
	
	String label;
}
