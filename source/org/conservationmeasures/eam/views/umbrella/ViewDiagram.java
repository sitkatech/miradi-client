/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class ViewDiagram extends ProjectDoer
{
	public ViewDiagram(BaseProject projectToUse)
	{
		super(projectToUse);
	}

	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			getProject().executeCommand(new CommandSwitchView(DiagramView.getViewName()));
		}
		catch(AlreadyInThatViewException ignore)
		{
			// not really a problem
		}
	}

}
