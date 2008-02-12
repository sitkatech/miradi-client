/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;

public class TreeNodeCreateMethodDoer extends TreeNodeCreateTaskDoer
{
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Indicator.getObjectType())
			return true;
		
		return false;
	}

}
