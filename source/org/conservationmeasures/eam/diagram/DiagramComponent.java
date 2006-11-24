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
import java.util.Vector;

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
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramNode;
import org.conservationmeasures.eam.diagram.cells.DiagramTarget;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.ComponentWithContextMenu;
import org.conservationmeasures.eam.main.KeyBinder;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphLayoutCache;
import org.martus.swing.Utilities;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu, LocationHolder
{
	public DiagramComponent()
	{
		setUI(new EAMGraphUI());

		setAntiAliased(true);
		disableInPlaceEditing();
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setBendable(false);
		setGridSize(Project.DEFAULT_GRID_SIZE);
		setGridEnabled(true);
		setGridMode(JGraph.CROSS_GRID_MODE);
		setSelectionModel(new EAMGraphSelectionModel(this));
		defaultBackgroundColor = getBackground();
	}    

	public DiagramComponent(MainWindow mainWindow)
	{
		this();
		project = mainWindow.getProject();
		updateDiagramComponent(mainWindow);
		Actions actions = mainWindow.getActions();
		installKeyBindings(actions);
		diagramContextMenuHandler = new DiagramContextMenuHandler(mainWindow, this, actions);
		MouseEventHandler mouseHandler = new MouseEventHandler(mainWindow);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
	}
    	
    public void updateDiagramComponent(MainWindow mainWindow)
    {
    		boolean isGridVisible = mainWindow.getBooleanPreference(AppPreferences.TAG_GRID_VISIBLE);
    		setGridVisible(isGridVisible);
    }
    	
	public EAMGraphSelectionModel getEAMGraphSelectionModel()
	{
		return (EAMGraphSelectionModel)getSelectionModel();
	}
	
	public BufferedImage getImage()
	{
		Color currentColor = getBackground();
		try
		{
			setToDefaultBackgroundColor();
			setGridVisible(false);
			return BufferedImageFactory.getImage(this,5);
		}
		finally
		{
			setBackground(currentColor);
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
	
	public boolean areLinkagesVisible()
	{
		return project.getLayerManager().areLinkagesVisible();
	}
	
	public boolean isProjectScopeVisible()
	{
		return project.getLayerManager().isTypeVisible(DiagramTarget.class);
	}

	public boolean areDesiresVisible()
	{
		return project.getLayerManager().areDesiresVisible();
	}
	
	public boolean areIndicatorsVisible()
	{
		return project.getLayerManager().areIndicatorsVisible();
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
	
	public void selectAll()
	{
		clearSelection();
		selectAllNodes();
		selectAllLinkages();
	}
	
	public void selectAllNodes()
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		Vector allNodes = getDiagramModel().getAllNodes();
		for (int i  = 0; i < allNodes.size(); i++)
		{
			DiagramNode dNode = (DiagramNode)allNodes.elementAt(i);
			if (glc.isVisible(dNode))
				addSelectionCell(dNode);
		}
	}
	
	public void selectAllLinkages()
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		Vector allLinkages = getDiagramModel().getAllLinkages();
		for (int i = 0 ; i < allLinkages.size(); i++){
			DiagramFactorLink dLinkage = (DiagramFactorLink)allLinkages.elementAt(i);
			if (glc.isVisible(dLinkage))
				addSelectionCell(dLinkage);
		}
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
		Action  selectAllAction = actions.get(ActionSelectAll.class);
		KeyBinder.bindKey(this, KeyEvent.VK_A, KeyBinder.KEY_MODIFIER_CTRL, selectAllAction);
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
	
	public CellView getNextSelectableViewAt(CellView current, double x, double y)
	{
		CellView candidateView = super.getNextSelectableViewAt(current, x, y);
		// if they want the first node, we know it is ok because the project scope box
		// is always behind the targets
		if(current == null || candidateView == null)
			return candidateView;
		
		EAMGraphCell candidateCell = (EAMGraphCell)candidateView.getCell();
		if(candidateCell.isProjectScope())
		{
			// skip the project scope box and get the next one (if any)
			candidateView = super.getNextSelectableViewAt(candidateView, x, y);
		}
		
		return candidateView;
	}
	
	public void setToDefaultBackgroundColor()
	{
		setBackground(defaultBackgroundColor);
	}

	/*
	 * NOTE: The following method is a refactored version of what is in the JGraph
	 * class in jgraph 5.8. It's here for debugging weird selection model issues.
	 */
//	public CellView getNextViewAt(CellView[] cells, CellView c, double x,
//			double y, boolean leafsOnly)
//	{
//		if(cells == null)
//			return null;
//		
//		Rectangle2D r = fromScreen(new Rectangle2D.Double(x - tolerance, y
//				- tolerance, 2 * tolerance, 2 * tolerance));
//		// Iterate through cells and switch to active
//		// if current is traversed. Cache first cell.
//		CellView first = null;
//		boolean active = (c == null);
//		for (int i = 0; i < cells.length; i++)
//		{
//			if(cells[i] == null)
//				continue;
//			
//			if(leafsOnly && !cells[i].isLeaf())
//				continue;
//			
//			if (!cells[i].intersects(this, r))
//				continue;
//			
//			// TODO: This behaviour is specific to selection and
//			// should be parametrized (it only returns a group with
//			// selected children if no other portview is available)
//			if(active)
//			{
//				boolean hasAnySelectedChildren = selectionModel.isChildrenSelected(cells[i].getCell());
//				System.out.println("Checking for selected children: " + hasAnySelectedChildren);
//				if(!hasAnySelectedChildren)
//				{
//					System.out.println("Returning cells[i]: " + cells[i].getCell());
//					return cells[i];
//				}
//			}
//			else if(cells[i] == c)
//			{
//				System.out.println("Setting active");
//				active = true;
//			}
//			
//			if (first == null)
//			{
//				first = cells[i];
//				System.out.println("Setting first: " + first);
//			}
//		}
//		return first;
//	}
	
	Color defaultBackgroundColor;
	Project project;
	DiagramContextMenuHandler diagramContextMenuHandler;
}

