/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.json.JSONObject;

public class ProjectInfo
{
	public ProjectInfo()
	{
		normalObjectIdAssigner = new IdAssigner();
		clear();
	}
	
	public void clear()
	{
		normalObjectIdAssigner.clear();
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
		return SummaryView.getViewName();
	}
	
	public IdAssigner getFactorAndLinkIdAssigner()
	{
		return getNormalIdAssigner();
	}
	
	public FactorId obtainRealFactorId(BaseId proposedId)
	{
		return new FactorId(normalObjectIdAssigner.obtainRealId(proposedId).asInt());
	}
	
	public FactorLinkId obtainRealLinkId(BaseId proposedId)
	{
		return new FactorLinkId(normalObjectIdAssigner.obtainRealId(proposedId).asInt());
	}

	public IdAssigner getNormalIdAssigner()
	{
		return normalObjectIdAssigner;
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
		json.put(TAG_HIGHEST_FACTOR_OR_LINK_ID, normalObjectIdAssigner.getHighestAssignedId());
		json.put(TAG_HIGHEST_NORMAL_ID, normalObjectIdAssigner.getHighestAssignedId());
		json.put(TAG_PROJECT_METADATA_ID, metadataId.asInt());
		return json;
	}
	
	public void fillFrom(JSONObject copyFrom) throws NoSuchElementException, ParseException
	{
		clear();
		currentView = copyFrom.optString(TAG_CURRENT_VIEW, getDefaultCurrentView());
		normalObjectIdAssigner.idTaken(new BaseId(copyFrom.optInt(TAG_HIGHEST_FACTOR_OR_LINK_ID, IdAssigner.INVALID_ID)));
		normalObjectIdAssigner.idTaken(new BaseId(copyFrom.optInt(TAG_HIGHEST_NORMAL_ID, IdAssigner.INVALID_ID)));
		metadataId = new BaseId(copyFrom.optInt(TAG_PROJECT_METADATA_ID, -1));
	}
	
	static String TAG_CURRENT_VIEW = "CurrentView";
	static String TAG_HIGHEST_FACTOR_OR_LINK_ID = "HighestUsedNodeId";
	static String TAG_HIGHEST_NORMAL_ID = "HighestUsedAnnotationId";
	static String TAG_PROJECT_METADATA_ID = "ProjectMetadataId";
	
	IdAssigner normalObjectIdAssigner;
	String currentView;
	BaseId metadataId;
}
