/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramModel extends EAMTestCase
{
	public TestDiagramModel(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testIsNode()
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createThreatNode();
		Node goal = model.createGoalNode();
		Linkage link = model.createLinkage(threat, goal);
		assertTrue("threat isn't a node?", model.isNode(threat));
		assertTrue("goal isn't a node?", model.isNode(goal));
		assertFalse("linkage is a node?", model.isNode(link));
		assertFalse("object is a node?", model.isNode(new Object()));
	}
}
