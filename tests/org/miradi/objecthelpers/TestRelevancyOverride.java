/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
