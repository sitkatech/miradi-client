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
import org.miradi.objects.BaseObject;
import org.miradi.objects.TncProjectData;
import org.miradi.project.ObjectManager;

public class TncProjectDataPool extends EAMNormalObjectPool
{
	public TncProjectDataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, TncProjectData.getObjectType());
	}
	
	public void put(TncProjectData tncProjectData) throws Exception
	{
		put(tncProjectData.getId(), tncProjectData);
	}
	
	public TncProjectData find(BaseId id)
	{
		return (TncProjectData) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new TncProjectData(objectManager, new BaseId(actualId.asInt()));
	}
}
