/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
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
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramTarget;
import org.conservationmeasures.eam.diagram.views.CellViewFactory;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.martus.swing.Utilities;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu, LocationHolder
{
	public DiagramComponent(Project projectToUse, Actions actions)
	{
		project = projectToUse;

		setSelectionModel(new SelectionModelWithLayers(this));
		getGraphLayoutCache().setFactory(new CellViewFactory());
		setUI(new EAMGraphUI());

		disableInPlaceEditing();
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setBendable(false);
		setGridSize(Project.DEFAULT_GRID_SIZE);
		setGridEnabled(true);
		setGridVisible(true);
		setGridMode(JGraph.CROSS_GRID_MODE);

		String title = EAM.text("Title|Properties");
		nodePropertiesDlg = new NodePropertiesDialog(EAM.mainWindow, this, title);

		installKeyBindings(actions);
		diagramContextMenuHandler = new DiagramContextMenuHandler(this, actions);
		MouseEventHandler mouseHandler = new MouseEventHandler(this, project, actions);
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
		try
		{
			setGridVisible(false);
			int insets = 5;
			return this.getImage(Color.WHITE, insets);
		}
		finally
		{
			setGridVisible(true);
		}
	}

	public JComponent getPrintableComponent()
	{
		BufferedImage image = getImage();
		JImage view = new JImage(image);
		view.setSize(getPreferredSize());
		view.setPreferredSize(getPreferredSize());
		return view;
	}
	
	//toScreen does not take into account Physical Window location.
	public Point toWindowCoordinates(Point scaledLocation)
	{
		Point2D screenLocation2D = toScreen(scaledLocation);
		Point scaledPoint = Utilities.createPointFromPoint2D(screenLocation2D);
		Point locationOnScreen = getLocationOnScreen();
		scaledPoint.x += locationOnScreen.x;
		scaledPoint.y += locationOnScreen.y;
		return scaledPoint;
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
	
	public Project getProject()
	{
		return project;
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
		return project.getLayerManager().isTypeVisible(DiagramTarget.class);
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
	
	public boolean hasLocation()
	{
		return false;
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
		KeyBinder.bindKey(this, KeyEvent.VK_KP_UP, KeyBinder.KEY_MODIFIER_NONE, nudgeActionUp);
		Action nudgeActionDown = actions.get(ActionNudgeNodeDown.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DOWN, KeyBinder.KEY_MODIFIER_NONE, nudgeActionDown);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_DOWN, KeyBinder.KEY_MODIFIER_NONE, nudgeActionDown);
		Action nudgeActionLeft = actions.get(ActionNudgeNodeLeft.class);
		KeyBinder.bindKey(this, KeyEvent.VK_LEFT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionLeft);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_LEFT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionLeft);
		Action nudgeActionRight = actions.get(ActionNudgeNodeRight.class);
		KeyBinder.bindKey(this, KeyEvent.VK_RIGHT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionRight);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_RIGHT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionRight);
	}
	
	public void selectionWasChanged()
	{
		DiagramNode selectedNode = getSelectedNode();
		if(selectedNode == null || !selectedNode.equals(nodePropertiesDlg.getCurrentNode()))
			nodePropertiesDlg.setVisible(false);
	}
	
	public void showNodeProperties(DiagramNode node)
	{
		nodePropertiesDlg.setCurrentNode(this, node);
		if(!nodePropertiesDlg.isVisible())
			nodePropertiesDlg.setVisible(true);

	}

	Project project;
	DiagramContextMenuHandler diagramContextMenuHandler;
	NodePropertiesDialog nodePropertiesDlg;
}

