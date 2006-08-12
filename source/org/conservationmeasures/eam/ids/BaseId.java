/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ids;

import org.conservationmeasures.eam.project.IdAssigner;

public class BaseId implements Comparable
{
	public BaseId()
	{
		this(IdAssigner.INVALID_ID);
	}
	
	public BaseId(int idToUse)
	{
		id = idToUse;
	}
	
	public int asInt()
	{
		return id;
	}
	
	public boolean isInvalid()
	{
		return (id == IdAssigner.INVALID_ID);
	}
	
	public boolean equals(Object other)
	{
		if(!(other instanceof BaseId))
			return false;
		
		return (compareTo(other) == 0);
	}
	
	public int hashCode()
	{
		return id;
	}
	
	public String toString()
	{
		return Integer.toString(id);
	}

	public int compareTo(Object other)
	{
		int otherId = ((BaseId)other).asInt();
		if(id < otherId)
			return -1;
		if(id > otherId)
			return 1;
		return  0;
	}

	private int id;

}
