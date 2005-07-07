/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.Action;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.diagram.cells.CellTypeThreat;
import org.conservationmeasures.eam.diagram.cells.CellViewFactory;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MouseContextMenuAdapter;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(MainWindow mainWindowToUse)
	{
		super(new DefaultGraphModel());
		mainWindow = mainWindowToUse;
		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());		
		installKeyBindings();
		addMouseListener(new MouseContextMenuAdapter(this));
		
		Point2D snappedPoint = snap(new Point(500, 250));
		String text = EAM.text("[Edit this threat]");
		Cell defaultThreat = createThreatCell(snappedPoint, text);
		insertCell(defaultThreat);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	void installKeyBindings()
	{
		Action helpAction = new ActionContextualHelp(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}

	private Cell createThreatCell(Point2D snappedPoint, String text)
	{
		return new Cell(new CellTypeThreat(), snappedPoint, scale, text, getFont());
	}
	
	private void insertCell(Cell cellToInsert)
	{
		Object[] cells = new Object[] {cellToInsert.getCell()};
		getModel().insert(cells, cellToInsert.getNestedAttributeMap(), null, null, null);
	}

	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

