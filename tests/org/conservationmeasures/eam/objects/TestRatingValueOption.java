/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestRatingValueOption extends EAMTestCase
{
	public TestRatingValueOption(String name)
	{
		super(name);
	}

	public void testEquals() throws Exception
	{
		int id = 17;
		ThreatRatingValueOption a = new ThreatRatingValueOption(id, "abc", Color.RED);
		ThreatRatingValueOption b = new ThreatRatingValueOption(id, "def", Color.BLUE);
		ThreatRatingValueOption c = new ThreatRatingValueOption(id + 1, "abc", Color.RED);
		assertTrue("same id isn't equal?", a.equals(b));
		assertTrue("same id isn't equal (reversed)?", b.equals(a));
		assertFalse("not comparing id?", a.equals(c));
		assertFalse("not comparing id (reversed)?", c.equals(a));
		assertFalse("equal to some other class?", a.equals(new Object()));
	}
}
