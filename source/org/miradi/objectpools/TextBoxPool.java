/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TextBox;
import org.miradi.project.ObjectManager;

public class TextBoxPool extends EAMNormalObjectPool
{
	public TextBoxPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TEXT_BOX);
	}
	
	public void put(TextBox textBox)
	{
		put(textBox.getId(), textBox);
	}
	
	public TextBox find(BaseId id)
	{
		return (TextBox)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new TextBox(objectManager, new FactorId(actualId.asInt()));
	}
	
	public TextBox[] getAllTextBoxes()
	{
		BaseId[] allIds = getIds();
		TextBox[] allTextBoxes = new TextBox[allIds.length];
		for (int i = 0; i < allTextBoxes.length; i++)
			allTextBoxes[i] = find(allIds[i]);
			
		return allTextBoxes;
	}
}
