/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.text.ParseException;

import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.main.EAM;

public class CodeListData extends ObjectData
{
	public CodeListData()
	{
		codes = new CodeList();
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
		return codes.toString();
	}
	
	public void set(CodeList newCodes)
	{
		codes = newCodes;
	}
	
	public CodeList getCodeList()
	{
		return codes;
	}
	
	public int size()
	{
		return codes.size();
	}
	
	public String get(int index)
	{
		return codes.get(index);
	}
	
	public void add(String code)
	{
		codes.add(code);
	}
	
	public void removeCode(String code)
	{
		codes.removeCode(code);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeListData))
			return false;
		
		CodeListData other = (CodeListData)rawOther;
		return codes.equals(other.codes);
	}

	public int hashCode()
	{
		return codes.hashCode();
	}
	
	
	CodeList codes;
}
