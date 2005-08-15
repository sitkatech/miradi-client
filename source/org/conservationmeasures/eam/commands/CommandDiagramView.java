/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class CommandDiagramView extends Command
{
	public CommandDiagramView()
	{
	}

	public CommandDiagramView(DataInputStream dataIn)
	{
	}

	public static String getCommandName()
	{
		return "DiagramView";
	}
	
	public void execute(BaseProject target) throws CommandFailedException
	{
		target.switchToView(DiagramView.getViewName());
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
	}

}
