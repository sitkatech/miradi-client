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
		fullText = "";
	}
	
	public Objective(JSONObject json)
	{
		super(json);
		shortLabel = json.optString(TAG_SHORT_LABEL, "");
		fullText = json.optString(TAG_FULL_TEXT, "");
	}
	
	public int getType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			return getShortLabel();
		if(fieldTag.equals(TAG_FULL_TEXT))
			return fullText;
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			shortLabel = (String)dataValue;
		else if(fieldTag.equals(TAG_FULL_TEXT))
			fullText = (String)dataValue;
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
		json.put(TAG_FULL_TEXT, fullText);
		
		return json;
	}
	
	public String toString()
	{
		if(getId() == IdAssigner.INVALID_ID)
			return "(None)";
		return shortLabel + ": " + getLabel();
	}

	
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";

	String shortLabel;
	String fullText;
}
