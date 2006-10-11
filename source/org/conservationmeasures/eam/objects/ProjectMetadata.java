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
		startDate = createFromIsoStringLenient(json.optString(TAG_START_DATE));
		effectiveDate = createFromIsoStringLenient(json.optString(TAG_DATA_EFFECTIVE_DATE));
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
		return convertMultiCalendarToIsoString(startDate);
	}
	
	public String getEffectiveDate()
	{
		return convertMultiCalendarToIsoString(effectiveDate);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			projectName = (String)dataValue;
		else if(TAG_START_DATE.equals(fieldTag))
			startDate = createFromIsoStringStrict((String)dataValue);
		else if(TAG_DATA_EFFECTIVE_DATE.equals(fieldTag))
			effectiveDate = createFromIsoStringStrict((String)dataValue);
		else
			super.setData(fieldTag, dataValue);
	}

	public String getData(String fieldTag)
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			return getProjectName();
		if(TAG_START_DATE.equals(fieldTag))
			return getStartDate();
		if(TAG_DATA_EFFECTIVE_DATE.equals(fieldTag))
			return getEffectiveDate();
		
		return super.getData(fieldTag);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_PROJECT_NAME, projectName);
		json.put(TAG_START_DATE, getStartDate());
		json.put(TAG_DATA_EFFECTIVE_DATE, getEffectiveDate());
		return json;
	}
	
	private String convertMultiCalendarToIsoString(MultiCalendar cal)
	{
		if(cal == null)
			return "";
		return cal.toIsoDateString();
	}
	
	private MultiCalendar createFromIsoStringLenient(String isoDate)
	{
		try
		{
			return createFromIsoStringStrict(isoDate);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	private MultiCalendar createFromIsoStringStrict(String iso) throws InvalidDateException
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
	public static final String TAG_DATA_EFFECTIVE_DATE = "DataEffectiveDate";

	String projectName;
	MultiCalendar startDate;
	MultiCalendar effectiveDate;
}
