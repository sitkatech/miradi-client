/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import java.text.ParseException;

import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.main.EAM;

public class CodeListData extends ObjectData
{
	public CodeListData()
	{
		ids = new CodeList();
	}
	
	public CodeListData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch (ParseException e)
		{
			EAM.logDebug("CodeListData ignoring invalid: " + valueToUse);
		}
	}

	public void set(String newValue) throws ParseException
	{
		set(new CodeList(newValue));
	}
	
	public String get()
	{
		return ids.toString();
	}
	
	public void set(CodeList newIds)
	{
		ids = newIds;
	}
	
	public CodeList getCodeList()
	{
		return ids;
	}
	
	public int size()
	{
		return ids.size();
	}
	
	public String get(int index)
	{
		return ids.get(index);
	}
	
	public void add(String id)
	{
		ids.add(id);
	}
	
	public void removeId(String id)
	{
		ids.removeId(id);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeListData))
			return false;
		
		CodeListData other = (CodeListData)rawOther;
		return ids.equals(other.ids);
	}

	public int hashCode()
	{
		return ids.hashCode();
	}
	
	
	CodeList ids;
}
