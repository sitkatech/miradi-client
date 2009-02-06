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
	
	public void put(TextBox textBox) throws Exception
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
