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


public class BaseId implements Comparable<BaseId>
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
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof BaseId))
			return false;
		
		return (compareTo((BaseId)other) == 0);
	}
	
	@Override
	public int hashCode()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(id);
	}

	public int compareTo(BaseId other)
	{
		int otherId = other.asInt();
		if(id < otherId)
			return -1;
		if(id > otherId)
			return 1;
		return  0;
	}
	
	public static final BaseId INVALID = new BaseId(IdAssigner.INVALID_ID);

	private int id;

}
