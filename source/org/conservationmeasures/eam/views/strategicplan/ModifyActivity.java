/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ModifyActivity extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			Task task = (Task)getObjects()[0];
			getView().modifyObject(task);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
