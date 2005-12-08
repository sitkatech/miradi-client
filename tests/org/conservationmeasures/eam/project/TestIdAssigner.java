/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.martus.util.TestCaseEnhanced;

public class TestIdAssigner extends TestCaseEnhanced
{
	public TestIdAssigner(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();

		assertEquals("didn't start at zero?", 0, idAssigner.takeNextId());
		assertEquals("didn't increment?", 1, idAssigner.takeNextId());
		
		assertEquals("didn't use next available?", 2, idAssigner.obtainRealId(-1));
		assertEquals("didn't keep available value?", 3, idAssigner.obtainRealId(3));
		int force = 100;
		idAssigner.obtainRealId(force);
		assertEquals("didn't update next available?", force+1, idAssigner.takeNextId());
		
		idAssigner.clear();
		assertEquals("didn't reset to zero?", 0, idAssigner.takeNextId());
	}
	
}
