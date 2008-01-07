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

public class TestRelevancyOverrideSet extends EAMTestCase
{
	public TestRelevancyOverrideSet(String name)
	{
		super(name);
	}
	
	public void testToJson() throws Exception
	{
		RelevancyOverride override1 = new RelevancyOverride(new ORef(Cause.getObjectType(), new BaseId(44)), true);
		RelevancyOverride override2 = new RelevancyOverride(new ORef(Cause.getObjectType(), new BaseId(55)), false);
		
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(override1);
		relevancyOverrides.add(override2);
		
		EnhancedJsonObject relevancyOverridesAsJson = relevancyOverrides.toJson();
		RelevancyOverrideSet buildFromJsonSet = new RelevancyOverrideSet(relevancyOverridesAsJson);
		assertEquals("wrong size?", 2, buildFromJsonSet.size());
		assertContains("not found?", override1, buildFromJsonSet);
		assertContains("not found?", override2, buildFromJsonSet);
	}
}
