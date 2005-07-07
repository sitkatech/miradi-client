/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
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
		installKeyBindings();
		addMouseListener(new MouseContextMenuAdapter(this));
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

	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

