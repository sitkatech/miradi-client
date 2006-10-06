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
import org.conservationmeasures.eam.ids.ModelNodeId;
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
		metadataId = BaseId.INVALID;
	}
	
	public void setMetadataId(BaseId newMetadataId)
	{
		metadataId = newMetadataId;
	}
	
	public BaseId getMetadataId()
	{
		return metadataId;
	}

	private String getDefaultCurrentView()
	{
		return InterviewView.getViewName();
	}
	
	public IdAssigner getNodeIdAssigner()
	{
		return nodeIdAssigner;
	}
	
	public ModelNodeId obtainRealNodeId(BaseId proposedId)
	{
		return new ModelNodeId(nodeIdAssigner.obtainRealId(proposedId).asInt());
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
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_CURRENT_VIEW, currentView);
		json.put(TAG_HIGHEST_NODE_ID, nodeIdAssigner.getHighestAssignedId());
		json.put(TAG_HIGHEST_ANNOTATION_ID, annotationIdAssigner.getHighestAssignedId());
		json.put(TAG_PROJECT_METADATA_ID, metadataId.asInt());
		return json;
	}
	
	public void fillFrom(JSONObject copyFrom) throws NoSuchElementException, ParseException
	{
		clear();
		currentView = copyFrom.optString(TAG_CURRENT_VIEW, getDefaultCurrentView());
		nodeIdAssigner = new IdAssigner(copyFrom.optInt(TAG_HIGHEST_NODE_ID, IdAssigner.INVALID_ID));
		annotationIdAssigner = new IdAssigner(copyFrom.optInt(TAG_HIGHEST_ANNOTATION_ID, IdAssigner.INVALID_ID));
		metadataId = new BaseId(copyFrom.optInt(TAG_PROJECT_METADATA_ID, -1));
	}
	
	static String TAG_CURRENT_VIEW = "CurrentView";
	static String TAG_HIGHEST_NODE_ID = "HighestUsedNodeId";
	static String TAG_HIGHEST_ANNOTATION_ID = "HighestUsedAnnotationId";
	static String TAG_PROJECT_METADATA_ID = "ProjectMetadataId";
	
	IdAssigner nodeIdAssigner;
	IdAssigner annotationIdAssigner;
	String currentView;
	BaseId metadataId;
}
