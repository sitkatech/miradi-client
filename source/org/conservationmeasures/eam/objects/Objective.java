/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.project.IdAssigner;
import org.json.JSONObject;



public class Objective extends EAMObject
{
	public Objective(int id)
	{
		super(id);
		shortLabel = "";
	}
	
	public Objective(JSONObject json)
	{
		super(json);
		shortLabel = json.optString(TAG_SHORT_LABEL, "");
	}
	
	public int getType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			return getShortLabel();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			shortLabel = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getShortLabel()
	{
		return shortLabel;
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_SHORT_LABEL, shortLabel);
		
		return json;
	}
	
	public String toString()
	{
		if(getId() == IdAssigner.INVALID_ID)
			return "(None)";
		return shortLabel + ": " + getLabel();
	}

	
	public final static String TAG_SHORT_LABEL = "ShortLabel";

	String shortLabel;
}
