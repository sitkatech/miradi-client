/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.martus.swing.Utilities;
import org.miradi.actions.ActionContextualHelp;
import org.miradi.actions.ActionDelete;
import org.miradi.actions.ActionNudgeDown;
import org.miradi.actions.ActionNudgeLeft;
import org.miradi.actions.ActionNudgeRight;
import org.miradi.actions.ActionNudgeUp;
import org.miradi.actions.ActionSelectAll;
import org.miradi.actions.Actions;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.AppPreferences;
import org.miradi.main.ComponentWithContextMenu;
import org.miradi.main.EAM;
import org.miradi.main.KeyBinder;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.LocationHolder;
import org.miradi.utils.Utility;
import org.miradi.views.diagram.LayerManager;

public class DiagramComponent extends JGraph implements ComponentWithContextMenu, LocationHolder, GraphSelectionListener
{
	public DiagramComponent(MainWindow mainWindowToUse, GraphModel diagramModel)
	{
		super(diagramModel);
		mainWindow = mainWindowToUse;
		setUI(new EAMGraphUI());
		setAntiAliased(true);
		disableInPlaceEditing();
		setDisconnectable(false);
		setDisconnectOnMove(false);
		setBendable(false);
		setGridSize(Project.DEFAULT_GRID_SIZE);
		setGridMode(JGraph.CROSS_GRID_MODE);
		setSelectionModel(new EAMGraphSelectionModel(this));
		setMarqueeHandler(new CustomMarqueeHandler(this));
		defaultBackgroundColor = getBackground();
		addGraphSelectionListener(this);
		ensureCellPerimetersAreRespectedByLinksWithBendPoints();
		project = mainWindow.getProject();
		updateDiagramComponent();
		Actions actions = mainWindow.getActions();
		installKeyBindings(actions);
		diagramContextMenuHandler = new DiagramContextMenuHandler(mainWindow, this, actions);
		MouseEventHandler mouseHandler = new MouseEventHandler(mainWindow);
		addMouseListener(mouseHandler);
		addGraphSelectionListener(mouseHandler);
		
		enableToolTips();
	}

	private void enableToolTips()
	{
		setToolTipText("");
	}
	
	private void ensureCellPerimetersAreRespectedByLinksWithBendPoints()
	{
		PortView.allowPortMagic = false;
	}

	public String getToolTipText(MouseEvent event)
	{
		Object cell = getFirstCellForLocation(event.getX(), event.getY());
		if (cell instanceof FactorCell) 
		{
			FactorCell factorCell = (FactorCell)cell;
			Point screenPoint = event.getPoint();
			Point pointRelativeToCellOrigin = convertScreenPointToCellRelativePoint(screenPoint, factorCell);
			return factorCell.getToolTipString(pointRelativeToCellOrigin);
		}
		if (cell instanceof LinkCell)
		{
			LinkCell linkCell = (LinkCell) cell;
			return linkCell.getToolTipString();
		}
		return null;
	}

	public Point convertScreenPointToCellRelativePoint(Point screenPoint, FactorCell factorCell)
	{
		Point unscaledPoint = Utility.convertPoint2DToPoint(fromScreen(screenPoint));
		Point cellOrigin = factorCell.getLocation();
		Point pointRelativeToCellOrigin = unscaledPoint; 
		pointRelativeToCellOrigin.translate(-cellOrigin.x, -cellOrigin.y);
		return pointRelativeToCellOrigin;
	}

	public void updateDiagramComponent()
    {
		boolean isGridVisible = mainWindow.getBooleanPreference(AppPreferences.TAG_GRID_VISIBLE);
		setGridVisible(isGridVisible);
		updateDiagramZoomSetting();
    }
    	
    
    public void updateDiagramZoomSetting()
    {
    	setScale(mainWindow.getDiagramZoomSetting(AppPreferences.TAG_DIAGRAM_ZOOM));
    }
    
    public void forceRepaint()
    {
    	Rectangle everything = new Rectangle(0, 0, getWidth(), getHeight());
    	fromScreen(everything);
    	addOffscreenDirty(everything);
    }
    
	public EAMGraphSelectionModel getEAMGraphSelectionModel()
	{
		return (EAMGraphSelectionModel)getSelectionModel();
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
	

	private void disableInPlaceEditing() 
	{
		setEditClickCount(0);
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	@Override
	public void setModel(GraphModel newModel)
	{
		if (getModel() != null)
			EAM.logWarning("DiagramComponent.setModel() was called after the model was already set.");
		
		super.setModel(newModel);
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	public DiagramModel getDiagramModel()
	{
		return (DiagramModel)getModel();
	}
	
	public LayerManager getLayerManager()
	{
		return getDiagramModel().getLayerManager();
	}
		
	public void showContextMenu(MouseEvent e)
	{
		diagramContextMenuHandler.showContextMenu(e);
	}
	
	public boolean areGoalsVisible()
	{
		return getLayerManager().areGoalsVisible();
	}
	
	public boolean areObjectivesVisible()
	{
		return getLayerManager().areObjectivesVisible();
	}
	
	public boolean areIndicatorsVisible()
	{
		return getLayerManager().areIndicatorsVisible();
	}
	
	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		try
		{
			Object[] selectedCells = getOnlySelectedCells();
			HashSet<EAMGraphCell> cellVector = getDiagramModel().getAllSelectedCellsWithRelatedLinkages(selectedCells);
			
			Vector<EAMGraphCell> stressCells = extractType(cellVector, Stress.getObjectType());
			Vector<EAMGraphCell> activityCells = extractType(cellVector, Task.getObjectType());
			Vector<EAMGraphCell> allCells = new Vector<EAMGraphCell>(cellVector);
			
			allCells.removeAll(stressCells);
			allCells.removeAll(activityCells);
			
			Vector<EAMGraphCell> sortedCells = new Vector<EAMGraphCell>();
			sortedCells.addAll(stressCells);
			sortedCells.addAll(activityCells);
			sortedCells.addAll(allCells);
			
			return sortedCells.toArray(new EAMGraphCell[0]);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new EAMGraphCell[0];
		}
	}

	private Vector<EAMGraphCell> extractType(HashSet<EAMGraphCell> cellVector, int type)
	{
		Vector<EAMGraphCell> extractedList = new Vector<EAMGraphCell>();
		for(EAMGraphCell graphCell : cellVector)
		{
			if (graphCell.getWrappedFactorRef().getObjectType() == type)
				extractedList.add(graphCell);
		}
		
		return extractedList;
	}

	public void selectCells(EAMGraphCell[] cellsToSelect)
	{
		getSelectionModel().addSelectionCells(cellsToSelect);
	}

	public void selectFactor(ORef factorRef)
	{
		try
		{
			FactorCell nodeToSelect = getDiagramModel().getFactorCellByWrappedRef(factorRef);
			getSelectionModel().setSelectionCell(nodeToSelect);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public HashSet<LinkCell> getOnlySelectedLinkCells()
	{
		if(getSelectionModel() == null)
			return new HashSet<LinkCell>();
	
		Object[] rawCells = getOnlySelectedCells();
		return getOnlySelectedLinkCells(rawCells);
	}
	
	public HashSet<LinkCell> getOnlySelectedLinkCells(Object [] allSelectedCells)
	{
		HashSet<LinkCell> linkCells = new HashSet<LinkCell>();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactorLink())
			{
				LinkCell cell = (LinkCell)allSelectedCells[i];
				linkCells.add(cell);
			}
		}
		return linkCells;
	}
	
	public DiagramLink[] getOnlySelectedLinks()
	{
		if(getSelectionModel() == null)
			return new DiagramLink[0];
		
		Object[] rawCells = getOnlySelectedCells();
		return getOnlySelectedDiagramLinks(rawCells);
	}
	
	public DiagramLink[] getOnlySelectedDiagramLinks(Object [] allSelectedCells)
	{
		Vector<DiagramLink> linkages = new Vector<DiagramLink>();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactorLink())
			{
				LinkCell cell = (LinkCell)allSelectedCells[i];
				linkages.add(cell.getDiagramLink());
			}
		}
		return linkages.toArray(new DiagramLink[0]);
	}
	
	public static FactorCell[] getOnlySelectedFactorCells(EAMGraphCell[] allSelectedCells)
	{
		Vector<FactorCell> nodes = new Vector<FactorCell>();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			EAMGraphCell cell = allSelectedCells[i];
			if(cell.isFactor())
				nodes.add((FactorCell)cell);
		}
		return nodes.toArray(new FactorCell[0]);
	}
	
	public Factor[] getOnlySelectedFactors()
	{
		if (getSelectionModel() == null)
			return new Factor[0];
		
		EAMGraphCell[] rawCells = getOnlySelectedCells();
		return getOnlySelectedFactors(rawCells);
	}
	
	private Factor[] getOnlySelectedFactors(EAMGraphCell[] allSelectedFactors)
	{
		Vector<Factor> nodes = new Vector<Factor>();
		for(int i = 0; i < allSelectedFactors.length; ++i)
		{
			EAMGraphCell graphCell = allSelectedFactors[i];
			if(graphCell.isFactor())
			{
				ORef ref = graphCell.getDiagramFactor().getWrappedORef();
				Factor factor = (Factor) project.findObject(ref);
				nodes.add(factor);
			}
		}
		return nodes.toArray(new Factor[0]);

	}
	
	public FactorCell[] getOnlySelectedFactorCells()
	{
		if(getSelectionModel() == null)
			return new FactorCell[0];
		
		EAMGraphCell[] rawCells = getOnlySelectedCells();
		return getOnlySelectedFactorCells(rawCells);
	}
	
	public HashSet<FactorCell> getOnlySelectedFactorAndGroupChildCells() throws Exception
	{
		HashSet<FactorCell> groupBoxChildrenCells = new HashSet<FactorCell>();
		FactorCell[] selectedCells = getOnlySelectedFactorCells();
		for (int i = 0; i < selectedCells.length; ++i)
		{
			FactorCell selectedCell = selectedCells[i];
			groupBoxChildrenCells.add(selectedCell);
			if (selectedCell.getDiagramFactor().isGroupBoxFactor())
			{
				DiagramModel diagramModel = getDiagramModel();
				groupBoxChildrenCells.addAll(diagramModel.getGroupBoxFactorChildren(selectedCell));
			}		
		}

		return groupBoxChildrenCells;
	}

	public HashSet<FactorCell> getOnlySelectedGroupBoxCells() throws Exception
	{
		HashSet<FactorCell> groupBoxCells = new HashSet<FactorCell>();
		FactorCell[] selectedCells = getOnlySelectedFactorCells();
		for (int i = 0; i < selectedCells.length; ++i)
		{
			FactorCell selectedCell = selectedCells[i];
			if (selectedCell.getDiagramFactor().isGroupBoxFactor())
			{
				groupBoxCells.add(selectedCell);
			}		
		}

		return groupBoxCells;
	}

	
	public EAMGraphCell[] getOnlySelectedCells()
	{
		Object[] rawCells = getSelectionModel().getSelectionCells();
		Vector<EAMGraphCell> cells = new Vector<EAMGraphCell>();
		for(int i=0; i < rawCells.length; ++i)
		{
			Object rawCell = rawCells[i];
			if(rawCell instanceof EAMGraphCell)
				cells.add((EAMGraphCell)rawCell);
		}
		
		return cells.toArray(new EAMGraphCell[0]);
	}
	
	public FactorCell getSingleSelectedFactor()
	{
		if (getSelectionCount() != 1)
			return null;
		return getSelectedFactor(0);
	}
	
	public FactorCell getSelectedFactor(int index)
	{
		Object[] selectedCells = getSelectionCells();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell thisCell = (EAMGraphCell)selectedCells[i];
			if(thisCell.isFactor())
			{
				if(index == 0)
					return (FactorCell)thisCell;
				--index;
			}
		}
		return null;
	}
	
	public void selectAll()
	{
		clearSelection();
		selectAllFactors();
		selectAllFactorLinks();
		
		requestFocusInWindow();
	}
	
	public void selectAllFactors()
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		Vector allDiagramFactors = getDiagramModel().getAllFactorCells();
		for (int i  = 0; i < allDiagramFactors.size(); i++)
		{
			FactorCell factorCell = (FactorCell)allDiagramFactors.elementAt(i);
			if (glc.isVisible(factorCell))
				addSelectionCell(factorCell);
		}
	}
	
	public boolean isCellVisible(Object cell)
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		return glc.isVisible(cell);
	}
	
	public void selectAllFactorLinks()
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		LinkCell[] allLinkCells = getDiagramModel().getAllFactorLinkCells();
		for (int i = 0 ; i < allLinkCells.length; i++)
		{
			LinkCell linkCell = allLinkCells[i];
			if (glc.isVisible(linkCell) && !linkCell.getDiagramLink().isCoveredByGroupBoxLink())
			{
				addSelectionCell(linkCell);
			}
		}
	}
	
	public void clearBendPointSelection(DiagramLink diagramLink)
	{
		LinkCell linkCell = getDiagramModel().getLinkCell(diagramLink);
		linkCell.clearBendPointSelectionList();
	}

	public void selectAllLinksAndThierBendPointsInsideGroupBox(HashSet<FactorCell> selectedFactorAndChildren)
	{
		HashSet<LinkCell> linksInsideGroupBoxes = getAllLinksInsideGroupBox(selectedFactorAndChildren);
		for(LinkCell linkCell : linksInsideGroupBoxes)
		{
			linkCell.getBendPointSelectionHelper().selectAll();
			addSelectionCell(linkCell);
		}
	}
	
	public HashSet<LinkCell> getAllLinksInsideGroupBox(HashSet<FactorCell> selectedFactorAndChildren)
	{
		HashSet<LinkCell> linkCells = new HashSet<LinkCell>();
		for(FactorCell factorCell : selectedFactorAndChildren)
		{
			linkCells.addAll(getAllLinksInsideGroupBox(selectedFactorAndChildren, factorCell.getDiagramFactor()));	
		}
		
		return linkCells;
	}

	private HashSet<LinkCell> getAllLinksInsideGroupBox(HashSet<FactorCell> selectedFactorAndChildren, DiagramFactor diagramFactor)
	{
		HashSet<LinkCell> linkCells = new HashSet<LinkCell>();
		DiagramModel diagramModel = getDiagramModel();
		ORefList diagramLinkReferrerRefs = diagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int referrrerIndex = 0; referrrerIndex < diagramLinkReferrerRefs.size(); ++referrrerIndex)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRefs.get(referrrerIndex));
			for(FactorCell thisFactorCell : selectedFactorAndChildren)
			{
				if (thisFactorCell.getDiagramFactorRef().equals(diagramFactor.getRef()))
					continue;
				
				if (!diagramLink.isToOrFrom(thisFactorCell.getDiagramFactorRef()))
					continue;
						
				LinkCell linkCell = diagramModel.findLinkCell(diagramLink);
				linkCells.add(linkCell);
			}
		}
		
		return linkCells;
	}
	
	public void zoom(double proportion)
	{
		setScale(getScale() * proportion);
		getMainWindow().saveDiagramZoomSetting(AppPreferences.TAG_DIAGRAM_ZOOM, getScale());
	}
	
	public void setZoomScale(double scale)
	{
		setScale(scale);
		getMainWindow().saveDiagramZoomSetting(AppPreferences.TAG_DIAGRAM_ZOOM, scale);
	}
		
	public boolean hasLocation()
	{
		return false;
	}
	
	private void installKeyBindings(Actions actions)
	{
		Action helpAction = actions.get(ActionContextualHelp.class);
		KeyBinder.bindKey(this, KeyEvent.VK_F1, KeyBinder.KEY_MODIFIER_NONE, helpAction);
		Action deleteAction = actions.get(ActionDelete.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DELETE, KeyBinder.KEY_MODIFIER_NONE, deleteAction);
		KeyBinder.bindKey(this, KeyEvent.VK_BACK_SPACE, KeyBinder.KEY_MODIFIER_NONE, deleteAction);
		//JAVA ISSUE: We had to create new actions here since the key pressed which caused this action
		//Is not sent to the action.
		//javax.swing.SwingUtilities doesn't pass the keycode to the action. 
		Action nudgeActionUp = actions.get(ActionNudgeUp.class);
		KeyBinder.bindKey(this, KeyEvent.VK_UP, KeyBinder.KEY_MODIFIER_NONE, nudgeActionUp);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_UP, KeyBinder.KEY_MODIFIER_NONE, nudgeActionUp);
		Action nudgeActionDown = actions.get(ActionNudgeDown.class);
		KeyBinder.bindKey(this, KeyEvent.VK_DOWN, KeyBinder.KEY_MODIFIER_NONE, nudgeActionDown);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_DOWN, KeyBinder.KEY_MODIFIER_NONE, nudgeActionDown);
		Action nudgeActionLeft = actions.get(ActionNudgeLeft.class);
		KeyBinder.bindKey(this, KeyEvent.VK_LEFT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionLeft);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_LEFT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionLeft);
		Action nudgeActionRight = actions.get(ActionNudgeRight.class);
		KeyBinder.bindKey(this, KeyEvent.VK_RIGHT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionRight);
		KeyBinder.bindKey(this, KeyEvent.VK_KP_RIGHT, KeyBinder.KEY_MODIFIER_NONE, nudgeActionRight);
		
		Action selectAll = actions.get(ActionSelectAll.class);
		KeyBinder.bindKey(this, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, selectAll);
	}
	
	public CellView getNextSelectableViewAt(CellView current, double x, double y)
	{
		CellView candidateView = super.getNextSelectableViewAt(current, x, y);
		// if they want the first node, we know it is ok because the project scope box
		// is always behind the targets
		if(current == null || candidateView == null)
			return candidateView;
		
		EAMGraphCell candidateCell = (EAMGraphCell)candidateView.getCell();
		if(candidateCell.isScopeBox())
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

	public void valueChanged(GraphSelectionEvent event)
	{
		try
		{
			Object[] cells = event.getCells();
			for(int i = 0; i < cells.length; ++i)
			{
				Object rawCell = cells[i];
				if(! (rawCell instanceof EAMGraphCell))
					continue;
				
				EAMGraphCell cell = (EAMGraphCell)rawCell;
				if(cell.isFactor())
				{
					GraphLayoutCache glc = getGraphLayoutCache();
					repaintLinks(glc.getOutgoingEdges(cell, null, true, false));
					repaintLinks(glc.getIncomingEdges(cell, null, true, false));
				}
				else if(cell.isFactorLink())
				{
					Vector<EAMGraphCell> thisLink = new Vector<EAMGraphCell>();
					thisLink.add(cell);
					repaintLinks(thisLink);
				}
			}
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	private void repaintLinks(List edges)
	{
		GraphLayoutCache glc = getGraphLayoutCache();
		Iterator iter = edges.iterator();
		while(iter.hasNext())
		{
			LinkCell link = (LinkCell)iter.next();
			CellView view = glc.getMapping(link, false);
			if(view != null)
			{
				repaint(view.getBounds().getBounds());
				link.update(this);
				view.update(glc);
				repaint(view.getBounds().getBounds());
				link.autoSelect(this);
			}
		}
		// TODO: This shouldn't be necessary, but merely repainting the bounds of
		// the individual links is not enough for the "Lake Ontario". When clicking 
		// on the links in the lower center and right, they are not fully repainted
		repaint(getBounds());
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
	
	public void setMarquee(boolean isMarqueeToUse)
	{
		isMarquee = isMarqueeToUse;
	}
	
	public boolean isMarquee()
	{
		return isMarquee;
	}
	
	public EdgeView getEdgeView(LinkCell linkCell)
	{
		return (EdgeView) getGraphLayoutCache().getMapping(linkCell, false);
	}
	
	public Point getUnscaledPoint(Point pointToUnscale)
	{
		return new Point((int)(pointToUnscale.x / scale), (int)(pointToUnscale.y / scale));
	}
	
	public Point2D.Double getScaledPoint(Point pointToScale)
	{
		return new Point2D.Double((pointToScale.x * scale), (pointToScale.y * scale));
	}
	
	public Point2D.Double getScaledPoint(Point2D.Double pointToScale)
	{
		return new Point2D.Double((pointToScale.x * scale), (pointToScale.y * scale));
	}
	
	public Rectangle2D.Double getScaledRectangle(Rectangle2D rect)
	{
		Point2D.Double scaledPoint = getScaledPoint(new Point2D.Double(rect.getX(), rect.getY()));
		Dimension scaledSize = getScaledDimension(rect.getWidth(), rect.getHeight());
		
		return new Rectangle2D.Double(scaledPoint.x, scaledPoint.y, scaledSize.width, scaledSize.height);
	}
	
	public Dimension getScaledDimension(double width, double height)
	{
		return new Dimension((int)(width * scale), (int)(height * scale));
	}
	
	public Rectangle2D.Double getScaledBounds(LinkCell linkCell)
	{
		Rectangle2D rect = getBounds(linkCell);

		return getScaledRectangle(rect);
	}
	
	public Rectangle2D getBounds(LinkCell linkCell)
	{
		EdgeView view = (EdgeView) getGraphLayoutCache().getMapping(linkCell, false);
	
		//TODO shoud check to see if the link is first visible outside of this method, in order to 
		// avoid the null test below.  the null test exists becuase if there are draft starts in a
		// project,  you cant create a bend point, exceptions are thrown.  view is null
		if (view != null)
			return view.getBounds();
		
		return new Rectangle(-1, -1, -1, -1);
	}
	
	public Rectangle2D getTotalBoundsUsed()
	{
		Rectangle2D totalBounds = null;
		Object[] allCells = getRoots();
		for (int i = 0 ; i < allCells.length; ++i)
		{
			DefaultGraphCell cell = (DefaultGraphCell)allCells[i];
			if (!graphLayoutCache.isVisible(cell))
				continue;
			
			Rectangle2D cellBounds = getCellBounds(cell);
			if (totalBounds == null)
				totalBounds = new Rectangle(cellBounds.getBounds());
			
			if (cellBounds != null)
				totalBounds.add(cellBounds);
		}
		
		return totalBounds;
	}
	
	private MainWindow mainWindow;
	private Color defaultBackgroundColor;
	private Project project;
	private DiagramContextMenuHandler diagramContextMenuHandler;
	private boolean isMarquee;
}

