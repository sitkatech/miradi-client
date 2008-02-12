/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.GroupBox;
import org.miradi.project.ObjectManager;

public class GroupBoxPool extends EAMNormalObjectPool
{
	public GroupBoxPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.GROUP_BOX);
	}
	
	public void put(GroupBox groupBox)
	{
		put(groupBox.getId(), groupBox);
	}
	
	public GroupBox find(BaseId id)
	{
		return (GroupBox) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new GroupBox(objectManager, new FactorId(actualId.asInt()));
	}
}
