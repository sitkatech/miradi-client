/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import org.json.JSONObject;

public class LinkageManifest extends Manifest
{
	public LinkageManifest()
	{
		super(ProjectServer.LINKAGE_MANIFEST);
	}
	
	public LinkageManifest(JSONObject copyFrom)
	{
		super(copyFrom);
		// TODO: Fail if wrong type
	}
	
}