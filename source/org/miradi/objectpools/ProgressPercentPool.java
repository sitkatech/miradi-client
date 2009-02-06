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
import org.miradi.objects.ProgressPercent;
import org.miradi.project.ObjectManager;

public class ProgressPercentPool extends EAMNormalObjectPool
{
	public ProgressPercentPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PROGRESS_PERCENT);
	}
	
	public void put(ProgressPercent progressPercent) throws Exception
	{
		put(progressPercent.getId(), progressPercent);
	}
	
	public ProgressPercent find(BaseId id)
	{
		return (ProgressPercent) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ProgressPercent(objectManager, actualId);
	}
}
