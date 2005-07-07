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
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.jgraph.JGraph;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(MainWindow mainWindowToUse)
	{
		super(new DiagramModel());
		mainWindow = mainWindowToUse;
		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());		
		installKeyBindings();
		MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
		
		Point rawPoint = new Point(500, 250);
		String text = EAM.text("[Edit this target]");
		createThreatNode(rawPoint, text);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
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

	private void createThreatNode(Point rawPoint, String text)
	{
		Point2D snappedPoint = snap(rawPoint);
		Node node = new Node(new NodeTypeThreat(), snappedPoint, scale, text, getFont());
		getDiagramModel().insertNode(node);
	}
	
	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

