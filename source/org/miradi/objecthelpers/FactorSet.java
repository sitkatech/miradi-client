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
package org.miradi.objecthelpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.miradi.objects.Factor;

public class FactorSet extends HashSet<Factor>
{
	public void attemptToAdd(Factor node)
	{
		if(isLegal(node))
		{
			super.add(node);
		}
	}
	
	public void attemptToAddAll(FactorSet nodesToAdd)
	{
		attemptToAddAll(nodesToAdd.toFactorArray());
	}
	
	public void attemptToAddAll(Factor[] nodesToAdd)
	{
		for(int i = 0; i < nodesToAdd.length; ++i)
			attemptToAdd(nodesToAdd[i]);
	}
	
	
	public boolean isLegal(Factor node)
	{
		return true;
	}

	public Factor[] toFactorArray()
	{
		return toArray(new Factor[0]);
	}
	
	public ORefSet getFactorRefs()
	{
		ORefSet factorRefs = new ORefSet();
		Iterator factors = iterator();
		while (factors.hasNext())
		{
			Factor factor = (Factor) factors.next();
			factorRefs.add(factor.getRef());
		}
		
		return factorRefs;
	}
	
	@Override
	public boolean add(Factor o)
	{
		throw new RuntimeException("Must use attemptToAdd instead of add");
	}

	@Override
	public boolean addAll(Collection c)
	{
		throw new RuntimeException("Must use attemptToAdd instead of addAll");
	}

	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		result.append("[");
		Iterator iter = iterator();
		while(iter.hasNext())
		{
			Factor node = (Factor)iter.next();
			result.append(node.getId());
			result.append(",");
		}
		result.append("]");
		return result.toString();
	}
}
