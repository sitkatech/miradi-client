/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.json.JSONObject;
import org.martus.util.MultiCalendar;

public class ProjectMetadata extends EAMBaseObject
{
	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
		projectName = "";
		startDate = null;
	}

	public ProjectMetadata(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
		projectName = json.getString(TAG_PROJECT_NAME);
		try
		{
			startDate = createFromIsoDate(json.optString(TAG_START_DATE));
		}
		catch (Exception e)
		{
			startDate = null;
		}
	}

	public int getType()
	{
		return ObjectType.PROJECT_METADATA;
	}
	
	public String getProjectName()
	{
		return projectName;
	}
	
	public String getStartDate()
	{
		if(startDate == null)
			return "";
		return startDate.toIsoDateString();
	}
	
	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			projectName = (String)dataValue;
		else if(TAG_START_DATE.equals(fieldTag))
			startDate = createFromIsoDate((String)dataValue);
		else
			super.setData(fieldTag, dataValue);
	}

	public String getData(String fieldTag)
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			return getProjectName();
		if(TAG_START_DATE.equals(fieldTag))
			return getStartDate();
		
		return super.getData(fieldTag);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_PROJECT_NAME, projectName);
		json.put(TAG_START_DATE, startDate);
		return json;
	}
	
	private MultiCalendar createFromIsoDate(String iso) throws InvalidDateException
	{
		try
		{
			return MultiCalendar.createFromIsoDateString(iso);
		}
		catch (RuntimeException e)
		{
			throw new InvalidDateException(e);
		}
	}

	public static final String TAG_PROJECT_NAME = "ProjectName";
	public static final String TAG_START_DATE = "StartDate";

	String projectName;
	MultiCalendar startDate;
}
