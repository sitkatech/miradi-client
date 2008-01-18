/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.Component;
import java.util.HashSet;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphSelectionModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.base.DisposablePanel;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.views.diagram.DiagramLegendPanel;
import org.conservationmeasures.eam.views.diagram.DiagramSplitPane;

abstract public class DiagramPanel extends DisposablePanel 
{
	public DiagramPanel(MainWindow mainWindowToUse) throws Exception
	{
		mainWindow = mainWindowToUse;
		diagramSplitter = createDiagramSplitter();
		add(diagramSplitter);
	}

	public void dispose()
	{
		super.dispose();
		diagramSplitter.dispose();
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}

	public void setSelectionModel(EAMGraphSelectionModel selectionModelToUse)
	{
		DiagramComponent diagram = getDiagramSplitPane().getDiagramComponent();
		diagram.setSelectionModel(selectionModelToUse);
	}
	
	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		return getdiagramComponent().getSelectedAndRelatedCells();
	}
	
	public void selectCells(EAMGraphCell[] cellsToSelect)
	{
		getdiagramComponent().selectCells(cellsToSelect);
	}
	
	public void selectFactor(FactorId idToUse)
	{
		getdiagramComponent().selectFactor(idToUse);
	}
	
	public HashSet<LinkCell> getOnlySelectedLinkCells()
	{
		return getdiagramComponent().getOnlySelectedLinkCells();
	}
	
	public DiagramLink[] getOnlySelectedLinks()
	{
		return getdiagramComponent().getOnlySelectedLinks();
	}
	
	public FactorCell[] getOnlySelectedFactorCells()
	{
		return getdiagramComponent().getOnlySelectedFactorCells();
	}
	
	public HashSet<FactorCell> getOnlySelectedFactorAndGroupChildCells() throws Exception
	{
		return getdiagramComponent().getOnlySelectedFactorAndGroupChildCells();
	}
	
	public static FactorCell[] getOnlySelectedFactorCells(Object[] allSelectedCells)
	{
		return DiagramComponent.getOnlySelectedFactorCells(allSelectedCells);
	}
	
	public Factor[] getOnlySelectedFactors()
	{
		return getdiagramComponent().getOnlySelectedFactors();
	}
	
	public EAMGraphCell[] getOnlySelectedCells()
	{
		return getdiagramComponent().getOnlySelectedCells();
	}
	
	public void moveFactors(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception 
	{
		getDiagramModel().moveFactors(deltaX, deltaY, ids);
	}

	public DiagramModel getDiagramModel()
	{
		DiagramComponent diagramComponent = getdiagramComponent();
		if (diagramComponent==null)
			return null;
		return diagramComponent.getDiagramModel();
	}
	
	public DiagramComponent[] getAllSplitterDiagramComponents()
	{
		return getDiagramSplitPane().getAllOwenedDiagramComponents();
	}
	
	public DiagramComponent getdiagramComponent()
	{
		DiagramComponent diagramComponent = getDiagramSplitPane().getDiagramComponent();
		return diagramComponent;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Panel");
	}

	public void addFieldComponent(Component component)
	{
		throw new RuntimeException("Not yet implemented");
	}

	public DiagramSplitPane getDiagramSplitPane()
	{
		return diagramSplitter;
	}
	
	public DiagramLegendPanel getDiagramLegendPanel()
	{
		return getDiagramSplitPane().getLegendPanel();
	}
	
	public void showCurrentDiagram() throws Exception
	{
		diagramSplitter.showCurrentCard();
	}
	
	abstract protected DiagramSplitPane createDiagramSplitter() throws Exception;
	
	private DiagramSplitPane diagramSplitter;
	protected MainWindow mainWindow;
}
