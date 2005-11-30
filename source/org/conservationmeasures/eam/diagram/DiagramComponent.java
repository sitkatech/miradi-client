/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionContextualHelp;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionNudgeNodeDown;
import org.conservationmeasures.eam.actions.ActionNudgeNodeLeft;
import org.conservationmeasures.eam.actions.ActionNudgeNodeRight;
import org.conservationmeasures.eam.actions.ActionNudgeNodeUp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.project.Project;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu
{
	public DiagramComponent(Project projectToUse, Actions actions)
	{
		super(projectToUse.getDiagramModel());
		project = projectToUse;

		setSelectionModel(new SelectionModelWithLayers(this));
		getGraphLayoutCache().setFactory(new CellViewFactory());

		disableInPlaceEditing();
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setBendable(false);
		setGridSize(Project.DEFAULT_GRID_SIZE);
		setGridEnabled(true);
		setGridVisible(true);

		installKeyBindings(actions);
		diagramContextMenuHandler = new DiagramContextMenuHandler(this, actions);
		MouseHandler mouseHandler = new MouseHandler(this, project, actions);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
	}
	
	public CellView getNextSelectableViewAt(CellView selectedCell, double x, double y)
	{
		// Always use the top-most cell at this location, 
		// never the one behind it
		return super.getNextSelectableViewAt(null, x, y);
	}

	public BufferedImage getImage()
	{
		int insets = 5;
		return this.getImage(Color.WHITE, insets);
	}

	public JComponent getPrintableComponent()
	{
		BufferedImage image = getImage();
		JImage view = new JImage(image);
		view.setSize(getPreferredSize());
		view.setPreferredSize(getPreferredSize());
		return view;
	}

	private class JImage extends JComponent
	{
		public JImage(BufferedImage imageToUse)
		{
			image = imageToUse;
		}
		public void paint(Graphics g) 
		{
			g.drawImage(image, 0, 0, null);
		}
		BufferedImage image;
	}
	
	private void disableInPlaceEditing() 
	{
		setEditClickCount(0);
	}

	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
	}
	
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}
	
	public boolean isNodeVisible(DiagramNode node)
	{
		return project.getLayerManager().isVisible(node);
	}
	
	public boolean isProjectScopeVisible()
	{
		return project.getLayerManager().isTypeVisible(DiagramNode.TYPE_TARGET);
	}

	public boolean isLinkageVisible(DiagramLinkage linkage)
	{
		DiagramNode from = linkage.getFromNode();
		DiagramNode to = linkage.getToNode();
		boolean bothNodesVisible = isNodeVisible(from) && isNodeVisible(to);
		return bothNodesVisible;
	}

	public DiagramNode getSelectedNode()
	{
		if (getSelectionCount() != 1)
			return null;
		return getSelectedNode(0);
	}
	
	public DiagramNode getSelectedNode(int index)
	{
		Object[] selectedCells = getSelectionCells();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell thisCell = (EAMGraphCell)selectedCells[i];
			if(thisCell.isNode())
			{
				if(index == 0)
					return (DiagramNode)thisCell;
				--index;
			}
		}
		return null;
	}
	
	public void zoom(double proportion)
	{
		setScale(getScale() * proportion);
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
		Action zoomInAction = actions.get(ActionZoomIn.class);
		KeyBinder.bindKey(this, KeyEvent.VK_EQUALS, KeyBinder.KEY_MODIFIER_CTRL, zoomInAction);
		Action zoomOutAction = actions.get(ActionZoomOut.class);
		KeyBinder.bindKey(this, KeyEvent.VK_MINUS, KeyBinder.KEY_MODIFIER_CTRL, zoomOutAction);
		//JAVA ISSUE: We had to create new actions here since the key pressed which caused this action
		//Is not sent to the action.
		//javax.swing.SwingUtilities doesn't pass the keycode to the action. 
		Action nudgeActionUp = actions.get(ActionNudgeNodeUp.class);
		KeyBinder.bindKey(this, KeyEvent.VK_UP, KeyBinder.KEY_MODIFIER_NONE, nudgeActionUp);
		Action nudgeActionDown = actions.get(ActionNudgeNodeDown.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DOWN, KeyBinder.KEY_MODIFIER_NONE, nudgeActionDown);
		Action nudgeActionLeft = actions.get(ActionNudgeNodeLeft.class);
		KeyBinder.bindKey(this, KeyEvent.VK_LEFT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionLeft);
		Action nudgeActionRight = actions.get(ActionNudgeNodeRight.class);
		KeyBinder.bindKey(this, KeyEvent.VK_RIGHT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionRight);
	}

	Project project;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

