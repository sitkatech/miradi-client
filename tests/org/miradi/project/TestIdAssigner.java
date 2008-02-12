/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;

public class TestIdAssigner extends TestCaseEnhanced
{
	public TestIdAssigner(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();

		assertEquals("didn't start at zero?", 0, idAssigner.takeNextId().asInt());
		assertEquals("didn't increment?", 1, idAssigner.takeNextId().asInt());
		
		assertEquals("didn't use next available?", 2, idAssigner.obtainRealId(new BaseId(-1)).asInt());
		assertEquals("didn't keep available value?", 3, idAssigner.obtainRealId(new BaseId(3)).asInt());
		BaseId force = new BaseId(100);
		idAssigner.obtainRealId(force);
		assertEquals("didn't update next available?", force.asInt()+1, idAssigner.takeNextId().asInt());
		
		idAssigner.clear();
		assertEquals("didn't reset to zero?", 0, idAssigner.takeNextId().asInt());
	}
	
}
