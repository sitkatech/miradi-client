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

public class CreateKeyEcologicalAttributeDoer extends CreateAnnotationDoer
{
	public boolean isAvailable() 
	{
		if (!super.isAvailable())
			return false;
		
		BaseObject selectedParent = getSelectedParent();
		if (!Factor.isFactor(selectedParent.getRef()))
			return false;
		
		if (((Factor)selectedParent).isTarget())
			return ((Target)selectedParent).isViabilityModeTNC();

		return false;
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
