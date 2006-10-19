/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

public class TaxonomyItem
{
	String taxonomyCode;

	String taxonomy_Description;

	public TaxonomyItem(String code, String description)
	{
		super();
		taxonomyCode = code;
		taxonomy_Description = description;
	}

	public String getTaxonomyCode()
	{
		return taxonomyCode;
	}

	public String getTaxonomy_Description()
	{
		return taxonomy_Description;
	}

	public String toString()
	{
		return getTaxonomy_Description();

	}
}
