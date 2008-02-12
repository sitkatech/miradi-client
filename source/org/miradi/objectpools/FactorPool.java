/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;

public class FactorPool extends PoolWithIdAssigner
{
	public FactorPool(IdAssigner idAssignerToUse)
	{
		super(ObjectType.FACTOR, idAssignerToUse);
	}
	
	public void put(Factor node)
	{
		put(node.getFactorId(), node);
	}
	
	public Factor find(FactorId id)
	{
		return (Factor)getRawObject(id);
	}
	
	public void remove(FactorId id)
	{
		super.remove(id);
	}	
}
