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
import org.conservationmeasures.eam.project.Project;

public abstract class Command
{
	public static Command readFrom(DataInputStream dataIn) throws IOException, UnknownCommandException
	{
		String commandName = dataIn.readUTF();
		return createFrom(commandName, dataIn);
	}

	public static Command createFrom(String commandName, DataInputStream dataIn) throws IOException, UnknownCommandException
	{
		if(commandName.equals(CommandDiagramMove.COMMAND_NAME))
			return new CommandDiagramMove(dataIn);
		if(commandName.equals(CommandSetNodeText.COMMAND_NAME))
			return new CommandSetNodeText(dataIn);
		if(commandName.equals(CommandInsertNode.COMMAND_NAME))
			return new CommandInsertNode(dataIn);
		if(commandName.equals(CommandLinkNodes.COMMAND_NAME))
			return new CommandLinkNodes(dataIn);
		if(commandName.equals(CommandDeleteLinkage.COMMAND_NAME))
			return new CommandDeleteLinkage(dataIn);
		if(commandName.equals(CommandDeleteNode.COMMAND_NAME))
			return new CommandDeleteNode(dataIn);
		if(commandName.equals(CommandUndo.COMMAND_NAME))
			return new CommandUndo(dataIn);
		if(commandName.equals(CommandRedo.COMMAND_NAME))
			return new CommandRedo(dataIn);
		if(commandName.equals(CommandSwitchView.COMMAND_NAME))
			return new CommandSwitchView(dataIn);
		if(commandName.equals(CommandBeginTransaction.COMMAND_NAME))
			return new CommandBeginTransaction();
		if(commandName.equals(CommandEndTransaction.COMMAND_NAME))
			return new CommandEndTransaction();
		if(commandName.equals(CommandInterviewSetStep.COMMAND_NAME))
			return new CommandInterviewSetStep(dataIn);
		if(commandName.equals(CommandSetData.COMMAND_NAME))
			return new CommandSetData(dataIn);
		if(commandName.equals(CommandSetNodePriority.COMMAND_NAME))
			return new CommandSetNodePriority(dataIn);
		if(commandName.equals(CommandSetIndicator.COMMAND_NAME))
			return new CommandSetIndicator(dataIn);
		if(commandName.equals(CommandSetNodeObjectives.COMMAND_NAME))
			return new CommandSetNodeObjectives(dataIn);
		if(commandName.equals(CommandSetTargetGoal.COMMAND_NAME))
			return new CommandSetTargetGoal(dataIn);
		if(commandName.equals(CommandSetNodeSize.COMMAND_NAME))
			return new CommandSetNodeSize(dataIn);
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
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		writeDataTo(dataOut);
	}

	abstract public String getCommandName();
	abstract public void execute(Project target) throws CommandFailedException;
	abstract public void undo(Project target) throws CommandFailedException;

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
	}
	
}
