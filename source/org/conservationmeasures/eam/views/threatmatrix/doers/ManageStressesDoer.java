/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ManageStressesDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getSelectedHierarchies().length == 0)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		
	}
}
