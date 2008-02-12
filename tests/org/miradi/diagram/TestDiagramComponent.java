/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.miradi.diagram;

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandCreateObject;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.DiagramCauseCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.FactorCommandHelper;
import org.miradi.views.diagram.LinkCreator;

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
