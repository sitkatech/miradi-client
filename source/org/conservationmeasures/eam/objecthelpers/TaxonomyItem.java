/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

public class TaxonomyItem
{
	boolean nodeTypeIindicator;
	
	public TaxonomyItem(String code, String description, boolean nodeTypeIindicator)
	{
		this.nodeTypeIindicator = nodeTypeIindicator;
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
	
	public boolean isLeaf() {
		return nodeTypeIindicator;
	}
	
	String taxonomyCode;

	String taxonomy_Description;
	
}
