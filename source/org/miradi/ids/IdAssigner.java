/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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