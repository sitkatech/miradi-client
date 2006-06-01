/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.main.EAM;

public class ObjectId
{
	public ObjectId()
	{
		this(ID_NONE);
	}
	
	public ObjectId(int idToUse)
	{
		id = idToUse;
	}

	public String toString()
	{
		if(hasId())
			return String.valueOf(id).toString();
		return ID_NONE_STRING;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof ObjectId))
			return false;
		return ((ObjectId)obj).id == id;
	}

	public boolean hasId()
	{
		return id != ID_NONE;
	}

	public int getValue()
	{
		return id;
	}

	public void setValue(int value)
	{
		id = value;
	}

	public static final int ID_NONE = -1;
	public static final String ID_NONE_STRING = EAM.text("None");

	int id;
}
