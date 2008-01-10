/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TncFreshwaterEcoRegionFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TncFreshwaterEcoRegionQuestion extends TwoLevelQuestion
{
	public TncFreshwaterEcoRegionQuestion(String tagToUse)
	{
		super(tagToUse, "Freshwater Eco Region", new TncFreshwaterEcoRegionFileLoader(TwoLevelFileLoader.TNC_FRESHWATER_ECO_REGION_FILE));
	}
}
