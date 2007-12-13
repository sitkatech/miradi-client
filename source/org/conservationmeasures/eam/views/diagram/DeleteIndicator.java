/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;

public class DeleteIndicator extends DeleteAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length == 0)
			return false;
		
		if (getSelectedObjectType() != Indicator.getObjectType())
			return false;
		
		return true;
	}
	
	public String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Indicator?",};
	}

	public String getAnnotationIdListTag()
	{
		return Factor.TAG_INDICATOR_IDS;
	}
	
	public int getAnnotationType()
	{
		return Indicator.getObjectType();
	}
}
