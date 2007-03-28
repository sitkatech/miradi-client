/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.text.ParseException;
import java.util.HashMap;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;

public class CommandSetObjectData extends Command
{
	
	static public CommandSetObjectData createNewPointList(BaseObject object, String listTag, PointList newList, PointList oldList)
	{
		String newListAsString = newList.toJson().toString();
		String oldListAsString = oldList.toJson().toString();
		
		return new CommandSetObjectData(object.getType(), object.getId(), listTag, newListAsString, oldListAsString);
	}
	
	static public CommandSetObjectData createAppendIdsCommand(BaseObject object, String idListTag, IdList idsToAppend) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.addAll(idsToAppend);
		
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	static public CommandSetObjectData createAppendIdCommand(BaseObject object, String idListTag, BaseId idToAppend) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.add(idToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createInsertIdCommand(BaseObject object, String idListTag, BaseId idToInsert, int position) throws ParseException
	{
		IdList newList = new IdList(object.getData(idListTag));
		newList.insertAt(idToInsert, position);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createRemoveIdCommand(BaseObject object, String idListTag, BaseId idToRemove) throws ParseException
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
	
	public CommandSetObjectData(int objectType, BaseId objectId, String fieldTag, String newValueToUse, String oldValueToUse)
	{
		this(objectType, objectId, fieldTag, newValueToUse);
		oldValue = oldValueToUse;
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
		return new CommandSetObjectData(type, id, tag, oldValue, newValue);
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("OBJECT_TYPE", new Integer(type));
		dataPairs.put(BaseId.class.getSimpleName(), id);
		dataPairs.put("TAG", tag);
		dataPairs.put("NEW_VALUE", newValue);
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "SetObjectData";

	int type;
	BaseId id;
	String tag;
	String newValue;
	String oldValue;
}
