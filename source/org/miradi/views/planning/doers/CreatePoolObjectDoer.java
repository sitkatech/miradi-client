/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.commands.CommandCreateObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

abstract public class CreatePoolObjectDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			CommandCreateObject cmd = new CommandCreateObject(getTypeToCreate());
			getProject().executeCommand(cmd);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	abstract protected int getTypeToCreate();
}
