/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.HashMap;
import java.util.Iterator;

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
	
	public int[] getIds()
	{
		int[] ids = new int[size()];
		int nextId = 0;
		
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			Integer idKey = (Integer)it.next();
			ids[nextId++] = idKey.intValue();
		}
		
		return ids;
	}

	public void put(int id, Object obj)
	{
		map.put(createKey(id), obj);
	}
	
	public Object getRawObject(int id)
	{
		return map.get(createKey(id));
	}
	
	public void remove(int id)
	{
		map.remove(createKey(id));
	}

	private Integer createKey(int id)
	{
		return new Integer(id);
	}
	
	
	HashMap map;
}
