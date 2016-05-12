/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import java.util.Comparator;
import java.util.HashMap;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;

public class RawPool extends HashMap<ORef, RawObject>
{
	public RawObject findObject(ORef ref)
	{
		return get(ref);
	}
	
	public RawObject createObject(ORef ref)
	{
		RawObject newObject = new RawObject(ref);
		put(ref, newObject);
		
		return newObject;
	}
	
	public void deleteObject(ORef ref)
	{
		remove(ref);
	}
	
	public ORefList getSortedReflist()
	{
		ORefList refList = new ORefSet(keySet()).toRefList();
		refList.sort(new ORefComparator());
		
		return refList;
	}

	private class ORefComparator implements Comparator<ORef>
	{
		public int compare(ORef ref1, ORef ref2)
		{
			if(ref1 == null && ref2 == null)
				return 0;
			if(ref1 == null)
				return -1;
			if(ref2 == null)
				return 1;

			if (ref1.getObjectType() < ref2.getObjectType())
				return -1;
			if (ref1.getObjectType() > ref2.getObjectType())
				return 1;

			return ref1.getObjectId().compareTo(ref2.getObjectId());
		}
	}

}
