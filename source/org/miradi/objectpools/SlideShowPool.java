/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.SlideShowId;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.SlideShow;
import org.miradi.project.ObjectManager;

public class SlideShowPool extends EAMNormalObjectPool
{
	public SlideShowPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.SLIDESHOW);
	}
	
	public void put(SlideShow slideShow)
	{
		put(slideShow.getId(), slideShow);
	}
	
	public SlideShow find(BaseId id)
	{
		return (SlideShow)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new SlideShow(objectManager, new SlideShowId(actualId.asInt()));
	}


}

