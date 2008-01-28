/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.martus.util.UnicodeWriter;

public class IdListData extends ObjectData
{
	public IdListData(String tagToUse, int objectTypeToStore)
	{
		super(tagToUse);
		ids = new IdList(objectTypeToStore);
	}
	
	public void set(String newValue) throws ParseException
	{
		set(new IdList(ids.getObjectType(), newValue));
	}
	
	public String get()
	{
		return ids.toString();
	}
	
	public void set(IdList newIds)
	{
		ids = newIds;
	}
	
	public IdList getIdList()
	{
		return ids;
	}
	
	public ORefList getRefList()
	{
		return new ORefList(ids.getObjectType(), ids);
	}
	
	public int size()
	{
		return ids.size();
	}
	
	public BaseId get(int index)
	{
		return ids.get(index);
	}
	
	public void add(BaseId id)
	{
		ids.add(id);
	}
	
	public void insertAt(BaseId id, int index)
	{
		ids.insertAt(id, index);
	}
	
	public void removeId(BaseId id)
	{
		ids.removeId(id);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof IdListData))
			return false;
		
		IdListData other = (IdListData)rawOther;
		return ids.equals(other.ids);
	}

	public int hashCode()
	{
		return ids.hashCode();
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		ids.toXml(out);
	}
	
	public boolean isIdListData()
	{
		return true;
	}
	
	private IdList ids;
}
