/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeStrategy;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeTypeStrategy extends EAMTestCase 
{
	public TestNodeTypeStrategy(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeTypeStrategy factor = new NodeTypeStrategy();
		assertTrue("Not a strategy?", factor.isStrategy());
		assertFalse(factor.isDirectThreat());
		assertFalse(factor.isTarget());
		assertFalse(factor.isContributingFactor());
	}
}
