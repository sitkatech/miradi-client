/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;
import org.jgraph.graph.EdgeView;
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
			model = getDiagramView().getDiagramModel();
			diagram = getDiagramView().getDiagramComponent();
			cache = diagram.getGraphLayoutCache();
			
			DiagramFactorLink selectedLink = getDiagramView().getDiagramPanel().getOnlySelectedLinks()[0];
			LinkCell selectedLinkCell = model.getDiagramFactorLink(selectedLink);
			insertBendPointForLink(selectedLinkCell);
//FIXME repair commented code below.  this code looks to see if it should create
// a bend point on any nearby link
//			LinkCell[] nearbyLinks = getNearbyLinks(getLocation(), selectedLinkCell);
//			for (int i = 0; i < nearbyLinks.length; ++i)
//			{
//				insertBendPointForLink(nearbyLinks[i]);
//			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private void insertBendPointForLink(LinkCell linkCell) throws CommandFailedException
	{
		DiagramFactorLink selectedLink = linkCell.getDiagramFactorLink();
		Point newBendPoint = linkCell.getNewBendPointLocation(model, cache, getLocation());
		Point snapped = getProject().getSnapped(newBendPoint);
		PointList newListWithBendPoint = linkCell.getNewBendPointList(model, cache, snapped);
		
		CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newListWithBendPoint);
		getProject().executeCommand(setBendPointsCommand);
	}
	
	public LinkCell[] getNearbyLinks(Point point, LinkCell selectedLinkCell)
	{
		LinkCell[] allCells = model.getAllFactorLinkCells();
		Vector nearbyLinks = new Vector();
		
		for (int i = 0; i < allCells.length; ++i)
		{
			LinkCell linkCell = allCells[i];
			if (selectedLinkCell.equals(linkCell))
				continue; 
			
			if (!isWithinBounds(linkCell, point))
				continue;
		
			PointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
			Line2D.Double[] lineSegments = bendPoints.convertToLineSegments();
			if (isWithinRange(lineSegments, point))
				nearbyLinks.add(linkCell);
		}
		
		return (LinkCell[]) nearbyLinks.toArray(new LinkCell[0]);
	}
	
	private boolean isWithinBounds(LinkCell linkCell, Point point)
	{
		EdgeView view = (EdgeView) cache.getMapping(linkCell, false);
		Rectangle2D bounds = view.getBounds();
		if (point == null)
			return false;
		
		return bounds.contains(point);
	}

	private boolean isWithinRange(Line2D.Double[] lineSegments, Point point)
	{
		for (int i = 0; i < lineSegments.length; ++i)
		{
			Line2D.Double line = lineSegments[i];
			Point2D point2D = Utility.convertToPoint2D(point);
			double distance = line.ptLineDist(point2D);
			if (distance < getProject().DEFAULT_GRID_SIZE)
				return true;
		}
		
		return false;
	}
	
	DiagramComponent diagram;
	GraphLayoutCache cache;
	DiagramModel model;
}
