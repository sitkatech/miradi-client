/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.views.interview.InterviewView;
import org.json.JSONObject;

public class ProjectInfo
{
	public ProjectInfo()
	{
		clear();
	}
	
	public void clear()
	{
		nodeIdAssigner = new IdAssigner(); 
		annotationIdAssigner = new IdAssigner();
		currentView = getDefaultCurrentView();
		projectData = new JSONObject();
		rootTaskId = BaseId.INVALID;
	}

	private String getDefaultCurrentView()
	{
		return InterviewView.getViewName();
	}
	
	public IdAssigner getNodeIdAssigner()
	{
		return nodeIdAssigner;
	}
	
	public BaseId obtainRealNodeId(BaseId proposedId)
	{
		return nodeIdAssigner.obtainRealId(proposedId);
	}
	
	public BaseId obtainRealLinkageId(BaseId proposedId)
	{
		return nodeIdAssigner.obtainRealId(proposedId);
	}

	public IdAssigner getAnnotationIdAssigner()
	{
		return annotationIdAssigner;
	}
	
	public String getCurrentView()
	{
		return currentView;
	}
	
	public void setCurrentView(String newCurrentView)
	{
		currentView = newCurrentView;
	}
	
	public BaseId getRootTaskId()
	{
		return rootTaskId;
	}
	
	public void setRootTaskId(BaseId newRootTaskId)
	{
		rootTaskId = newRootTaskId;
	}
	
	public JSONObject getProjectData()
	{
		return projectData;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_CURRENT_VIEW, currentView);
		json.put(TAG_HIGHEST_NODE_ID, nodeIdAssigner.getHighestAssignedId());
		json.put(TAG_HIGHEST_ANNOTATION_ID, annotationIdAssigner.getHighestAssignedId());
		json.put(TAG_ROOT_TASK_ID, rootTaskId.asInt());
		json.put(TAG_PROJECT_DATA, projectData);
		return json;
	}
	
	public void fillFrom(JSONObject copyFrom) throws NoSuchElementException, ParseException
	{
		clear();
		currentView = copyFrom.optString(TAG_CURRENT_VIEW, getDefaultCurrentView());
		nodeIdAssigner = new IdAssigner(copyFrom.optInt(TAG_HIGHEST_NODE_ID, IdAssigner.INVALID_ID));
		annotationIdAssigner = new IdAssigner(copyFrom.optInt(TAG_HIGHEST_ANNOTATION_ID, IdAssigner.INVALID_ID));
		rootTaskId = new BaseId(copyFrom.optInt(TAG_ROOT_TASK_ID, IdAssigner.INVALID_ID));
		JSONObject rawProjectData = copyFrom.optJSONObject(TAG_PROJECT_DATA);
		if(rawProjectData == null)
			rawProjectData = new JSONObject();
		projectData = new JSONObject(rawProjectData.toString());
	}
	
	static String TAG_CURRENT_VIEW = "CurrentView";
	static String TAG_HIGHEST_NODE_ID = "HighestUsedNodeId";
	static String TAG_HIGHEST_ANNOTATION_ID = "HighestUsedAnnotationId";
	static String TAG_ROOT_TASK_ID = "RootTaskId";
	static String TAG_PROJECT_DATA = "ProjectData";
	
	IdAssigner nodeIdAssigner;
	IdAssigner annotationIdAssigner;
	String currentView;
	BaseId rootTaskId;
	JSONObject projectData;

}
