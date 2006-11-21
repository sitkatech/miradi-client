/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIndirectFactor;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeIndirectFactor extends EAMTestCase 
{
	public TestNodeTypeIndirectFactor(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeIndirectFactor factor = new NodeTypeIndirectFactor();
		assertTrue("Not a contributing factor?", factor.isIndirectFactor());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isTarget());
	}
}
