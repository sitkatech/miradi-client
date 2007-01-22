/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;

public class IdListData extends ObjectData
{
	public IdListData()
	{
		ids = new IdList();
	}
	
	public IdListData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch (ParseException e)
		{
			EAM.logDebug("IdListData ignoring invalid: " + valueToUse);
		}
	}

	public void set(String newValue) throws ParseException
	{
		set(new IdList(newValue));
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
	
	
	IdList ids;
}
