/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.json.JSONObject;

public class Task extends EAMObject
{
	public Task(int idToUse)
	{
		super(idToUse);
		subtaskIds = new IdList();
	}
	
	public Task(JSONObject json) throws ParseException
	{
		super(json);
		String subtaskIdsAsString = json.optString(TAG_SUBTASK_IDS, "{}");
		setSubtaskIdsFromString(subtaskIdsAsString);
	}

	private void setSubtaskIdsFromString(String subtaskIdsAsString) throws ParseException
	{
		subtaskIds = new IdList(subtaskIdsAsString);
	}

	public int getType()
	{
		return ObjectType.TASK;
	}

	public void addSubtaskId(int subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public int getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.createClone();
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_SUBTASK_IDS, getSubtaskIdsAsString());
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
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SUBTASK_IDS))
			setSubtaskIdsFromString((String)dataValue);
		else super.setData(fieldTag, dataValue);
	}

	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	
	IdList subtaskIds;

}
