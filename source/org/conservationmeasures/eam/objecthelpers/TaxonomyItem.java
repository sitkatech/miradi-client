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
	
	public boolean isLeaf() {
		if(isNonSelectedItem())
			return true;
		if(taxonomyCode.indexOf(".") >= 0)
			return true;
		return false;
	}

	private boolean isNonSelectedItem()
	{
		return taxonomyCode.length()==0;
	}

	
	String taxonomyCode;
	String taxonomyDescription;
	boolean isLeafFlag;
	
}
