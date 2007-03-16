/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;

public class CreateBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		Point clickLocation = getLocation();
		LinkCell cell = getLinkCellAtLocation(clickLocation);
		if (cell == null)
			return false;
			
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			DiagramFactorLink selectedLink = getLinkCellAtLocation(getLocation()).getDiagramFactorLink();
			Point newBendPoint = getNewBendPointLocation();
			PointList newListWithBendPoint = getNewBendPointList(selectedLink, newBendPoint);
			
			String newList = newListWithBendPoint.toString();
			String previousList = selectedLink.getBendPoints().toString();
			DiagramFactorLinkId linkId = selectedLink.getDiagramLinkageId();
			
			CommandSetObjectData setBendPointsCommand= new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramFactorLink.TAG_BEND_POINTS, newList, previousList);
			getProject().executeCommand(setBendPointsCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private PointList getNewBendPointList(DiagramFactorLink selectedLink, Point newBendPoint)
	{
		DiagramModel diagramModel = getProject().getDiagramModel();
		LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
		
		DiagramComponent diagram = getMainWindow().getDiagramComponent();
		GraphLayoutCache cache = diagram.getGraphLayoutCache();
		PortView sourceView = (PortView) cache.getMapping(linkCell.getSource(), false);
		PortView targetView = (PortView) cache.getMapping(linkCell.getTarget(), false);
		PointList bendPointsOnly = selectedLink.getBendPoints();
		
		PointList allLinkPoints = new PointList();
		allLinkPoints.add(Utility.convertToPoint(sourceView.getLocation()));
		allLinkPoints.addAll(bendPointsOnly.getAllPoints());
		allLinkPoints.add(Utility.convertToPoint(targetView.getLocation()));
		
		double closestDistance = Double.MAX_VALUE;
		int insertionIndex = -1;
		for (int i = 0; i < allLinkPoints.size() - 1; i++)
		{
			Line2D.Double lineSegment = createLineSegment(allLinkPoints, i);
			Point2D convertedPoint = Utility.convertToPoint2D(newBendPoint);
			
			Rectangle bound = lineSegment.getBounds();
			bound.grow(5, 5);
			if (! bound.contains(newBendPoint))
			{
				continue;
			}
			
			double distance = lineSegment.ptLineDist(convertedPoint);
			if (distance < closestDistance )
			{
				closestDistance = distance;
				insertionIndex = i;
			}
		}
		
		bendPointsOnly.insertAt(newBendPoint, insertionIndex);
		return bendPointsOnly;
	}

	private Line2D.Double createLineSegment(PointList allLinkPoints, int index)
	{
		Point point1 = Utility.convertToPoint(allLinkPoints.get(index));
		Point point2 = Utility.convertToPoint(allLinkPoints.get(index + 1));
		
		return new Line2D.Double(point1, point2);
	}
	
	private Point getNewBendPointLocation()
	{
		Point newBendPoint = getLocation();
		if (newBendPoint != null)
			return newBendPoint;
	
		//TODO in this case find first segement and insert (get ) the middle location 
		
		//TODO calculate location without mouse click location 
		return new Point(0, 0);
	}
	
	private LinkCell getLinkCellAtLocation(Point point)
	{
		if (point == null)
			return null;
		
		DiagramComponent diagram = getMainWindow().getDiagramComponent();
		EAMGraphCell cell = (EAMGraphCell) diagram.getFirstCellForLocation(point.x, point.y);
		if (cell == null)
			return null;
		
		if (!cell.isFactorLink())
			return null;
		
		return (LinkCell) cell;

	}

}
