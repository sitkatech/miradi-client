/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateFundingSourceDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			CommandCreateObject cmd = new CommandCreateObject(ObjectType.FUNDING_SOURCE);
			getProject().executeCommand(cmd);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}

