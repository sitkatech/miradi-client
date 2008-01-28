/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TncFreshwaterEcoRegionFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TncFreshwaterEcoRegionQuestion extends TwoLevelQuestion
{
	public TncFreshwaterEcoRegionQuestion(String tagToUse)
	{
		super(tagToUse, new TncFreshwaterEcoRegionFileLoader(TwoLevelFileLoader.TNC_FRESHWATER_ECO_REGION_FILE));
	}
}
