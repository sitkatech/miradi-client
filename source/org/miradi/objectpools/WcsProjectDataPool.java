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
import org.miradi.objects.WcsProjectData;
import org.miradi.project.ObjectManager;

public class WcsProjectDataPool extends EAMNormalObjectPool
{
	public WcsProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.WCS_PROJECT_DATA);
	}
	
	public void put(WcsProjectData wcsProjectData)
	{
		put(wcsProjectData.getId(), wcsProjectData);
	}
	
	public WcsProjectData find(BaseId id)
	{
		return (WcsProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new WcsProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
