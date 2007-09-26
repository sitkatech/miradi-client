/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

public class TwoLevelEntry
{
	
	public TwoLevelEntry(String code, String description)
	{
		entryCode = code;
		taxonomyDescription = description;
	}

	public String getEntryCode()
	{
		return entryCode;
	}

	public String getEntryDescription()
	{
		return taxonomyDescription;
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

	
	String entryCode;
	String taxonomyDescription;
	
}
