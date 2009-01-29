/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.util.Arrays;
import java.util.HashSet;

import org.miradi.objects.BaseObject;

public class ORefSet extends HashSet<ORef>
{
	public ORefSet()
	{
		super();
	}

	public ORefSet(ORef ref)
	{
		this();
		add(ref);
	}

	public ORefSet(ORefList refList)
	{
		this();
		addAll(Arrays.asList(refList.toArray()));
	}
	
	public ORefSet(BaseObject[] baseObjects)
	{
		this(new ORefList(baseObjects));
	}

	public ORefSet(ORefSet other)
	{
		super(other);
	}

	public void addAllRefs(ORefList refs)
	{
		addAll(Arrays.asList(refs.toArray()));
	}

	public static ORefSet subtract(ORefSet newReferrals, ORefSet oldReferrals)
	{
		ORefSet result = new ORefSet();
		for(ORef ref : newReferrals)
		{
			if(!oldReferrals.contains(ref))
				result.add(ref);
		}

		return result;
	}

	public boolean containsAny(ORefSet other)
	{
		for(ORef ref : other)
		{
			if(contains(ref))
				return true;
		}
		
		return false;
	}

	public ORefList toRefList()
	{
		return new ORefList(toArray(new ORef[0]));
	}
}
