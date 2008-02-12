/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.database;

import java.util.Iterator;

import org.json.JSONObject;
import org.miradi.ids.BaseId;
import org.miradi.utils.EnhancedJsonObject;

public class Manifest
{
	public Manifest(String type)
	{
		data = new JSONObject();
		data.put(ProjectServer.OBJECT_TYPE, type);
	}
	
	public Manifest(JSONObject copyFrom)
	{
		data = copyFrom;
	}
	
	public String getObjectType()
	{
		return data.getString(ProjectServer.OBJECT_TYPE);
	}
	
	public BaseId[] getAllKeys()
	{
		BaseId[] keys = new BaseId[size()];
		int at = 0;
		Iterator iter = data.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			if(!key.equals(ProjectServer.OBJECT_TYPE))
				keys[at++] = new BaseId(Integer.parseInt(key));
		}
		
		return keys;
	}
	
	private void put(String key)
	{
		data.put(key, true);
	}
	
	public void put(BaseId key)
	{
		put(key.asInt());
	}
	
	public void put(int key)
	{
		put(Integer.toString(key));
	}
	
	private boolean has(String key)
	{
		return data.has(key);
	}
	
	public boolean has(BaseId id)
	{
		return has(id.asInt());
	}

	public boolean has(int key)
	{
		return has(Integer.toString(key));
	}
	
	private void remove(String key)
	{
		data.remove(key);
	}
	
	public void remove(BaseId key)
	{
		remove(key.asInt());
	}
	
	public void remove(int key)
	{
		remove(Integer.toString(key));
	}
	
	public int size()
	{
		return data.length() - 1;
	}
	
	public EnhancedJsonObject toJson()
	{
		return new EnhancedJsonObject(data);
	}
	
	
	JSONObject data;
}
