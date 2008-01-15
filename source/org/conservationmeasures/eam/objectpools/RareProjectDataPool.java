/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

public class RareProjectDataPool extends EAMNormalObjectPool
{
	public RareProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.RARE_PROJECT_DATA);
	}
	
	public void put(RareProjectData rareProjectData)
	{
		put(rareProjectData.getId(), rareProjectData);
	}
	
	public RareProjectData find(BaseId id)
	{
		return (RareProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new RareProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
