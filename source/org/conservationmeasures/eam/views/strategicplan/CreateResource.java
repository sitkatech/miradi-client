/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.views.ProjectDoer;

public class CreateResource extends ProjectDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.PROJECT_RESOURCE);
		getProject().executeCommand(cmd);
	}

}
