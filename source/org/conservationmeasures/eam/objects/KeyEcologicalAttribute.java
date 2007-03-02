/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class KeyEcologicalAttribute extends EAMBaseObject
{
	public KeyEcologicalAttribute(KeyEcologicalAttributeId idToUse)
	{
		super(idToUse);
		clear();
	}

	public KeyEcologicalAttribute(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	void clear()
	{
		super.clear();
	}
	
	public int getType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}


	public static final String OBJECT_NAME = "KeyEcologicalAttribute";



}
