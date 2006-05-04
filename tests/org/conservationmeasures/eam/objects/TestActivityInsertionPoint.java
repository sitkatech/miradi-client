/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestActivityInsertionPoint extends EAMTestCase
{
	public TestActivityInsertionPoint(String name)
	{
		super(name);
	}

	public void testInvalidInsertionPoint()
	{
		ActivityInsertionPoint aip = new ActivityInsertionPoint();
		assertFalse("was valid?", aip.isValid());
	}
	
	public void testValidInsertionPoint()
	{
		int id = 23;
		int index = 9;
		ActivityInsertionPoint aip = new ActivityInsertionPoint(id, index);
		assertTrue("was invalid?", aip.isValid());
		assertEquals("wrong id?", id, aip.getInterventionId());
		assertEquals("wrong index?", index, aip.getIndex());
	}
}
