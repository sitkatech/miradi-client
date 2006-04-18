/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

public class Task extends EAMObject
{
	public Task(int idToUse)
	{
		super(idToUse);
	}

	public int getType()
	{
		return ObjectType.TASK;
	}

}
