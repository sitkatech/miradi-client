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
	}
	
	EAMObject(JSONObject json)
	{
		id = json.getInt(TAG_ID);
	}
	
	public static EAMObject createFromJson(int type, JSONObject json)
	{
		switch(type)
		{
			case ObjectType.THREAT_RATING_CRITERION:
				return new ThreatRatingCriterion(json);
				
			case ObjectType.THREAT_RATING_VALUE_OPTION:
				return new ThreatRatingValueOption(json);
			
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}
	
	abstract public int getType();

	public int getId()
	{
		return id;
	}

	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_ID, getId());
		
		return json;
	}
	
	protected static final String TAG_ID = "Id";

	private int id;
}
