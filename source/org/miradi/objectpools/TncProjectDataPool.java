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
import org.miradi.objects.TncProjectData;
import org.miradi.project.ObjectManager;

public class TncProjectDataPool extends EAMNormalObjectPool
{
	public TncProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, TncProjectData.getObjectType());
	}
	
	public void put(TncProjectData tncProjectData)
	{
		put(tncProjectData.getId(), tncProjectData);
	}
	
	public TncProjectData find(BaseId id)
	{
		return (TncProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new TncProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
