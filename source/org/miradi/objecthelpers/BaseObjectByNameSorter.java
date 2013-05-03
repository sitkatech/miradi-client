/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import java.util.Comparator;

import org.miradi.objects.BaseObject;

public class BaseObjectByNameSorter implements Comparator<BaseObject>
{
	public int compare(BaseObject baseObject1, BaseObject baseObject2)
	{
		if(baseObject1 == baseObject2)
			return 0;
		
		if(baseObject1 == null)
			return -1;
		
		if(baseObject2 == null)
			return 1;
		
		String name1 = baseObject1.getLabel();
		String name2 = baseObject2.getLabel();
		return name1.compareToIgnoreCase(name2);
	}

}
