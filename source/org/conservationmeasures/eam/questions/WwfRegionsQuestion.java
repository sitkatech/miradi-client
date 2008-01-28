/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.RegionsFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class WwfRegionsQuestion extends TwoLevelQuestion
{
	public WwfRegionsQuestion(String tagToUse)
	{
		super(tagToUse, new RegionsFileLoader(TwoLevelFileLoader.WWF_REGIONS_FILE));
	}
}
