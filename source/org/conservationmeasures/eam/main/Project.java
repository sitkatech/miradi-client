/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;

public class Project
{
	public Project()
	{
	}

	public void recordCommand(Command command)
	{
		EAM.logDebug("Command: " + command);
	}
}
