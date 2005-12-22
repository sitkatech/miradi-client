
/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeStress;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeStress extends EAMTestCase 
{
	public TestNodeTypeStress(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeStress factor = new NodeTypeStress();
		assertTrue("Not a Stress Factor?", factor.isStress());
		assertFalse(factor.isTarget());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIndirectFactor());
	}
}
