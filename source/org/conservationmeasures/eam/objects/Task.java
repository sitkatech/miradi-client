/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class Task extends EAMBaseObject
{
	public Task(BaseId idToUse)
	{
		super(idToUse);
		subtaskIds = new IdList();
		resourceIds = new IdList();
	}
	
	public Task(int idAsInt, JSONObject json) throws ParseException
	{
		super(new BaseId(idAsInt), json);
		String subtaskIdsAsString = json.optString(TAG_SUBTASK_IDS, "{}");
		setSubtaskIdsFromString(subtaskIdsAsString);
		String resourceIdsAsString = json.optString(TAG_RESOURCE_IDS, "{}");
		setResourceIdsFromString(resourceIdsAsString);
	}

	private void setSubtaskIdsFromString(String subtaskIdsAsString) throws ParseException
	{
		subtaskIds = new IdList(subtaskIdsAsString);
	}

	private void setResourceIdsFromString(String resourceIdsAsString) throws ParseException
	{
		resourceIds = new IdList(resourceIdsAsString);
	}

	public int getType()
	{
		return ObjectType.TASK;
	}

	public void addSubtaskId(BaseId subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.createClone();
	}
	
	public int getResourceCount()
	{
		return resourceIds.size();
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds.createClone();
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_SUBTASK_IDS, getSubtaskIdsAsString());
		json.put(TAG_RESOURCE_IDS, getResourceIdsAsString());
		return json;
	}

	private String getSubtaskIdsAsString()
	{
		return subtaskIds.toString();
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SUBTASK_IDS))
			return getSubtaskIdsAsString();
		
		if(fieldTag.equals(TAG_RESOURCE_IDS))
			return getResourceIdsAsString();
		
		return super.getData(fieldTag);
	}

	private String getResourceIdsAsString()
	{
		return getResourceIdList().toString();
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SUBTASK_IDS))
			setSubtaskIdsFromString((String)dataValue);
		else if(fieldTag.equals(TAG_RESOURCE_IDS))
			setResourceIdsFromString((String)dataValue);
		else super.setData(fieldTag, dataValue);
	}

	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_RESOURCE_IDS = "ResourceIds";
	
	IdList subtaskIds;
	IdList resourceIds;
}
