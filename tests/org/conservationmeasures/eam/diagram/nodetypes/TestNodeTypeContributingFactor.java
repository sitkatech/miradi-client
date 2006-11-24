/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeContributingFactor;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeContributingFactor extends EAMTestCase 
{
	public TestNodeTypeContributingFactor(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeContributingFactor factor = new NodeTypeContributingFactor();
		assertTrue("Not a contributing factor?", factor.isContributingFactor());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isIntervention());
		assertFalse(factor.isTarget());
	}
}
