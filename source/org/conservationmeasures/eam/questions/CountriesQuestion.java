/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;
import org.conservationmeasures.eam.objecthelpers.WwfCountriesFileLoader;

public class CountriesQuestion extends TwoLevelQuestion
{
	public CountriesQuestion()
	{
		super(new WwfCountriesFileLoader(TwoLevelFileLoader.COUNTRIES_FILE));
	}
}
