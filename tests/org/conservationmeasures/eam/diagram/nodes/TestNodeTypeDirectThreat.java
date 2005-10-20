/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeDirectThreat;
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
		assertEquals(Color.PINK, factor.getColor());
		assertFalse(factor.isTarget());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isStress());
		assertFalse(factor.isIndirectFactor());
	}
}
