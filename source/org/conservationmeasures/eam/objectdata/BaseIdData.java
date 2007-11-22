/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.ids.BaseId;

public class BaseIdData extends ObjectData
{
	public BaseIdData()
	{
		id = BaseId.INVALID;
	}
	
	public String get()
	{
		return id.toString();
	}
	
	public BaseId getId()
	{
		return id;
	}
	
	public void setId(BaseId newId)
	{
		id = newId;
	}

	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
			id = BaseId.INVALID;
		else
			id = new BaseId(Integer.parseInt(newValue));
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof BaseIdData))
			return false;
		
		BaseIdData other = (BaseIdData)rawOther;
		return id.equals(other.id);
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	BaseId id;
}
