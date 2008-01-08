/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;
import org.conservationmeasures.eam.objecthelpers.WwfCountriesFileLoader;

public class CountriesQuestion extends TwoLevelQuestion
{
	public CountriesQuestion(String tagToUse)
	{
		super(tagToUse, "Countries", new WwfCountriesFileLoader(TwoLevelFileLoader.COUNTRIES_FILE));
	}
}
