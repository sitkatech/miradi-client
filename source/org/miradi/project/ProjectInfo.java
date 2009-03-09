/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.utils.EnhancedJsonObject;

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
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
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
