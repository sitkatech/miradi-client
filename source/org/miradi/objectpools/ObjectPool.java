/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORefList;

public class ObjectPool
{
	public ObjectPool(int objectTypeToStore)
	{
		objectType = objectTypeToStore;
		map = new HashMap();
	}
	
	public int getObjectType()
	{
		return objectType;
	}
	
	public int size()
	{
		return map.size();
	}
	
	public BaseId[] getIds()
	{
		return (BaseId[])getRawIds().toArray(new BaseId[0]);
	}
	
	public IdList getIdList()
	{
		return new IdList(objectType, getIds());
	}
	
	public ORefList getRefList()
	{
		return new ORefList(objectType, getIdList());
	}

	public void put(BaseId id, Object obj)
	{
		if(map.containsKey(id))
			throw new RuntimeException("Id Already Exists: " + id.asInt() + " in " + getClass().getName());
		
		map.put(id, obj);
	}
	
	Set getRawIds()
	{
		return map.keySet();
	}
	
	Collection getValues()
	{
		return map.values();
	}

	public Object getRawObject(BaseId id)
	{
		return map.get(id);
	}
	
	public void remove(BaseId id)
	{
		map.remove(id);
	}

	private int objectType;
	private HashMap map;
}
