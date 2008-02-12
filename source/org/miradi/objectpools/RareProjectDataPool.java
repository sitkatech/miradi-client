/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.RareProjectData;
import org.miradi.project.ObjectManager;

public class RareProjectDataPool extends EAMNormalObjectPool
{
	public RareProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.RARE_PROJECT_DATA);
	}
	
	public void put(RareProjectData rareProjectData)
	{
		put(rareProjectData.getId(), rareProjectData);
	}
	
	public RareProjectData find(BaseId id)
	{
		return (RareProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new RareProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
