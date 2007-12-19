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
import org.conservationmeasures.eam.ids.TemporaryIdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;

public class CommandSetObjectData extends Command
{
	static public CommandSetObjectData createNewPointList(BaseObject object, String listTag, PointList newList)
	{
		String newListAsString = newList.toJson().toString();
		
		return new CommandSetObjectData(object.getType(), object.getId(), listTag, newListAsString);
	}
	
	static public CommandSetObjectData createAppendIdsCommand(BaseObject object, String idListTag, IdList idsToAppend) throws ParseException
	{
		IdList newList = new IdList(idsToAppend.getObjectType(), object.getData(idListTag));
		newList.addAll(idsToAppend);
		
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	static public CommandSetObjectData createAppendIdCommand(BaseObject object, String idListTag, BaseId idToAppend) throws ParseException
	{
		TemporaryIdList newList = new TemporaryIdList(object.getData(idListTag));
		newList.add(idToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createInsertIdCommand(BaseObject object, String idListTag, BaseId idToInsert, int position) throws ParseException
	{
		TemporaryIdList newList = new TemporaryIdList(object.getData(idListTag));
		newList.insertAt(idToInsert, position);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}

	static public CommandSetObjectData createRemoveIdCommand(BaseObject object, String idListTag, BaseId idToRemove) throws ParseException
	{
		TemporaryIdList newList = new TemporaryIdList(object.getData(idListTag));
		newList.removeId(idToRemove);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	static public CommandSetObjectData createRemoveORefCommand(BaseObject object, String oRefListTag, ORef oRefToRemove) throws ParseException
	{
		ORefList newList = new ORefList(object.getData(oRefListTag));
		newList.remove(oRefToRemove);
		return new CommandSetObjectData(object.getType(), object.getId(), oRefListTag, newList.toString());
	}
	
	//TODO find a better method name
	static public CommandSetObjectData createAppendListCommand(BaseObject object, String idListTag, IdList listToAppend) throws ParseException
	{
		IdList newList = new IdList(listToAppend.getObjectType(), object.getData(idListTag));
		newList.addAll(listToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	static public CommandSetObjectData createAppendORefCommand(BaseObject object, String oRefListTag, ORef oRefToAppend) throws ParseException
	{
		ORefList newList = new ORefList(object.getData(oRefListTag));
		newList.add(oRefToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), oRefListTag, newList.toString());
	}
	
	static public CommandSetObjectData createAppendORefListCommand(BaseObject object, String idListTag, ORefList refListToAppend) throws ParseException
	{
		ORefList newList = new ORefList(object.getData(idListTag));
		newList.addAll(refListToAppend);
		return new CommandSetObjectData(object.getType(), object.getId(), idListTag, newList.toString());
	}
	
	public CommandSetObjectData(ORef objectRef, String fieldTag, ORef oref)
	{
		this(objectRef.getObjectType(), objectRef.getObjectId(), fieldTag, oref.toString());
	}
	
	public CommandSetObjectData(ORef objectRef, String fieldTag, String dataValue)
	{
		this(objectRef.getObjectType(), objectRef.getObjectId(), fieldTag, dataValue);
	}

	public CommandSetObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue)
	{
		type = objectType;
		id = objectId;
		tag = fieldTag;
		newValue = dataValue;
	}
	
	public ORef getObjectORef()
	{
		return new ORef(getObjectType(), getObjectId());
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
	
	public boolean isDoNothingCommand(Project project) throws CommandFailedException
	{
		try
		{
			String dataValue = getDataValue();
			String existingData = getExistingData(project);
			return dataValue.equals(existingData);
		}
		catch(RuntimeException e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + getObjectType() + ", " + getObjectId() + ", [" + getFieldTag() + "] = [" + getDataValue() + "]";
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			oldValue = getExistingData(target);
			target.setObjectData(type, id, tag, newValue);		
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private String getExistingData(Project target)
	{
		return target.getObjectData(type, id, tag);
	}

	public Command getReverseCommand() throws CommandFailedException
	{
		CommandSetObjectData commandSetObjectData = new CommandSetObjectData(type, id, tag, oldValue);
		commandSetObjectData.setPreviousDataValue(newValue);
		
		return commandSetObjectData;
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put("OBJECT_TYPE", new Integer(type));
		dataPairs.put(BaseId.class.getSimpleName(), id);
		dataPairs.put("TAG", tag);
		dataPairs.put("NEW_VALUE", newValue);
		dataPairs.put("PREVIOUS_VALUE", oldValue);
		
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "SetObjectData";

	int type;
	BaseId id;
	String tag;
	String newValue;
	String oldValue;
}
