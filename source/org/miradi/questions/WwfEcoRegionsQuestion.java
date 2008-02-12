/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.EcoRegionsFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class WwfEcoRegionsQuestion extends TwoLevelQuestion
{
	public WwfEcoRegionsQuestion()
	{
		super(new EcoRegionsFileLoader(TwoLevelFileLoader.WWF_ECO_REGIONS_FILE));
	}
}
