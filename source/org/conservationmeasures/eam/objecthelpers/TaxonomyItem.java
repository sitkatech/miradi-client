/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

public class TaxonomyItem
{
	boolean isLeafFlag;
	
	public TaxonomyItem(String code, String description)
	{
		this.isLeafFlag = isLeafFlag;
		taxonomyCode = code;
		taxonomy_Description = description;
		isLeafFlag = code.indexOf(".")!=-1;
	}

	public String getTaxonomyCode()
	{
		return taxonomyCode;
	}

	public String getTaxonomyDescription()
	{
		return taxonomy_Description;
	}

	public String toString()
	{
		return getTaxonomyDescription();

	}
	
	public boolean isLeaf() {
		return isLeafFlag;
	}
	
	String taxonomyCode;

	String taxonomy_Description;
	
}
