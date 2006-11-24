/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestFactorTypeTarget extends EAMTestCase 
{
	public TestFactorTypeTarget(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		FactorTypeTarget factor = new FactorTypeTarget();
		assertTrue("Not a Target?", factor.isTarget());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isStrategy());
		assertFalse(factor.isContributingFactor());
	}
}
