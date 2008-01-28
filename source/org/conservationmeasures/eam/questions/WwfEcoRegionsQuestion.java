/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.EcoRegionsFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class WwfEcoRegionsQuestion extends TwoLevelQuestion
{
	public WwfEcoRegionsQuestion(String tagToUse)
	{
		super(tagToUse, new EcoRegionsFileLoader(TwoLevelFileLoader.WWF_ECO_REGIONS_FILE));
	}
}
