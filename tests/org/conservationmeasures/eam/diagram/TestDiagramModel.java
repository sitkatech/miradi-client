/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Set;

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

	public void testIsNode() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		Linkage link = model.createLinkage(Node.INVALID_ID, model.getNodeId(threat), model.getNodeId(goal));
		assertTrue("threat isn't a node?", model.isNode(threat));
		assertTrue("goal isn't a node?", model.isNode(goal));
		assertFalse("linkage is a node?", model.isNode(link));
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		assertFalse("already linked?", model.hasLinkage(threat, goal));
		model.createLinkage(Node.INVALID_ID, model.getNodeId(threat), model.getNodeId(goal));
		assertTrue("not linked?", model.hasLinkage(threat, goal));
		assertTrue("reverse link not detected?", model.hasLinkage(goal, threat));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat1 = model.createNode(Node.TYPE_THREAT);
		Node threat2 = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		Linkage linkage1 = model.createLinkage(Node.INVALID_ID, model.getNodeId(threat1), model.getNodeId(goal));
		Linkage linkage2 = model.createLinkage(Node.INVALID_ID, model.getNodeId(threat2), model.getNodeId(goal));
		Set found = model.getLinkages(goal);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
}
