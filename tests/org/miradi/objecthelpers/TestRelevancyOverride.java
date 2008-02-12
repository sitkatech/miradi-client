/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objects.Cause;
import org.miradi.utils.EnhancedJsonObject;

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
