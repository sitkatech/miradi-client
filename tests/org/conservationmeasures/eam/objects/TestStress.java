/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestStress extends ObjectTestCase
{
	public TestStress(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.STRESS);
	}
	
	public void testCalculateStressRating() throws Exception
	{
		ORef stressRef = getProject().createObject(Stress.getObjectType());
		Stress stress = (Stress) getProject().findObject(stressRef);
		assertEquals("has value?", "", stress.calculateStressRating());
		
		stress.setData(Stress.TAG_SCOPE, "1");
		assertEquals("has value?", "", stress.calculateStressRating());
		
		stress.setData(Stress.TAG_SEVERITY, "4");
		assertEquals("has value?", 1, stress.calculateStressRating().length());
		assertEquals("is not min value", "1", stress.calculateStressRating());
	}
}
