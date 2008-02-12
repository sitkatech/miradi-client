/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.diagram;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;

//TODO: this class needs to be moved to the dialogs viabilty package : need first to resolve dependecny 
// issue with CreateAnnotationDoer abstract method signitures
public class CreateViabilityKeyEcologicalAttributeDoer  extends CreateAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getPicker()==null)
			return false;
		
		if (getPicker().getSelectedObjects().length != 1)
			return false;
		
		if (selectedObject().getType() == ObjectType.TARGET)
			return ((Target)selectedObject()).isViabilityModeTNC();
		
		if (selectedObject().getType()  == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return true;
		
		return false;
	}

	public Factor getSelectedParent()
	{
		if (selectedObject().getType() == ObjectType.TARGET)
			return (Factor) selectedObject();
		
		if (selectedObject().getType()  == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return (Factor) selectedObject().getOwner();
		
		return null;
	}
	
	private BaseObject selectedObject()
	{
		return getPicker().getSelectedObjects()[0];
	}
	
	
	public int getAnnotationType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	public String getAnnotationListTag()
	{
		return Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS;
	}
}
