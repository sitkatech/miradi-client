/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.diagram.cells.DiagramCauseCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.views.diagram.LinkCreator;
import org.jgraph.graph.GraphLayoutCache;

public class TestDiagramComponent extends TestCaseWithProject
{
	public TestDiagramComponent(String name)
	{
		super(name);
	}
	
	private FactorCell createNode(int nodeType) throws Exception
	{
		DiagramModel model = getProject().getDiagramModel();
		FactorCommandHelper commandHelper = new FactorCommandHelper(getProject(), getProject().getDiagramModel());
		CommandCreateObject createCommand = commandHelper.createFactorAndDiagramFactor(nodeType);
		DiagramFactorId diagramFactorId = (DiagramFactorId) createCommand.getCreatedId();
		FactorCell factorCell = model.getFactorCellById(diagramFactorId);
		
		return factorCell;
	}
	
	public void testSelectAll() throws Exception
	{
		EAM.setMainWindow(new MainWindow(getProject()));
		DiagramComponent diagramComponent = new DiagramComponent(EAM.getMainWindow());
		diagramComponent.setModel(getProject().getDiagramModel());
		diagramComponent.setGraphLayoutCache(getProject().getDiagramModel().getGraphLayoutCache());
		
		DiagramCauseCell hiddenNode = (DiagramCauseCell) createNode(ObjectType.CAUSE);
		ORef hiddenRef = hiddenNode.getWrappedORef();

		DiagramCauseCell visibleNode = (DiagramCauseCell) createNode(ObjectType.CAUSE);
		ORef visibleRef = visibleNode.getWrappedORef();
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(hiddenRef, visibleRef);
		BaseId id = getObjectManager().createObject(FactorLink.getObjectType(), new FactorLinkId(100), extraInfo);
		ORef linkRef = new ORef(FactorLink.getObjectType(), id);
		FactorLink cmLinkage =	FactorLink.find(getObjectManager(), linkRef);
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		FactorLinkId factorLinkId = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(getProject().getDiagramModel(), hiddenNode.getDiagramFactor(), visibleNode.getDiagramFactor());
		DiagramLink diagramLink = getProject().getDiagramModel().getDiagramFactorLinkbyWrappedId(factorLinkId);
		
		GraphLayoutCache graphLayoutCache = diagramComponent.getGraphLayoutCache();
		graphLayoutCache.setVisible(cmLinkage, false);
		graphLayoutCache.setVisible(visibleNode, true);
		graphLayoutCache.setVisible(hiddenNode, false);
		
		assertFalse("Link still visible?", graphLayoutCache.isVisible(diagramLink.getDiagramLinkageId()));
		assertFalse("Hidden Node still visible?", graphLayoutCache.isVisible(hiddenNode));
		assertTrue("Visible Node Not visible?", graphLayoutCache.isVisible(visibleNode));
		
		diagramComponent.selectAll();
		Object[] selectionCells = diagramComponent.getSelectionCells();
		assertEquals("Selection count wrong?", 1, selectionCells.length);
		assertEquals("Wrong selection?", visibleNode, selectionCells[0]);
		EAM.setMainWindow(null);
	}
}
