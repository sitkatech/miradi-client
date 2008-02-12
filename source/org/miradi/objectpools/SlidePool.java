/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.SlideId;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Slide;
import org.miradi.project.ObjectManager;

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
