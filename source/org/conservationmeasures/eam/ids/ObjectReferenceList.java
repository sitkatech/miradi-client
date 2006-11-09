/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ids;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.objects.ObjectReference;
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
		EnhancedJsonArray array = json.getJsonArray(TAG_REFERENCES);
		for(int i = 0; i < array.length(); ++i)
			add(new ObjectReference(array.getJson(i)));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < data.size(); i++)
		{
			array.put(get(i).toJson());
		}
		return json;
	}
	
	private void add(ObjectReference objectReferenceToUse)
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
	
	private Vector data;
	private static final String TAG_REFERENCES = "references";
}
