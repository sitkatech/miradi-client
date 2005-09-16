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
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.main.BaseProject;

public abstract class Command
{
	public static Command readFrom(DataInputStream dataIn) throws IOException, UnknownCommandException
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
		if(commandName.equals(CommandUndo.getCommandName()))
			return new CommandUndo(dataIn);
		if(commandName.equals(CommandRedo.getCommandName()))
			return new CommandRedo(dataIn);
		if(commandName.equals(CommandSwitchView.getCommandName()))
			return new CommandSwitchView(dataIn);
		if(commandName.equals(CommandBeginTransaction.getCommandName()))
			return new CommandBeginTransaction();
		if(commandName.equals(CommandEndTransaction.getCommandName()))
			return new CommandEndTransaction();
		if(commandName.equals(CommandWizardPrevious.getCommandName()))
			return new CommandWizardPrevious(dataIn);
		if(commandName.equals(CommandInterviewSetStep.getCommandName()))
			return new CommandInterviewSetStep(dataIn);
		throw new UnknownCommandException("Attempted to load unknown command type: " + commandName);
	}
	
	public boolean equals(Object other)
	{
		return toString().equals(other.toString());
	}
	
	public boolean isUndo()
	{
		return false;
	}

	public boolean isRedo()
	{
		return false;
	}
	
	public boolean isBeginTransaction()
	{
		return false;
	}
	
	public boolean isEndTransaction()
	{
		return false;
	}

	abstract public void execute(BaseProject target) throws CommandFailedException;
	abstract public void writeTo(DataOutputStream dataOut) throws IOException;
	
	public void undo(BaseProject target) throws CommandFailedException
	{
		throw new RuntimeException("Not implemented yet");
	}
}
