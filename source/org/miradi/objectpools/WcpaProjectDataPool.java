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
import org.miradi.objects.WcpaProjectData;
import org.miradi.project.ObjectManager;

public class WcpaProjectDataPool extends EAMNormalObjectPool
{
	public WcpaProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.WCPA_PROJECT_DATA);
	}
	
	public void put(WcpaProjectData wcpaProjectData)
	{
		put(wcpaProjectData.getId(), wcpaProjectData);
	}
	
	public WcpaProjectData find(BaseId id)
	{
		return (WcpaProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new WcpaProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
