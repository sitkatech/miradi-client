/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ProjectMetadata extends EAMBaseObject
{
	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
	}

	public ProjectMetadata(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.PROJECT_METADATA;
	}

}
