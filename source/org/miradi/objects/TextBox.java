/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.diagram.factortypes.FactorTypeTextBox;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class TextBox extends Factor
{
	public TextBox(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, new FactorTypeTextBox());
		clear();
	}
	
	public TextBox(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_TEXT_BOX, json);
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
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
	
	public static final String OBJECT_NAME = "TextBox";
}
