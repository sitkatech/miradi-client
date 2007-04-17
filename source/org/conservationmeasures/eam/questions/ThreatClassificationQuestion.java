/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;

public class ThreatClassificationQuestion  extends ClassificationQuestion
{
	public ThreatClassificationQuestion(String tag)
	{
		super(tag, taxonomyFile);
	}

	private static String taxonomyFile = TaxonomyLoader.THREAT_TAXONOMIES_FILE;
}
