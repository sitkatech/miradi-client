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
import org.miradi.objects.WwfProjectData;
import org.miradi.project.ObjectManager;

public class WwfProjectDataPool extends EAMNormalObjectPool
{
	public WwfProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.WWF_PROJECT_DATA);
	}
	
	public void put(WwfProjectData wwfProjectData)
	{
		put(wwfProjectData.getId(), wwfProjectData);
	}
	
	public WwfProjectData find(BaseId id)
	{
		return (WwfProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new WwfProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
