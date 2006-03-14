/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.conservationmeasures.eam.views.noproject.NoProjectView;
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
		currentView = NoProjectView.getViewName();
		projectData = new JSONObject();

	}
	
	public IdAssigner getNodeIdAssigner()
	{
		return nodeIdAssigner;
	}
	
	public int obtainRealNodeId(int proposedId)
	{
		return nodeIdAssigner.obtainRealId(proposedId);
	}
	
	public int obtainRealLinkageId(int proposedId)
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
		json.put(TAG_PROJECT_DATA, projectData);
		return json;
	}
	
	public void fillFrom(JSONObject copyFrom) throws NoSuchElementException, ParseException
	{
		clear();
		currentView = copyFrom.getString(TAG_CURRENT_VIEW);
		nodeIdAssigner = new IdAssigner(copyFrom.getInt(TAG_HIGHEST_NODE_ID));
		annotationIdAssigner = new IdAssigner(copyFrom.getInt(TAG_HIGHEST_ANNOTATION_ID));
		projectData = new JSONObject(copyFrom.get(TAG_PROJECT_DATA).toString());
	}
	
	static String TAG_CURRENT_VIEW = "CurrentView";
	static String TAG_HIGHEST_NODE_ID = "HighestUsedNodeId";
	static String TAG_HIGHEST_ANNOTATION_ID = "HighestUsedAnnotationId";
	static String TAG_PROJECT_DATA = "ProjectData";
	
	IdAssigner nodeIdAssigner;
	IdAssigner annotationIdAssigner;
	String currentView;
	JSONObject projectData;

}
