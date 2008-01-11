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
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.project.ObjectManager;

public class SubTargetPool extends EAMNormalObjectPool
{
	public SubTargetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.SUB_TARGET);
	}
	
	public void put(SubTarget subTarget)
	{
		put(subTarget.getId(), subTarget);
	}
	
	public SubTarget find(BaseId id)
	{
		return (SubTarget) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new SubTarget(objectManager, actualId);
	}
}
