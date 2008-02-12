/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;


public class BaseId implements Comparable
{
	public BaseId(String idAsString)
	{
		if(idAsString.length() == 0)
			id = IdAssigner.INVALID_ID;
		else
			id = Integer.parseInt(idAsString);
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
		return equals(INVALID);
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
	
	public static final BaseId INVALID = new BaseId(IdAssigner.INVALID_ID);

	private int id;

}
