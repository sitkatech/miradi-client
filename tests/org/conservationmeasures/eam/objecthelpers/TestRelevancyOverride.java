/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestRelevancyOverride extends EAMTestCase
{
	public TestRelevancyOverride(String name)
	{
		super(name);
	}
	
	public void testIsOverride()
	{
		RelevancyOverride override = new RelevancyOverride(ORef.INVALID, true);
		assertTrue(override.isOverride());
	}
}
