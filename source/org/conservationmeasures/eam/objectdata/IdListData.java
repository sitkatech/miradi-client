/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;

public class IdListData extends ObjectData
{
	public IdListData()
	{
		ids = new IdList();
	}
	
	public IdListData(String valueToUse) throws ParseException
	{
		set(valueToUse);
	}

	public void set(String newValue) throws ParseException
	{
		if(newValue.length() == 0)
			newValue = "{}";
		set(new IdList(newValue));
	}
	
	public String get()
	{
		return ids.toString();
	}
	
	public void set(IdList newIds)
	{
		ids = newIds;
	}
	
	public IdList getIdList()
	{
		return ids;
	}
	
	public int size()
	{
		return ids.size();
	}
	
	public BaseId get(int index)
	{
		return ids.get(index);
	}
	
	public void add(BaseId id)
	{
		ids.add(id);
	}
	
	public void insertAt(BaseId id, int index)
	{
		ids.insertAt(id, index);
	}
	
	public void removeId(BaseId id)
	{
		ids.removeId(id);
	}
	
	IdList ids;
}
