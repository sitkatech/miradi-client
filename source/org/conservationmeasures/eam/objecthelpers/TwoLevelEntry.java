/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

public class TwoLevelEntry
{
	
	public TwoLevelEntry(String code, String description)
	{
		entryCode = code;
		entryDescription = description;
	}

	public String getEntryCode()
	{
		return entryCode;
	}

	public String getEntryDescription()
	{
		return entryDescription;
	}

	public String toString()
	{
		return getEntryDescription();
	}
	
	public boolean isLeaf() 
	{
		if(isEmptyItem())
			return true;
		if(entryCode.indexOf(".") >= 0)
			return true;
		return false;
	}

	private boolean isEmptyItem()
	{
		return entryCode.length()==0;
	}

	private String entryCode;
	private String entryDescription;
}
