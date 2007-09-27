/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TaxonomyFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;


public class WwfManagingOfficesQuestion extends TwoLevelQuestion
{
	public WwfManagingOfficesQuestion(String tag)
	{
		super(tag, "WwfManagingOffices", new TaxonomyFileLoader(TwoLevelFileLoader.WWF_MANAGING_OFFICES_FILE));
	}
}
