/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.main.Project;

public abstract class Command
{
	public static Command readFrom(DataInputStream dataIn) throws IOException
	{
		String commandName = dataIn.readUTF();
		if(commandName.equals(CommandDiagramMove.getCommandName()))
			return new CommandDiagramMove(dataIn);
		if(commandName.equals(CommandSetNodeText.getCommandName()))
			return new CommandSetNodeText(dataIn);
		if(commandName.equals(CommandInsertNode.getCommandName()))
			return new CommandInsertNode(dataIn);
		if(commandName.equals(CommandLinkNodes.getCommandName()))
			return new CommandLinkNodes(dataIn);
		if(commandName.equals(CommandDeleteLinkage.getCommandName()))
			return new CommandDeleteLinkage(dataIn);
		if(commandName.equals(CommandDeleteNode.getCommandName()))
			return new CommandDeleteNode(dataIn);
		
		throw new RuntimeException("Attempted to load unknown command type: " + commandName);
	}
	
	public boolean equals(Object other)
	{
		return toString().equals(other.toString());
	}

	abstract public void execute(Project target) throws CommandFailedException;
	abstract public void writeTo(DataOutputStream out) throws IOException;
	
	public void undo(Project target)
	{
		throw new RuntimeException("Not implemented yet");
	}
}
