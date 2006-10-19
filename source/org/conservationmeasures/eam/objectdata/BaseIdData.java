/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.ids.BaseId;

public class BaseIdData extends ObjectData
{
	public BaseIdData()
	{
		id = BaseId.INVALID;
	}
	
	public BaseIdData(String data) throws Exception
	{
		set(data);
	}
	
	public String get()
	{
		return id.toString();
	}
	
	public BaseId getId()
	{
		return id;
	}
	
	public void setId(BaseId newId)
	{
		id = newId;
	}

	public void set(String newValue) throws Exception
	{
		id = new BaseId(Integer.parseInt(newValue));
	}

	BaseId id;
}
