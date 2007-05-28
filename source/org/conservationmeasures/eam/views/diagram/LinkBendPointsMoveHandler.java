/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;

public class LinkBendPointsMoveHandler
{

	public LinkBendPointsMoveHandler(Project projectToUse)
	{
		project = projectToUse;
	}

	public void moveLinkBendPoints(LinkCell[] linkCells, int deltaX, int deltaY) throws CommandFailedException
	{
		for (int i = 0; i < linkCells.length; i++)
		{
			moveBendPoints(linkCells[i], deltaX, deltaY);
		}
	}
	
	//TODO needs test
	public void moveBendPoints(LinkCell linkCell, int deltaX, int deltaY) throws CommandFailedException
	{
		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
		int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
		PointList pointsToMove = diagramLink.getBendPoints();
		PointList movedPoints = new PointList();
		
		for (int i = 0; i < pointsToMove.size(); i++)
		{
			Point originalPoint = pointsToMove.get(i);
			Point movedPoint = moveSelectedBendPoint(selectionIndexes, i, originalPoint, deltaX, deltaY);
			movedPoints.add(movedPoint);			
		}
		
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramFactorLink.TAG_BEND_POINTS, movedPoints);
		project.executeCommand(bendPointMoveCommand);
	}
	
	private Point moveSelectedBendPoint(int[] selectionIndexes, int bendPointListIndex, Point originalPoint, int deltaX, int deltaY)
	{
		for (int k = 0; k < selectionIndexes.length; ++k)
		{
			if (bendPointListIndex == selectionIndexes[k])
			{
				return new Point(originalPoint.x + deltaX, originalPoint.y + deltaY);
			}
		}
		
		return originalPoint;
	}
	
	Project project;
}
