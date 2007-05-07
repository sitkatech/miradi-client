/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.renderers.ArrowLineRenderer;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class LinkCell extends EAMGraphCell implements Edge
{
	public LinkCell(FactorLink linkToUse, DiagramFactorLink diagramLinkToUse, FactorCell fromToUse, FactorCell toToUse)
	{
		link = linkToUse;
		diagramLink = diagramLinkToUse;
		from = fromToUse;
		to = toToUse;
		updateFromDiagramFactorLink();
	}
	
	public void updateFromDiagramFactorLink()
	{
		fillConnectorAttributeMap("");
		updateBendPoints();
	}
	
	public boolean isFactorLink()
	{
		return true;
	}
	
	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return diagramLink.getDiagramLinkageId();
	}
	
	public DiagramFactorLink getDiagramFactorLink()
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
	
	private void updateBendPoints()
	{
		PointList bendPoints = getDiagramFactorLink().getBendPoints();
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
		
		if (link.isBidirectional())
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
	
	public static PointList extractBendPointsOnly(List points)
	{
		PointList pointList = new PointList();
		
		int FROM_INDEX = 1;
		int TO_INDEX = points.size() - 1;
		for (int index = FROM_INDEX; index < TO_INDEX; index++)
		{
			Point convertedPoint = Utility.convertToPoint((Point2D) points.get(index));
			pointList.add(convertedPoint);
		}

		return pointList;
	}



	FactorLink link;
	DiagramFactorLink diagramLink;
	FactorCell from;
	FactorCell to;
}
