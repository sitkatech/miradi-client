/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;



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