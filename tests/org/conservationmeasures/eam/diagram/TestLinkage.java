/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeGoal;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLinkage extends EAMTestCase
{
	public TestLinkage(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Node threat = new Node(new NodeTypeThreat());
		Node goal = new Node(new NodeTypeGoal());
		Linkage linkage = new Linkage(threat, goal);
		assertEquals("didn't remember from?", threat, linkage.getFromNode());
		assertEquals("didn't remember to?", goal, linkage.getToNode());

		assertEquals("source not the port of from?", threat.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", goal.getPort(), linkage.getTarget());
	}
}
