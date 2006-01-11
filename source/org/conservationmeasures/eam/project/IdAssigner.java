/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;


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
	
	public int takeNextId()
	{
		return nextId++;
	}
	
	public void idTaken(int id)
	{
		if(id >= nextId)
			nextId = id + 1;
	}
	
	public int obtainRealId(int id)
	{
		if(id == IdAssigner.INVALID_ID)
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