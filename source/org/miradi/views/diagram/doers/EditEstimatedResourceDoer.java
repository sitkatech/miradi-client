/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ObjectsDoer;

public class EditEstimatedResourceDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return false;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	}
}
