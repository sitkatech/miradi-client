/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.SlideId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.project.ObjectManager;

public class SlidePool extends EAMNormalObjectPool
{
	public SlidePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.SLIDE);
	}
	
	public void put(Slide slide)
	{
		put(slide.getId(), slide);
	}
	
	public Slide find(BaseId id)
	{
		return (Slide)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new Slide(objectManager, new SlideId(actualId.asInt()));
	}


}
