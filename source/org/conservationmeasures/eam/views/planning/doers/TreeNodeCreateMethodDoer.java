/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;

public class TreeNodeCreateMethodDoer extends TreeNodeCreateTaskDoer
{
	boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Indicator.getObjectType())
			return true;
		
		return false;
	}

}
