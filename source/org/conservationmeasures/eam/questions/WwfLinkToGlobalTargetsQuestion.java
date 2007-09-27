/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;
import org.conservationmeasures.eam.objecthelpers.WwfLinkToGlobalTargetsFileLoader;

public class WwfLinkToGlobalTargetsQuestion extends TwoLevelQuestion
{
	public WwfLinkToGlobalTargetsQuestion(String tagToUse)
	{
		super(tagToUse, "WwfLinkToGlobalTargets", new WwfLinkToGlobalTargetsFileLoader(TwoLevelFileLoader.WWF_LINK_TO_GLOBAL_TARGETS));
	}
}
