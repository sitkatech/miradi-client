/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.conservationmeasures.eam.main.Project;

public abstract class Command
{
	public static Command readFrom(InputStream in) throws IOException
	{
		DataInputStream dataIn = new DataInputStream(in);
		String commandName = dataIn.readUTF();
		if(commandName.equals(CommandDiagramMove.getCommandName()))
			return new CommandDiagramMove(dataIn);
		if(commandName.equals(CommandSetNodeText.getCommandName()))
			return new CommandSetNodeText(dataIn);
		if(commandName.equals(CommandInsertGoal.getCommandName()))
			return new CommandInsertGoal(dataIn);
		if(commandName.equals(CommandInsertThreat.getCommandName()))
			return new CommandInsertThreat(dataIn);
		if(commandName.equals(CommandInsertIntervention.getCommandName()))
			return new CommandInsertIntervention(dataIn);
		
		throw new RuntimeException("Attempted to load unknown command type: " + commandName);
	}
	
	abstract public Object execute(Project target);
	abstract public void writeTo(OutputStream out) throws IOException;
}
