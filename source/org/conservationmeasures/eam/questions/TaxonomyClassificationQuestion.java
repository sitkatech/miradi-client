/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TaxonomyFileLoader;


public abstract class TaxonomyClassificationQuestion extends TwoLevelQuestion
{
	public TaxonomyClassificationQuestion(String tag, String fileName)
	{
		super(tag, new TaxonomyFileLoader(fileName));
	}
}
