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
import org.miradi.objects.SubTarget;
import org.miradi.project.ObjectManager;

public class SubTargetPool extends EAMNormalObjectPool
{
	public SubTargetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.SUB_TARGET);
	}
	
	public void put(SubTarget subTarget)
	{
		put(subTarget.getId(), subTarget);
	}
	
	public SubTarget find(BaseId id)
	{
		return (SubTarget) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new SubTarget(objectManager, actualId);
	}
}
