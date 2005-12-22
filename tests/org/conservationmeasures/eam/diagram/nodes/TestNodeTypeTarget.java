/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeTarget extends EAMTestCase 
{
	public TestNodeTypeTarget(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeTarget factor = new NodeTypeTarget();
		assertTrue("Not a Target?", factor.isTarget());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isStress());
		assertFalse(factor.isIndirectFactor());
	}
}
