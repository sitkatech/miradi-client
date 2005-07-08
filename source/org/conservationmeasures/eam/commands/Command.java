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

import org.conservationmeasures.eam.main.MainWindow;

public abstract class Command
{
	public static Command readFrom(InputStream in) throws IOException
	{
		DataInputStream dataIn = new DataInputStream(in);
		String commandName = dataIn.readUTF();
		if(commandName.equals(CommandDiagramMove.getCommandName()))
			return new CommandDiagramMove(dataIn);
		if(commandName.equals(CommandDiagramSelectCells.getCommandName()))
			return new CommandDiagramSelectCells(dataIn);
		if(commandName.equals(CommandInsertThreat.getCommandName()))
			return new CommandInsertThreat(dataIn);
		
		return null;
	}
	
	abstract public void execute(MainWindow target);
	abstract public void writeTo(OutputStream out) throws IOException;
}
