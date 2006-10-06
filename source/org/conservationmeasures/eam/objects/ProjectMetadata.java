/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ProjectMetadata extends EAMBaseObject
{
	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
		projectName = "";
	}

	public ProjectMetadata(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
		projectName = json.getString(TAG_PROJECT_NAME);
	}

	public int getType()
	{
		return ObjectType.PROJECT_METADATA;
	}
	
	public String getProjectName()
	{
		return projectName;
	}
	
	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			projectName = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}

	public String getData(String fieldTag)
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			return getProjectName();
		
		return super.getData(fieldTag);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_PROJECT_NAME, projectName);
		return json;
	}

	public static final String TAG_PROJECT_NAME = "ProjectName";

	String projectName;

}
