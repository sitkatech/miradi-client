/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TestRelevancyOverride extends EAMTestCase
{
	public TestRelevancyOverride(String name)
	{
		super(name);
	}
	
	public void testRelevancyOverrideStringConstructor() throws Exception
	{	
		RelevancyOverride valid = new RelevancyOverride(getRandomRef(), true);
		EnhancedJsonObject json = valid.toJson();
		RelevancyOverride fromJson = new RelevancyOverride(json.toString());
		assertEquals("not same ref?", getRandomRef(), fromJson.getRef());
		assertEquals("not same flag?", true, fromJson.isOverride());
	}

	public void testRelevancyOverrideJsonConstructor() throws Exception
	{
		RelevancyOverride valid = new RelevancyOverride(getRandomRef(), true);
		EnhancedJsonObject json = valid.toJson();
		RelevancyOverride fromJson = new RelevancyOverride(json);
		assertEquals("not same ref?", getRandomRef(), fromJson.getRef());
		assertEquals("not same flag?", true, fromJson.isOverride());
	}
	
	public void testEquals()
	{
		RelevancyOverride override1 = new RelevancyOverride(getRandomRef(), true);
		RelevancyOverride override2 = new RelevancyOverride(getRandomRef(), true);
		assertEquals("not the equals?", override1, override2);
	}
	
	public void testIsOverride()
	{
		RelevancyOverride override = new RelevancyOverride(ORef.INVALID, true);
		assertTrue(override.isOverride());
	}
	
	private ORef getRandomRef()
	{
		return new ORef(Cause.getObjectType(), new BaseId(44));
	}
}
