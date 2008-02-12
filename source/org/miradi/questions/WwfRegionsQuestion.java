/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.RegionsFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class WwfRegionsQuestion extends TwoLevelQuestion
{
	public WwfRegionsQuestion()
	{
		super(new RegionsFileLoader(TwoLevelFileLoader.WWF_REGIONS_FILE));
	}
}
