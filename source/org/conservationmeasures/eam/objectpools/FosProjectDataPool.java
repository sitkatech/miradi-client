/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FosProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

public class FosProjectDataPool extends EAMNormalObjectPool
{
	public FosProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, FosProjectData.getObjectType());
	}
	
	public void put(FosProjectData fosProjectData)
	{
		put(fosProjectData.getId(), fosProjectData);
	}
	
	public FosProjectData find(BaseId id)
	{
		return (FosProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new FosProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
