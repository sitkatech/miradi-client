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
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.ObjectManager;

public class ViewPool extends EAMNormalObjectPool
{
	public ViewPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.VIEW_DATA);
	}
	
	public void put(ViewData viewData)
	{
		put(viewData.getId(), viewData);
	}
	
	public ViewData find(BaseId id)
	{
		return (ViewData)getRawObject(id);
	}

	public ViewData findByLabel(String label)
	{
		BaseId[] ids = getIds();
		for(int i = 0; i < ids.length; ++i)
		{
			ViewData viewData = find(ids[i]);
			if(viewData.getLabel().equals(label))
				return viewData;
		}
		return null;
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ViewData(objectManager, actualId);
	}
}
