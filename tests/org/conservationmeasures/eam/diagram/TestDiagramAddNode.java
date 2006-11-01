/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramAddNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestDiagramAddNode extends EAMTestCase
{
	public TestDiagramAddNode(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
	}
	
	public void tearDown() throws Exception
	{
		project.close();
	}
	
	public void testBadInsert() throws Exception
	{
		Command insertCommand = new CommandDiagramAddNode(new ModelNodeId(BaseId.INVALID.asInt()));
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
		project.createNodeAndAddToDiagram(DiagramNode.TYPE_TARGET, BaseId.INVALID);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getAllNodes().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramNodeId id = insertedNode.getDiagramNodeId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a target?", foundNode.isTarget());
		assertEquals(DiagramNode.TYPE_TARGET, foundNode.getNodeType());
	}

	public void testInsertFactor() throws Exception
	{
		project.createNodeAndAddToDiagram(DiagramNode.TYPE_FACTOR, BaseId.INVALID);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getAllNodes().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramNodeId id = insertedNode.getDiagramNodeId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not an indirect factor?", foundNode.isIndirectFactor());
		assertEquals(DiagramNode.TYPE_FACTOR, foundNode.getNodeType());
	}

	public void testInsertIntervention() throws Exception
	{
		project.createNodeAndAddToDiagram(DiagramNode.TYPE_INTERVENTION, BaseId.INVALID);
		DiagramModel model = project.getDiagramModel();
		DiagramNode insertedNode = (DiagramNode)model.getAllNodes().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramNodeId id = insertedNode.getDiagramNodeId();
		DiagramNode foundNode = model.getNodeById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a strategy?", foundNode.isIntervention());
		assertEquals(DiagramNode.TYPE_INTERVENTION, foundNode.getNodeType());
	}
	
	ProjectForTesting project;
}
