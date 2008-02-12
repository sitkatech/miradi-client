/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TncFreshwaterEcoRegionFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class TncFreshwaterEcoRegionQuestion extends TwoLevelQuestion
{
	public TncFreshwaterEcoRegionQuestion()
	{
		super(new TncFreshwaterEcoRegionFileLoader(TwoLevelFileLoader.TNC_FRESHWATER_ECO_REGION_FILE));
	}
}
