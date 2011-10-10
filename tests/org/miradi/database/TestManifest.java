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
package org.miradi.database;

import org.json.JSONObject;
import org.martus.util.TestCaseEnhanced;
import org.miradi.database.Manifest;

public class TestManifest extends TestCaseEnhanced
{
	public TestManifest(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		String sampleType = "whatever";
		Manifest m = new Manifest(sampleType);
		assertEquals(sampleType, m.getObjectType());
		m.put(12);
		
		JSONObject json = m.toJson();
		Manifest got = new Manifest(json);
		assertEquals("wrong type?", m.getObjectType(), got.getObjectType());
		assertEquals("not just one entry?", 1, got.getAllKeys().length);
		assertTrue("wrong entry?", got.has(12));
	}
}
