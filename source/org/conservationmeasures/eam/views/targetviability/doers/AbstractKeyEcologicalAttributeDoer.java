/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.targetviability.doers;

import org.conservationmeasures.eam.views.ObjectsDoer;

abstract public class AbstractKeyEcologicalAttributeDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects() == null)
			return false;
		
		if ((getObjects().length != 1))
			return false;
		
		if (getSelectedObjectType() != getRequiredObjectType())
			return false;
		
		return true;
	}
	
	abstract public int getRequiredObjectType();
}
