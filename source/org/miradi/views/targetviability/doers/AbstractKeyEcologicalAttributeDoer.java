/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.targetviability.doers;

import org.miradi.views.ObjectsDoer;

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
