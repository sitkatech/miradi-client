
/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeStress;
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
		assertEquals(Color.MAGENTA, factor.getColor());
		assertFalse(factor.isTarget());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIndirectFactor());
	}
}
