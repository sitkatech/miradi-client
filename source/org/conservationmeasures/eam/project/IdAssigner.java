/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;


public class IdAssigner
{
	public IdAssigner()
	{
	}
	
	public IdAssigner(int highestIdAlreadyUsed)
	{
		nextId = highestIdAlreadyUsed + 1;
	}
	
	public void clear()
	{
		nextId = 0;
	}
	
	public BaseId takeNextId()
	{
		return new BaseId(nextId++);
	}
	
	public void idTaken(BaseId id)
	{
		if(id.asInt() >= nextId)
			nextId = id.asInt() + 1;
	}
	
	public BaseId obtainRealId(BaseId id)
	{
		if(id.isInvalid())
			id = takeNextId();
		else
			idTaken(id);

		return id;
	}
	
	public int getHighestAssignedId()
	{
		return (nextId - 1);
	}
	
	private int nextId;
	public static final int INVALID_ID = -1;
}