/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateResource extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			CommandCreateObject cmd = new CommandCreateObject(ObjectType.PROJECT_RESOURCE);
			getProject().executeCommand(cmd);
			ProjectResource resource = getProject().getResourcePool().find(cmd.getCreatedId());
			getView().modifyObject(resource);
			getView().selectObject(resource);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
