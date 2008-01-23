/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.martus.util.UnicodeWriter;

public class BaseIdData extends ObjectData
{
	public BaseIdData(int objectTypeToStore)
	{
		objectType = objectTypeToStore;
		id = BaseId.INVALID;
	}
	
	public String get()
	{
		if(id.isInvalid())
			return "";
		return id.toString();
	}
	
	public ORef getRef()
	{
		return new ORef(objectType, id);
	}
	
	public ORefList getRefList()
	{
		ORefList list = new ORefList();
		list.add(getRef());
		return list;
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
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		new ORef(objectType, id).toXml(out);
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

	int objectType;
	BaseId id;
}
