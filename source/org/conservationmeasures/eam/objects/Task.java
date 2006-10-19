/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Task extends EAMBaseObject
{
	public Task(BaseId idToUse)
	{
		super(idToUse);
		subtaskIds = new IdListData();
		resourceIds = new IdListData();
	}
	
	public Task(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
		subtaskIds = new IdListData(json.optString(TAG_SUBTASK_IDS));
		resourceIds = new IdListData(json.optString(TAG_RESOURCE_IDS));
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
		return subtaskIds.getIdList().createClone();
	}
	
	public int getResourceCount()
	{
		return resourceIds.size();
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds.getIdList().createClone();
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_SUBTASK_IDS, subtaskIds.toString());
		json.put(TAG_RESOURCE_IDS, resourceIds.toString());
		return json;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SUBTASK_IDS))
			return subtaskIds.get();
		
		if(fieldTag.equals(TAG_RESOURCE_IDS))
			return resourceIds.get();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SUBTASK_IDS))
			subtaskIds.set(dataValue);
		else if(fieldTag.equals(TAG_RESOURCE_IDS))
			resourceIds.set(dataValue);
		else super.setData(fieldTag, dataValue);
	}

	
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_RESOURCE_IDS = "ResourceIds";
	
	IdListData subtaskIds;
	IdListData resourceIds;
}
