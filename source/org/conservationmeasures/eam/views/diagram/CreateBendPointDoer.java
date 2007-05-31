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
import org.conservationmeasures.eam.utils.BendPointList;
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
			diagramModel = getDiagramView().getDiagramModel();
			diagram = getDiagramView().getDiagramComponent();
			cache = diagram.getGraphLayoutCache();
			
			DiagramFactorLink selectedLink = getDiagramView().getDiagramPanel().getOnlySelectedLinks()[0];
			LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
			Point newBendPoint = linkCell.getNewBendPointLocation(diagramModel, cache, getLocation());
			Point snapped = getProject().getSnapped(newBendPoint);
			BendPointList newListWithBendPoint = linkCell.getNewBendPointList(diagramModel, cache, snapped);
			
			CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newListWithBendPoint);
			getProject().executeCommand(setBendPointsCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public LinkCell[] getNearbyLinks(DiagramModel model, Point point)
	{
		final double WITHIN_RANGE_PIXEL_COUNT = 2;
		LinkCell[] allCells = model.getAllFactorLinkCells();
		Vector nearbyLinks = new Vector();
		
		for (int i = 0; i < allCells.length; ++i)
		{
			LinkCell linkCell = allCells[i];
			
			if (!isWithinBounds(linkCell, point))
				continue;
		
			BendPointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
			Line2D.Double[] lineSegments = bendPoints.convertToLineSegments();
			if (isWithinRange(lineSegments, point, WITHIN_RANGE_PIXEL_COUNT))
				nearbyLinks.add(linkCell);
		}
		
		return (LinkCell[]) nearbyLinks.toArray(new LinkCell[0]);
	}
	
	private boolean isWithinBounds(LinkCell linkCell, Point point)
	{
		EdgeView view = (EdgeView) cache.getMapping(linkCell, false);
		Rectangle2D bounds = view.getBounds();
		
		return bounds.contains(point);
	}

	private boolean isWithinRange(Line2D.Double[] lineSegments, Point point, final double range)
	{
		for (int i = 0; i < lineSegments.length; ++i)
		{
			Line2D.Double line = lineSegments[i];
			Point2D point2D = Utility.convertToPoint2D(point);
			double distance = line.ptLineDist(point2D);
			if (distance < range)
				return true;
		}
		
		return false;
	}
	
	DiagramComponent diagram;
	GraphLayoutCache cache;
	DiagramModel diagramModel;
}
