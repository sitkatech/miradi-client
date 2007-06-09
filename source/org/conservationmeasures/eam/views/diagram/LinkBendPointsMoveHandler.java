/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;

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
	
	public void moveBendPoints(LinkCell linkCell, int deltaX, int deltaY) throws Exception
	{
		int[] selectionIndexes = linkCell.getBendPointSelectionHelper().getSelectedIndexes();
		moveBendPoints(linkCell, selectionIndexes, deltaX, deltaY);
	}
	
	public void moveBendPoints(LinkCell linkCell, int[] selectionIndexes, int deltaX, int deltaY) throws Exception
	{
		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
		PointList pointsToMove = diagramLink.getBendPoints().createClone();
		
		for (int i = 0; i < selectionIndexes.length; ++i)
		{
			Point newPointLocation = pointsToMove.get(selectionIndexes[i]);
			newPointLocation.x = newPointLocation.x + deltaX;
			newPointLocation.y = newPointLocation.y + deltaY;
			Point snapped = project.getSnapped(newPointLocation);
			newPointLocation.x = snapped.x;
			newPointLocation.y = snapped.y;
			
			createBendPointOnNeabyLinks(linkCell, newPointLocation);
		}
		
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramFactorLink.TAG_BEND_POINTS, pointsToMove);
		project.executeCommand(bendPointMoveCommand);
	}
		
	private void createBendPointOnNeabyLinks(LinkCell linkCell, Point pointToMove) throws Exception
	{
		if (moreThanOneBendPointSelected())
			return;
		
		BendPointCreator bendPointCreator = new BendPointCreator(diagram);
		LinkCell[] nearbyLinkCells = bendPointCreator.getNearbyLinks(pointToMove, linkCell);
		for (int i = 0; i < nearbyLinkCells.length; ++i)
		{
			LinkCell nearByLinkCell = nearbyLinkCells[i];
			DiagramFactorLink nearbyDiagramLink = nearByLinkCell.getDiagramFactorLink();
			if (nearByLinkCell.equals(linkCell))
				continue;
			
			PointList bendPoints = nearbyDiagramLink.getBendPoints();
			if (! bendPoints.contains(pointToMove))
				bendPointCreator.insertBendPointForLink(nearByLinkCell, pointToMove);
		}
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
