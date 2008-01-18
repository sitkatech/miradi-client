/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.ids;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;
import org.martus.util.UnicodeWriter;

public class IdList
{
	public IdList(int type)
	{
		this(type, new Vector());
	}
	
	public IdList(IdList copyFrom)
	{
		this(copyFrom.idListType, new Vector(copyFrom.data));
	}
	
	public IdList(int objectTypeToStore, BaseId[] ids)
	{
		this(objectTypeToStore);
		for(int i = 0; i < ids.length; ++i)
			add(ids[i]);
	}
	
	public IdList(int type, String listAsJsonString) throws ParseException
	{
		this(type, new EnhancedJsonObject(listAsJsonString));
		idListType = type;
	}

	public IdList(int objectTypeToStore, EnhancedJsonObject json)
	{
		this(objectTypeToStore);
		EnhancedJsonArray array = json.optJsonArray(TAG_IDS);
		if(array == null)
			array = new EnhancedJsonArray();
		for(int i = 0; i < array.length(); ++i)
			add(new BaseId(array.getInt(i)));
		
	}
	
	public IdList(int objectTypeToStore, List dataToUse)
	{
		data = new Vector(dataToUse);
		idListType = objectTypeToStore;
	}
	
	public int getObjectType()
	{
		return idListType;
	}
	
	public int size()
	{
		return data.size();
	}
	
	public void clear()
	{
		data.clear();
	}
	
	public boolean isEmpty()
	{
		return (size() == 0);
	}
	
	public void addRef(ORef ref) throws Exception
	{
		if (idListType != ref.getObjectType())
			throw new Exception("trying to add wrong type to idList");
		
		add(ref.getObjectId());
	}
	
	public void add(BaseId id)
	{
		data.add(id);
	}
	
	public void add(int id)
	{
		add(new BaseId(id));
	}
	
	public void addAll(IdList otherList)
	{
		for(int i = 0; i < otherList.size(); ++i)
			add(otherList.get(i));
	}
	
	public void insertAt(BaseId id, int at)
	{
		data.insertElementAt(id, at);
	}
	
	public ORef getRef(int index)
	{
		return new ORef(idListType, get(index));
	}
	
	public BaseId get(int index)
	{
		return (BaseId)data.get(index);
	}
	
	public boolean contains(ORef ref)
	{
		if (idListType != ref.getObjectType())
			return false;
		
		return contains(ref.getObjectId());
	}
	
	public boolean contains(BaseId id)
	{
		return data.contains(id);
	}
	
	public int find(BaseId id)
	{
		return data.indexOf(id);
	}
	
	public void removeId(BaseId id)
	{
		if(!data.contains(id))
			throw new RuntimeException("Attempted to remove non-existant Id: " + id + " from: " + toString());
		data.remove(id);
	}
	
	public void subtract(IdList other)
	{
		for(int i = 0; i < other.size(); ++i)
		{
			BaseId id = other.get(i);
			if(contains(id))
				removeId(id);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for(int i = 0; i < size(); ++i)
			array.appendInt(get(i).asInt());
		json.put(TAG_IDS, array);
		return json;
	}
	
	public String toString()
	{
		if(size() == 0)
			return "";
		return toJson().toString();
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof IdList))
			return false;
		
		IdList other = (IdList)rawOther;
		return data.equals(other.data);
	}
	
	public int hashCode()
	{
		return data.hashCode();
	}
	
	public IdList createClone()
	{
		return new IdList(this);
	}
	
	public void sort()
	{
		Collections.sort(data);
	}
	
	public int[] toIntArray()
	{
		int[] intArray = new int[size()];
		for (int i = 0; i < size(); ++i)
		{
			intArray[i] = get(i).asInt();
		}
			
		return intArray;
	}

	public void toXml(UnicodeWriter out) throws IOException
	{
		for(int i = 0; i < size(); ++i)
		{
			if(i > 0)
				out.write(",");
			out.write(get(i).toString());
		}
	}

	private static final String TAG_IDS = "Ids";

	private Vector data;
	private int idListType;
}
