/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.ObjectsDoer;

abstract public class AbstractTreeNodeDoer extends ObjectsDoer
{

	protected BaseObject getSingleSelectedObject()
	{
		BaseObject[] selectedObjects = getObjects();
		if(selectedObjects.length != 1)
			return null;
		
		return selectedObjects[0];
	}
}
