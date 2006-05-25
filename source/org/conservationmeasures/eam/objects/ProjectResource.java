/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ProjectResource extends EAMObject
{
	public ProjectResource(int idToUse)
	{
		super(idToUse);
	}
	
	public ProjectResource(JSONObject json)
	{
		super(json);
	}

	public int getType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}

}
