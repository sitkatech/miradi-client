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
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.jgraph.JGraph;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(MainWindow mainWindowToUse, DiagramModel model)
	{
		super(model);
		mainWindow = mainWindowToUse;
		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());
		installKeyBindings();
		MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setEditClickCount(0);//We don't have an editor
		setBendable(false);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
	}
	
	public void nodesWereMoved(int deltaX, int deltaY, int[] ids)
	{
		mainWindow.recordCommand(new CommandDiagramMove(deltaX, deltaY, ids));
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}
	
	public Node getSelectedNode()
	{
		if (getSelectionCount() != 1)
			return null;
		Object selectedCell = getSelectionCell();
		if(!isNode(selectedCell))
			return null;
		return (Node)selectedCell;
	}
	
	private boolean isNode(Object cell)
	{
		if(cell instanceof Node)
			return true;
		return false;
	}
	


	private void installKeyBindings()
	{
		Action helpAction = new ActionContextualHelp(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
		Action undoAction = new ActionUndo(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_Z, KeyBinder.KEY_MODIFIER_CTRL, undoAction);
		Action redoAction = new ActionRedo(mainWindow);
		KeyBinder.bindKey(this, KeyEvent.VK_Y, KeyBinder.KEY_MODIFIER_CTRL, redoAction);
	}
	
	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

