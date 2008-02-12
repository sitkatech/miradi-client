/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TaxonomyFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class StrategyTaxonomyQuestion extends TwoLevelQuestion
{
	public StrategyTaxonomyQuestion()
	{
		super(new TaxonomyFileLoader(TwoLevelFileLoader.STRATEGY_TAXONOMIES_FILE));
	}
}
