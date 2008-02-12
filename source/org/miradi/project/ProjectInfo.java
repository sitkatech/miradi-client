/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;

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
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_HIGHEST_FACTOR_OR_LINK_ID, normalObjectIdAssigner.getHighestAssignedId());
		json.put(TAG_HIGHEST_NORMAL_ID, normalObjectIdAssigner.getHighestAssignedId());
		json.put(TAG_PROJECT_METADATA_ID, metadataId.asInt());
		return json;
	}
	
	public void fillFrom(JSONObject copyFrom) throws NoSuchElementException, ParseException
	{
		clear();
		normalObjectIdAssigner.idTaken(new BaseId(copyFrom.optInt(TAG_HIGHEST_FACTOR_OR_LINK_ID, IdAssigner.INVALID_ID)));
		normalObjectIdAssigner.idTaken(new BaseId(copyFrom.optInt(TAG_HIGHEST_NORMAL_ID, IdAssigner.INVALID_ID)));
		metadataId = new BaseId(copyFrom.optInt(TAG_PROJECT_METADATA_ID, -1));
	}
	
	static String TAG_HIGHEST_FACTOR_OR_LINK_ID = "HighestUsedNodeId";
	static String TAG_HIGHEST_NORMAL_ID = "HighestUsedAnnotationId";
	static String TAG_PROJECT_METADATA_ID = "ProjectMetadataId";
	
	IdAssigner normalObjectIdAssigner;
	BaseId metadataId;
}
