/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

public class IdAssigner
{
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
	
	private int nextId;
}