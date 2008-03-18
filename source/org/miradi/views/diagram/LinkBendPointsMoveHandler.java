/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.awt.Point;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.utils.PointList;

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
	
	public void moveBendPoints(LinkCell linkCell, int deltaX, int deltaY) throws Exception
	{
		int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
		moveBendPoints(linkCell, selectionIndexes, deltaX, deltaY);
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
	
	public void executeBendPointMoveCommand(DiagramLink diagramLink, PointList pointsToMove) throws CommandFailedException
	{
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramLink.TAG_BEND_POINTS, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
		
	private Project project;
}
