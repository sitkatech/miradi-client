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
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;

public class BendPointCreator
{
	public BendPointCreator(Project projectToUse, DiagramComponent diagramToUse)
	{
		project = projectToUse;
		diagram = diagramToUse;
		model = diagram.getDiagramModel();
	}
	
	//TODO write test for this method (nima)
	public void createBendPoint(Point insertionLocation, DiagramFactorLink selectedLink) throws Exception
	{
		LinkCell selectedLinkCell = model.getDiagramFactorLink(selectedLink);
		Point insertPoint = selectedLinkCell.getNewBendPointLocation(model, getCache(), insertionLocation);
		LinkCell[] nearbyLinks = getNearbyLinks(insertPoint, selectedLinkCell);
		for (int i = 0; i < nearbyLinks.length; ++i)
		{
			insertBendPointForLink(nearbyLinks[i], insertPoint);
		}
	
	}
	
	private void insertBendPointForLink(LinkCell linkCell, Point insertPoint) throws Exception
	{
		DiagramFactorLink selectedLink = linkCell.getDiagramFactorLink();
		Point snapped = project.getSnapped(insertPoint);
		PointList newListWithBendPoint = linkCell.getNewBendPointList(model, getCache(), snapped);
		
		CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newListWithBendPoint);
		project.executeCommand(setBendPointsCommand);
	}
	
	public LinkCell[] getNearbyLinks(Point point, LinkCell selectedLinkCell)
	{
		LinkCell[] allCells = model.getAllFactorLinkCells();
		Vector nearbyLinks = new Vector();
		
		for (int i = 0; i < allCells.length; ++i)
		{
			LinkCell linkCell = allCells[i];
			if (! getBounds(linkCell).contains(point))
				continue;
		 
			PointList pointList = getAllLinkPoints(linkCell);
			Line2D.Double[] lineSegments = pointList.convertToLineSegments();
			if (isWithinRange(lineSegments, point))
				nearbyLinks.add(linkCell);
		}
		
		return (LinkCell[]) nearbyLinks.toArray(new LinkCell[0]);
	}

	private PointList getAllLinkPoints(LinkCell linkCell)
	{
		PointList pointList = new PointList();
		pointList.add(linkCell.getSourceLocation(getCache()));
		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
		pointList.addAll(diagramLink.getBendPoints().getAllPoints());
		pointList.add(linkCell.getTargetLocation(getCache()));
		
		return pointList;
	}
	
	private Rectangle2D getBounds(LinkCell linkCell)
	{
		EdgeView view = (EdgeView) getCache().getMapping(linkCell, false);
		
		return view.getBounds(); 
	}
	
	private boolean isWithinRange(Line2D.Double[] lineSegments, Point point)
	{
		for (int i = 0; i < lineSegments.length; ++i)
		{
			Line2D.Double line = lineSegments[i];
			Point2D point2D = Utility.convertToPoint2D(point);
			double distance = line.ptLineDist(point2D);
			if (distance < project.getGridSize())
				return true;
		}
		
		return false;
	}
	
	private GraphLayoutCache getCache()
	{
		return diagram.getGraphLayoutCache();
	}
	
	DiagramComponent diagram;
	DiagramModel model;
	Project project;
}
