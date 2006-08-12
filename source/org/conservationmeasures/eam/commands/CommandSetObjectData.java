/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.project.Project;

public class CommandSetObjectData extends Command
{
	static public CommandSetObjectData createAppendIdCommand(EAMObject object, String idListTag, BaseId idToAppend) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.add(idToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createInsertIdCommand(EAMObject object, String idListTag, BaseId idToInsert, int position) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.insertAt(idToInsert, position);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createRemoveIdCommand(EAMObject object, String idListTag, BaseId idToRemove) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.removeId(idToRemove);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	public CommandSetObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue)
	{
		type = objectType;
		id = objectId;
		tag = fieldTag;
		newValue = dataValue;
	}
	
	public CommandSetObjectData(DataInputStream dataIn) throws IOException
	{
		type = dataIn.readInt();
		id = new BaseId(dataIn.readInt());
		tag = dataIn.readUTF();
		newValue = dataIn.readUTF();
		oldValue = dataIn.readUTF();
	}
	
	public int getObjectType()
	{
		return type;
	}
	
	public BaseId getObjectId()
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
	
	public String getPreviousDataValue()
	{
		return oldValue;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}
	
	public String toString()
	{
		return getCommandName() + ": " + getObjectType() + ", " + getObjectId() + ", [" + getFieldTag() + "] = [" + getDataValue() + "]";
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
		dataOut.writeInt(id.asInt());
		dataOut.writeUTF(tag);
		dataOut.writeUTF(newValue);
		dataOut.writeUTF(oldValue);
	}

	public static final String COMMAND_NAME = "SetObjectData";

	int type;
	BaseId id;
	String tag;
	String newValue;
	String oldValue;
}
