/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.project.IdAssigner;
import org.json.JSONObject;

public class Indicator extends EAMObject
{
	public Indicator(int idToUse)
	{
		super(idToUse);
		identifier = "";
	}
	
	public Indicator(JSONObject json)
	{
		super(json);
		identifier = json.optString(TAG_IDENTIFIER, "");
	}
	
	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_IDENTIFIER))
			return getIdentifier();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_IDENTIFIER))
			identifier = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getIdentifier()
	{
		return identifier;
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_IDENTIFIER, identifier);
		
		return json;
	}
	
	public String toString()
	{
		if(getId() == IdAssigner.INVALID_ID)
			return "(None)";
		return identifier + ": " + getLabel();
	}

	public static final String TAG_IDENTIFIER = "Identifier";

	String identifier;

}
