/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.EAMObject;

public class EAMObjectPool extends ObjectPool
{
	public EAMObjectPool(int objectTypeToStore)
	{
		objectType = objectTypeToStore;
	}
	
	public EAMObject findObject(BaseId id)
	{
		return (EAMObject)getRawObject(id);
	}
	
	public int getObjectType()
	{
		return objectType;
	}
	
	int objectType;
}
