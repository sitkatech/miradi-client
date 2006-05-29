/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ProjectResource extends EAMObject
{
	public ProjectResource(int idToUse)
	{
		super(idToUse);
		initials = "";
		name = "";
		position = "";
	}
	
	public ProjectResource(JSONObject json)
	{
		super(json);
		initials = json.optString(TAG_INITIALS, "");
		name = json.optString(TAG_NAME, "");
		position = json.optString(TAG_POSITION);
	}

	public int getType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_INITIALS))
			return initials;
		if(fieldTag.equals(TAG_NAME))
			return name;
		if(fieldTag.equals(TAG_POSITION))
			return position;
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_INITIALS))
			initials = (String)dataValue;
		else if(fieldTag.equals(TAG_NAME))
			name = (String)dataValue;
		else if(fieldTag.equals(TAG_POSITION))
			position = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_INITIALS, initials);
		json.put(TAG_NAME, name);
		json.put(TAG_POSITION, position);
		return json;
	}
	
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_NAME = "Name";
	public static final String TAG_POSITION = "Position";

	String initials;
	String name;
	String position;
}
