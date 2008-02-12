/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

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
