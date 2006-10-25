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
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectMetadata extends EAMBaseObject
{
	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}

	public ProjectMetadata(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
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

	void clear()
	{
		super.clear();
		projectName = new StringData();
		projectScope = new StringData();
		projectVision = new StringData();
		startDate = new DateData();
		effectiveDate = new DateData();
		sizeInHectares = new NumberData();
		teamResourceIds = new IdListData();
		
		addField(TAG_PROJECT_NAME, projectName);
		addField(TAG_PROJECT_SCOPE, projectScope);
		addField(TAG_PROJECT_VISION, projectVision);
		addField(TAG_START_DATE, startDate);
		addField(TAG_DATA_EFFECTIVE_DATE, effectiveDate);
		addField(TAG_SIZE_IN_HECTARES, sizeInHectares);
		addField(TAG_TEAM_RESOURCE_IDS, teamResourceIds);
		
		
		
		tncLessonsLearned = new StringData();
		
		addField(TAG_TNC_LESSONS_LEARNED, tncLessonsLearned);
	}

	public static final String TAG_PROJECT_NAME = "ProjectName";
	public static final String TAG_PROJECT_SCOPE = "ProjectScope";
	public static final String TAG_PROJECT_VISION = "ProjectVision";
	public static final String TAG_START_DATE = "StartDate";
	public static final String TAG_DATA_EFFECTIVE_DATE = "DataEffectiveDate";
	public static final String TAG_SIZE_IN_HECTARES = "SizeInHectares";
	public static final String TAG_TEAM_RESOURCE_IDS = "TeamResourceIds";
	public static final String TAG_TNC_LESSONS_LEARNED = "TNC.LessonsLearned";

	StringData projectName;
	StringData projectScope;
	StringData projectVision;
	DateData startDate;
	DateData effectiveDate;
	NumberData sizeInHectares;
	IdListData teamResourceIds;
	StringData tncLessonsLearned;
}
