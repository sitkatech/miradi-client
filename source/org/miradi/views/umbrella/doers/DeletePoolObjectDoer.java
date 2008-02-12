/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.views.ObjectsDoer;

abstract public class DeletePoolObjectDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete " + getCustomText(), new String[]{"\nAre you sure you want to delete this " + getCustomText() + "?"}, buttons))
			return;

		try
		{
			getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				BaseObject objectToDelete = getObjects()[0];
				getProject().executeCommandsWithoutTransaction(objectToDelete.createCommandsToClear());
				getProject().executeCommand(new CommandDeleteObject(objectToDelete.getRef()));
			}
			finally
			{
				getProject().executeCommand(new CommandEndTransaction());
			}
		}
		catch(CommandFailedException e)
		{
			throw(e);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	abstract protected String getCustomText();	
}
