/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.project.ObjectManager;

public class FosProjectDataPool extends EAMNormalObjectPool
{
	public FosProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, FosProjectData.getObjectType());
	}
	
	public void put(FosProjectData fosProjectData)
	{
		put(fosProjectData.getId(), fosProjectData);
	}
	
	public FosProjectData find(BaseId id)
	{
		return (FosProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new FosProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
