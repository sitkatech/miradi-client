/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.ProjectForTesting;
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
		
		CreateFactorParameter cmnp = new CreateFactorParameter(new FactorTypeCause());
		FactorId hiddenId = (FactorId) project.createObject(ObjectType.FACTOR, BaseId.INVALID, cmnp);
		FactorId visibleId = (FactorId) project.createObject(ObjectType.FACTOR, BaseId.INVALID, cmnp);
		FactorLink cmLinkage = new FactorLink(new FactorLinkId(100), hiddenId, visibleId);
		
		DiagramFactor hiddenNode = diagramComponent.getDiagramModel().createDiagramFactor(hiddenId);
		DiagramFactor visibleNode = diagramComponent.getDiagramModel().createDiagramFactor(visibleId);
		DiagramFactorLink hiddenLinkage = diagramComponent.getDiagramModel().createDiagramFactorLink(cmLinkage);
		
		GraphLayoutCache graphLayoutCache = diagramComponent.getGraphLayoutCache();
		graphLayoutCache.setVisible(cmLinkage, false);
		graphLayoutCache.setVisible(visibleNode, true);
		graphLayoutCache.setVisible(hiddenNode, false);
		
		assertFalse("Link still visible?", graphLayoutCache.isVisible(hiddenLinkage));
		assertFalse("Hidden Node still visible?", graphLayoutCache.isVisible(hiddenNode));
		assertTrue("Visible Node Not visible?", graphLayoutCache.isVisible(visibleNode));
		
		diagramComponent.selectAll();
		Object[] selectionCells = diagramComponent.getSelectionCells();
		assertEquals("Selection count wrong?", 1, selectionCells.length);
		assertEquals("Wrong selection?", visibleNode, selectionCells[0]);
	}
}
