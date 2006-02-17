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
import org.conservationmeasures.eam.project.Project;

public class CommandSetObjectData extends Command
{
	public CommandSetObjectData(int objectType, int objectId, String fieldTag, String dataValue)
	{
		type = objectType;
		id = objectId;
		tag = fieldTag;
		newValue = dataValue;
	}
	
	public CommandSetObjectData(DataInputStream dataIn) throws IOException
	{
		type = dataIn.readInt();
		id = dataIn.readInt();
		tag = dataIn.readUTF();
		newValue = dataIn.readUTF();
		oldValue = dataIn.readUTF();
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public int getObjectId()
	{
		return id;
	}
	
	public String getFieldTag()
	{
		return tag;
	}
	
	public String getDataValue()
	{
		return newValue;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			oldValue = target.getObjectData(type, id, tag);
			target.setObjectData(type, id, tag, newValue);
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
			String currentValue = target.getObjectData(type, id, tag);
			if(!newValue.equals(currentValue))
				throw new RuntimeException("CommandSetDataObject undo expected " + newValue + " but was " + currentValue);
			target.setObjectData(type, id, tag, oldValue);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(type);
		dataOut.writeInt(id);
		dataOut.writeUTF(tag);
		dataOut.writeUTF(newValue);
		dataOut.writeUTF(oldValue);
	}

	public static final String COMMAND_NAME = "SetObjectData";

	int type;
	int id;
	String tag;
	String newValue;
	String oldValue;
}
