/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class DeleteAccountingCodeDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		AccountingCode resource = (AccountingCode)getObjects()[0];
		BaseId idToRemove = resource.getId();
		
		Vector dialogText = new Vector();
		
		dialogText.add("\nAre you sure you want to delete this accounting code?");

		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Accounting Code", (String[])dialogText.toArray(new String[0]), buttons))
			return;

		try
		{
			getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				int type = resource.getType();
				BaseId id = idToRemove;
				getProject().executeCommandsWithoutTransaction(resource.createCommandsToClear());
				getProject().executeCommand(new CommandDeleteObject(type, id));
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

}

