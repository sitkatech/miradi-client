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
import org.miradi.project.ObjectManager;

abstract public class EAMNormalObjectPool extends PoolWithIdAssigner
{
	public EAMNormalObjectPool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(objectTypeToStore, idAssignerToUse);
	}
	
	public BaseObject createObject(ObjectManager objectManager, BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId actualId = idAssigner.obtainRealId(objectId);
		BaseObject created = createRawObject(objectManager, actualId, extraInfo);
		put(created.getId(), created);
		return created;
	}
	
	abstract BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception;
}
