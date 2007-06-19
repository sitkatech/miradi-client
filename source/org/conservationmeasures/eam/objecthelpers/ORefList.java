/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;

public class ORefList
{
	public ORefList()
	{
		this(new Vector());
	}
	
	public ORefList(ORefList copyFrom)
	{
		this(copyFrom.toJson());
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
	
	public ORefList(ORef[] orefs)
	{
		this();
		for (int i=0; i<orefs.length; ++i)
			add(orefs[i]);
	}
	
	public ORefList(int objectType, IdList idList)
	{
		this();
		for (int i=0; i<idList.size(); ++i)
			add(new ORef(objectType,idList.get(i)));
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
	
	
	public void addAll(ORefList otherList)
	{
		for(int i = 0; i < otherList.size(); ++i)
			data.add(otherList.get(i));
	}
	
	public void remove(ORef oRefToRemove)
	{
		data.remove(oRefToRemove);
	}
	
	public ORef get(int index)
	{
		return (ORef)data.get(index);
	}
		
	private ORefList(List listToUse)
	{
		data = new Vector(listToUse);
	}
		
	public ORefList extractByType(int objectTypeToFilterOn)
	{
		ORefList newList = new ORefList();
		for(int i = 0; i < data.size(); ++i)
		{
			if (get(i).getObjectType() == objectTypeToFilterOn)
				newList.add((ORef)data.get(i));
		}
		return newList;
	}
	
	public ORef[] toArray()
	{
		return (ORef[]) data.toArray(new ORef[0]);
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
	
	public boolean isEmpty()
	{
		return (size() == 0);
	}
	
	private Vector data;
	private static final String TAG_REFERENCES = "References";
}
