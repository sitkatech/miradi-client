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
		taxonomyCode = code;
		taxonomyDescription = description;
	}

	public String getTaxonomyCode()
	{
		return taxonomyCode;
	}

	public String getTaxonomyDescription()
	{
		return taxonomyDescription;
	}

	public String toString()
	{
		return getTaxonomyDescription();

	}
	
	public boolean isLeaf() 
	{
		if(isEmptyItem())
			return true;
		if(taxonomyCode.indexOf(".") >= 0)
			return true;
		return false;
	}

	private boolean isEmptyItem()
	{
		return taxonomyCode.length()==0;
	}

	
	String taxonomyCode;
	String taxonomyDescription;
	
}
