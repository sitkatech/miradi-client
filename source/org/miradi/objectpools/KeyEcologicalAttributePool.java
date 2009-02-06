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
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.ObjectManager;

public class KeyEcologicalAttributePool extends EAMNormalObjectPool
{
	public KeyEcologicalAttributePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}
	
	public void put(KeyEcologicalAttribute keyEcologicalAttribute) throws Exception
	{
		put(keyEcologicalAttribute.getId(), keyEcologicalAttribute);
	}
	
	public KeyEcologicalAttribute find(BaseId id)
	{
		return (KeyEcologicalAttribute)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new KeyEcologicalAttribute(objectManager, new KeyEcologicalAttributeId(actualId.asInt()));
	}
	
	public KeyEcologicalAttribute[] getAllKeyEcologicalAttribute()
	{
		BaseId[] allIds = getIds();
		KeyEcologicalAttribute[] allKeyEcologicalAttribute = new KeyEcologicalAttribute[allIds.length];
		for (int i = 0; i < allKeyEcologicalAttribute.length; i++)
			allKeyEcologicalAttribute[i] = find(allIds[i]);
			
		return allKeyEcologicalAttribute;
	}
}

