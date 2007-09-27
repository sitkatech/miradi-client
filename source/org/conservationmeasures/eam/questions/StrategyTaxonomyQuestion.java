/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TaxonomyFileLoader;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class StrategyTaxonomyQuestion extends TwoLevelQuestion
{
	public StrategyTaxonomyQuestion(String tag)
	{
		super(tag, "Strategy Taxonomy Translations", new TaxonomyFileLoader(TwoLevelFileLoader.STRATEGY_TAXONOMIES_FILE));
	}
}
