/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;

public class EAMObjectPool extends ObjectPool
{
	public EAMObjectPool(int objectTypeToStore)
	{
		objectType = objectTypeToStore;
	}
	
	public BaseObject findObject(BaseId id)
	{
		return (BaseObject)getRawObject(id);
	}
	
	public ORefList getORefList()
	{
		ORefList orefList = new ORefList();
		BaseId[] baseIds = getIds();
		for (int i=0; i<baseIds.length; ++i)
		{
			orefList.add(new ORef(getObjectType(),baseIds[i]));
		}
		return orefList;
	}

	
	public int getObjectType()
	{
		return objectType;
	}
	
	int objectType;
}
