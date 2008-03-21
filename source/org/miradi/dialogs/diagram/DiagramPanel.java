/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.diagram;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import javax.swing.JComponent;

import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.EAMGraphSelectionModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.diagram.DiagramLegendPanel;
import org.miradi.views.diagram.DiagramSplitPane;

abstract public class DiagramPanel extends DisposablePanel implements MiradiTabContentsPanelInterface
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
	
	public void restoreSavedLocation()
	{
		diagramSplitter.restoreSavedLocation();
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}

	public void setSelectionModel(EAMGraphSelectionModel selectionModelToUse)
	{
		DiagramComponent diagram = getDiagramComponent();
		diagram.setSelectionModel(selectionModelToUse);
	}

	private DiagramComponent getDiagramComponent()
	{
		DiagramComponent diagram = getDiagramSplitPane().getDiagramComponent();
		return diagram;
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
	
	public static FactorCell[] getOnlySelectedFactorCells(EAMGraphCell[] allSelectedCells)
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
	
	
	public DisposablePanel getTabContentsComponent()
	{
		return this;
	}

	public BufferedImage getImage() throws Exception
	{
		return BufferedImageFactory.createImageFromComponent(getdiagramComponent());
	}

	public boolean isImageAvailable()
	{
		return getTabContentsComponent() != null;
	}

	public ExportableTableInterface getExportableTable()
	{
		return null;
	}

	public boolean isExportableTableAvailable()
	{
		return false;
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		return getdiagramComponent();
	}
	
	public boolean isPrintable()
	{
		return true;
	}

	abstract protected DiagramSplitPane createDiagramSplitter() throws Exception;
	
	private DiagramSplitPane diagramSplitter;
	protected MainWindow mainWindow;
}
