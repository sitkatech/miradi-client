/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;

public class CreateKeyEcologicalAttributeDoer  extends CreateAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length != 1)
			return true;
		
		if (getSelectedObjectType() == ObjectType.TARGET)
			return ((Target)getObjects()[0]).isViabilityModeTNC();
		
		if (getSelectedObjectType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE)
			return true;
		
		return false;
	}
	
	// TODO: Should not trust that the selected object is a target or KEA
	// (should test for those specifically)
	public Factor getSelectedFactor()
	{
		BaseObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(! Factor.isFactor(selected.getType()))
			selected = selected.getOwner();
		
		return (Factor)selected;
	}
	
	int getAnnotationType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	String getAnnotationIdListTag()
	{
		return Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS;
	}
}
