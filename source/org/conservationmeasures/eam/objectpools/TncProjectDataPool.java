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
import org.conservationmeasures.eam.objects.TncProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

public class TncProjectDataPool extends EAMNormalObjectPool
{
	public TncProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, TncProjectData.getObjectType());
	}
	
	public void put(TncProjectData tncProjectData)
	{
		put(tncProjectData.getId(), tncProjectData);
	}
	
	public TncProjectData find(BaseId id)
	{
		return (TncProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new TncProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
