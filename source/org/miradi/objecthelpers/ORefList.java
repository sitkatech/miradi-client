/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.martus.util.UnicodeWriter;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objects.BaseObject;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class ORefList
{
	public ORefList()
	{
		this(new Vector());
	}
	
	public ORefList(BaseObject baseObject)
	{
		this(baseObject.getRef());
	}
	
	public ORefList(ORef orefToAdd)
	{
		this();
		add(orefToAdd);
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
	
	public ORefList(BaseObject[] baseObjects)
	{
		this();
		for (int i = 0; i < baseObjects.length; ++i)
		{
			add(baseObjects[i].getRef());
		}
	}
	
	public ORefList(int objectType, IdList idList)
	{
		this();
		for (int i=0; i<idList.size(); ++i)
			add(new ORef(objectType,idList.get(i)));
	}

	public ORefList(ORefSet referringObjects)
	{
		this(referringObjects.toArray(new ORef[0]));
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
	
	public void add(int index, ORef objectReferenceToUse)
	{
		data.add(index, objectReferenceToUse);
	}
	
	public void addAll(ORefList otherList)
	{
		for(int i = 0; i < otherList.size(); ++i)
			add(otherList.get(i));
	}
	
	public void addAll(ORef[] otherList)
	{
		addAll(new ORefList(otherList));
	}
	
	public void remove(ORef oRefToRemove)
	{
		data.remove(oRefToRemove);
	}
	
	public void removeAll(ORefList refsToRemove)
	{
		data.removeAll(refsToRemove.data);
	}

	public ORef get(int index)
	{
		return data.get(index);
	}
		
	private ORefList(List listToUse)
	{
		data = new Vector(listToUse);
	}
		
	public ORef[] toArray()
	{
		return data.toArray(new ORef[0]);
	}
	
	public String toString()
	{
		if(size() == 0)
			return "";
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
	
	public boolean containsAnyOf(ORefList otherList)
	{
		return getOverlappingRefs(otherList).size() > 0;
	}
	
	public ORefList getOverlappingRefs(ORefList otherList)
	{
		ORefList overlappingRefs = new ORefList();
		for (int i = 0; i < data.size(); ++i)
		{
			ORef thisRef = data.get(i);
			if (otherList.contains(thisRef))
				overlappingRefs.add(data.get(i));
		}
		
		return overlappingRefs;		
	}
	
	public int find(ORef oref)
	{
	  for (int i=0; i<data.size(); ++i)
	  {
		  if (data.get(i).equals(oref))
			  return i;
	  }
	  return -1;
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
	
	public void sort(Comparator<ORef> comparator)
	{
		Collections.sort(data, comparator);
	}
	
	public void sort()
	{
		Collections.sort(data);
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.writeln("<RefList>");
		for(int i = 0; i < size(); ++i)
		{
			out.write("<Ref>");
			get(i).toXml(out);
			out.writeln("</Ref>");
		}
		out.writeln("</RefList>");
	}
	
	public IdList convertToIdList(int objectType)
	{
		IdList convertedList = new IdList(objectType);
		for (int i = 0; i < data.size(); ++i)
		{
			ORef refToConvert = data.get(i);
			if (refToConvert.getObjectType() != objectType)
				throw new RuntimeException("Found wrong type " + refToConvert.getObjectType() + " in ORefList thought to contain " + objectType);
			
			convertedList.add(refToConvert.getObjectId());
		}
		
		return convertedList;
	}
	
	public static ORefList subtract(ORefList bigger, ORefList smaller)
	{
		ORefList result = new ORefList();
		for(int i = 0; i < bigger.size(); ++i)
		{
			ORef ref = bigger.get(i);
			if(!smaller.contains(ref))
				result.add(ref);
		}
		return result;
	}

	public ORefList filterByType(int typeToFilterOn)
	{
		ORefList newList = new ORefList();
		for(int i = 0; i < data.size(); ++i)
		{
			if (get(i).getObjectType() == typeToFilterOn)
				newList.add(data.get(i));
		}
		return newList;
	}
	
	public ORef getRefForTypes(int[] objectTypes)
	{
		for (int dataIndex = 0; dataIndex < data.size(); ++dataIndex)
		{
			ORef oref = data.get(dataIndex);
			for (int typeIndex = 0; typeIndex < objectTypes.length; ++typeIndex)
			{
				if (objectTypes[typeIndex] == oref.getObjectType())
					return  oref;
			}
		}
		
		return ORef.INVALID;
	}
	
	public ORef getRefForType(int objectType)
	{
		for (int i = 0; i < data.size(); ++i)
		{
			ORef oref = data.get(i);
			if (objectType == oref.getObjectType())
				return  oref;
		}
		return new ORef(objectType, BaseId.INVALID);
	}
	
	private Vector<ORef> data;
	private static final String TAG_REFERENCES = "References";
}
