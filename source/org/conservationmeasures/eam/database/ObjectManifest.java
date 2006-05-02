/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import org.json.JSONObject;

public class ObjectManifest extends Manifest
{
	public ObjectManifest()
	{
		super(ProjectServer.OBJECT_MANIFEST);
	}
	
	public ObjectManifest(JSONObject copyFrom)
	{
		super(copyFrom);
		// TODO: Fail if wrong type
	}

}
