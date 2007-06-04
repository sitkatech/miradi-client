/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
	
		if (! isDiagramView())
			return false;
		
		DiagramFactorLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (links.length == 0)
			return false;
		
		PointList bendPoints = links[0].getBendPoints();
		if (bendPoints.size() <= 0)
			return false;

		return true;
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
		DiagramFactorLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		
		for (int i = 0; i < links.length; ++i)
		{
			DiagramFactorLink diagramFactorLink = links[i];
			LinkCell linkCell = getDiagramView().getDiagramModel().getDiagramFactorLink(diagramFactorLink);
			PointList bendPoints = diagramFactorLink.getBendPoints();
			PointList newBendPoints = getBendPointListMinusDeletedPoint(bendPoints, linkCell);
			
			String newBendPointList = newBendPoints.toJson().toString();
			DiagramFactorLinkId linkId = diagramFactorLink.getDiagramLinkageId();
			CommandSetObjectData removeBendPointCommand = new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramFactorLink.TAG_BEND_POINTS, newBendPointList);
			getProject().executeCommand(removeBendPointCommand);
		}
	}
	
	private PointList getBendPointListMinusDeletedPoint(PointList currentBendPoints, LinkCell linkCell)
	{
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
