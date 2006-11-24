/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.FactorTypeContributingFactor;
import org.conservationmeasures.eam.testall.EAMTestCase;

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
