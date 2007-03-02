/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;

public class KeyEcologicalAttributePool extends EAMNormalObjectPool
{
	public KeyEcologicalAttributePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}
	
	public void put(KeyEcologicalAttribute indicator)
	{
		put(indicator.getId(), indicator);
	}
	
	public KeyEcologicalAttribute find(BaseId id)
	{
		return (KeyEcologicalAttribute)getRawObject(id);
	}

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new KeyEcologicalAttribute(new KeyEcologicalAttributeId(actualId.asInt()));
	}
	
	public KeyEcologicalAttribute[] getAllIndicators()
	{
		BaseId[] allIds = getIds();
		KeyEcologicalAttribute[] allIndicators = new KeyEcologicalAttribute[allIds.length];
		for (int i = 0; i < allIndicators.length; i++)
			allIndicators[i] = find(allIds[i]);
			
		return allIndicators;
	}
}

