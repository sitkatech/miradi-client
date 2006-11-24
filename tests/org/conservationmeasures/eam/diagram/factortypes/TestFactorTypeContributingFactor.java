/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeContributingFactor;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestFactorTypeContributingFactor extends EAMTestCase 
{
	public TestFactorTypeContributingFactor(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		FactorTypeContributingFactor factor = new FactorTypeContributingFactor();
		assertTrue("Not a contributing factor?", factor.isContributingFactor());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isStrategy());
		assertFalse(factor.isTarget());
	}
}
