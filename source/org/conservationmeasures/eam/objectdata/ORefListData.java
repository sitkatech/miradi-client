/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.martus.util.UnicodeWriter;

public class ORefListData extends ObjectData
{
	public ORefListData(String tagToUse)
	{
		super(tagToUse);
		objectReferenceList = new ORefList();
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof ORefListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	public String get()
	{
		return objectReferenceList.toString();
	}

	public ORefList getORefList()
	{
		return objectReferenceList;
	}
	
	public ORefList getRefList()
	{
		return getORefList();
	}
	
	public ORefList getORefList(int objectTypeToFilterOn)
	{
		return objectReferenceList.filterByType(objectTypeToFilterOn);
	}
	
	public int hashCode()
	{
		return toString().hashCode();
	}

	public void set(String newValue) throws Exception
	{
		set(new ORefList(newValue));	
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		objectReferenceList.toXml(out);
	}

	private void set(ORefList objectReferenceToUse)
	{
		objectReferenceList = objectReferenceToUse;
	}
	
	public boolean isORefListData()
	{
		return true;
	}

	private ORefList objectReferenceList;
}
