/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class CommandCreateObject extends Command
{
	public CommandCreateObject(int typeToCreate)
	{
		type = typeToCreate;
		createdId = IdAssigner.INVALID_ID;
	}
	
	public CommandCreateObject(DataInputStream dataIn) throws IOException
	{
		type = dataIn.readInt();
		createdId = dataIn.readInt();
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public int getCreatedId()
	{
		return createdId;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			createdId = target.createObject(type);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			target.deleteObject(type, createdId);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(type);
		dataOut.writeInt(createdId);
	}

	public static final String COMMAND_NAME = "CommandCreateObject";

	int type;
	int createdId;
}
