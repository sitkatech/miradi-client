/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.project.ObjectManager;

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
