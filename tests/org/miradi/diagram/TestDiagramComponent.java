/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.diagram;

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandCreateObject;
import org.miradi.diagram.cells.DiagramCauseCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.BaseId;
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
		ORef diagramFactorRef = createCommand.getObjectRef();
		FactorCell factorCell = model.getFactorCellByRef(diagramFactorRef);
		
		return factorCell;
	}
	
	public void testSelectAll() throws Exception
	{
		EAM.setMainWindow(new MainWindow(getProject()));
		DiagramComponent diagramComponent = new DiagramComponent(EAM.getMainWindow(), getProject().getDiagramModel());
		diagramComponent.setGraphLayoutCache(getProject().getDiagramModel().getGraphLayoutCache());
		
		DiagramCauseCell hiddenNode = (DiagramCauseCell) createNode(ObjectType.CAUSE);
		ORef hiddenRef = hiddenNode.getWrappedFactorRef();

		DiagramCauseCell visibleNode = (DiagramCauseCell) createNode(ObjectType.CAUSE);
		ORef visibleRef = visibleNode.getWrappedFactorRef();
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(hiddenRef, visibleRef);
		BaseId id = getObjectManager().createObject(FactorLink.getObjectType(), new FactorLinkId(100), extraInfo);
		ORef linkRef = new ORef(FactorLink.getObjectType(), id);
		FactorLink cmLinkage =	FactorLink.find(getObjectManager(), linkRef);
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		ORef factorLinkRef = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(getProject().getDiagramModel(), hiddenNode.getDiagramFactor(), visibleNode.getDiagramFactor());
		DiagramLink diagramLink = getProject().getDiagramModel().getDiagramLinkByWrappedRef(factorLinkRef);
		
		GraphLayoutCache graphLayoutCache = diagramComponent.getGraphLayoutCache();
		graphLayoutCache.setVisible(cmLinkage, false);
		graphLayoutCache.setVisible(visibleNode, true);
		graphLayoutCache.setVisible(hiddenNode, false);
		
		assertFalse("Link still visible?", graphLayoutCache.isVisible(diagramLink.getDiagramLinkId()));
		assertFalse("Hidden Node still visible?", graphLayoutCache.isVisible(hiddenNode));
		assertTrue("Visible Node Not visible?", graphLayoutCache.isVisible(visibleNode));
		
		diagramComponent.selectAll();
		Object[] selectionCells = diagramComponent.getSelectionCells();
		assertEquals("Selection count wrong?", 1, selectionCells.length);
		assertEquals("Wrong selection?", visibleNode, selectionCells[0]);
		EAM.setMainWindow(null);
	}
}
