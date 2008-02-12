/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
	
	public void put(KeyEcologicalAttribute keyEcologicalAttribute)
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

