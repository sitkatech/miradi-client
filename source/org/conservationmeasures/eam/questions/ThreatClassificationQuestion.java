/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class ThreatClassificationQuestion  extends TaxonomyClassificationQuestion
{
	public ThreatClassificationQuestion(String tag)
	{
		super(tag, taxonomyFile);
	}

	private static String taxonomyFile = TwoLevelFileLoader.THREAT_TAXONOMIES_FILE;
}
