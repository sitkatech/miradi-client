/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;

public class ORefList
{
	public ORefList()
	{
		this(new Vector());
	}
	
	public ORefList(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	public ORefList(EnhancedJsonObject json)
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_REFERENCES);
		for(int i = 0; i < array.length(); ++i)
			add(new ORef(array.getJson(i)));
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < data.size(); i++)
		{
			array.put(get(i).toJson());
		}
		json.put(TAG_REFERENCES, array);
		return json;
	}
	
	public void add(ORef objectReferenceToUse)
	{
		data.add(objectReferenceToUse);
	}
	
	public ORef get(int index)
	{
		return (ORef)data.get(index);
	}
		
	private ORefList(List listToUse)
	{
		data = new Vector(listToUse);
	}
		
	public String toString()
	{
		return toJson().toString();
	}
	
	public boolean equals(Object other)
	{	
		if (! (other instanceof ORefList))
			return false;
		
		return other.toString().equals(toString());	
	}
	
	public boolean contains(ORef objectRef)
	{
		return data.contains(objectRef);
	}
	
	public int hashCode()
	{
		return data.hashCode();
	}
		
	public int size()
	{
		return data.size();
	}
	
	private Vector data;
	private static final String TAG_REFERENCES = "References";
}
