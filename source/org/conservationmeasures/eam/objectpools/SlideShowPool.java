/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.SlideShowId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.project.ObjectManager;

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

