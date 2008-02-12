/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.database;

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
