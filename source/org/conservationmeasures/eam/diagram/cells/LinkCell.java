/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.BendPointSelectionHelper;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.renderers.ArrowLineRenderer;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;

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
	
	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return diagramLink.getDiagramLinkageId();
	}
	
	public DiagramLink getDiagramLink()
	{
		return diagramLink;
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
	
	public ORef getWrappedORef()
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
		if(diagram.isCellSelected(this))
			return true;
		if(diagram.isCellSelected(getFrom()) || diagram.isCellSelected(getTo()))
			return true;
		
		LayerManager layerManager = diagram.getProject().getLayerManager();
		if(!layerManager.areFactorLinksVisible())
			return false;
		if(!getTo().isTarget())
			return true;
		return layerManager.areTargetLinksVisible();
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
	    GraphConstants.setBackground(getAttributes(), Color.BLACK);
	    GraphConstants.setForeground(getAttributes(), Color.BLACK);
	    GraphConstants.setGradientColor(getAttributes(), Color.BLACK); //Windows 2000 quirk required to see line.
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
	
	private BendPointSelectionHelper bendSelectionHelper;
	private FactorLink link;
	private DiagramLink diagramLink;
	private FactorCell from;
	private FactorCell to;
}
