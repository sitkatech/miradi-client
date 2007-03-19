/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
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

	public void moveLinkBendPoints(DiagramFactorLink[] diagramLinks, int deltaX, int deltaY) throws CommandFailedException
	{
		for (int i = 0; i < diagramLinks.length; i++)
		{
			moveBendPoints(diagramLinks[i], deltaX, deltaY);
		}
	}
	
	private void moveBendPoints(DiagramFactorLink diagramLink, int deltaX, int deltaY) throws CommandFailedException
	{
		PointList pointsToMove = diagramLink.getBendPoints();
		PointList movedPoints = new PointList();
		for (int i = 0; i < pointsToMove.size(); i++)
		{
			Point originalPoint = pointsToMove.get(i);
			Point movedPoint = new Point(originalPoint.x + deltaX, originalPoint.y + deltaY);
			movedPoints.add(movedPoint);
		}
		
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramFactorLink.TAG_BEND_POINTS, movedPoints, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
	
	Project project;
}
