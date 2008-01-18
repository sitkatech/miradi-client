/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;

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
