/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.database;

import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;

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
