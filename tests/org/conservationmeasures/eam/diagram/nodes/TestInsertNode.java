/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestInsertNode extends EAMTestCase
{
	public TestInsertNode(String name)
	{
		super(name);
	}
	
	public void testBadInsert() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(createTempDirectory());
		Command insertCommand = new CommandInsertNode(-1);
		try
		{
			EAM.setLogToString();
			insertCommand.execute(project);
			fail("should have thrown");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
	}

	public void testInsertTarget() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(createTempDirectory());
		Command insertCommand = new CommandInsertNode(Node.TYPE_TARGET);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		Node insertedNode = (Node)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		Node foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a target?", foundNode.isTarget());
	}

	public void testInsertFactor() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(createTempDirectory());
		Command insertCommand = new CommandInsertNode(Node.TYPE_FACTOR);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		Node insertedNode = (Node)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		Node foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a factor?", foundNode.isFactor());
	}

	public void testInsertIntervention() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(createTempDirectory());
		Command insertCommand = new CommandInsertNode(Node.TYPE_INTERVENTION);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		Node insertedNode = (Node)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		Node foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an intervention?", foundNode.isIntervention());
	}
}
