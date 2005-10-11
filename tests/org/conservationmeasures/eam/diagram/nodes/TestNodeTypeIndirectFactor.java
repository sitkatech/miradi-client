/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

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
		assertTrue("Not a indirect factor?", factor.isIndirectFactor());
		assertEquals(Color.ORANGE, factor.getColor());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isStress());
		assertFalse(factor.isTarget());
	}
}
