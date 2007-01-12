/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

public class TaxonomyItem
{
	
	public TaxonomyItem(String code, String description)
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
