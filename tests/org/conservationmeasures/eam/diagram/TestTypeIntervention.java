/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTypeIntervention extends EAMTestCase 
{
	public TestTypeIntervention(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeIntervention factor = new NodeTypeIntervention();
		assertTrue("Not an Intervention?", factor.isIntervention());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isTarget());
		assertFalse(factor.isStress());
		assertFalse(factor.isIndirectFactor());
	}
}
