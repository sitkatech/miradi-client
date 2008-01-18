/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
