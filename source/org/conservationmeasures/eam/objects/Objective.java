/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.json.JSONObject;



public class Objective extends EAMObject
{
	public Objective(int id)
	{
		super(id);
	}
	
	public Objective(JSONObject json)
	{
		super(json);
	}
	
	public int getType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	public String getShortLabel()
	{
		return "xxx";
	}
}
