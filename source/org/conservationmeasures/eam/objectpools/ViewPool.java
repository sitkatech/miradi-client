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
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ViewData;

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

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ViewData(actualId);
	}
}
