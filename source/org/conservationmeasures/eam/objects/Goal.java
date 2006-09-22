/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;


public class Goal extends EAMBaseObject 
{
	public Goal(BaseId idToUse)
	{
		super(idToUse);
		shortLabel = "";
		fullText = "";
	}
	
	public Goal(JSONObject json)
	{
		super(json);
		shortLabel = json.optString(TAG_SHORT_LABEL, "");
		fullText = json.optString(TAG_FULL_TEXT, "");
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return shortLabel + ": " + getLabel();
	}

	public int getType()
	{
		return ObjectType.GOAL;
	}

	public String getShortLabel()
	{
		return shortLabel;
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
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_SHORT_LABEL, shortLabel);
		json.put(TAG_FULL_TEXT, fullText);
		
		return json;
	}
	
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";

	
	String shortLabel;
	String fullText;
}
