/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TncMarineEcoRegionFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class TncMarineEcoRegionQuestion extends TwoLevelQuestion
{
	public TncMarineEcoRegionQuestion()
	{
		super(new TncMarineEcoRegionFileLoader(TwoLevelFileLoader.TNC_MARINE_ECO_REGION_FILE));
	}
}
