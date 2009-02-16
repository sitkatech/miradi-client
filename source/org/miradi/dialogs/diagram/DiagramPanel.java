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
package org.miradi.dialogs.diagram;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import javax.swing.JComponent;

import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.EAMGraphSelectionModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.diagram.DiagramLegendPanel;
import org.miradi.views.diagram.DiagramSplitPane;

abstract public class DiagramPanel extends DisposablePanelWithDescription implements MiradiTabContentsPanelInterface
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
		DiagramComponent diagram = getCurrentDiagramComponent();
		diagram.setSelectionModel(selectionModelToUse);
	}

	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		return getCurrentDiagramComponent().getSelectedAndRelatedCells();
	}
	
	public void selectCells(EAMGraphCell[] cellsToSelect)
	{
		getCurrentDiagramComponent().selectCells(cellsToSelect);
	}
	
	public void selectFactor(ORef factorRef)
	{
		getCurrentDiagramComponent().selectFactor(factorRef);
	}
	
	public HashSet<LinkCell> getOnlySelectedLinkCells()
	{
		return getCurrentDiagramComponent().getOnlySelectedLinkCells();
	}
	
	public DiagramLink[] getOnlySelectedLinks()
	{
		return getCurrentDiagramComponent().getOnlySelectedLinks();
	}
	
	public FactorCell[] getOnlySelectedFactorCells()
	{
		return getCurrentDiagramComponent().getOnlySelectedFactorCells();
	}
	
	public HashSet<FactorCell> getOnlySelectedFactorAndGroupChildCells() throws Exception
	{
		return getCurrentDiagramComponent().getOnlySelectedFactorAndGroupChildCells();
	}
	
	public static FactorCell[] getOnlySelectedFactorCells(EAMGraphCell[] allSelectedCells)
	{
		return DiagramComponent.getOnlySelectedFactorCells(allSelectedCells);
	}
	
	public Factor[] getOnlySelectedFactors()
	{
		return getCurrentDiagramComponent().getOnlySelectedFactors();
	}
	
	public EAMGraphCell[] getOnlySelectedCells()
	{
		return getCurrentDiagramComponent().getOnlySelectedCells();
	}
		
	public void moveFactors(int deltaX, int deltaY, ORefList diagramFactorRefs) throws Exception 
	{
		getDiagramModel().moveFactors(deltaX, deltaY, diagramFactorRefs);
	}

	public PersistentDiagramModel getDiagramModel()
	{
		DiagramComponent diagramComponent = getCurrentDiagramComponent();
		if (diagramComponent==null)
			return null;
		return diagramComponent.getDiagramModel();
	}
	
	public DiagramComponent[] getAllSplitterDiagramComponents()
	{
		return getDiagramSplitPane().getAllOwenedDiagramComponents();
	}
	
	public DiagramComponent getCurrentDiagramComponent()
	{
		DiagramComponent diagramComponent = getDiagramSplitPane().getCurrentDiagramComponent();
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
	
	
	public DisposablePanelWithDescription getTabContentsComponent()
	{
		return this;
	}

	public BufferedImage getImage() throws Exception
	{
		return BufferedImageFactory.createImageFromDiagramWithCurrentSettings(mainWindow, getDiagramObject());
	}

	public boolean isImageAvailable()
	{
		return getTabContentsComponent() != null;
	}

	public AbstractTableExporter getTableExporter()
	{
		return null;
	}

	public boolean isExportableTableAvailable()
	{
		return false;
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		return BufferedImageFactory.createDiagramComponent(mainWindow, getDiagramObject());
	}
	
	public boolean isPrintable()
	{
		return true;
	}
	
	public boolean isRtfExportable()
	{
		return false;
	}
	
	public void exportRtf(RtfWriter writer) throws Exception
	{
	}

	abstract protected DiagramSplitPane createDiagramSplitter() throws Exception;
	
	private DiagramSplitPane diagramSplitter;
	protected MainWindow mainWindow;
}
