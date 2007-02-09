/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.text.ParseException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class CommandSetObjectData extends Command
{
	static public CommandSetObjectData createAppendIdsCommand(EAMObject object, String idListTag, IdList idsToAppend) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.addAll(idsToAppend);
		
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
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
		oldValue = "";
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
	
	public void setPreviousDataValue(String forcedOldValue)
	{
		oldValue = forcedOldValue;
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
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public Command getReverseCommand() throws CommandFailedException
	{
		return new CommandSetObjectData(type, id, tag, oldValue);
	}

	public static final String COMMAND_NAME = "SetObjectData";

	int type;
	BaseId id;
	String tag;
	String newValue;
	String oldValue;
}
