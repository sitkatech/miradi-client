/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class StrategyClassificationQuestion extends TaxonomyClassificationQuestion
{
	public StrategyClassificationQuestion(String tag)
	{
		super(tag, taxonomyFile);
	}
	
	private static String taxonomyFile = TwoLevelFileLoader.STRATEGY_TAXONOMIES_FILE;
}
