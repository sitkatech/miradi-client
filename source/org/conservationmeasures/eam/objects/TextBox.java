/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTextBox;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TextBox extends Factor
{
	public TextBox(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, new FactorTypeTextBox());
		clear();
	}
	
	public TextBox(FactorId idToUse)
	{
		super(idToUse, new FactorTypeTextBox());
		clear();
	}
	
	public TextBox(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_TEXT_BOX, json);
	}
	
	public TextBox(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_TEXT_BOX, json);
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.TEXT_BOX;
	}
	
	public boolean isTextBox()
	{
		return true;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
	}
	
	void clear()
	{
		super.clear();
		
		description = new StringData();
		
		addField(TAG_DESCRIPTION, description);
	}

	public static final String OBJECT_NAME = "Text Box";
	public static String TAG_DESCRIPTION = "description";
	
	StringData description;
}
