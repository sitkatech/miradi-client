/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;
import org.miradi.project.ObjectManager;

public class ViewPool extends EAMNormalObjectPool
{
	public ViewPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.VIEW_DATA);
	}
	
	public void put(ViewData viewData) throws Exception
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
