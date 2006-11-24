/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.main.EAMTestCase;

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
