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
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.jgraph.JGraph;

public class DiagramView extends JGraph implements ComponentWithContextMenu
{
	public DiagramView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject().getDiagramModel());
		mainWindow = mainWindowToUse;
		mainWindowToUse.getProject().setSelectionModel(getSelectionModel());

		diagramContextMenuHandler = new DiagramContextMenuHandler(this);
		getGraphLayoutCache().setFactory(new CellViewFactory());
		installKeyBindings();
		MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
		setDisconnectable(false);
		setDisconnectOnMove(false);
		disableInPlaceEditing();
		setBendable(false);
	}

	private void disableInPlaceEditing() 
	{
		setEditClickCount(0);
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
		mainWindow.getProject().recordCommand(new CommandDiagramMove(deltaX, deltaY, ids));
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}
	
	public Node getSelectedNode()
	{
		if (getSelectionCount() != 1)
			return null;
		EAMGraphCell selectedCell = (EAMGraphCell)getSelectionCell();
		if(!selectedCell.isNode())
			return null;
		return (Node)selectedCell;
	}
	private void installKeyBindings()
	{
		Action helpAction = mainWindow.getActions().get(ActionContextualHelp.class);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
		Action undoAction = mainWindow.getActions().get(ActionUndo.class);
		KeyBinder.bindKey(this, KeyEvent.VK_Z, KeyBinder.KEY_MODIFIER_CTRL, undoAction);
		Action redoAction = mainWindow.getActions().get(ActionRedo.class);
		KeyBinder.bindKey(this, KeyEvent.VK_Y, KeyBinder.KEY_MODIFIER_CTRL, redoAction);
		Action cutAction = mainWindow.getActions().get(ActionCut.class);
		KeyBinder.bindKey(this, KeyEvent.VK_X, KeyBinder.KEY_MODIFIER_CTRL, cutAction);
		Action copyAction = mainWindow.getActions().get(ActionCopy.class);
		KeyBinder.bindKey(this, KeyEvent.VK_C, KeyBinder.KEY_MODIFIER_CTRL, copyAction);
		Action pasteAction = mainWindow.getActions().get(ActionPaste.class);
		KeyBinder.bindKey(this, KeyEvent.VK_V, KeyBinder.KEY_MODIFIER_CTRL, pasteAction);
		Action deleteAction = mainWindow.getActions().get(ActionDelete.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DELETE, KeyBinder.KEY_MODIFIER_NONE, deleteAction);
	}
	
	public static String cardName()
	{
		return "Diagram";
	}

	MainWindow mainWindow;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

