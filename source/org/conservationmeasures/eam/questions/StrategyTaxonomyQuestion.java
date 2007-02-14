/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;

public class StrategyTaxonomyQuestion extends ChoiceQuestion
{
	public StrategyTaxonomyQuestion(String tag)
	{
		super(tag, "Strategy Taxonomy Translations", getImpactChoices());
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		try 
		{
			TaxonomyItem[] taxonomyItems = TaxonomyLoader.load(TaxonomyLoader.STRATEGY_TAXONOMIES_FILE);
			ChoiceItem[] choiceItems =  new ChoiceItem[taxonomyItems.length]; 
			for (int i=0; i<taxonomyItems.length; ++i)
			{
				choiceItems[i] = 
					new ChoiceItem(taxonomyItems[i].getTaxonomyCode(), 
						taxonomyItems[i].getTaxonomyDescription());
			}
			return choiceItems;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ChoiceItem[0];
		}
	}
}
