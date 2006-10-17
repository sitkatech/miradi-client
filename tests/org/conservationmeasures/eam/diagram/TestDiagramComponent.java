/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphLayoutCache;

public class TestDiagramComponent extends EAMTestCase
{
	private ProjectForTesting project;
	
	public TestDiagramComponent(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testSelectAll() throws Exception
	{
		DiagramComponent diagramComponent = new DiagramComponent();
		diagramComponent.setModel(project.getDiagramModel());
		diagramComponent.setGraphLayoutCache(project.getGraphLayoutCache());
		
		CreateModelNodeParameter cmnp = new CreateModelNodeParameter(new NodeTypeFactor());
		ModelNodeId hiddenId = (ModelNodeId) project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, cmnp);
		ModelNodeId visibleId = (ModelNodeId) project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, cmnp);
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(new BaseId(100), hiddenId, visibleId);
		
		DiagramNode hiddenNode = diagramComponent.getDiagramModel().createNode(hiddenId);
		DiagramNode visibleNode = diagramComponent.getDiagramModel().createNode(visibleId);
		DiagramLinkage hiddenLinkage = diagramComponent.getDiagramModel().createLinkage(cmLinkage);
		
		GraphLayoutCache graphLayoutCache = diagramComponent.getGraphLayoutCache();
		graphLayoutCache.setVisible(cmLinkage, false);
		graphLayoutCache.setVisible(visibleNode, true);
		graphLayoutCache.setVisible(hiddenNode, false);
		
		assertFalse("Linkage still visible?", graphLayoutCache.isVisible(hiddenLinkage));
		assertFalse("Hidden Node still visible?", graphLayoutCache.isVisible(hiddenNode));
		assertTrue("Visible Node Not visible?", graphLayoutCache.isVisible(visibleNode));
		
		diagramComponent.selectAll();
		Object[] selectionCells = diagramComponent.getSelectionCells();
		assertEquals("Selection count wrong?", 1, selectionCells.length);
		assertEquals("Wrong selection?", visibleNode, selectionCells[0]);
	}
}
