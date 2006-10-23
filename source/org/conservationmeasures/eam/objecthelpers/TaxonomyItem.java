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
		isLeafFlag = code.indexOf(".")!=-1;
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
		return isLeafFlag;
	}
	
	String taxonomyCode;
	String taxonomyDescription;
	boolean isLeafFlag;
	
}
