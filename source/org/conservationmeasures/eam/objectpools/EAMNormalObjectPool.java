/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.ObjectManager;

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
