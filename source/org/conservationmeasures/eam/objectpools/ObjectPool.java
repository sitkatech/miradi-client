/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import java.util.HashMap;
import java.util.Set;

import org.conservationmeasures.eam.ids.BaseId;

public class ObjectPool
{
	public ObjectPool()
	{
		map = new HashMap();
	}
	
	public int size()
	{
		return map.size();
	}
	
	public BaseId[] getIds()
	{
		return (BaseId[])getRawIds().toArray(new BaseId[0]);
	}

	public void put(BaseId id, Object obj)
	{
		map.put(id, obj);
	}
	
	Set getRawIds()
	{
		return map.keySet();
	}
	
	public Object getRawObject(BaseId id)
	{
		return map.get(id);
	}
	
	public void remove(BaseId id)
	{
		map.remove(id);
	}

	
	HashMap map;
}
