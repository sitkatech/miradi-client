/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.ObjectManager;

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

