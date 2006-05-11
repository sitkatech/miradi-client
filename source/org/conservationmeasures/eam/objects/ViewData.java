/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ViewData extends EAMObject
{
	public ViewData(int idToUse)
	{
		super(idToUse);
	}
	
	public ViewData(JSONObject json)
	{
		super(json);
	}

	public int getType()
	{
		return ObjectType.VIEW_DATA;
	}

}
