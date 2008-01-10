/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TncTerrestrialEcoRegionFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TncTerrestrialEcoRegionQuestion extends TwoLevelQuestion
{
	public TncTerrestrialEcoRegionQuestion(String tagToUse)
	{
		super(tagToUse, "Terrestrial", new TncTerrestrialEcoRegionFileLoader(TwoLevelFileLoader.TNC_TERRESTRIAL_ECO_REGION_FILE));
	}
}
