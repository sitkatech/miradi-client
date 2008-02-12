/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;

import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ProjectForTesting;

public class TestDiagramAddFactor extends EAMTestCase
{
	public TestDiagramAddFactor(String name)
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
		project = null;
	}
	
	public void testInsertTarget() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.TARGET);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllFactorCells().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a target?", foundNode.isTarget());
		assertEquals(ObjectType.TARGET, foundNode.getUnderlyingFactorType());
	}

	public void testInsertFactor() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.CAUSE);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllFactorCells().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a contributing factor?", foundNode.isContributingFactor());
		assertEquals(ObjectType.CAUSE, foundNode.getWrappedType());
	}

	public void testInsertIntervention() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.STRATEGY);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllFactorCells().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a strategy?", foundNode.isStrategy());
		assertEquals(ObjectType.STRATEGY, foundNode.getUnderlyingFactorType());
	}
	
	ProjectForTesting project;
}
