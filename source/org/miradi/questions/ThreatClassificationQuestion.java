/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.TwoLevelFileLoader;

public class ThreatClassificationQuestion  extends TaxonomyClassificationQuestion
{
	public ThreatClassificationQuestion()
	{
		super(taxonomyFile);
	}

	private static String taxonomyFile = TwoLevelFileLoader.THREAT_TAXONOMIES_FILE;
}
