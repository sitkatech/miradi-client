/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FosProjectDataSchema;

public class FosProjectDataPool extends BaseObjectPool
{
	public FosProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, FosProjectDataSchema.getObjectType());
	}
	
	public void put(FosProjectData fosProjectData) throws Exception
	{
		put(fosProjectData.getId(), fosProjectData);
	}
	
	public FosProjectData find(BaseId id)
	{
		return (FosProjectData) getRawObject(id);
	}

	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId)
	{
		return new FosProjectData(objectManager, new BaseId(actualId.asInt()));
	}
	
	@Override
	public BaseObjectSchema createBaseObjectSchema(Project projectToUse)
	{
		return FosProjectData.createSchema(projectToUse);
	}
}
