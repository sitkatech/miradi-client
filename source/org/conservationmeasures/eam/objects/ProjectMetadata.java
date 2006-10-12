/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.NumberData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ProjectMetadata extends EAMBaseObject
{
	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
		projectName = new StringData();
		projectScope = new StringData();
		projectVision = new StringData();
		startDate = new DateData();
		effectiveDate = new DateData();
		sizeInHectares = new NumberData();
		teamResourceIds = new IdListData();
	}

	public ProjectMetadata(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
		projectName = new StringData(json.getString(TAG_PROJECT_NAME));
		projectScope = new StringData(json.optString(TAG_PROJECT_SCOPE));
		projectVision = new StringData(json.optString(TAG_PROJECT_VISION));
		startDate = new DateData(json.optString(TAG_START_DATE));
		effectiveDate = new DateData(json.optString(TAG_DATA_EFFECTIVE_DATE));
		sizeInHectares = new NumberData(json.optString(TAG_SIZE_IN_HECTARES));
		teamResourceIds = new IdListData(json.optString(TAG_TEAM_RESOURCE_IDS));
	}
	
	public int getType()
	{
		return ObjectType.PROJECT_METADATA;
	}
	
	public String getProjectName()
	{
		return projectName.get();
	}
	
	public String getProjectScope()
	{
		return projectScope.get();
	}
	
	public String getProjectVision()
	{
		return projectVision.get();
	}
	
	public String getStartDate()
	{
		return startDate.get();
	}
	
	public String getEffectiveDate()
	{
		return effectiveDate.get();
	}
	
	public String getSizeInHectares()
	{
		return sizeInHectares.get();
	}
	
	public String getTeamResourceIds()
	{
		return teamResourceIds.get();
	}
	
	public IdList getTeamResourceIdList()
	{
		return teamResourceIds.getIdList();
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			projectName.set(dataValue);
		else if(TAG_PROJECT_SCOPE.equals(fieldTag))
			projectScope.set(dataValue);
		else if(TAG_PROJECT_VISION.equals(fieldTag))
			projectVision.set(dataValue);
		else if(TAG_START_DATE.equals(fieldTag))
			startDate.set(dataValue);
		else if(TAG_DATA_EFFECTIVE_DATE.equals(fieldTag))
			effectiveDate.set(dataValue);
		else if(TAG_SIZE_IN_HECTARES.equals(fieldTag))
			sizeInHectares.set(dataValue);
		else if(TAG_TEAM_RESOURCE_IDS.equals(fieldTag))
			teamResourceIds.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}

	public String getData(String fieldTag)
	{
		if(TAG_PROJECT_NAME.equals(fieldTag))
			return getProjectName();
		if(TAG_PROJECT_SCOPE.equals(fieldTag))
			return getProjectScope();
		if(TAG_PROJECT_VISION.equals(fieldTag))
			return getProjectVision();
		if(TAG_START_DATE.equals(fieldTag))
			return startDate.get();
		if(TAG_DATA_EFFECTIVE_DATE.equals(fieldTag))
			return effectiveDate.get();
		if(TAG_SIZE_IN_HECTARES.equals(fieldTag))
			return sizeInHectares.get();
		if(TAG_TEAM_RESOURCE_IDS.equals(fieldTag))
			return teamResourceIds.get();
		
		return super.getData(fieldTag);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_PROJECT_NAME, projectName.get());
		json.put(TAG_PROJECT_SCOPE, projectScope.get());
		json.put(TAG_PROJECT_VISION, projectVision.get());
		json.put(TAG_START_DATE, getStartDate());
		json.put(TAG_DATA_EFFECTIVE_DATE, getEffectiveDate());
		json.put(TAG_SIZE_IN_HECTARES, sizeInHectares.get());
		json.put(TAG_TEAM_RESOURCE_IDS, teamResourceIds.get());
		return json;
	}
	
	public static final String TAG_PROJECT_NAME = "ProjectName";
	public static final String TAG_PROJECT_SCOPE = "ProjectScope";
	public static final String TAG_PROJECT_VISION = "ProjectVision";
	public static final String TAG_START_DATE = "StartDate";
	public static final String TAG_DATA_EFFECTIVE_DATE = "DataEffectiveDate";
	public static final String TAG_SIZE_IN_HECTARES = "SizeInHectares";
	public static final String TAG_TEAM_RESOURCE_IDS = "TeamResourceIds";

	StringData projectName;
	StringData projectScope;
	StringData projectVision;
	DateData startDate;
	DateData effectiveDate;
	NumberData sizeInHectares;
	IdListData teamResourceIds;
}
