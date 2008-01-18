/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;

public class LinkBendPointsMoveHandler
{

	public LinkBendPointsMoveHandler(Project projectToUse)
	{
		project = projectToUse;
	}

	public void moveLinkBendPoints(LinkCell[] linkCells, int deltaX, int deltaY) throws Exception
	{
		for (int i = 0; i < linkCells.length; i++)
		{
			LinkCell linkCell = linkCells[i];
			moveBendPoints(linkCell, deltaX, deltaY);
		}
	}
	
	public void moveBendPoints(LinkCell linkCell, Point2D[] bendPoints) throws Exception
	{
		int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
		moveBendPoints(linkCell, selectionIndexes, bendPoints);
	}
	
	public void moveBendPoints(LinkCell linkCell, int deltaX, int deltaY) throws Exception
	{
		int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
		moveBendPoints(linkCell, selectionIndexes, deltaX, deltaY);
	}

	//TODO nima check for possible duplicate code in this class
	private void moveBendPoints(LinkCell linkCell, int[] selectionIndexes, Point2D[] movedBendPoints) throws Exception
	{		
		PointList snappedMovedBendPoints = createSnappedBendPoints(movedBendPoints);
        PointList movedBendPointWithoutDuplicates = omitDuplicateBendPoints(snappedMovedBendPoints);
        DiagramLink diagramLink = linkCell.getDiagramLink();
		executeBendPointMoveCommand(diagramLink, movedBendPointWithoutDuplicates);
	}

	private PointList createSnappedBendPoints(Point2D[] movedBendPoints)
	{
		PointList snappedPoints = new PointList();
		for (int i = 0; i < movedBendPoints.length; ++i)
		{
			Point movedPoint = Utility.convertPoint2DToPoint(movedBendPoints[i]);
        	snappedPoints.add(project.getSnapped(movedPoint));
		}
		
		return snappedPoints;
	}

	private PointList omitDuplicateBendPoints(PointList movedBendPoints)
	{
		PointList bendPointsWithoutDuplicates = new PointList();
		for (int i = 0; i < movedBendPoints.size(); ++i)
		{
			Point movedPoint = movedBendPoints.get(i);
			if (!bendPointsWithoutDuplicates.contains(movedPoint))
				bendPointsWithoutDuplicates.add(movedPoint);
		}
		
		return bendPointsWithoutDuplicates;
	}

	public void moveBendPoints(LinkCell linkCell, int[] selectionIndexes, int deltaX, int deltaY) throws Exception
	{
		DiagramLink diagramLink = linkCell.getDiagramLink();
		PointList pointsToMove = diagramLink.getBendPoints().createClone();
		
		for (int i = 0; i < selectionIndexes.length; ++i)
		{
			Point newPointLocation = pointsToMove.get(selectionIndexes[i]);
			newPointLocation.setLocation(newPointLocation.x + deltaX, newPointLocation.y + deltaY);
			Point snapped = project.getSnapped(newPointLocation);
			newPointLocation.setLocation(snapped);
		}
		
		executeBendPointMoveCommand(diagramLink, pointsToMove);
	}
	
	private void executeBendPointMoveCommand(DiagramLink diagramLink, PointList pointsToMove) throws CommandFailedException
	{
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramLink.TAG_BEND_POINTS, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
		
	private Project project;
}
