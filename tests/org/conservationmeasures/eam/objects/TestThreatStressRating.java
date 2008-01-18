/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestThreatStressRating extends ObjectTestCase
{
	public TestThreatStressRating(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		ORef stressRef = new ORef(33, new BaseId(30));
		CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef);
		verifyFields(ObjectType.THREAT_STRESS_RATING, extraInfo);
	}
}
