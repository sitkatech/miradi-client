/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.EcoRegionsFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class WwfEcoRegionsQuestion extends TwoLevelQuestion
{
	public WwfEcoRegionsQuestion(String tagToUse)
	{
		super(tagToUse, "EcoRegions", new EcoRegionsFileLoader(TwoLevelFileLoader.WWF_ECO_REGIONS_FILE));
	}
}
