/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TncOperatingUnitsFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TncOperatingUnitsQuestion extends TwoLevelQuestion
{
	public TncOperatingUnitsQuestion(String tagToUse)
	{
		super(tagToUse, "Operating Unit(s)", new TncOperatingUnitsFileLoader(TwoLevelFileLoader.TNC_OPERATING_UNITS_FILE));
	}
}
