/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		
		String[] buttons = {EAM.text("Yes"), EAM.text("No"), };
		final String title = EAM.substitute(EAM.text("Delete %s"), getCustomText());
		final String text = EAM.substitute(EAM.text("\nAre you sure you want to delete the selected %s?"), getCustomText());
		if(!EAM.confirmDialog(title, new String[]{text}, buttons))
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
