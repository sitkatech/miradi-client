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
package org.miradi.objectpools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.project.ObjectManager;

public class CausePool extends EAMNormalObjectPool
{
	public CausePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.CAUSE);
	}
	
	public void put(Cause cause) throws Exception
	{
		put(cause.getId(), cause);
	}
	
	public Cause find(BaseId id)
	{
		return (Cause)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Cause(objectManager ,new FactorId(actualId.asInt()));
	}
	
	public FactorId[] getModelNodeIds()
	{
		return (FactorId[])new HashSet(getRawIds()).toArray(new FactorId[0]);
	}
	
	public Cause[] getDirectThreats()
	{
		Vector<Cause> cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			Cause cause = (Cause)getRawObject(ids[i]);
			if(cause.isDirectThreat())
				cmNodes.add(cause);
		}
		return cmNodes.toArray(new Cause[0]);
	}
}
