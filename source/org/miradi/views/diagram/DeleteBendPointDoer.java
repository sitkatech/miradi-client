/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.awt.Point;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.utils.PointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
	
		if (! isInDiagram())
			return false;
		
		DiagramLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (links.length == 0)
			return false;

		if (! hasSelectedBendPoint(links))
			return false;
			
		return true;
	}

	private boolean hasSelectedBendPoint(DiagramLink[] links)
	{
		for (int i = 0; i < links.length; ++i)
		{
			LinkCell linkCell = getDiagramView().getDiagramModel().getDiagramFactorLink(links[i]);
			int bendPointSelectionCount = linkCell.getSelectedBendPointIndexes().length;
			if (bendPointSelectionCount > 0)
				return true;
		}
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			deleteBendPoints();
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	private void deleteBendPoints() throws CommandFailedException
	{
		DiagramLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		
		for (int i = 0; i < links.length; ++i)
		{
			DiagramLink diagramFactorLink = links[i];			
			PointList newBendPoints = getBendPointListMinusDeletedPoint(diagramFactorLink);
			
			String newBendPointList = newBendPoints.toJson().toString();
			DiagramFactorLinkId linkId = diagramFactorLink.getDiagramLinkageId();
			CommandSetObjectData removeBendPointCommand = new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramLink.TAG_BEND_POINTS, newBendPointList);
			getProject().executeCommand(removeBendPointCommand);
		}
	}
	
	private PointList getBendPointListMinusDeletedPoint(DiagramLink diagramFactorLink)
	{
		LinkCell linkCell = getDiagramView().getDiagramModel().getDiagramFactorLink(diagramFactorLink);
		PointList currentBendPoints = diagramFactorLink.getBendPoints();
		int[] selectedBendPointIndexes = linkCell.getSelectedBendPointIndexes();
		PointList newBendPoints = new PointList();
		newBendPoints.addAll(currentBendPoints.getAllPoints());
		
		for (int i = 0; i < selectedBendPointIndexes.length; ++i)
		{
			Point pointToRemove = currentBendPoints.get(selectedBendPointIndexes[i]);
			newBendPoints.removePoint(pointToRemove);
			linkCell.getBendPointSelectionHelper().removeSelectionIndex(selectedBendPointIndexes[i]);
		}
		
		return newBendPoints;
	}
}
