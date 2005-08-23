/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.jgraph.JGraph;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(BaseProject projectToUse, Actions actions)
	{
		super(projectToUse.getDiagramModel());
		project = projectToUse;

		getGraphLayoutCache().setFactory(new CellViewFactory());

		disableInPlaceEditing();
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setBendable(false);

		installKeyBindings(actions);
		diagramContextMenuHandler = new DiagramContextMenuHandler(this, actions);
		MouseHandler mouseHandler = new MouseHandler(this, project, actions);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
	}
	
	public BufferedImage getImage()
	{
		int insets = 5;
		return this.getImage(Color.WHITE, insets);
	}

	private void disableInPlaceEditing() 
	{
		setEditClickCount(0);
	}

	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
	}
	
	public void nodesWereMoved(int deltaX, int deltaY, int[] ids)
	{
		project.recordCommand(new CommandDiagramMove(deltaX, deltaY, ids));
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
	
	private void installKeyBindings(Actions actions)
	{
		Action helpAction = actions.get(ActionContextualHelp.class);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
		Action undoAction = actions.get(ActionUndo.class);
		KeyBinder.bindKey(this, KeyEvent.VK_Z, KeyBinder.KEY_MODIFIER_CTRL, undoAction);
		Action redoAction = actions.get(ActionRedo.class);
		KeyBinder.bindKey(this, KeyEvent.VK_Y, KeyBinder.KEY_MODIFIER_CTRL, redoAction);
		Action cutAction = actions.get(ActionCut.class);
		KeyBinder.bindKey(this, KeyEvent.VK_X, KeyBinder.KEY_MODIFIER_CTRL, cutAction);
		Action copyAction = actions.get(ActionCopy.class);
		KeyBinder.bindKey(this, KeyEvent.VK_C, KeyBinder.KEY_MODIFIER_CTRL, copyAction);
		Action pasteAction = actions.get(ActionPaste.class);
		KeyBinder.bindKey(this, KeyEvent.VK_V, KeyBinder.KEY_MODIFIER_CTRL, pasteAction);
		Action deleteAction = actions.get(ActionDelete.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DELETE, KeyBinder.KEY_MODIFIER_NONE, deleteAction);
	}

	BaseProject project;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

