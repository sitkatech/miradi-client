/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeDirectThreat extends EAMTestCase 
{
	public TestNodeTypeDirectThreat(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeDirectThreat factor = new NodeTypeDirectThreat();
		assertTrue("Not a Direct Threat?", factor.isDirectThreat());
		assertFalse(factor.isTarget());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isIndirectFactor());
	}
}
