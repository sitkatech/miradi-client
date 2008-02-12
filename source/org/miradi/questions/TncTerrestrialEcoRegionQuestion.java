/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TncTerrestrialEcoRegionFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class TncTerrestrialEcoRegionQuestion extends TwoLevelQuestion
{
	public TncTerrestrialEcoRegionQuestion()
	{
		super(new TncTerrestrialEcoRegionFileLoader(TwoLevelFileLoader.TNC_TERRESTRIAL_ECO_REGION_FILE));
	}
}
