/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.WwfProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

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
