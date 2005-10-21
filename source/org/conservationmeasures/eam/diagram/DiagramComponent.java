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
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.nodes.CellViewFactory;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.project.Project;
import org.jgraph.JGraph;

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

	Project project;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

