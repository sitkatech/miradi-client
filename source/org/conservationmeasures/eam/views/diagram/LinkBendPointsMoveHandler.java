/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.utils.Utility;

public class LinkBendPointsMoveHandler
{

	public LinkBendPointsMoveHandler(DiagramComponent diagramToUse, Project projectToUse)
	{
		diagram = diagramToUse;
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
	private void moveBendPoints(LinkCell linkCell, int[] selectionIndexes, Point2D[] bendPoints) throws Exception
	{
		DiagramLink diagramLink = linkCell.getDiagramLink();
		PointList pointsToMove = diagramLink.getBendPoints().createClone();
		
		for (int i = 0; i < selectionIndexes.length; ++i)
		{
			int selectionIndex = selectionIndexes[i];
			Point newPointLocation = Utility.convertToPoint(bendPoints[selectionIndex]);
			Point snapped = project.getSnapped(newPointLocation);
			newPointLocation.setLocation(snapped.x, snapped.y);
			
			Point point = pointsToMove.get(selectionIndex);
			point.setLocation(newPointLocation);
			
			createBendPointOnNeabyLinks(linkCell, newPointLocation);
		}
		
		executeBendPointMoveCommand(diagramLink, pointsToMove);
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
		
			createBendPointOnNeabyLinks(linkCell, newPointLocation);
		}
		
		executeBendPointMoveCommand(diagramLink, pointsToMove);
	}
	
	private void executeBendPointMoveCommand(DiagramLink diagramLink, PointList pointsToMove) throws CommandFailedException
	{
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramLink.TAG_BEND_POINTS, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
		
	private void createBendPointOnNeabyLinks(LinkCell linkCell, Point pointToMove) throws Exception
	{
		if (moreThanOneBendPointSelected())
			return;
		
		BendPointCreator bendPointCreator = new BendPointCreator(diagram);
		bendPointCreator.createBendPointOnNearbyLinks(linkCell, pointToMove);
	}

	private boolean moreThanOneBendPointSelected()
	{
		LinkCell[] allCells = diagram.getDiagramModel().getAllFactorLinkCells();
		int totalSelectedBendPointCount = 0;
		for (int i = 0; i < allCells.length; ++i)
		{
			LinkCell linkCell = allCells[i];
			int[] selectedBendPointIndexes = linkCell.getSelectedBendPointIndexes();
			totalSelectedBendPointCount += selectedBendPointIndexes.length;
			if (totalSelectedBendPointCount > 1)
				return true;
		}
		
		return false;
	}

	DiagramComponent diagram;
	Project project;
}
