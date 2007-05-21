/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TextBox extends BaseObject
{
	public TextBox(ObjectManager objectManagerToUse, BaseId idToUse)
	{
		super(objectManagerToUse, idToUse);
	}

	public TextBox(BaseId idToUse)
	{
		super(idToUse);
	}

	public TextBox(ObjectManager objectManagerToUse, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManagerToUse, idToUse, json);
	}

	public TextBox(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
	}

	public int getType()
	{
		return ObjectType.TEXT_BOX;
	}

}
