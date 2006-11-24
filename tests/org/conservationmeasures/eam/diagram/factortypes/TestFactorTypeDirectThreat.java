/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeDirectThreat;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestFactorTypeDirectThreat extends EAMTestCase 
{
	public TestFactorTypeDirectThreat(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		FactorTypeDirectThreat factor = new FactorTypeDirectThreat();
		assertTrue("Not a Direct Threat?", factor.isDirectThreat());
		assertFalse(factor.isTarget());
		assertFalse(factor.isStrategy());
		assertFalse(factor.isContributingFactor());
	}
}
