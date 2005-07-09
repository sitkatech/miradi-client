/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestInsertNode extends EAMTestCase
{
	public TestInsertNode(String name)
	{
		super(name);
	}

	public void testInsertGoal()
	{
		Point location = new Point(23, 45);
		String text = "a goal";
		Project project = new Project();
		Command insertCommand = new CommandInsertGoal(location, text);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		Node insertedNode = (Node)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getMap());
		assertEquals("wrong x?", location.x, (int)bounds.getX());
		assertEquals("wrong y?", location.y, (int)bounds.getY());
		assertContains("wrong text?", text, (String)GraphConstants.getValue(insertedNode.getMap()));
		int id = model.getNodeId(insertedNode);
		Node foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a goal?", foundNode.isGoal());
	}

	public void testInsertThreat()
	{
		Point location = new Point(23, 45);
		String text = "a threat";
		Project project = new Project();
		Command insertCommand = new CommandInsertThreat(location, text);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		Node insertedNode = (Node)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getMap());
		assertEquals("wrong x?", location.x, (int)bounds.getX());
		assertEquals("wrong y?", location.y, (int)bounds.getY());
		assertContains("wrong text?", text, (String)GraphConstants.getValue(insertedNode.getMap()));
		int id = model.getNodeId(insertedNode);
		Node foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a threat?", foundNode.isThreat());
	}

}
