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
import org.conservationmeasures.eam.objects.WcpaProjectData;
import org.conservationmeasures.eam.project.ObjectManager;

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
