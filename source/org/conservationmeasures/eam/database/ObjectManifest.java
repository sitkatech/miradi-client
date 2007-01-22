/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
