/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

public class TaxonomyItem
{
	public TaxonomyItem(String code, String description)
	{
		taxonomyCode = code;
		taxonomy_Description = description;
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
	
	String taxonomyCode;

	String taxonomy_Description;
	
}
