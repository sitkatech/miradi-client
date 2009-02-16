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
package org.miradi.diagram.cells;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;
import org.miradi.diagram.BendPointSelectionHelper;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.renderers.ArrowLineRenderer;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.utils.PointList;
import org.miradi.utils.Utility;
import org.miradi.views.diagram.LayerManager;

public class LinkCell extends EAMGraphCell implements Edge
{
	public LinkCell(FactorLink linkToUse, DiagramLink diagramLinkToUse, FactorCell fromToUse, FactorCell toToUse)
	{
		link = linkToUse;
		diagramLink = diagramLinkToUse;
		from = fromToUse;
		to = toToUse;
		bendSelectionHelper = new BendPointSelectionHelper(this);
		
		updateFromDiagramFactorLink();
	}
	
	public void updateFromDiagramFactorLink()
	{
		fillConnectorAttributeMap("");
		updateBendPoints();
	}
	
	public BendPointSelectionHelper getBendPointSelectionHelper()
	{
		return bendSelectionHelper;
	}
	
	public String getToolTipString() 
	{
		return getDiagramLink().getToolTipString();
	}
	
	public Color getColor()
	{
		return getDiagramLink().getColor();
	}

	public int[] getSelectedBendPointIndexes()
	{
		return bendSelectionHelper.getSelectedIndexes();
	}
	
	public void clearBendPointSelectionList()
	{
		bendSelectionHelper.clearSelection();
	}
	
	public boolean isFactorLink()
	{
		return true;
	}
	
	public DiagramLinkId getDiagramFactorLinkId()
	{
		return diagramLink.getDiagramLinkId();
	}
	
	public DiagramLink getDiagramLink()
	{
		return diagramLink;
	}
	
	public ORef getDiagramLinkRef()
	{
		return getDiagramLink().getRef();
	}

	public FactorCell getFrom()
	{
		return from;
	}
	
	public FactorCell getTo()
	{
		return to;
	}
	
	public FactorLink getFactorLink()
	{
		return link;
	}
	
	public ORef getWrappedFactorLinkRef()
	{
		return getFactorLink().getRef();
	}
	
	private void updateBendPoints()
	{
		PointList bendPoints = getDiagramLink().getBendPoints();
		Vector bendPointList = new Vector(bendPoints.getAllPoints());
		Vector newList = new Vector();
		newList.add(to.getLocation());
		newList.addAll(bendPointList);
		newList.add(from.getLocation());
		GraphConstants.setPoints(getAttributes(), newList);
	}
	
	public void update(DiagramComponent diagram)
	{
		int arrowTailStyle = GraphConstants.ARROW_NONE;
		
		if (getDiagramLink().isBidirectional())
			arrowTailStyle = GraphConstants.ARROW_TECHNICAL;
		else if(!isThisLinkBodyVisible(diagram))
			arrowTailStyle = ArrowLineRenderer.ARROW_STUB_LINE;

		setTail(arrowTailStyle);
		updateBendPoints();
	}

	public boolean isThisLinkBodyVisible(DiagramComponent diagram)
	{
		if (diagram == null)
		{
			EAM.logWarning("Diagram in LinkCell.isThisLinkBodyVisible is null.");
			return true;
		}
		
		if(isSelectedOrRelated(diagram))
			return true;
		
		LayerManager layerManager = diagram.getLayerManager();
		if(!layerManager.areFactorLinksVisible())
			return false;
		if(!getTo().isTarget())
			return true;
		return layerManager.areTargetLinksVisible();
	}
	
	private boolean isSelectedOrRelated(DiagramComponent diagram)
	{
		if(diagram == null)
			return false;
		
		if(diagram.isCellSelected(this))
			return true;

		if(diagram.isCellSelected(getFrom()) || diagram.isCellSelected(getTo()))
			return true;
		
		return false;
	}
	
	public boolean isLinkedToAnySelectedFactor(DiagramComponent diagram) throws Exception
	{
		IdList listOfGroupBoxesAndChildren = new IdList(DiagramFactor.getObjectType());
		listOfGroupBoxesAndChildren.addAll(from.getDiagramFactor().getSelfAndChildren().convertToIdList(DiagramFactor.getObjectType()));
		listOfGroupBoxesAndChildren.addAll(to.getDiagramFactor().getSelfAndChildren().convertToIdList(DiagramFactor.getObjectType()));
		for (int i = 0; i < listOfGroupBoxesAndChildren.size(); ++i)
		{			
			FactorCell factorCell = diagram.getDiagramModel().getFactorCellByRef(listOfGroupBoxesAndChildren.getRef(i));
			if (diagram.isCellSelected(factorCell))
					return true;
		}
		
		return false;
	}
	
	private void setTail(int arrowStyle)
	{
		GraphConstants.setLineBegin(getAttributes(), arrowStyle);
		GraphConstants.setBeginFill(getAttributes(), true);
		GraphConstants.setBeginSize(getAttributes(), 10);
	}

	public Point getFromLocation()
	{
		return from.getLocation();
	}
	
	public Point getToLocation()
	{
		return to.getLocation();
	}
	
	// BEGIN Edge Interface
	public Object getSource()
	{
		return from.getPort();
	}

	public Object getTarget()
	{
		return to.getPort();
	}

	public void setSource(Object source)
	{
		// not allowed--ignore attempts to reset the source
	}

	public void setTarget(Object target)
	{
		// not allowed--ignore attempts to reset the target
	}
	// END Edge interface
	
	private void fillConnectorAttributeMap(String label)
	{
	    GraphConstants.setValue(getAttributes(), label);
	    GraphConstants.setOpaque(getAttributes(), true);
		GraphConstants.setBackground(getAttributes(), getColor());
	    GraphConstants.setForeground(getAttributes(), getColor());
	    GraphConstants.setGradientColor(getAttributes(), getColor()); //Windows 2000 quirk required to see line.
		GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setEndFill(getAttributes(), true);
	}
	
	public void autoSelect(DiagramComponent diagram)
	{
		if (diagram.isCellSelected(from) && diagram.isCellSelected(to))
		{
			diagram.addSelectionCell(this);
			autoSelectAllBendPoints();
		}
		
		else if (diagram.isCellSelected(this))
			diagram.addSelectionCell(this);
		
		else 
			diagram.removeSelectionCell(this);
	}

	private void autoSelectAllBendPoints()
	{
		if (bendSelectionHelper.getSelectedIndexes().length != 0)
			return;
		
		bendSelectionHelper.selectAll();
	}
	
	public PointList getNewBendPointList(DiagramModel diagramModel, GraphLayoutCache cache, Point newBendPoint)
	{
		Point sourceLocation = getSourceLocation(cache);
		Point targetLocation = getTargetLocation(cache);
		PointList bendPointsOnly = new PointList(diagramLink.getBendPoints());
		
		PointList allLinkPoints = new PointList();
		allLinkPoints.add(sourceLocation);
		allLinkPoints.addAll(bendPointsOnly.getAllPoints());
		allLinkPoints.add(targetLocation);
		
		double closestDistance = Double.MAX_VALUE;
		int insertionIndex = 0;
		for (int i = 0; i < allLinkPoints.size() - 1; i++)
		{
			Point fromBendPoint = allLinkPoints.get(i);
			Point toBendPoint = allLinkPoints.get(i + 1);
			Line2D.Double lineSegment = allLinkPoints.createLineSegment(fromBendPoint, toBendPoint);
			Point2D convertedPoint = Utility.convertToPoint2D(newBendPoint);
			
			double distance = lineSegment.ptSegDist(convertedPoint);
			if (distance < closestDistance )
			{
				closestDistance = distance;
				insertionIndex = i;
			}
		}
		bendPointsOnly.insertAt(newBendPoint, insertionIndex);
		selectNewBendPoint(insertionIndex);
		
		return bendPointsOnly;
	}

	private void selectNewBendPoint(int insertionIndex)
	{
		bendSelectionHelper.clearSelection();
		bendSelectionHelper.addToSelectionIndexList(insertionIndex);
	}

	public Point getTargetLocation(GraphLayoutCache cache)
	{
		PortView targetView = (PortView) cache.getMapping(getTarget(), false);
		Point targetLocation = Utility.convertPoint2DToPoint(targetView.getLocation());
		
		return targetLocation;
	}

	public Point getSourceLocation(GraphLayoutCache cache)
	{
		PortView sourceView = (PortView) cache.getMapping(getSource(), false);
		Point sourceLocation = Utility.convertPoint2DToPoint(sourceView.getLocation());
	
		return sourceLocation;
	}

	public Point getNewBendPointLocation(DiagramModel diagramModel, GraphLayoutCache cache, Point newBendPoint)
	{
		if (newBendPoint != null)
			return newBendPoint;
	
		EdgeView view = (EdgeView) cache.getMapping(this, false);
		PointList currentBendPoints = diagramLink.getBendPoints();
		
		//TODO getTargetLocation returs the center of the factor box, which
		//causes the calcuation on the first bendpoint to be wrong.  must get the 
		//point where the link interestects the factor box.  
		Point firstBendPoint = getTargetLocation(cache);
		if (currentBendPoints.size() > 0)
			firstBendPoint = currentBendPoints.get(0);
		
		//note : view.getPoint(0) is not return the same thing as view.getpoints().get(0)
		Point2D point = view.getPoint(0);
		Point sourceLocation = Utility.convertPoint2DToPoint(point);
		
		int middleX = (sourceLocation.x + firstBendPoint.x) / 2; 
		int middleY = (sourceLocation.y + firstBendPoint.y) / 2;
		
		return new Point(middleX, middleY);
	}
	
	public PointList getJGraphCurrentBendPoints(EdgeView view)
	{
		List points = view.getPoints();
		if (points.size() == 2)
			return new PointList();
		
		return extractGraphBendPointsOnly(points);
	}

	private PointList extractGraphBendPointsOnly(List points)
	{
		final int IGNORE_IN = 1;
		final int IGNORE_OUT = points.size() - 1;
		PointList onlyBendPoints = new PointList();
		for (int i = IGNORE_IN; i < IGNORE_OUT; ++i)
		{
			Point rawPoint = Utility.convertPoint2DToPoint((Point2D.Double)(points.get(i)));
			Point snappedPoint = getDiagramLink().getProject().getSnapped(rawPoint);
			onlyBendPoints.add(snappedPoint);
		}
		
		return onlyBendPoints;
	}
	
	private BendPointSelectionHelper bendSelectionHelper;
	private FactorLink link;
	private DiagramLink diagramLink;
	private FactorCell from;
	private FactorCell to;
}
