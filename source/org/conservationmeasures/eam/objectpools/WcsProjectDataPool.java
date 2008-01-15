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
import org.conservationmeasures.eam.objects.WcsProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

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
