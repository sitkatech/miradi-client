/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Cause;
import org.miradi.utils.EnhancedJsonObject;

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
	
	public void testFind()
	{
		ORef ref1 = new ORef(Cause.getObjectType(), new BaseId(44));
		ORef ref2 = new ORef(Cause.getObjectType(), new BaseId(55));
		
		RelevancyOverride override1 = new RelevancyOverride(ref1, true);
		RelevancyOverride override2 = new RelevancyOverride(ref2, false);
		
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(override1);
		relevancyOverrides.add(override2);
		
		assertEquals("didnt find correct relevancyOverride1?", override1, relevancyOverrides.find(ref1));
		assertEquals("didnt find correct relevancyOverride2?", override2, relevancyOverrides.find(ref2));
		assertEquals("found incorrect relevancyOverride?", null, relevancyOverrides.find(ORef.INVALID));
	}
	
	public void testRemove()
	{
		ORef ref1 = new ORef(Cause.getObjectType(), new BaseId(44));
		ORef ref2 = new ORef(Cause.getObjectType(), new BaseId(55));
		
		RelevancyOverride override1 = new RelevancyOverride(ref1, true);
		RelevancyOverride override2 = new RelevancyOverride(ref2, false);
		
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(override1);
		relevancyOverrides.add(override2);
		
		assertEquals("wrong count?", 2, relevancyOverrides.size());
	
		relevancyOverrides.remove(ref1);
		assertEquals("wrong count after remove?", 1, relevancyOverrides.size());
		assertTrue("wrong ref removed?", relevancyOverrides.find(ref2) != null);
	}
}
