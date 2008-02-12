/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TaxonomyFileLoader;


public abstract class TaxonomyClassificationQuestion extends TwoLevelQuestion
{
	public TaxonomyClassificationQuestion(String fileName)
	{
		super(new TaxonomyFileLoader(fileName));
	}
}
