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
package org.miradi.objectdata.pseudodata;

import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.StringData;
import org.miradi.objects.BaseObject;

public class PseudoTimestampData extends ObjectData
{
	public PseudoTimestampData(BaseObject owningObject, String tag)
	{
		super(tag);
		owner = owningObject;
	}

	public boolean isPseudoField()
	{
		return true;
	}
	
	public void set(String newValue) throws Exception
	{
		if (newValue.length()!=0)
			throw new RuntimeException("Set not allowed in a pseuod field");
	}

	public String get()
	{
		return owner.getPseudoData(getTag());
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringData))
			return false;
		
		StringData other = (StringData)rawOther;
		return get().equals(other.get());
	}

	public int hashCode()
	{
		return get().hashCode();
	}

	private BaseObject owner;
}
