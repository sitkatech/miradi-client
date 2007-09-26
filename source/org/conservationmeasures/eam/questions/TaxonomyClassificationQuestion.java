/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public abstract class TaxonomyClassificationQuestion extends TwoLevelQuestion
{
	public TaxonomyClassificationQuestion(String tag, String fileName)
	{
		super(tag, "Taxonomy Classifications", fileName);
	}
}
