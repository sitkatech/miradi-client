/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.utils;

import org.miradi.objects.BaseObject;

//NOTE: This class places blank strings at the end of the list.
// if two object's dates are equal,  then the object id is used
public class BaseObjectFieldComparator
{
	public static int compare(BaseObject rawObject1, BaseObject rawObject2, String tag)
	{
		String safeDate1 = rawObject1.getData(tag);
		String safeDate2 = rawObject2.getData(tag);
		
		if (safeDate2.equals(safeDate1))
			return rawObject2.getId().compareTo(rawObject1.getId());
		
		if (safeDate1.length() == 0)
			return 1;
		
		if (safeDate2.length() == 0)
			return -1;
		
		return  safeDate2.compareTo(safeDate1);
	}
}