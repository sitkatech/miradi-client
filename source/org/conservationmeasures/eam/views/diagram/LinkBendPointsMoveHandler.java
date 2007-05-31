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
import org.conservationmeasures.eam.utils.BendPointList;

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
			LinkCell linkCell = linkCells[i];
			int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
			moveBendPoints(linkCell, selectionIndexes, deltaX, deltaY);
		}
	}
	
	public void moveBendPoints(LinkCell linkCell, int[] selectionIndexes, int deltaX, int deltaY) throws CommandFailedException
	{
		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
		BendPointList pointsToMove = diagramLink.getBendPoints();
		
		for (int i = 0; i < selectionIndexes.length; ++i)
		{
			Point pointToMove = pointsToMove.get(selectionIndexes[i]);
			pointToMove.x = pointToMove.x + deltaX;
			pointToMove.y = pointToMove.y + deltaY;		
		}
		
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramFactorLink.TAG_BEND_POINTS, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
		
	Project project;
}
