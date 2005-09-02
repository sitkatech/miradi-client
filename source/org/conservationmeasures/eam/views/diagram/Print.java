/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Print extends ProjectDoer 
{

	public Print(BaseProject project)
	{
		super(project);
	}

	public boolean isAvailable() 
	{
		if(!getProject().isOpen())
			return false;
		
		return getProject().getDiagramModel().getCellCount() > 0;
	}

	public void doIt() throws CommandFailedException 
	{
		//TODO implement doIt.
	}

}
