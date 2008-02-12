/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
