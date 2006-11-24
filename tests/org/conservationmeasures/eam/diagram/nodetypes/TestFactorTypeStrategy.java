/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeStrategy;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestFactorTypeStrategy extends EAMTestCase 
{
	public TestFactorTypeStrategy(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		FactorTypeStrategy factor = new FactorTypeStrategy();
		assertTrue("Not a strategy?", factor.isStrategy());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isTarget());
		assertFalse(factor.isContributingFactor());
	}
}
