/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import java.util.HashMap;
import java.util.Iterator;

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
		BaseId[] ids = new BaseId[size()];
		int nextId = 0;
		
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			BaseId idKey = (BaseId)it.next();
			ids[nextId++] = idKey;
		}
		
		return ids;
	}

	public void put(BaseId id, Object obj)
	{
		map.put(id, obj);
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
