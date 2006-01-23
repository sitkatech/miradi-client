/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import org.json.JSONObject;

public class NodeManifest extends Manifest
{
	public NodeManifest()
	{
		super(ProjectServer.NODE_MANIFEST);
	}

	public NodeManifest(JSONObject copyFrom)
	{
		super(copyFrom);
		// TODO: Fail if wrong type
	}

}
