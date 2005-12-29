/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

public class Manifest
{
	public Manifest()
	{
		data = new JSONObject();
		data.put(ProjectServer.OBJECT_TYPE, ProjectServer.LINKAGE_MANIFEST);
	}
	
	public Manifest(JSONObject copyFrom)
	{
		data = copyFrom;
	}
	
	public void put(String key)
	{
		data.put(key, true);
	}
	
	public void put(int key)
	{
		put(Integer.toString(key));
	}
	
	public boolean has(String key)
	{
		return data.has(key);
	}

	public boolean has(int key)
	{
		return has(Integer.toString(key));
	}
	
	public void remove(String key)
	{
		data.remove(key);
	}
	
	public void remove(int key)
	{
		remove(Integer.toString(key));
	}
	
	public int size()
	{
		return data.length() - 1;
	}
	
	public void write(File dest) throws IOException
	{
		JSONFile.write(dest, data);
	}
	
	JSONObject data;
}
