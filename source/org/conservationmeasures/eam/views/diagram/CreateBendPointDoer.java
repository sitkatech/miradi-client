/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.graph.GraphLayoutCache;

public class CreateBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		DiagramFactorLink[] selectedLinks = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (selectedLinks.length != 1)
			return false;
		
		if (selectedLinks[0].bendPointAlreadyExists(getLocation()))
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			diagramModel = getDiagramView().getDiagramModel();
			diagram = getDiagramView().getDiagramComponent();
			cache = diagram.getGraphLayoutCache();
			
			DiagramFactorLink selectedLink = getDiagramView().getDiagramPanel().getOnlySelectedLinks()[0];
			LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
			Point newBendPoint = linkCell.getNewBendPointLocation(diagramModel, cache, getLocation());
			Point snapped = getProject().getSnapped(newBendPoint);
			PointList newListWithBendPoint = linkCell.getNewBendPointList(diagramModel, cache, snapped);
			
			CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newListWithBendPoint);
			getProject().executeCommand(setBendPointsCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

//TODO remove commented code after transfer and testing
//	private PointList getNewBendPointList(DiagramFactorLink selectedLink, Point newBendPoint)
//	{
//		LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
//		Point sourceLocation = getSourceLocation(linkCell);
//		Point targetLocation = getTargetLocation(linkCell);
//		PointList bendPointsOnly = new PointList(selectedLink.getBendPoints());
//		
//		BendPointList allLinkPoints = new BendPointList();
//		allLinkPoints.add(sourceLocation);
//		allLinkPoints.addAll(bendPointsOnly.getAllPoints());
//		allLinkPoints.add(targetLocation);
//		
//		double closestDistance = Double.MAX_VALUE;
//		int insertionIndex = 0;
//		for (int i = 0; i < allLinkPoints.size() - 1; i++)
//		{
//			Point fromBendPoint = allLinkPoints.get(i);
//			Point toBendPoint = allLinkPoints.get(i + 1);
//			Line2D.Double lineSegment = allLinkPoints.createLineSegment(fromBendPoint, toBendPoint);
//			Point2D convertedPoint = Utility.convertToPoint2D(newBendPoint);
//			
//			Rectangle bound = lineSegment.getBounds();
//			bound.grow(5, 5);
//			if (! bound.contains(newBendPoint))
//			{
//				continue;
//			}
//			
//			double distance = lineSegment.ptLineDist(convertedPoint);
//			if (distance < closestDistance )
//			{
//				closestDistance = distance;
//				insertionIndex = i;
//			}
//		}
//		bendPointsOnly.insertAt(newBendPoint, insertionIndex);
//	
//		return bendPointsOnly;
//	}
//
//	private Point getTargetLocation(LinkCell linkCell)
//	{
//		PortView targetView = (PortView) cache.getMapping(linkCell.getTarget(), false);
//		Point targetLocation = Utility.convertToPoint(targetView.getLocation());
//		
//		return targetLocation;
//	}
//
//	private Point getSourceLocation(LinkCell linkCell)
//	{
//		PortView sourceView = (PortView) cache.getMapping(linkCell.getSource(), false);
//		Point sourceLocation = Utility.convertToPoint(sourceView.getLocation());
//	
//		return sourceLocation;
//	}
//
//	private Point getNewBendPointLocation(DiagramFactorLink selectedLink)
//	{
//		Point newBendPoint = getLocation();
//		if (newBendPoint != null)
//			return newBendPoint;
//	
//		LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
//		EdgeView view = (EdgeView) cache.getMapping(linkCell, false);
//		PointList currentBendPoints = selectedLink.getBendPoints();
//		
//		//TODO getTargetLocation returs the center of the factor box, which
//		//causes the calcuation on the first bendpoint to be wrong.  must get the 
//		//point where the link interestects the factor box.  
//		Point firstBendPoint = getTargetLocation(linkCell);
//		if (currentBendPoints.size() > 0)
//			firstBendPoint = currentBendPoints.get(0);
//		
//		//note : view.getPoint(0) is not return the same thing as view.getpoints().get(0)
//		Point2D point = view.getPoint(0);
//		Point sourceLocation = Utility.convertToPoint(point);
//		
//		int middleX = (sourceLocation.x + firstBendPoint.x) / 2; 
//		int middleY = (sourceLocation.y + firstBendPoint.y) / 2;
//		
//		return new Point(middleX, middleY);
//	}

	DiagramComponent diagram;
	GraphLayoutCache cache;
	DiagramModel diagramModel;
}
