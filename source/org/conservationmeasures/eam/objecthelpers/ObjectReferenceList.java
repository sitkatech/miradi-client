/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;

public class ObjectReferenceList
{
	public ObjectReferenceList()
	{
		this(new Vector());
	}
	
	public ObjectReferenceList(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	public ObjectReferenceList(EnhancedJsonObject json)
	{
		this();
		try
		{
			EnhancedJsonArray array = json.optJsonArray(TAG_REFERENCES);
			for(int i = 0; i < array.length(); ++i){
				add(new ObjectReference(array.getJson(i)));
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
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
	
	public void add(ObjectReference objectReferenceToUse)
	{
		data.add(objectReferenceToUse);
	}
	
	public ObjectReference get(int index)
	{
		return (ObjectReference)data.get(index);
	}
		
	private ObjectReferenceList(List listToUse)
	{
		data = new Vector(listToUse);
	}
		
	public String toString()
	{
		return toJson().toString();
	}
	
	public boolean equals(Object other)
	{	
		if (! (other instanceof ObjectReferenceList))
			return false;
		
		return other.toString().equals(toString());	
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
