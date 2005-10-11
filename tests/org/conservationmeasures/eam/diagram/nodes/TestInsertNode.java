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
import org.conservationmeasures.eam.project.ProjectForTesting;
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
		ProjectForTesting project = new ProjectForTesting(getName());
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
		ProjectForTesting project = new ProjectForTesting(getName());
		Command insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a target?", foundNode.isTarget());
		assertEquals(DiagramNode.TYPE_TARGET, foundNode.getNodeType());
	}

	public void testInsertIndirectFactor() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		Command insertCommand = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an indirect factor?", foundNode.isIndirectFactor());
		assertEquals(DiagramNode.TYPE_INDIRECT_FACTOR, foundNode.getNodeType());
	}

	public void testInsertDirectThreat() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		Command insertCommand = new CommandInsertNode(DiagramNode.TYPE_DIRECT_THREAT);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an direct Threat?", foundNode.isDirectThreat());
		assertEquals(DiagramNode.TYPE_DIRECT_THREAT, foundNode.getNodeType());
	}

	public void testInsertStress() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		Command insertCommand = new CommandInsertNode(DiagramNode.TYPE_STRESS);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an Stress?", foundNode.isStress());
		assertEquals(DiagramNode.TYPE_STRESS, foundNode.getNodeType());
	}

	public void testInsertIntervention() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		Command insertCommand = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		insertCommand.execute(project);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getRootAt(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", (String)GraphConstants.getValue(insertedNode.getAttributes()));
		int id = insertedNode.getId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an intervention?", foundNode.isIntervention());
		assertEquals(DiagramNode.TYPE_INTERVENTION, foundNode.getNodeType());
	}
}
